package org.apache.tools.ant.util.depend.bcel;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.EmptyVisitor;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.ConstantNameAndType;

/**
 * A BCEL visitor implementation to collect class dependency information
 *
 */
public class DependencyVisitor extends EmptyVisitor {
    /** The collectd dependencies */
    private Hashtable dependencies = new Hashtable();
    /**
     * The current class's constant pool - used to determine class names
     * from class references.
     */
    private ConstantPool constantPool;

    /**
     * Get the dependencies collected by this visitor
     *
     * @return a Enumeration of classnames, being the classes upon which the
     *      visited classes depend.
     */
    public Enumeration getDependencies() {
        return dependencies.keys();
    }

    /** Clear the curretn set of collected dependencies. */
    public void clearDependencies() {
        dependencies.clear();
    }

    /**
     * Visit the constant pool of a class
     *
     * @param constantPool the constant pool of the class being visited.
     */
    public void visitConstantPool(ConstantPool constantPool) {
        this.constantPool = constantPool;
    }

    /**
     * Visit a class reference
     *
     * @param constantClass the constantClass entry for the class reference
     */
    public void visitConstantClass(ConstantClass constantClass) {
        String classname
             = constantClass.getConstantValue(constantPool).toString();
        addSlashClass(classname);
    }

    /**
     * Visit a name and type ref
     *
     * Look for class references in this
     *
     * @param obj the name and type reference being visited.
     */
    public void visitConstantNameAndType(ConstantNameAndType obj) {
        String name = obj.getName(constantPool);
        if (obj.getSignature(constantPool).equals("Ljava/lang/Class;")
                && name.startsWith("class$")) {
            String classname
                = name.substring("class$".length()).replace('$', '.');
            int index = classname.lastIndexOf(".");
            if (index > 0) {
                char start;
                int index2 = classname.lastIndexOf(".", index - 1);
                if (index2 != -1) {
                    start = classname.charAt(index2 + 1);
                } else {
                    start = classname.charAt(0);
                }
                if ((start > 0x40) && (start < 0x5B)) {
                    classname = classname.substring(0, index) + "$"
                        + classname.substring(index + 1);
                    addClass(classname);
                } else {
                    addClass(classname);
                }
            } else {
                addClass(classname);
            }
        }
    }

    /**
     * Visit a field of the class.
     *
     * @param field the field being visited
     */
    public void visitField(Field field) {
        addClasses(field.getSignature());
    }

    /**
     * Visit a Java class
     *
     * @param javaClass the class being visited.
     */
    public void visitJavaClass(JavaClass javaClass) {
        addClass(javaClass.getClassName());
    }

    /**
     * Visit a method of the current class
     *
     * @param method the method being visited.
     */
    public void visitMethod(Method method) {
        String signature = method.getSignature();
        int pos = signature.indexOf(")");
        addClasses(signature.substring(1, pos));
        addClasses(signature.substring(pos + 1));
    }

    /**
     * Add a classname to the list of dependency classes
     *
     * @param classname the class to be added to the list of dependencies.
     */
    void addClass(String classname) {
        dependencies.put(classname, classname);
    }

    /**
     * Add all the classes from a descriptor string.
     *
     * @param string the descriptor string, being descriptors separated by
     *      ';' characters.
     */
    private void addClasses(String string) {
        StringTokenizer tokens = new StringTokenizer(string, ";");
        while (tokens.hasMoreTokens()) {
            String descriptor = tokens.nextToken();
            int pos = descriptor.indexOf('L');
            if (pos != -1) {
                addSlashClass(descriptor.substring(pos + 1));
            }
        }
    }

    /**
     * Adds a class name in slash format
     * (for example org/apache/tools/ant/Main).
     *
     * @param classname the class name in slash format
     */
    private void addSlashClass(String classname) {
        addClass(classname.replace('/', '.'));
    }
}

