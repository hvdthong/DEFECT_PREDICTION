package org.apache.camel.model.dataformat;

import javax.xml.bind.annotation.XmlEnum;

/**
 * @version $Revision: 640438 $
 */
@XmlEnum(String.class)
public enum ArtixDSContentType {
    Default, Auto, Binary, Text, Java, Xml, Sax, TagValuePair
}
