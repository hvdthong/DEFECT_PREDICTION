package org.apache.tools.ant.taskdefs.optional.scm; 
 
import com.starbase.starteam.*;
import com.starbase.util.Platform;
import java.io.*;
import java.util.*;
import org.apache.tools.ant.*;

/**
 * Checks out files from a specific StarTeam server, project, view, and
 * folder.
 * <BR><BR>
 * This program logs in to a StarTeam server and opens up the specified
 * project and view.  Then, it searches through that view for the given
 * folder (or, if you prefer, it uses the root folder).  Beginning with
 * that folder and optionally continuing recursivesly, AntStarTeamCheckOut
 * compares each file with your include and exclude filters and checks it
 * out only if appropriate.
 * <BR><BR>
 * Checked out files go to a directory you specify under the subfolder
 * named for the default StarTeam path to the view.  That is, if you
 * entered /home/cpovirk/work as the target folder, your project was named
 * "OurProject," the given view was named "TestView," and that view is
 * stored by default at "C:\projects\Test," your files would be checked
 * out to /home/cpovirk/work/Test."  I avoided using the project name in
 * the path because you may want to keep several versions of the same
 * project on your computer, and I didn't want to use the view name, as
 * there may be many "Test" or "Version 1.0" views, for example.  This
 * system's success, of course, depends on what you set the default path
 * to in StarTeam.
 * <BR><BR>
 * You can set AntStarTeamCheckOut to verbose or quiet mode.  Also, it has
 * a safeguard against overwriting the files on your computer:  If the
 * target directory you specify already exists, the program will throw a
 * BuildException.  To override the exception, set <CODE>force</CODE> to
 * true.
 * <BR><BR>
 * <B>This program makes use of functions from the StarTeam API.  As a result
 * AntStarTeamCheckOut is available only to licensed users of StarTeam and
 * requires the StarTeam SDK to function.  You must have
 * <CODE>starteam-sdk.jar</CODE> in your classpath to run this program.
 * For more information about the StarTeam API and how to license it, see
 * the link below.</B>
 *
 * @author <A HREF="mailto:chris.povirk@paytec.com">Chris Povirk</A>
 * @author <A HREF="mailto:jc.mann@paytec.com">JC Mann</A>
 * @author <A HREF="mailto:jeff.gettle@paytec.com">Jeff Gettle</A>
 * @version 1.0
 */
public class AntStarTeamCheckOut extends org.apache.tools.ant.Task 
{
    /**
     * By default, <CODE>force</CODE> is set to "false" through this field.
     * If you set <CODE>force</CODE> to "true," AntStarTeamCheckOut will
     * overwrite files in the target directory. If the target directory does
     * not exist, the <CODE>force</CODE> setting does nothing. Note that
     * <CODE>DEFAULT_FORCESETTING</CODE> and <CODE>force</CODE> are strings,
     * not boolean values. See the links below for more information.
     * 
     * @see #getForce()
     * @see #getForceAsBoolean()
     * @see #setForce(String force)
     */
    static public final String DEFAULT_FORCESETTING = "false";

    /**
     * This field is used in setting <CODE>verbose</CODE> to "false", the
     * default. If <CODE>verbose</CODE> is true, AntStarTeamCheckOut will
     * display file and directory names as it checks files out. The default
     * setting displays only a total. Note that
     * <CODE>DEFAULT_VERBOSESETTING</CODE> and <CODE>verbose</CODE> are
     * strings, not boolean values. See the links below for more
     * information.
     * 
     * @see #getVerbose()
     * @see #getVerboseAsBoolean()
     * @see #setVerbose(String verbose)
     */
    static public final String DEFAULT_VERBOSESETTING = "false";

    /**
     * <CODE>DEFAULT_RECURSIONSETTING</CODE> contains the normal setting --
     * true -- for recursion.  Thus, if you do not
     * <CODE>setRecursion("false")</CODE> somewhere in your program,
     * AntStarTeamCheckOut will check files out from all subfolders as well
     * as from the given folder.
     * 
     * @see #getRecursion()
     * @see #setRecursion(String recursion)
     */
    static public final String DEFAULT_RECURSIONSETTING = "true";

    /**
     * This constant sets the filter to include all files. This default has
     * the same result as <CODE>setIncludes("*")</CODE>.
     * 
     * @see #getIncludes()
     * @see #setIncludes(String includes)
     */
    static public final String DEFAULT_INCLUDESETTING = "*";

