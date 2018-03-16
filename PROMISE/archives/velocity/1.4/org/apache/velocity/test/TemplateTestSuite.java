import java.io.FileInputStream;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.velocity.app.Velocity;

import junit.framework.TestSuite;

/**
 * Test suite for Templates.
 *
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @version $Id: TemplateTestSuite.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class TemplateTestSuite extends TestSuite implements TemplateTestBase
{
    private Properties testProperties;

    /**
     * Creates an instace of the Apache Velocity test suite.
     */
    public TemplateTestSuite()
    {
        try
        {
            Velocity.setProperty(
                Velocity.FILE_RESOURCE_LOADER_PATH, FILE_RESOURCE_LOADER_PATH);
            
            Velocity.setProperty(Velocity.RUNTIME_LOG_ERROR_STACKTRACE, "true");
            Velocity.setProperty(Velocity.RUNTIME_LOG_WARN_STACKTRACE, "true");
            Velocity.setProperty(Velocity.RUNTIME_LOG_INFO_STACKTRACE, "true");

            Velocity.init();
            
            testProperties = new Properties();
            testProperties.load(new FileInputStream(TEST_CASE_PROPERTIES));
        }
        catch (Exception e)
        {
            System.err.println("Cannot setup TemplateTestSuite!");
            e.printStackTrace();
            System.exit(1);
        }            

        addTemplateTestCases();
    }

    /**
     * Adds the template test cases to run to this test suite.  Template test
     * cases are listed in the <code>TEST_CASE_PROPERTIES</code> file.
     */
    private void addTemplateTestCases()
    {
        String template;
        for (int i = 1 ;; i++)
        {
            template = testProperties.getProperty(getTemplateTestKey(i));

            if (template != null)
            {
                System.out.println("Adding TemplateTestCase : " + template);
                addTest(new TemplateTestCase(template));
            }
            else
            {
                break;
            }
        }
    }

    /**
     * Macro which returns the properties file key for the specified template
     * test number.
     *
     * @param nbr The template test number to return a property key for.
     * @return    The property key.
     */
    private static final String getTemplateTestKey(int nbr)
    {
        return ("test.template." + nbr);
    }
}
