package org.apache.tools.ant;

import java.util.Enumeration;
import java.util.Vector;
import org.xml.sax.AttributeList;
import org.xml.sax.helpers.AttributeListImpl;

/**
 * Wrapper class that holds the attributes of a Task (or elements
 * nested below that level) and takes care of configuring that element
 * at runtime.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class RuntimeConfigurable {

    private String elementTag = null;
    private Vector children = new Vector();
    private Object wrappedObject = null;
    private AttributeList attributes;
    private StringBuffer characters = new StringBuffer();

    /**
     * @param proxy The element to wrap.
     */
    public RuntimeConfigurable(Object proxy, String elementTag) {
        wrappedObject = proxy;
        this.elementTag = elementTag;
    }

    void setProxy(Object proxy) {
        wrappedObject = proxy;
    }

    /**
     * Set's the attributes for the wrapped element.
     */
    public void setAttributes(AttributeList attributes) {
        this.attributes = new AttributeListImpl(attributes);
    }

    /**
     * Returns the AttributeList of the wrapped element.
     */
    public AttributeList getAttributes() {
        return attributes;
    }

    /**
     * Adds child elements to the wrapped element.
     */
    public void addChild(RuntimeConfigurable child) {
        children.addElement(child);
    }

    /**
     * Returns the child with index <code>index</code>.
     */
    RuntimeConfigurable getChild(int index) {
        return (RuntimeConfigurable) children.elementAt(index);
    }

    /**
     * Add characters from #PCDATA areas to the wrapped element.
     */
    public void addText(String data) {
        characters.append(data);
    }

    /**
     * Add characters from #PCDATA areas to the wrapped element.
     */
    public void addText(char[] buf, int start, int end) {
        addText(new String(buf, start, end));
    }

    public String getElementTag() {
        return elementTag;
    }
    
    
    /**
     * Configure the wrapped element and all children.
     */
    public void maybeConfigure(Project p) throws BuildException {
	String id = null;
	
        if (attributes != null) {
            ProjectHelper.configure(wrappedObject, attributes, p);
            id = attributes.getValue("id");
            attributes = null;
        }
        if (characters.length() != 0) {
            ProjectHelper.addText(p, wrappedObject, characters.toString());
            characters.setLength(0);
        }
        Enumeration enum = children.elements();
        while (enum.hasMoreElements()) {
            RuntimeConfigurable child = (RuntimeConfigurable) enum.nextElement();
            child.maybeConfigure(p);
            ProjectHelper.storeChild(p, wrappedObject, child.wrappedObject, child.getElementTag().toLowerCase());
        }

        if (id != null) {
            p.addReference(id, wrappedObject);
        }
    }

}
