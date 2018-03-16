import java.util.Stack;

/**
 * Exception for #parse() problems
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ParseDirectiveException.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class ParseDirectiveException extends Exception
{
    private Stack filenameStack = new Stack();
    private String msg = "";
    private int depthCount = 0;

    /**
     * Constructor
     */
    ParseDirectiveException( String m, int i )
    {
        msg = m;
        depthCount = i;
    }

    /**
     * Get a message.
     */
    public String getMessage()
    {
        String returnStr  =  "#parse() exception : depth = " + 
            depthCount + " -> " + msg;

        returnStr += " File stack : ";

        try
        {
            while( !filenameStack.empty())
            {
                returnStr += (String) filenameStack.pop();
                returnStr += " -> ";
            }
        }
        catch( Exception e)
        {
        }

        return returnStr;
    }

    /**
     * Add a file to the filename stack
     */
    public void addFile( String s )
    {
        filenameStack.push( s );
    }

}
