package org.apache.camel.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.model.language.ExpressionType;

/**
 * Represents an XML &lt;serviceActivation/&gt; element
 * 
 * @version $Revision: 660266 $
 */
@XmlRootElement(name = "serviceActivation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceActivationType {
    @XmlAttribute
    private String group = "default";
    @XmlElementRef
    private List<ExpressionType> uris = new ArrayList<ExpressionType>();

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<ExpressionType> getUris() {
        return uris;
    }

    public void setUris(List<ExpressionType> uris) {
        this.uris = uris;
    }
}