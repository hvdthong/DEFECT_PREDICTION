package org.apache.camel.converter.jaxp;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.apache.camel.Converter;


/**
 * Converts from some DOM types to Java types
 *
 * @version $Revision: 660275 $
 */
@Converter
public final class DomConverter {

    private DomConverter() {
    }

    @Converter
    public static String toString(NodeList nodeList) {
        StringBuffer buffer = new StringBuffer();
        append(buffer, nodeList);
        return buffer.toString();
    }

/*
    @Converter
    public static String toString(Node node) {
        StringBuffer buffer = new StringBuffer();
        append(buffer, node);
        return buffer.toString();
    }
*/

    protected static void append(StringBuffer buffer, NodeList nodeList) {
        int size = nodeList.getLength();
        for (int i = 0; i < size; i++) {
            append(buffer, nodeList.item(i));
        }
    }

    protected static void append(StringBuffer buffer, Node node) {
        if (node instanceof Text) {
            Text text = (Text) node;
            buffer.append(text.getTextContent());
        } else if (node instanceof Attr) {
            Attr attribute = (Attr) node;
            buffer.append(attribute.getTextContent());

        } else if (node instanceof Element) {
            Element element = (Element) node;
            append(buffer, element.getChildNodes());
        } else if (node instanceof Document) {
            Document doc = (Document) node;
            append(buffer, doc.getChildNodes());
        }
    }
}
