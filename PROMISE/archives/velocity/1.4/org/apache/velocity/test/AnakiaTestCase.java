public class AnakiaTestCase extends BaseTestCase
{
    private static final String COMPARE_DIR = "../test/anakia/compare";
    private static final String RESULTS_DIR = "../test/anakia/results";
    private static final String FILE_EXT = ".html";

    /**
     * Creates a new instance.
     *
     */
    public AnakiaTestCase()
    {
        super("AnakiaTestCase");
    }

    public static junit.framework.Test suite()
    {
        return new AnakiaTestCase();
    }

    /**
     * Runs the test. This is empty on purpose because the
     * code to do the Anakia output is in the .xml file that runs
     * this test.
     */
    public void runTest ()
    {
        try
        {
            assureResultsDirectoryExists(RESULTS_DIR);
            
            if (!isMatch(RESULTS_DIR,COMPARE_DIR,"index",FILE_EXT,FILE_EXT))
            {
                fail("Output is incorrect!");
            }
            else
            {
                System.out.println ("Passed!");
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