    /**
     * This disables the exclude filter by default. In other words, no files
     * are excluded. This setting is equivalent to
     * <CODE>setExcludes(null)</CODE>.
     * 
     * @see #getExcludes()
     * @see #setExcludes(String excludes)
     */
    static public final String DEFAULT_EXCLUDESETTING = null;

    /**
     * The default folder to search; the root folder.  Since
     * AntStarTeamCheckOut searches subfolders, by default it processes an
     * entire view.
     * 
     * @see #getFolderName()
     * @see #setFolderName(String folderName)
     */
    static public final String DEFAULT_FOLDERSETTING = null;

    /**
     * This is used when formatting the output. The directory name is
     * displayed only when it changes.
     */
    private Folder prevFolder = null;

    /**
     * This field keeps count of the number of files checked out.
     */
    private int checkedOut;

    
    /**
     * The name of the server you wish to connect to.
     */
    private String serverName = null;

    /**
     * The port on the server used for StarTeam.
     */
    private String serverPort = null;

    /**
     * The name of your project.
     */
    private String projectName = null;

    /**
     * The name of the folder you want to check out files from. All
     * subfolders will be searched, as well.
     */
    private String folderName = DEFAULT_FOLDERSETTING;

    /**
     * The view that the files you want are in.
     */
    private String viewName = null;

    /**
     * Your username on the StarTeam server.
     */
    private String username = null;

    /**
     * Your StarTeam password.
     */
    private String password = null;

    /**
     * The path to the root folder you want to check out to. This is a local
     * directory.
     */
    private String targetFolder = null;

    /**
     * If force set to true, AntStarTeamCheckOut will overwrite files in the
     * target directory.
     */
    private String force = DEFAULT_FORCESETTING;

    /**
     * When verbose is true, the program will display all files and
     * directories as they are checked out.
     */
    private String verbose = DEFAULT_VERBOSESETTING;

    /**
     * Set recursion to false to check out files in only the given folder
     * and not in its subfolders.
     */
    private String recursion = DEFAULT_RECURSIONSETTING;


    /**
     * All files that fit this pattern are checked out.
     */
    private String includes = DEFAULT_INCLUDESETTING;

    /**
     * All files fitting this pattern are ignored.
     */
    private String excludes = DEFAULT_EXCLUDESETTING;
    
    /**
     * The file delimitor on the user's system.
     */
    private String delim = Platform.getFilePathDelim();
    
    /**
     * Do the execution.
     * 
     * @exception BuildException
     */
    public void execute() throws BuildException
    {
        if ( getServerName() == null )
        {
            project.log("ServerName must not be null.");
            return;
        }
        if ( getServerPort() == null )
        {
            project.log("ServerPort must not be null.");
            return;
        }
        if ( getProjectName() == null )
        {
            project.log("ProjectName must not be null.");
            return;
        }
        if ( getViewName() == null )
        {
            project.log("ViewName must not be null.");
            return;
        }
        if ( getUsername() == null )
        {
            project.log("Username must not be null.");
            return;
        }
        if ( getPassword() == null )
        {
            project.log("Password must not be null.");
            return;
        }
        if ( getTargetFolder() == null )
        {
            project.log("TargetFolder must not be null.");
            return;
        }

        if (null != getTargetFolder())
        {
            if ((getTargetFolder().endsWith("/") || 
                 getTargetFolder().endsWith("\\")) && getTargetFolder().length() > 1)
            {
                setTargetFolder(getTargetFolder().substring(0, getTargetFolder().length() - 1));
            }
        }

        if ( null != getFolderName() )
        {
            if ((getFolderName().endsWith("/") || 
                 getFolderName().endsWith("\\")) && getFolderName().length() > 1)
            {
                setFolderName(getFolderName().substring(0, getFolderName().length() - 1));
            }
        }

        java.io.File dirExist = new java.io.File(getTargetFolder());
        if (dirExist.isDirectory() && !getForceAsBoolean())
        {
            project.log( "Target directory exists. Set \"force\" to \"true\" " +
                         "to continue anyway." );
            return;
        }

        try
        {
            Server s = getServer();

            runServer(s);

            s.disconnect();


            project.log(checkedOut + " files checked out.");
        }
        catch (Throwable e)
        {
            project.log("    " + e.getMessage());
        }
    }

