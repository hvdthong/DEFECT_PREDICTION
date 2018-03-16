import java.io.IOException;
import java.io.StringWriter;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 * This class extends XMLOutputter in order to provide
 * a way to walk an Element tree into a String.
 *
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:rubys@us.ibm.com">Sam Ruby</a>
 * @version $Id: OutputWrapper.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class OutputWrapper extends XMLOutputter
{
    /**
     * Empty constructor
     */
    public OutputWrapper()
    {
    }
    
    /**
     * This method walks an Element tree into a String. The cool
     * thing about it is that it will strip off the first Element.
     * For example, if you have:
     * <p>
     * &lt;td&gt; foo &lt;strong&gt;bar&lt;/strong&gt; ack &lt;/td&gt;
     * </p>
     * It will output
     * <p>
     *  foo &lt;strong&gt;bar&lt;/strong&gt; ack &lt;/td&gt;
     * </p>
     */
    public String outputString(Element element, boolean strip)
    {
        StringWriter buff = new StringWriter();
        String name = element.getName();

        try
        {
            outputElementContent(element, buff);
        }
        catch (IOException e)
        {
        }
        return buff.toString();
    }
}
