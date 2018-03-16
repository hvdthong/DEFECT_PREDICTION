import junit.framework.TestCase;
import junit.framework.Test;

import org.apache.velocity.util.StringUtils;

/**
 * Test case for any miscellaneous stuff.  If it isn't big, and doesn't fit
 * anywhere else, it goes here
 *
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @version $Id: MiscTestCase.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class MiscTestCase extends BaseTestCase
{
    public MiscTestCase()
    {
        super("MiscTestCase");
    }

    public MiscTestCase (String name)
    {
        super(name);
    }

    public static Test suite ()
    {
        return new MiscTestCase();
    }

    public void runTest()
    {
        /*
         *  some StringUtils tests
         */

        String eol = "XY";

        String arg = "XY";
        String res = StringUtils.chop(arg, 1, eol );
        assertTrue( "Test 1", res.equals("") );

        arg = "X";
        res = StringUtils.chop( arg, 1, eol );
        assertTrue( "Test 2", res.equals("") );

        arg = "ZXY";
        res = StringUtils.chop( arg, 1, eol );
        assertTrue( "Test 3", res.equals("Z") );


        arg = "Hello!";
        res = StringUtils.chop( arg, 2, eol );
        assertTrue( "Test 4", res.equals("Hell"));

    }

}