    /**
     * Creates and logs in to a StarTeam server.
     * 
     * @return A StarTeam server.
     */
    protected Server getServer()
    {
        Server s = new Server(getServerName(), getServerPortAsInt());

        s.connect();

        s.logOn(getUsername(), getPassword());

        return s;
    }

    /**
     * Searches for the specified project on the server.
     * 
     * @param s      A StarTeam server.
     */
    protected void runServer(Server s)
    {
        com.starbase.starteam.Project[] projects = s.getProjects();
        for (int i = 0; i < projects.length; i++)
        {
            com.starbase.starteam.Project p = projects[i];
            
            if (p.getName().equals(getProjectName()))
            {
                if (getVerboseAsBoolean())
                {
                    project.log("Found " + getProjectName() + delim);
                }
                runProject(s, p);
                break;
            }
        }
    }

    /**
     * Searches for the given view in the project.
     * 
     * @param s      A StarTeam server.
     * @param p      A valid project on the given server.
     */
    protected void runProject(Server s, com.starbase.starteam.Project p)
    {
        View[] views = p.getViews();
        for (int i = 0; i < views.length; i++)
        {
            View v = views[i];
            if (v.getName().equals(getViewName()))
            {
                if (getVerboseAsBoolean())
                {
                    project.log("Found " + getProjectName() + delim + getViewName() + delim);
                }
                runType(s, p, v, s.typeForName((String)s.getTypeNames().FILE));
                break;
            }
        }
    }

    /**
     * Searches for folders in the given view.
     * 
     * @param s      A StarTeam server.
     * @param p      A valid project on the server.
     * @param v      A view name from the specified project.
     * @param t      An item type which is currently always "file".
     */
    protected void runType(Server s, com.starbase.starteam.Project p, View v, Type t)
    {
        Folder f = v.getRootFolder();
        if (!(getFolderName()==null))
        {
            if (getFolderName().equals("\\") || getFolderName().equals("/"))
            {
                setFolderName(null);
            }
            else
            {
                f = StarTeamFinder.findFolder(v.getRootFolder(), getFolderName());
            }
        }

        if (getVerboseAsBoolean() && !(getFolderName()==null))
        {
            project.log( "Found " + getProjectName() + delim + getViewName() + 
                         delim + getFolderName() + delim + "\n" );
        }


        int nProperties = 2;

        Property p1 = getPrimaryDescriptor(t);

        Property p2 = getSecondaryDescriptor(t);
        if (p2 != null)
        {
            nProperties++;
        }

        String[] strNames = new String[nProperties];
        int iProperty = 0;
        strNames[iProperty++] = s.getPropertyNames().OBJECT_ID;
        strNames[iProperty++] = p1.getName();
        if (p2 != null)
        {
            strNames[iProperty++] = p2.getName();
        }

        f.populateNow(t.getName(), strNames, -1);

        runFolder(s, p, v, t, f);

        f.discardItems(t.getName(), -1);
    }

    /**
     * Searches for files in the given folder.  This method is recursive and
     * thus searches all subfolders.
     * 
     * @param s      A StarTeam server.
     * @param p      A valid project on the server.
     * @param v      A view name from the specified project.
     * @param t      An item type which is currently always "file".
     * @param f      The folder to search.
     */
    protected void runFolder( Server s, 
                              com.starbase.starteam.Project p, 
                              View v, 
                              Type t, 
                              Folder f )
    {
        Item[] items = f.getItems(t.getName());
        for (int i = 0; i < items.length; i++)
        {
            runItem(s, p, v, t, f, items[i]);
        }

        if (getRecursionAsBoolean())
        {
            Folder[] subfolders = f.getSubFolders();
            for (int i = 0; i < subfolders.length; i++)
            {
                runFolder(s, p, v, t, subfolders[i]);
            }
        }
    }

