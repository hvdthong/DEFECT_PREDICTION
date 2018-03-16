package org.apache.tools.ant;

import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.EnumeratedAttribute;

import java.lang.reflect.*;
import java.io.File;
import java.util.*;

/**
 * Helper class that collects the methods a task or nested element
 * holds to set attributes, create nested elements or hold PCDATA
 * elements.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class IntrospectionHelper implements BuildListener {

    /**
     * holds the types of the attributes that could be set.
     */
    private Hashtable attributeTypes;

    /**
     * holds the attribute setter methods.
     */
    private Hashtable attributeSetters;

    /**
     * Holds the types of nested elements that could be created.
     */
    private Hashtable nestedTypes;

    /**
     * Holds methods to create nested elements.
     */
    private Hashtable nestedCreators;

    /**
     * Holds methods to store configured nested elements.
     */
    private Hashtable nestedStorers;

    /**
     * The method to add PCDATA stuff.
     */
    private Method addText = null;

    /**
     * The Class that's been introspected.
     */
    private Class bean;

    /**
     * instances we've already created
     */
    private static Hashtable helpers = new Hashtable();

    private IntrospectionHelper(final Class bean) {
        attributeTypes = new Hashtable();
        attributeSetters = new Hashtable();
        nestedTypes = new Hashtable();
        nestedCreators = new Hashtable();
        nestedStorers = new Hashtable();
        
        this.bean = bean;

        Method[] methods = bean.getMethods();
        for (int i=0; i<methods.length; i++) {
            final Method m = methods[i];
            final String name = m.getName();
            Class returnType = m.getReturnType();
            Class[] args = m.getParameterTypes();

            if (org.apache.tools.ant.Task.class.isAssignableFrom(bean) 
                && args.length == 1 &&
                (
                 (
                  "setLocation".equals(name) && org.apache.tools.ant.Location.class.equals(args[0])
                  ) || (
                   "setTaskType".equals(name) && java.lang.String.class.equals(args[0])
                  )
                 )) {
                continue;
            }
            
            if (org.apache.tools.ant.TaskContainer.class.isAssignableFrom(bean) 
                && args.length == 1 && "addTask".equals(name) 
                && org.apache.tools.ant.Task.class.equals(args[0])) {
                continue;
            }
            

            if ("addText".equals(name)
                && java.lang.Void.TYPE.equals(returnType)
                && args.length == 1
                && java.lang.String.class.equals(args[0])) {

                addText = methods[i];

            } else if (name.startsWith("set")
                       && java.lang.Void.TYPE.equals(returnType)
                       && args.length == 1
                       && !args[0].isArray()) {

                String propName = getPropertyName(name, "set");
                AttributeSetter as = createAttributeSetter(m, args[0]);
                if (as != null) {
                    attributeTypes.put(propName, args[0]);
                    attributeSetters.put(propName, as);
                }

            } else if (name.startsWith("create")
                       && !returnType.isArray()
                       && !returnType.isPrimitive()
                       && args.length == 0) {

                String propName = getPropertyName(name, "create");
                nestedTypes.put(propName, returnType);
                nestedCreators.put(propName, new NestedCreator() {

                        public Object create(Object parent) 
                            throws InvocationTargetException, 
                            IllegalAccessException {

                            return m.invoke(parent, new Object[] {});
                        }

                    });
                
            } else if (name.startsWith("addConfigured")
                       && java.lang.Void.TYPE.equals(returnType)
                       && args.length == 1
                       && !java.lang.String.class.equals(args[0])
                       && !args[0].isArray()
                       && !args[0].isPrimitive()) {
                 
                try {
                    final Constructor c = 
                        args[0].getConstructor(new Class[] {});
                    String propName = getPropertyName(name, "addConfigured");
                    nestedTypes.put(propName, args[0]);
                    nestedCreators.put(propName, new NestedCreator() {

                            public Object create(Object parent) 
                                throws InvocationTargetException, IllegalAccessException, InstantiationException {
                                
                                Object o = c.newInstance(new Object[] {});
                                return o;
                            }

                        });
                    nestedStorers.put(propName, new NestedStorer() {

                            public void store(Object parent, Object child) 
                                throws InvocationTargetException, IllegalAccessException, InstantiationException {
                                
                                m.invoke(parent, new Object[] {child});
                            }

                        });
                } catch (NoSuchMethodException nse) {
                }
            } else if (name.startsWith("add")
                       && java.lang.Void.TYPE.equals(returnType)
                       && args.length == 1
                       && !java.lang.String.class.equals(args[0])
                       && !args[0].isArray()
                       && !args[0].isPrimitive()) {
                 
                try {
                    final Constructor c = 
                        args[0].getConstructor(new Class[] {});
                    String propName = getPropertyName(name, "add");
                    nestedTypes.put(propName, args[0]);
                    nestedCreators.put(propName, new NestedCreator() {

                            public Object create(Object parent) 
                                throws InvocationTargetException, IllegalAccessException, InstantiationException {
                                
                                Object o = c.newInstance(new Object[] {});
                                m.invoke(parent, new Object[] {o});
                                return o;
                            }

                        });
                } catch (NoSuchMethodException nse) {
                }
            }
        }
    }
    
    /**
     * Factory method for helper objects.
     */
    public synchronized static IntrospectionHelper getHelper(Class c) {
        IntrospectionHelper ih = (IntrospectionHelper) helpers.get(c);
        if (ih == null) {
            ih = new IntrospectionHelper(c);
            helpers.put(c, ih);
        }
        return ih;
    }

    /**
     * Sets the named attribute.
     */
    public void setAttribute(Project p, Object element, String attributeName, 
                             String value)
        throws BuildException {
        AttributeSetter as = (AttributeSetter) attributeSetters.get(attributeName);
        if (as == null) {
	    String msg = getElementName(p, element) +
                " doesn't support the \"" + attributeName + "\" attribute.";
            throw new BuildException(msg);
        }
        try {
            as.set(p, element, value);
        } catch (IllegalAccessException ie) {
            throw new BuildException(ie);
        } catch (InvocationTargetException ite) {
            Throwable t = ite.getTargetException();
            if (t instanceof BuildException) {
                throw (BuildException) t;
            }
            throw new BuildException(t);
        }
    }

    /**
     * Adds PCDATA areas.
     */
    public void addText(Project project, Object element, String text) {
        if (addText == null) {
	   String msg = getElementName(project, element) +
                " doesn't support nested text data.";
            throw new BuildException(msg);
        }
        try {
            addText.invoke(element, new String[] {text});
        } catch (IllegalAccessException ie) {
            throw new BuildException(ie);
        } catch (InvocationTargetException ite) {
            Throwable t = ite.getTargetException();
            if (t instanceof BuildException) {
                throw (BuildException) t;
            }
            throw new BuildException(t);
        }
    }

    /**
     * Creates a named nested element.
     */
    public Object createElement(Project project, Object element, String elementName) 
        throws BuildException {
        NestedCreator nc = (NestedCreator) nestedCreators.get(elementName);
        if (nc == null) {
	    String msg = getElementName(project, element) +
                " doesn't support the nested \"" + elementName + "\" element.";
            throw new BuildException(msg);
        }
        try {
            Object nestedElement = nc.create(element);
            if (nestedElement instanceof DataType) {
                ((DataType)nestedElement).setProject(project);
            }
            return nestedElement;
        } catch (IllegalAccessException ie) {
            throw new BuildException(ie);
        } catch (InstantiationException ine) {
            throw new BuildException(ine);
        } catch (InvocationTargetException ite) {
            Throwable t = ite.getTargetException();
            if (t instanceof BuildException) {
                throw (BuildException) t;
            }
            throw new BuildException(t);
        }
    }

    /**
     * Creates a named nested element.
     */
    public void storeElement(Project project, Object element, Object child, String elementName) 
        throws BuildException {
        if (elementName == null) {
            return;
        }
        NestedStorer ns = (NestedStorer)nestedStorers.get(elementName);
        if (ns == null) {
            return;
        }
        try {
            ns.store(element, child);
        } catch (IllegalAccessException ie) {
            throw new BuildException(ie);
        } catch (InstantiationException ine) {
            throw new BuildException(ine);
        } catch (InvocationTargetException ite) {
            Throwable t = ite.getTargetException();
            if (t instanceof BuildException) {
                throw (BuildException) t;
            }
            throw new BuildException(t);
        }
    }

    /**
     * returns the type of a named nested element.
     */
    public Class getElementType(String elementName) 
        throws BuildException {
        Class nt = (Class) nestedTypes.get(elementName);
        if (nt == null) {
            String msg = "Class " + bean.getName() +
                " doesn't support the nested \"" + elementName + "\" element.";
            throw new BuildException(msg);
        }
        return nt;
    }

    /**
     * returns the type of a named attribute.
     */
    public Class getAttributeType(String attributeName) 
        throws BuildException {
        Class at = (Class) attributeTypes.get(attributeName);
        if (at == null) {
            String msg = "Class " + bean.getName() +
                " doesn't support the \"" + attributeName + "\" attribute.";
            throw new BuildException(msg);
        }
        return at;
    }

    /**
     * Does the introspected class support PCDATA?
     */
    public boolean supportsCharacters() {
        return addText != null;
    }

    /**
     * Return all attribues supported by the introspected class.
     */
    public Enumeration getAttributes() {
        return attributeSetters.keys();
    }

    /**
     * Return all nested elements supported by the introspected class.
     */
    public Enumeration getNestedElements() {
        return nestedTypes.keys();
    }

    /**
     * Create a proper implementation of AttributeSetter for the given
     * attribute type.  
     */
    private AttributeSetter createAttributeSetter(final Method m,
                                                  final Class arg) {

        if (java.lang.String.class.equals(arg)) {
            return new AttributeSetter() {
                    public void set(Project p, Object parent, String value) 
                        throws InvocationTargetException, IllegalAccessException {
                        m.invoke(parent, new String[] {value});
                    }
                };

        } else if (java.lang.Character.class.equals(arg)
                   || java.lang.Character.TYPE.equals(arg)) {
            return new AttributeSetter() {
                    public void set(Project p, Object parent, String value) 
                        throws InvocationTargetException, IllegalAccessException {
                        m.invoke(parent, new Character[] {new Character(value.charAt(0))});
                    }

                };
        } else if (java.lang.Byte.TYPE.equals(arg)) {
            return new AttributeSetter() {
                    public void set(Project p, Object parent, String value) 
                        throws InvocationTargetException, IllegalAccessException {
                        m.invoke(parent, new Byte[] {new Byte(value)});
                    }

                };
        } else if (java.lang.Short.TYPE.equals(arg)) {
            return new AttributeSetter() {
                    public void set(Project p, Object parent, String value) 
                        throws InvocationTargetException, IllegalAccessException {
                        m.invoke(parent, new Short[] {new Short(value)});
                    }

                };
        } else if (java.lang.Integer.TYPE.equals(arg)) {
            return new AttributeSetter() {
                    public void set(Project p, Object parent, String value) 
                        throws InvocationTargetException, IllegalAccessException {
                        m.invoke(parent, new Integer[] {new Integer(value)});
                    }

                };
        } else if (java.lang.Long.TYPE.equals(arg)) {
            return new AttributeSetter() {
                    public void set(Project p, Object parent, String value) 
                        throws InvocationTargetException, IllegalAccessException {
                        m.invoke(parent, new Long[] {new Long(value)});
                    }

                };
        } else if (java.lang.Float.TYPE.equals(arg)) {
            return new AttributeSetter() {
                    public void set(Project p, Object parent, String value) 
                        throws InvocationTargetException, IllegalAccessException {
                        m.invoke(parent, new Float[] {new Float(value)});
                    }

                };
        } else if (java.lang.Double.TYPE.equals(arg)) {
            return new AttributeSetter() {
                    public void set(Project p, Object parent, String value) 
                        throws InvocationTargetException, IllegalAccessException {
                        m.invoke(parent, new Double[] {new Double(value)});
                    }

                };

        } else if (java.lang.Boolean.class.equals(arg) 
                   || java.lang.Boolean.TYPE.equals(arg)) {
            return new AttributeSetter() {
                    public void set(Project p, Object parent, String value) 
                        throws InvocationTargetException, IllegalAccessException {
                        m.invoke(parent, 
                                 new Boolean[] {new Boolean(Project.toBoolean(value))});
                    }

                };

        } else if (java.lang.Class.class.equals(arg)) {
            return new AttributeSetter() {
                    public void set(Project p, Object parent, String value) 
                        throws InvocationTargetException, IllegalAccessException, BuildException {
                        try {
                            m.invoke(parent, new Class[] {Class.forName(value)});
                        } catch (ClassNotFoundException ce) {
                            throw new BuildException(ce);
                        }
                    }
                };

        } else if (java.io.File.class.equals(arg)) {
            return new AttributeSetter() {
                    public void set(Project p, Object parent, String value) 
                        throws InvocationTargetException, IllegalAccessException {
                        m.invoke(parent, new File[] {p.resolveFile(value)});
                    }

                };

        } else if (org.apache.tools.ant.types.Path.class.equals(arg)) {
            return new AttributeSetter() {
                    public void set(Project p, Object parent, String value) 
                        throws InvocationTargetException, IllegalAccessException {
                        m.invoke(parent, new Path[] {new Path(p, value)});
                    }

                };

        } else if (org.apache.tools.ant.types.EnumeratedAttribute.class.isAssignableFrom(arg)) {
            return new AttributeSetter() {
                    public void set(Project p, Object parent, String value) 
                        throws InvocationTargetException, IllegalAccessException, BuildException {
                        try {
                            org.apache.tools.ant.types.EnumeratedAttribute ea = (org.apache.tools.ant.types.EnumeratedAttribute)arg.newInstance();
                            ea.setValue(value);
                            m.invoke(parent, new EnumeratedAttribute[] {ea});
                        } catch (InstantiationException ie) {
                            throw new BuildException(ie);
                        }
                    }
                };
        

        } else {

            try {
                final Constructor c = 
                    arg.getConstructor(new Class[] {java.lang.String.class});

                return new AttributeSetter() {
                        public void set(Project p, Object parent, 
                                        String value) 
                            throws InvocationTargetException, IllegalAccessException, BuildException {
                            try {
                                Object attribute = c.newInstance(new String[] {value});
                                if (attribute instanceof DataType) {
                                    ((DataType)attribute).setProject(p);
                                }
                                m.invoke(parent, new Object[] {attribute});
                            } catch (InstantiationException ie) {
                                throw new BuildException(ie);
                            }
                        }
                    };
                
            } catch (NoSuchMethodException nme) {
            }
        }
        
        return null;
    }

    protected String getElementName(Project project, Object element)
    {
	Hashtable elements = project.getTaskDefinitions();
	String typeName = "task";
	if (!elements.contains( element.getClass() ))
	{
	    elements = project.getDataTypeDefinitions();
	    typeName = "data type";
	    if (!elements.contains( element.getClass() ))
	    {
		elements = null;
	    }
	}

	if (elements != null)
	{
	    Enumeration e = elements.keys();
	    while (e.hasMoreElements())
	    {
		String elementName = (String) e.nextElement();
		Class elementClass = (Class) elements.get( elementName );
		if ( element.getClass().equals( elementClass ) )
		{
		    return "The <" + elementName + "> " + typeName;
		}
	    }
	}
	
	return "Class " + element.getClass().getName();
    }

    /**
     * extract the name of a property from a method name - subtracting
     * a given prefix.  
     */
    private String getPropertyName(String methodName, String prefix) {
        int start = prefix.length();
        return methodName.substring(start).toLowerCase();
    }

    private interface NestedCreator {
        public Object create(Object parent) 
            throws InvocationTargetException, IllegalAccessException, InstantiationException;
    }
    
    private interface NestedStorer {
        public void store(Object parent, Object child) 
            throws InvocationTargetException, IllegalAccessException, InstantiationException;
    }
    
    private interface AttributeSetter {
        public void set(Project p, Object parent, String value)
            throws InvocationTargetException, IllegalAccessException, 
                   BuildException;
    }

    public void buildStarted(BuildEvent event) {}
    public void buildFinished(BuildEvent event) {
        attributeTypes.clear();
        attributeSetters.clear();
        nestedTypes.clear();
        nestedCreators.clear();
        addText = null;
        helpers.clear();
    }

    public void targetStarted(BuildEvent event) {}
    public void targetFinished(BuildEvent event) {}
    public void taskStarted(BuildEvent event) {}
    public void taskFinished(BuildEvent event) {}
    public void messageLogged(BuildEvent event) {}
}
