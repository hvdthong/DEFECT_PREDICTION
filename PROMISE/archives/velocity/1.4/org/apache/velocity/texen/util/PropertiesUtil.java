import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.velocity.texen.Generator;

/**
 * A property utility class for the texen text/code generator
 * Usually this class is only used from a Velocity context.
 *
 * @author <a href="mailto:leon@opticode.co.za">Leon Messerschmidt</a>
 * @author <a href="mailto:sbailliez@apache.org">Stephane Bailliez</a>
 * @version $Id: PropertiesUtil.java 75955 2004-03-03 23:23:08Z geirm $ 
 */
public class PropertiesUtil
{
    /**
     * Load properties from either a file in the templatePath if there
     * is one or the classPath.
     *
     * @param propertiesFile the properties file to load through
     * either the templatePath or the classpath.
     * @return a properties instance filled with the properties found
     * in the file or an empty instance if no file was found.
     */
    public Properties load(String propertiesFile)
    {
        Properties properties = new Properties();
        String templatePath = Generator.getInstance().getTemplatePath();
        if (templatePath != null)
        {
            properties = loadFromTemplatePath(propertiesFile);
        }
        else
        {
            properties = loadFromClassPath(propertiesFile);
        }
    
        return properties;
        
    }
    
    /**
     * Load a properties file from the templatePath defined in the
     * generator. As the templatePath can contains multiple paths,
     * it will cycle through them to find the file. The first file
     * that can be successfully loaded is considered. (kind of
     * like the java classpath), it is done to clone the Velocity
     * process of loading templates.
     *
     * @param propertiesFile the properties file to load. It must be
     * a relative pathname.
     * @return a properties instance loaded with the properties from
     * the file. If no file can be found it returns an empty instance.
     */
    protected Properties loadFromTemplatePath(String propertiesFile)
    {
        Properties properties = new Properties();
        String templatePath = Generator.getInstance().getTemplatePath();
        
        StringTokenizer st = new StringTokenizer(templatePath, ",");
        while (st.hasMoreTokens())
        {
            String templateDir = st.nextToken();
            try
            {
                String fullPath = propertiesFile;
                
                if (!fullPath.startsWith(templateDir))
                {
                    fullPath = templateDir + "/" + propertiesFile;
                }

                properties.load(new FileInputStream(fullPath));
                break;
            }
            catch (Exception e)
            {
            }
        } 
        return properties;
    }

    /**
     * Load a properties file from the classpath
     *
     * @param propertiesFile the properties file to load.
     * @return a properties instance loaded with the properties from
     * the file. If no file can be found it returns an empty instance.
     */ 
    protected Properties loadFromClassPath(String propertiesFile)
    {
        Properties properties = new Properties();
        ClassLoader classLoader = this.getClass().getClassLoader();
        
        try
        {
            if (propertiesFile.startsWith("$generator"))
            {
                propertiesFile = propertiesFile.substring(
                    "$generator.templatePath/".length());
            }
            
            InputStream inputStream = classLoader.getResourceAsStream(propertiesFile);
            properties.load(inputStream);
        }
        catch (IOException ioe)
        {
        }
        return properties;
    }
}