    /**
     * Check out one file if it matches the include filter but not the
     * exclude filter.
     * 
     * @param s      A StarTeam server.
     * @param p      A valid project on the server.
     * @param v      A view name from the specified project.
     * @param t      An item type which is currently always "file".
     * @param f      The folder the file is localed in.
     * @param item   The file to check out.
     */
    protected void runItem( Server s, 
                            com.starbase.starteam.Project p, 
                            View v, 
                            Type t, 
                            Folder f, 
                            Item item )
    {
        Property p1 = getPrimaryDescriptor(t);
        Property p2 = getSecondaryDescriptor(t);

        String pName = (String)item.get(p1.getName());
        boolean includeIt = false;
        boolean excludeIt = false;

        if (getIncludes()!=null)
        {
            StringTokenizer inStr = new StringTokenizer(getIncludes(), " ");
            while (inStr.hasMoreTokens())
            {
                if (match(inStr.nextToken(), pName))
                {
                    includeIt = true;
                }
            }
        }

        if (getExcludes()!=null)
        {
            StringTokenizer exStr = new StringTokenizer(getExcludes(), " ");
            while (exStr.hasMoreTokens())
            {
                if (match(exStr.nextToken(), pName))
                {
                    excludeIt = true;
                }
            }
        }

        if (!includeIt | excludeIt)
        {
            return;
        }

        if (getVerboseAsBoolean())
        {
            boolean bShowHeader = true;
            if (f != prevFolder)
            {
                String strFolder = f.getFolderHierarchy();
                int i = strFolder.indexOf(delim);
                if (i >= 0)
                {
                    strFolder = strFolder.substring(i+1);
                }
                System.out.println("            Folder: \"" + strFolder + "\"");
                prevFolder = f;
            }
            else
                bShowHeader        = false;

            if (bShowHeader)
            {
                System.out.print("                Item");
                System.out.print(",\t" + p1.getDisplayName());
                if (p2 != null)
                {
                    System.out.print(",\t" + p2.getDisplayName());
                }
                System.out.println("");
            }


            System.out.print("                " + item.getItemID());

            System.out.print(",\t" + formatForDisplay(p1, item.get(p1.getName())));

            if (p2 != null)
            {
                System.out.print(",\t" + formatForDisplay(p2, item.get(p2.getName())));
            }

            int locker = item.getLocker();
            if (locker>-1)
            {
                System.out.println(",\tLocked by " + locker);
            }
            else
            {
                System.out.println(",\tNot locked");
            }
        }


        com.starbase.starteam.File remote = (com.starbase.starteam.File)item;

        String dirName = v.getDefaultPath();
        dirName = dirName.replace('\\', '/');
        int endDirIndex = dirName.length();
        if (dirName.endsWith("/"))
        {
            endDirIndex--;
        }
        dirName = 
            dirName.substring(dirName.lastIndexOf("/", dirName.length() - 2) + 1, endDirIndex);
                
        StringTokenizer pathTokenizer = 
            new StringTokenizer(item.getParentFolder().getFolderHierarchy(), delim);
        String localName = delim;
        String currentToken = null;
        while (pathTokenizer.hasMoreTokens())
        {
            currentToken = pathTokenizer.nextToken();
            if (currentToken.equals(getProjectName()))
            {
                currentToken = dirName;
            }
            localName += currentToken + delim;
        }
        java.io.File local = new java.io.File( getTargetFolder() + localName + 
                                               item.get(p1.getName()) );
        try
        {
            remote.checkoutTo(local, Item.LockType.UNCHANGED, false, true, true);
        }
        catch (Throwable e)
        {
            project.log("    " + e.getMessage());
        }
        checkedOut++;
    }

    /**
     * Get the primary descriptor of the given item type.
     *  Returns null if there isn't one.
     *  In practice, all item types have a primary descriptor.
     * 
     * @param t      An item type. At this point it will always be "file".
     * @return The specified item's primary descriptor.
     */
    protected Property getPrimaryDescriptor(Type t)
    {
        Property[] properties = t.getProperties();
        for (int i = 0; i < properties.length; i++)
        {
            Property p = properties[i];
            if (p.isPrimaryDescriptor())
            {
                return p;
            }
        }
        return null;
    }

    /**
     * Get the secondary descriptor of the given item type.
     * Returns null if there isn't one.
     * 
     * @param t      An item type. At this point it will always be "file".
     * @return The specified item's secondary descriptor. There may not be
     *         one for every file.
     */
    protected Property getSecondaryDescriptor(Type t)
    {
        Property[] properties = t.getProperties();
        for (int i = 0; i < properties.length; i++)
        {
            Property p = properties[i];
            if (p.isDescriptor() && !p.isPrimaryDescriptor())
            {
                return p;
            }
        }
        return null;
    }

