package org.apache.tools.ant.taskdefs.optional.sitraka;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.ClassFile;
import org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.ClassPathLoader;
import org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.MethodInfo;
import org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Little hack to process XML report from JProbe. It will fix
 * some reporting errors from JProbe 3.0 and makes use of a reference
 * classpath to add classes/methods that were not reported by JProbe
 * as being used (ie loaded)
 *
 */
public class XMLReport {
    /** task caller, can be null, used for logging purpose */
    private Task task;

    /** the XML file to process just from CovReport */
    private File file;

    /** jprobe home path. It is used to get the DTD */
    private File jprobeHome;

    /** parsed document */
    private Document report;

    /**
     * mapping of class names to <code>ClassFile</code>s from the reference
     * classpath.  It is used to filter the JProbe report.
     */
    private Hashtable classFiles;

    /** mapping package name / package node for faster access */
    private Hashtable pkgMap;

    /** mapping classname / class node for faster access */
    private Hashtable classMap;

    /** method filters */
    private ReportFilters filters;

    /** create a new XML report, logging will be on stdout */
    public XMLReport(File file) {
        this(null, file);
    }

    /** create a new XML report, logging done on the task */
    public XMLReport(Task task, File file) {
        this.file = file;
        this.task = task;
    }

    /** set the JProbe home path. Used to get the DTD */
    public void setJProbehome(File home) {
        jprobeHome = home;
    }

    /** set the  */
    public void setReportFilters(ReportFilters filters) {
        this.filters = filters;
    }


    /** create node maps so that we can access node faster by their name */
    protected void createNodeMaps() {
        pkgMap = new Hashtable();
        classMap = new Hashtable();
        NodeList packages = report.getElementsByTagName("package");
        final int pkglen = packages.getLength();
        log("Indexing " + pkglen + " packages");
        for (int i = pkglen - 1; i > -1; i--) {
            Element pkg = (Element) packages.item(i);
            String pkgname = pkg.getAttribute("name");

            int nbclasses = 0;
            NodeList classes = pkg.getElementsByTagName("class");
            final int classlen = classes.getLength();
            log("Indexing " + classlen + " classes in package " + pkgname);
            for (int j = classlen - 1; j > -1; j--) {
                Element clazz = (Element) classes.item(j);
                String classname = clazz.getAttribute("name");
                if (pkgname != null && pkgname.length() != 0) {
                    classname = pkgname + "." + classname;
                }

                int nbmethods = 0;
                NodeList methods = clazz.getElementsByTagName("method");
                final int methodlen = methods.getLength();
                for (int k = methodlen - 1; k > -1; k--) {
                    Element meth = (Element) methods.item(k);
                    StringBuffer methodname = new StringBuffer(meth.getAttribute("name"));
                    methodname.delete(methodname.toString().indexOf("("),
                        methodname.toString().length());
                    String signature = classname + "." + methodname + "()";
                    if (filters.accept(signature)) {
                        log("kept method:" + signature);
                        nbmethods++;
                    } else {
                        clazz.removeChild(meth);
                    }
                }
                if (nbmethods != 0 && classFiles.containsKey(classname)) {
                    log("Adding class '" + classname + "'");
                    classMap.put(classname, clazz);
                    nbclasses++;
                } else {
                    pkg.removeChild(clazz);
                }
            }
            if (nbclasses != 0) {
                log("Adding package '" + pkgname + "'");
                pkgMap.put(pkgname, pkg);
            } else {
                pkg.getParentNode().removeChild(pkg);
            }
        }
        log("Indexed " + classMap.size() + " classes in " + pkgMap.size() + " packages");
    }

    /** create the whole new document */
    public Document createDocument(String[] classPath) throws Exception {

        classFiles = new Hashtable();
        ClassPathLoader cpl = new ClassPathLoader(classPath);
        Enumeration e = cpl.loaders();
        while (e.hasMoreElements()) {
            ClassPathLoader.FileLoader fl = (ClassPathLoader.FileLoader) e.nextElement();
            ClassFile[] classes = fl.getClasses();
            log("Processing " + classes.length + " classes in " + fl.getFile());
            for (int i = 0; i < classes.length; i++) {
                classFiles.put(classes[i].getFullName(), classes[i]);
            }
        }

        DocumentBuilder dbuilder = newBuilder();
        InputSource is = new InputSource(new FileInputStream(file));
        if (jprobeHome != null) {
            File dtdDir = new File(jprobeHome, "dtd");
        }
        report = dbuilder.parse(is);
        report.normalize();

        createNodeMaps();

        Enumeration classes = classFiles.elements();
        while (classes.hasMoreElements()) {
            ClassFile cf = (ClassFile) classes.nextElement();
            serializeClass(cf);
        }
        update();
        return report;
    }

