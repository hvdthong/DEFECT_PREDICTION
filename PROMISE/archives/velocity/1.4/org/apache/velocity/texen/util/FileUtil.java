import java.io.File;

/**
 * A general file utility for use in the context
 *
 * @author <a href="mailto:leon@opticode.co.za">Leon Messerschmidt</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @version $Id: FileUtil.java 75955 2004-03-03 23:23:08Z geirm $ 
 */
public class FileUtil
{
    /**
     * Creates the directory s (and any parent directories needed).
     *
     * @param String path/directory to create.
     * @param String report of path/directory creation.
     */
    static public String mkdir (String s)
    {
        try
        {
            if ((new File(s)).mkdirs())
                return "Created dir: "+s;
            else
                return "Failed to create dir or dir already exists: "+s;
        }
        catch (Exception e)
        {
            return e.toString();
        }
    }

    /**
     * A method to get a File object.
     *
     * @param String path to file object to create.
     * @return File created file object.
     */
    public static File file(String s)
    {
        File f = new File(s);
        return f;
    }
    
    /**
     * A method to get a File object.
     *
     * @param String base path
     * @param String file name
     * @return File created file object.
     */
    public static File file(String base, String s)
    {
        File f = new File(base, s);
        return f;
    }
}