    /**
     * Formats a property value for display to the user.
     * 
     * @param p      An item property to format.
     * @param value
     * @return A string containing the property, which is truncated to 35
     *         characters for display.
     */
    protected String formatForDisplay(Property p, Object value)
    {
        if (p.getTypeCode() == Property.Types.TEXT)
        {
            String str = value.toString();
            if (str.length() > 35)
            {
                str = str.substring(0, 32) + "...";
            }
            return "\"" + str + "\"";
        }
        else
        {
            if (p.getTypeCode() == Property.Types.ENUMERATED)
            {
                return "\"" + p.getEnumDisplayName(((Integer)value).intValue()) + "\"";
            }
            else
            {
                return value.toString();
            }
        }
    }


    /**
     * <B>TORN STRAIGHT FROM ANT.DIRECTORYSCANNER</B>
     * 
     * Matches a string against a pattern. The pattern contains two special
     * characters:<BR>
     * '*' which means zero or more characters,<BR>
     * '?' which means one and only one character.
     * 
     * @param pattern the (non-null) pattern to match against
     * @param str     the (non-null) string that must be matched against the
     *                pattern
     * @return <code>true</code> when the string matches against the
     *         pattern, <code>false</code> otherwise.
     */
    private static boolean match(String pattern, String str)
    {
        char[] patArr = pattern.toCharArray();
        char[] strArr = str.toCharArray();
        int patIdxStart = 0;
        int patIdxEnd   = patArr.length-1;
        int strIdxStart = 0;
        int strIdxEnd   = strArr.length-1;
        char ch;

        boolean containsStar = false;
        for (int i = 0; i < patArr.length; i++)
        {
            if (patArr[i] == '*')
            {
                containsStar = true;
                break;
            }
        }

        if (!containsStar)
        {
            if (patIdxEnd != strIdxEnd)
            {
            }
            for (int i = 0; i <= patIdxEnd; i++)
            {
                ch = patArr[i];
                if (ch != '?' && ch != strArr[i])
                {
                }
            }
        }

        if (patIdxEnd == 0)
        {
        }

        while ((ch = patArr[patIdxStart]) != '*' && strIdxStart <= strIdxEnd)
        {
            if (ch != '?' && ch != strArr[strIdxStart])
            {
                return false;
            }
            patIdxStart++;
            strIdxStart++;
        }
        if (strIdxStart > strIdxEnd)
        {
            for (int i = patIdxStart; i <= patIdxEnd; i++)
            {
                if (patArr[i] != '*')
                {
                    return false;
                }
            }
            return true;
        }

        while ((ch = patArr[patIdxEnd]) != '*' && strIdxStart <= strIdxEnd)
        {
            if (ch != '?' && ch != strArr[strIdxEnd])
            {
                return false;
            }
            patIdxEnd--;
            strIdxEnd--;
        }
        if (strIdxStart > strIdxEnd)
        {
            for (int i = patIdxStart; i <= patIdxEnd; i++)
            {
                if (patArr[i] != '*')
                {
                    return false;
                }
            }
            return true;
        }

        while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd)
        {
            int patIdxTmp = -1;
            for (int i = patIdxStart+1; i <= patIdxEnd; i++)
            {
                if (patArr[i] == '*')
                {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == patIdxStart+1)
            {
                patIdxStart++;
                continue;
            }
            int patLength = (patIdxTmp-patIdxStart-1);
            int strLength = (strIdxEnd-strIdxStart+1);
            int foundIdx  = -1;
        strLoop:
            for (int i = 0; i <= strLength - patLength; i++)
            {
                for (int j = 0; j < patLength; j++)
                {
                    ch = patArr[patIdxStart+j+1];
                    if (ch != '?' && ch != strArr[strIdxStart+i+j])
                    {
                        continue strLoop;
                    }
                }

                foundIdx = strIdxStart+i;
                break;
            }

            if (foundIdx == -1)
            {
                return false;
            }

            patIdxStart = patIdxTmp;
            strIdxStart = foundIdx+patLength;
        }

        for (int i = patIdxStart; i <= patIdxEnd; i++)
        {
            if (patArr[i] != '*')
            {
                return false;
            }
        }
        return true;
    }