    /**
     * JProbe does not put the java.lang prefix for classes
     * in this package, so used this nice method so that
     * I have the same signature for methods
     */
    protected String getMethodSignature(MethodInfo method) {
        StringBuffer buf = new StringBuffer(method.getName());
        buf.append("(");
        String[] params = method.getParametersType();
        for (int i = 0; i < params.length; i++) {
            String type = params[i];
            int pos = type.lastIndexOf('.');
            if (pos != -1) {
                String pkg = type.substring(0, pos);
                if ("java.lang".equals(pkg)) {
                    params[i] = type.substring(pos + 1);
                }
            }
            buf.append(params[i]);
            if (i != params.length - 1) {
                buf.append(", ");
            }
        }
        buf.append(")");
        return buf.toString();
    }

    /**
     * Convert to a CovReport-like signature - &lt;classname&gt;&#046;&lt;method&gt;().
     */
    protected String getMethodSignature(ClassFile clazz, MethodInfo method) {
        StringBuffer buf = new StringBuffer(clazz.getFullName());
        buf.append(".");
        buf.append(method.getName());
        buf.append("()");
        return buf.toString();
    }

    /**
     * Do additional work on an element to remove abstract methods that
     * are reported by JProbe 3.0
     */
    protected void removeAbstractMethods(ClassFile classFile, Element classNode) {
        MethodInfo[] methods = classFile.getMethods();
        Hashtable methodNodeList = getMethods(classNode);
        final int size = methods.length;
        for (int i = 0; i < size; i++) {
            MethodInfo method = methods[i];
            String methodSig = getMethodSignature(method);
            Element methodNode = (Element) methodNodeList.get(methodSig);
            if (methodNode != null
                && Utils.isAbstract(method.getAccessFlags())) {
                log("\tRemoving abstract method " + methodSig);
                classNode.removeChild(methodNode);
            }
        }
    }

    /** create an empty method element with its cov.data values */
    protected Element createMethodElement(MethodInfo method) {
        String methodsig = getMethodSignature(method);
        Element methodElem = report.createElement("method");
        methodElem.setAttribute("name", methodsig);
        Element methodData = report.createElement("cov.data");
        methodElem.appendChild(methodData);
        methodData.setAttribute("calls", "0");
        methodData.setAttribute("hit_lines", "0");
        methodData.setAttribute("total_lines", String.valueOf(method.getNumberOfLines()));
        return methodElem;
    }

    /** create an empty package element with its default cov.data (0) */
    protected Element createPackageElement(String pkgname) {
        Element pkgElem = report.createElement("package");
        pkgElem.setAttribute("name", pkgname);
        Element pkgData = report.createElement("cov.data");
        pkgElem.appendChild(pkgData);
        pkgData.setAttribute("calls", "0");
        pkgData.setAttribute("hit_methods", "0");
        pkgData.setAttribute("total_methods", "0");
        pkgData.setAttribute("hit_lines", "0");
        pkgData.setAttribute("total_lines", "0");
        return pkgElem;
    }

    /** create an empty class element with its default cov.data (0) */
    protected Element createClassElement(ClassFile classFile) {
        Element classElem = report.createElement("class");
        classElem.setAttribute("name", classFile.getName());
        if (null != classFile.getSourceFile()) {
            classElem.setAttribute("source", classFile.getSourceFile());
        }
        Element classData = report.createElement("cov.data");
        classElem.appendChild(classData);
        classData.setAttribute("calls", "0");
        classData.setAttribute("hit_methods", "0");
        classData.setAttribute("total_methods", "0");
        classData.setAttribute("hit_lines", "0");
        classData.setAttribute("total_lines", "0");
        return classElem;
    }

    /** serialize a classfile into XML */
    protected void serializeClass(ClassFile classFile) {
        String fullclassname = classFile.getFullName();
        log("Looking for '" + fullclassname + "'");
        Element clazz = (Element) classMap.get(fullclassname);

        if (clazz != null) {
            log("Ignoring " + fullclassname);
            removeAbstractMethods(classFile, clazz);
            return;
        }

        if (Utils.isInterface(classFile.getAccess())) {
            return;
        }

        Vector methods = getFilteredMethods(classFile);
        if (methods.size() == 0) {
            return;
        }

        String pkgname = classFile.getPackage();
        Element pkgElem = (Element) pkgMap.get(pkgname);
        if (pkgElem == null) {
            pkgElem = createPackageElement(pkgname);
            report.getDocumentElement().appendChild(pkgElem);
        }

        Element classElem = createClassElement(classFile);
        pkgElem.appendChild(classElem);

        int total_lines = 0;
        int total_methods = 0;
        final int count = methods.size();
        for (int i = 0; i < count; i++) {
            MethodInfo method = (MethodInfo) methods.elementAt(i);
            if (Utils.isAbstract(method.getAccessFlags())) {
            }
            Element methodElem = createMethodElement(method);
            classElem.appendChild(methodElem);
            total_lines += method.getNumberOfLines();
            total_methods++;
        }
        Element classData = getCovDataChild(classElem);
        classData.setAttribute("total_methods", String.valueOf(total_methods));
        classData.setAttribute("total_lines", String.valueOf(total_lines));

        classMap.put(fullclassname, classElem);
    }

