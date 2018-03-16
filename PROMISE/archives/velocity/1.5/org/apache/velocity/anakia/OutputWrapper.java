import java.io.IOException;
import java.io.StringWriter;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;

/**
 * This class extends XMLOutputter in order to provide
 * a way to walk an Element tree into a String.
 *
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:rubys@us.ibm.com">Sam Ruby</a>
 * @version $Id: OutputWrapper.java 463298 2006-10-12 16:10:32Z henning $
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
     * @param f
     */
    public OutputWrapper(Format f)
    {
        super(f);
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
     * @param element
     * @param strip
     * @return The output string.
     */
    public String outputString(Element element, boolean strip)
    {
        StringWriter buff = new StringWriter();

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