    /**
     * Sets the <CODE>serverName</CODE> attribute to the given value.
     * 
     * @param serverName The name of the server you wish to connect to.
     * @see #getServerName()
     */
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    /**
     * Gets the <CODE>serverName</CODE> attribute.
     * 
     * @return The StarTeam server to log in to.
     * @see #setServerName(String serverName)
     */
    public String getServerName()
    {
        return serverName;
    }

    /**
     * Sets the <CODE>serverPort</CODE> attribute to the given value. The
     * given value must be a valid integer, but it must be a string object.
     * 
     * @param serverPort A string containing the port on the StarTeam server
     *                   to use.
     * @see #getServerPort()
     */
    public void setServerPort(String serverPort)
    {
        this.serverPort = serverPort;
    }

    /**
     * Gets the <CODE>serverPort</CODE> attribute.
     * 
     * @return A string containing the port on the StarTeam server to use.
     * @see #getServerPortAsInt()
     * @see #setServerPort(String serverPort)
     */
    public String getServerPort()
    {
        return serverPort;
    }

    /**
     * Gets the <CODE>serverPort</CODE> attribute as an integer.
     * 
     * @return An integer value for the port on the StarTeam server to use.
     * @see #getServerPort()
     * @see #setServerPort(String serverPort)
     */
    public int getServerPortAsInt()
    {
        return Integer.parseInt(serverPort);
    }

    /**
     * Sets the <CODE>projectName</CODE> attribute to the given value.
     * 
     * @param projectName
     *               The StarTeam project to search.
     * @see #getProjectName()
     */
    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    /**
     * Gets the <CODE>projectName</CODE> attribute.
     * 
     * @return The StarTeam project to search.
     * @see #setProjectName(String projectName)
     */
    public String getProjectName()
    {
        return projectName;
    }

    /**
     * Sets the <CODE>viewName</CODE> attribute to the given value.
     * 
     * @param viewName The view to find the specified folder in.
     * @see #getViewName()
     */
    public void setViewName(String viewName)
    {
        this.viewName = viewName;
    }

    /**
     * Gets the <CODE>viewName</CODE> attribute.
     * 
     * @return The view to find the specified folder in.
     * @see #setViewName(String viewName)
     */
    public String getViewName()
    {
        return viewName;
    }

    /**
     * Sets the <CODE>folderName</CODE> attribute to the given value. To
     * search the root folder, use a slash or backslash, or simply don't set
     * a folder at all.
     * 
     * @param folderName The subfolder from which to check out files.
     * @see #getFolderName()
     */
    public void setFolderName(String folderName)
    {
        this.folderName = folderName;
    }

    /**
     * Gets the <CODE>folderName</CODE> attribute.
     *
     * @return The subfolder from which to check out files. All subfolders
     * will be searched, as well.
     * @see #setFolderName(String folderName)
     */
    public String getFolderName()
    {
        return folderName;
    }

    /**
     * Sets the <CODE>username</CODE> attribute to the given value.
     * 
     * @param username Your username for the specified StarTeam server.
     * @see #getUsername()
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Gets the <CODE>username</CODE> attribute.
     * 
     * @return The username given by the user.
     * @see #setUsername(String username)
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the <CODE>password</CODE> attribute to the given value.
     * 
     * @param password Your password for the specified StarTeam server.
     * @see #getPassword()
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Gets the <CODE>password</CODE> attribute.
     * 
     * @return The password given by the user.
     * @see #setPassword(String password)
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets the <CODE>targetFolder</CODE> attribute to the given value.
     * 
     * @param target The target path on the local machine to check out to.
     * @see #getTargetFolder()
     */
    public void setTargetFolder(String targetFolder)
    {
        this.targetFolder = targetFolder;
    }

    /**
     * Gets the <CODE>targetFolder</CODE> attribute.
     * 
     * @return The target path on the local machine to check out to.
     *
     * @see #setTargetFolder(String targetFolder)
     */
    public String getTargetFolder()
    {
        return targetFolder;
    }

