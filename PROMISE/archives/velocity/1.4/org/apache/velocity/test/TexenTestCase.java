import java.io.File;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.util.StringUtils;
import junit.framework.TestCase;

/**
 * This is a test case for Texen. Simply executes a simple
 * generative task and compares the output.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @version $Id: TexenTestCase.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class TexenTestCase extends BaseTestCase
{
    /**
     * Directory where results are generated.
     */
    private static final String RESULTS_DIR = "../test/texen/results";

    /**
     * Directory where comparison output is stored.
     */
    private static final String COMPARE_DIR = "../test/texen/compare";

    /**
     * Creates a new instance.
     *
     */
    public TexenTestCase()
    {
        super("TexenTestCase");
    }

    public static junit.framework.Test suite()
    {
        return new TexenTestCase();
    }

    /**
     * Sets up the test.
     */
    protected void setUp ()
    {
    }

    /**
     * Runs the test.
     */
    public void runTest ()
    {
        try
        {
            assureResultsDirectoryExists(RESULTS_DIR);
            
            if (!isMatch(RESULTS_DIR,COMPARE_DIR,"TurbineWeather","java","java") ||
                !isMatch(RESULTS_DIR,COMPARE_DIR,"TurbineWeatherService","java","java") ||
                !isMatch(RESULTS_DIR,COMPARE_DIR,"WeatherService","java","java") ||
                !isMatch(RESULTS_DIR,COMPARE_DIR,"book","txt","txt") ||
                !isMatch(RESULTS_DIR,COMPARE_DIR,"Test","txt","txt"))
            {
                fail("Output is incorrect!");
            }
        }            
        catch(Exception e)
        {
            /*
             * do nothing.
             */
        }
    }
}
