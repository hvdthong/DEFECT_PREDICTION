public interface TemplateTestBase
{
    /**
     * VTL file extension.
     */
    public final static String TMPL_FILE_EXT = "vm";

    /**
     * Comparison file extension.
     */
    public final static String CMP_FILE_EXT = "cmp";

    /**
     * Comparison file extension.
     */
    public final static String RESULT_FILE_EXT = "res";

    /**
     * Path for templates. This property will override the
     * value in the default velocity properties file.
     */
    public final static String FILE_RESOURCE_LOADER_PATH = 
                          "../test/templates";

    /**
     * Properties file that lists which template tests to run.
     */
    public final static String TEST_CASE_PROPERTIES = 
                          FILE_RESOURCE_LOADER_PATH + "/templates.properties";

    /**
     * Results relative to the build directory.
     */
    public final static String RESULT_DIR = 
                          FILE_RESOURCE_LOADER_PATH + "/results";

    /**
     * Results relative to the build directory.
     */
    public final static String COMPARE_DIR = 
                          FILE_RESOURCE_LOADER_PATH + "/compare";

}