    /**
     * Sets the <CODE>force</CODE> attribute to the given value.
     * 
     * @param force  A string containing "true" or "false" that tells the
     *               application whether to continue if the target directory
     *               exists.  If <CODE>force</CODE> is true,
     *               AntStarTeamCheckOut will overwrite files in the target
     *               directory.  By default it set to false as a safeguard.
     *               Note that if the target directory does not exist, this
     *               setting has no effect.
     * @see #DEFAULT_FORCESETTING
     * @see #getForce()
     * @see #getForceAsBoolean()
     */
    public void setForce(String force)
    {
        this.force = force;
    }

    /**
     * Gets the <CODE>force</CODE> attribute.
     * 
     * @return A string containing "true" or "false" telling the application
     *         whether to continue if the target directory exists.  If
     *         <CODE>force</CODE> is true, AntStarTeamCheckOut will
     *         overwrite files in the target directory. If it is false and
     *         the target directory exists, AntStarTeamCheckOut will exit
     *         with a warning.  If the target directory does not exist, this
     *         setting has no effect. The default setting is false.
     * @see #DEFAULT_FORCESETTING
     * @see #getForceAsBoolean()
     * @see #setForce(String force)
     */
    public String getForce()
    {
        return force;
    }

    /**
     * Gets the <CODE>force</CODE> attribute as a boolean value.
     * 
     * @return A boolean value telling whether to continue if the target
     *         directory exists.
     * @see #DEFAULT_FORCESETTING
     * @see #getForce()
     * @see #setForce(String force)
     */
    public boolean getForceAsBoolean()
    {
        return project.toBoolean(force);
    }

    /**
     * Turns recursion on or off.
     *
     * @param verbose A string containing "true" or "false."  If it is true,
     *                the default, subfolders are searched recursively for
     *                files to check out.  Otherwise, only files specified
     *                by <CODE>folderName</CODE> are scanned.
     * @see #DEFAULT_RECURSIONSETTING
     * @see #getRecursion()
     * @see #getRecursionAsBoolean()
     */
    public void setRecursion(String recursion)
    {
        this.recursion = recursion;
    }

    /**
     * Gets the <CODE>recursion</CODE> attribute, which tells
     * AntStarTeamCheckOut whether to search subfolders when checking out
     * files.
     *
     * @return A string telling whether <CODE>recursion</CODE> is "true" or
     * "false."
     *
     * @see #DEFAULT_RECURSIONSETTING
     * @see #getRecursionAsBoolean()
     * @see #setRecursion(String recursion)
     */
    public String getRecursion()
    {
        return recursion;
    }

    /**
     * Gets the <CODE>recursion</CODE> attribute as a boolean value.
     *
     * @return A boolean value telling whether subfolders of
     * <CODE>folderName</CODE> will be scanned for files to check out.
     *
     * @see #DEFAULT_RECURSIONSETTING
     * @see #getRecursion()
     * @see #setRecursion(String recursion)
     */
    public boolean getRecursionAsBoolean()
    {
        return project.toBoolean(recursion);
    }

    /**
     * Sets the <CODE>verbose</CODE> attribute to the given value.
     * 
     * @param verbose A string containing "true" or "false" to tell
     *                AntStarTeamCheckOut whether to display files as they
     *                are checked out.  By default it is false, so the
     *                program only displays the total number of files unless
     *                you override this default.
     * @see #DEFAULT_FORCESETTING
     * @see #getForce()
     * @see #getForceAsBoolean()
     */
    public void setVerbose(String verbose)
    {
        this.verbose = verbose;
    }
    
    /**
     * Gets the <CODE>verbose</CODE> attribute.
     * 
     * @return A string containing "true" or "false" telling the application
     *         to display all files as it checks them out.  By default it is
     *         false, so the program only displays the total number of
     *         files.
     * @see #DEFAULT_VERBOSESETTING
     * @see #getVerboseAsBoolean()
     * @see #setVerbose(String verbose)
     */
    public String getVerbose()
    {
        return verbose;
    }

    /**
     * Gets the <CODE>verbose</CODE> attribute as a boolean value.
     * 
     * @return A boolean value telling whether to display all files as they
     *         are checked out.
     * @see #DEFAULT_VERBOSESETTING
     * @see #getVerbose()
     * @see #setVerbose(String verbose)
     */
    public boolean getVerboseAsBoolean()
    {
        return project.toBoolean(verbose);
    }