    protected Vector getFilteredMethods(ClassFile classFile) {
        MethodInfo[] methodlist = classFile.getMethods();
        Vector methods = new Vector(methodlist.length);
        for (int i = 0; i < methodlist.length; i++) {
            MethodInfo method = methodlist[i];
            String signature = getMethodSignature(classFile, method);
            if (filters.accept(signature)) {
                methods.addElement(method);
                log("keeping " + signature);
            } else {
            }
        }
        return methods;
    }


    /** update the count of the XML, that is accumulate the stats on
     * methods, classes and package so that the numbers are valid
     * according to the info that was appended to the XML.
     */
    protected void update() {
        int calls = 0;
        int hit_methods = 0;
        int total_methods = 0;
        int hit_lines = 0;
        int total_lines = 0;

        Enumeration e = pkgMap.elements();
        while (e.hasMoreElements()) {
            Element pkgElem = (Element) e.nextElement();
            String pkgname = pkgElem.getAttribute("name");
            Element[] classes = getClasses(pkgElem);
            int pkg_calls = 0;
            int pkg_hit_methods = 0;
            int pkg_total_methods = 0;
            int pkg_hit_lines = 0;
            int pkg_total_lines = 0;
            for (int j = 0; j < classes.length; j++) {
                Element clazz = classes[j];
                String classname = clazz.getAttribute("name");
                if (pkgname != null && pkgname.length() != 0) {
                    classname = pkgname + "." + classname;
                }
                Element covdata = getCovDataChild(clazz);
                try {
                    pkg_calls += Integer.parseInt(covdata.getAttribute("calls"));
                    pkg_hit_methods += Integer.parseInt(covdata.getAttribute("hit_methods"));
                    pkg_total_methods += Integer.parseInt(covdata.getAttribute("total_methods"));
                    pkg_hit_lines += Integer.parseInt(covdata.getAttribute("hit_lines"));
                    pkg_total_lines += Integer.parseInt(covdata.getAttribute("total_lines"));
                } catch (NumberFormatException ex) {
                    log("Error parsing '" + classname + "' (" + j + "/"
                        + classes.length + ") in package '" + pkgname + "'");
                    throw ex;
                }
            }
            Element covdata = getCovDataChild(pkgElem);
            covdata.setAttribute("calls", String.valueOf(pkg_calls));
            covdata.setAttribute("hit_methods", String.valueOf(pkg_hit_methods));
            covdata.setAttribute("total_methods", String.valueOf(pkg_total_methods));
            covdata.setAttribute("hit_lines", String.valueOf(pkg_hit_lines));
            covdata.setAttribute("total_lines", String.valueOf(pkg_total_lines));
            calls += pkg_calls;
            hit_methods += pkg_hit_methods;
            total_methods += pkg_total_methods;
            hit_lines += pkg_hit_lines;
            total_lines += pkg_total_lines;
        }
        Element covdata = getCovDataChild(report.getDocumentElement());
        covdata.setAttribute("calls", String.valueOf(calls));
        covdata.setAttribute("hit_methods", String.valueOf(hit_methods));
        covdata.setAttribute("total_methods", String.valueOf(total_methods));
        covdata.setAttribute("hit_lines", String.valueOf(hit_lines));
        covdata.setAttribute("total_lines", String.valueOf(total_lines));
    }

    protected Element getCovDataChild(Element parent) {
        NodeList children = parent.getChildNodes();
        int len = children.getLength();
        for (int i = 0; i < len; i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) child;
                if ("cov.data".equals(elem.getNodeName())) {
                    return elem;
                }
            }
        }
        throw new NoSuchElementException("Could not find 'cov.data' "
            + "element in parent '" + parent.getNodeName() + "'");
    }

    protected Hashtable getMethods(Element clazz) {
        Hashtable map = new Hashtable();
        NodeList children = clazz.getChildNodes();
        int len = children.getLength();
        for (int i = 0; i < len; i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) child;
                if ("method".equals(elem.getNodeName())) {
                    String name = elem.getAttribute("name");
                    map.put(name, elem);
                }
            }
        }
        return map;
    }

    protected Element[] getClasses(Element pkg) {
        Vector v = new Vector();
        NodeList children = pkg.getChildNodes();
        int len = children.getLength();
        for (int i = 0; i < len; i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) child;
                if ("class".equals(elem.getNodeName())) {
                    v.addElement(elem);
                }
            }
        }
        Element[] elems = new Element[v.size()];
        v.copyInto(elems);
        return elems;

    }

    protected Element[] getPackages(Element snapshot) {
        Vector v = new Vector();
        NodeList children = snapshot.getChildNodes();
        int len = children.getLength();
        for (int i = 0; i < len; i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) child;
                if ("package".equals(elem.getNodeName())) {
                    v.addElement(elem);
                }
            }
        }
        Element[] elems = new Element[v.size()];
        v.copyInto(elems);
        return elems;
    }

    private static DocumentBuilder newBuilder() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            factory.setValidating(false);
            return factory.newDocumentBuilder();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public void log(String message) {
        if (task == null) {
        } else {
            task.log(message, Project.MSG_DEBUG);
        }
    }

}

