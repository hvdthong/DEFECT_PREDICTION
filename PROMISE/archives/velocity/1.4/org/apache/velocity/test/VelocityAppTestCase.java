import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import java.util.Vector;

import org.apache.velocity.VelocityContext;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.test.provider.TestProvider;
import org.apache.velocity.util.StringUtils;

import org.apache.velocity.app.Velocity;

import junit.framework.TestCase;

/**
 * This class is intended to test the app.Velocity.java class.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @version $Id: VelocityAppTestCase.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class VelocityAppTestCase extends BaseTestCase implements TemplateTestBase
{
    private StringWriter compare1 = new StringWriter();
    private String input1 = "My name is $name -> $Floog";
    private String result1 = "My name is jason -> floogie woogie";

    public VelocityAppTestCase()
    {
        super("VelocityAppTestCase");

        try
        {
            Velocity.setProperty(
	           Velocity.FILE_RESOURCE_LOADER_PATH, FILE_RESOURCE_LOADER_PATH);
	        
            Velocity.init();
        }
        catch (Exception e)
        {
            System.err.println("Cannot setup VelocityAppTestCase!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static junit.framework.Test suite()
    {
        return new VelocityAppTestCase();
    }

    /**
     * Runs the test.
     */
    public void runTest ()
    {
        VelocityContext context = new VelocityContext();
        context.put("name", "jason");
        context.put("Floog", "floogie woogie");

        try
        {
            Velocity.evaluate(context, compare1, "evaltest", input1);

/*
            FIXME: Not tested right now.
            
            StringWriter result2 = new StringWriter();
            Velocity.mergeTemplate("mergethis.vm",  context, result2);

            StringWriter result3 = new StringWriter();
            Velocity.invokeVelocimacro("floog", "test", new String[2], 
                                        context, result3);
*/
            if (!result1.equals(compare1.toString()))
            {
                fail("Output incorrect.");
            }
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }
}