    /**
     * Sets the include filter. When filtering files, AntStarTeamCheckOut
     * uses an unmodified version of <CODE>DirectoryScanner</CODE>'s
     * <CODE>match</CODE> method, so here are the patterns straight from the
     * Ant source code:
     * <BR><BR>
     * Matches a string against a pattern. The pattern contains two special
     * characters:
     * <BR>'*' which means zero or more characters,
     * <BR>'?' which means one and only one character.
     * <BR><BR>
     * I would have used the Ant method directly from its class, but
     * <CODE>match</CODE> is a private member, so I cannot access it from
     * this program.
     * <BR><BR>
     * Separate multiple inlcude filters by <I>spaces</I>, not commas as Ant
     * uses. For example, if you want to check out all .java and .class\
     * files, you would put the following line in your program:
     * <CODE>setIncludes("*.java *.class");</CODE>
     * Finally, note that filters have no effect on the <B>directories</B>
     * that are scanned; you could not check out files from directories with
     * names beginning only with "build," for instance. Of course, you
     * could limit AntStarTeamCheckOut to a particular folder and its
     * subfolders with the <CODE>setFolderName(String folderName)</CODE>
     * command.
     * <BR><BR>
     * Treatment of overlapping inlcudes and excludes: To give a simplistic
     * example suppose that you set your include filter to "*.htm *.html"
     * and your exclude filter to "index.*". What happens to index.html?
     * AntStarTeamCheckOut will not check out index.html, as it matches an
     * exclude filter ("index.*"), even though it matches the include
     * filter, as well.
     * <BR><BR>
     * Please also read the following sections before using filters:
     * 
     * @param includes A string of filter patterns to include. Separate the
     *                 patterns by spaces.
     * @see #getIncludes()
     * @see #setExcludes(String excludes)
     * @see #getExcludes()
     */
    public void setIncludes(String includes)
    {
        this.includes = includes;
    }

    /**
     * Gets the patterns from the include filter. Rather that duplicate the
     * details of AntStarTeanCheckOut's filtering here, refer to these
     * links:
     * 
     * @return A string of filter patterns separated by spaces.
     * @see #setIncludes(String includes)
     * @see #setExcludes(String excludes)
     * @see #getExcludes()
     */
    public String getIncludes()
    {
        return includes;
    }

    /**
     * Sets the exclude filter. When filtering files, AntStarTeamCheckOut
     * uses an unmodified version of <CODE>DirectoryScanner</CODE>'s
     * <CODE>match</CODE> method, so here are the patterns straight from the
     * Ant source code:
     * <BR><BR>
     * Matches a string against a pattern. The pattern contains two special
     * characters:
     * <BR>'*' which means zero or more characters,
     * <BR>'?' which means one and only one character.
     * <BR><BR>
     * I would have used the Ant method directly from its class, but
     * <CODE>match</CODE> is a private member, so I cannot access it from
     * this program.
     * <BR><BR>
     * Separate multiple exlcude filters by <I>spaces</I>, not commas as Ant
     * uses. For example, if you want to check out all files except .XML and
     * .HTML files, you would put the following line in your program:
     * <CODE>setExcludes("*.XML *.HTML");</CODE>
     * Finally, note that filters have no effect on the <B>directories</B>
     * that are scanned; you could not skip over all files in directories
     * whose names begin with "project," for instance.
     * <BR><BR>
     * Treatment of overlapping inlcudes and excludes: To give a simplistic
     * example suppose that you set your include filter to "*.htm *.html"
     * and your exclude filter to "index.*". What happens to index.html?
     * AntStarTeamCheckOut will not check out index.html, as it matches an
     * exclude filter ("index.*"), even though it matches the include
     * filter, as well.
     * <BR><BR>
     * Please also read the following sections before using filters:
     * 
     * @param excludes A string of filter patterns to exclude. Separate the
     *                 patterns by spaces.
     * @see #setIncludes(String includes)
     * @see #getIncludes()
     * @see #getExcludes()
     */
    public void setExcludes(String excludes)
    {
        this.excludes = excludes;
    }

    /**
     * Gets the patterns from the exclude filter. Rather that duplicate the
     * details of AntStarTeanCheckOut's filtering here, refer to these
     * links:
     * 
     * @return A string of filter patterns separated by spaces.
     * @see #setExcludes(String excludes)
     * @see #setIncludes(String includes)
     * @see #getIncludes()
     */
    public String getExcludes()
    {
        return excludes;
    }
}
