import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.util.Vector;

import org.apache.velocity.VelocityContext;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.test.provider.TestProvider;
import org.apache.velocity.util.StringUtils;

import junit.framework.TestCase;

/**
 * Tests input encoding handling.  The input target is UTF-8, having
 * chinese and and a spanish enyay (n-twiddle)
 *
 *  Thanks to Kent Johnson for the example input file.
 *
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: EncodingTestCase.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class EncodingTestCase extends BaseTestCase implements TemplateTestBase
{
    public EncodingTestCase()
    {
        super("EncodingTestCase");
        
        try
        {
	        Velocity.setProperty(
	            Velocity.FILE_RESOURCE_LOADER_PATH, FILE_RESOURCE_LOADER_PATH);
	        
            Velocity.setProperty( Velocity.INPUT_ENCODING, "UTF-8" );

            Velocity.init();
	    }
	    catch (Exception e)
	    {
            System.err.println("Cannot setup EncodingTestCase!");
            e.printStackTrace();
            System.exit(1);
	    }
    }

    public static junit.framework.Test suite()
    {
        return new EncodingTestCase();
    }

    /**
     * Runs the test.
     */
    public void runTest ()
    {
        
        VelocityContext context = new VelocityContext();
       
        try
        {
            assureResultsDirectoryExists(RESULT_DIR);
            
            /*
             *  get the template and the output
             */

            /*
             *  Chinese and spanish
             */

            Template template = Velocity.getTemplate(
                getFileName(null, "encodingtest", TMPL_FILE_EXT), "UTF-8");

            FileOutputStream fos = 
                new FileOutputStream (
                    getFileName(RESULT_DIR, "encodingtest", RESULT_FILE_EXT));

            Writer writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
           
            template.merge(context, writer);
            writer.flush();
            writer.close();
            
            if (!isMatch(RESULT_DIR,COMPARE_DIR,"encodingtest",
                    RESULT_FILE_EXT,CMP_FILE_EXT) )
            {
                fail("Output 1 incorrect.");
            }

            /*
             *  a 'high-byte' chinese example from Michael Zhou
             */

            template = Velocity.getTemplate( 
                  getFileName( null, "encodingtest2", TMPL_FILE_EXT), "UTF-8");

            fos = 
                new FileOutputStream (
                    getFileName(RESULT_DIR, "encodingtest2", RESULT_FILE_EXT));

            writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
           
            template.merge(context, writer);
            writer.flush();
            writer.close();
            
            if (!isMatch(RESULT_DIR,COMPARE_DIR,"encodingtest2",
                    RESULT_FILE_EXT,CMP_FILE_EXT) )
            {
                fail("Output 2 incorrect.");
            }

            /*
             *  a 'high-byte' chinese from Ilkka
             */

            template = Velocity.getTemplate( 
                  getFileName( null, "encodingtest3", TMPL_FILE_EXT), "GBK");

            fos = 
                new FileOutputStream (
                    getFileName(RESULT_DIR, "encodingtest3", RESULT_FILE_EXT));

            writer = new BufferedWriter(new OutputStreamWriter(fos, "GBK"));
           
            template.merge(context, writer);
            writer.flush();
            writer.close();
            
            if (!isMatch(RESULT_DIR,COMPARE_DIR,"encodingtest3",
                    RESULT_FILE_EXT,CMP_FILE_EXT) )
            {
                fail("Output 3 incorrect.");
            }

            /*
             *  Russian example from Vitaly Repetenko
             */

            template = Velocity.getTemplate( 
                  getFileName( null, "encodingtest_KOI8-R", TMPL_FILE_EXT), "KOI8-R");

            fos = 
                new FileOutputStream (
                    getFileName(RESULT_DIR, "encodingtest_KOI8-R", RESULT_FILE_EXT));

            writer = new BufferedWriter(new OutputStreamWriter(fos, "KOI8-R"));
           
            template.merge(context, writer);
            writer.flush();
            writer.close();
            
            if (!isMatch(RESULT_DIR,COMPARE_DIR,"encodingtest_KOI8-R",
                    RESULT_FILE_EXT,CMP_FILE_EXT) )
            {
                fail("Output 4 incorrect.");
            }
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }
}



