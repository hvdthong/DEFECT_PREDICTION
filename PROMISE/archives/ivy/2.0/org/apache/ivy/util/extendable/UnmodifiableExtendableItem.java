package org.apache.ivy.util.extendable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class UnmodifiableExtendableItem implements ExtendableItem {
    private final Map attributes = new HashMap();

    private final Map unmodifiableAttributesView = Collections.unmodifiableMap(attributes);

    private final Map extraAttributes = new HashMap();

    private final Map unmodifiableExtraAttributesView = 
                                    Collections.unmodifiableMap(extraAttributes);

    /*
     * this is the only place where extra attributes are stored in qualified form. In all other maps
     * they are stored unqualified.
     */
    private final Map qualifiedExtraAttributes = new HashMap();

    private final Map unmodifiableQualifiedExtraAttributesView = 
                                    Collections.unmodifiableMap(qualifiedExtraAttributes);

    public UnmodifiableExtendableItem(Map stdAttributes, Map extraAttributes) {
        if (stdAttributes != null) {
            this.attributes.putAll(stdAttributes);
        }
        if (extraAttributes != null) {
            for (Iterator iter = extraAttributes.entrySet().iterator(); iter.hasNext();) {
                Entry extraAtt = (Entry) iter.next();
                setExtraAttribute((String) extraAtt.getKey(), (String) extraAtt.getValue());
            }
        }
    }

    public String getAttribute(String attName) {
        return (String) attributes.get(attName);
    }

    public String getExtraAttribute(String attName) {
        String v = (String) qualifiedExtraAttributes.get(attName);
        if (v == null) {
            v = (String) extraAttributes.get(attName);
        }
        return v;
    }


    protected void setExtraAttribute(String attName, String attValue) {
        qualifiedExtraAttributes.put(attName, attValue);
        
        int index = attName.indexOf(':');
        if (index != -1) {
            attName = attName.substring(index + 1);
        }
        extraAttributes.put(attName, attValue);
        attributes.put(attName, attValue);
    }

    protected void setStandardAttribute(String attName, String attValue) {
        attributes.put(attName, attValue);
    }

    public Map getAttributes() {
        return unmodifiableAttributesView;
    }


    public Map getExtraAttributes() {
        return unmodifiableExtraAttributesView;
    }
    
    public Map getQualifiedExtraAttributes() {
        return unmodifiableQualifiedExtraAttributesView;
    }

}
