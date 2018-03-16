import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.oro.text.perl.Perl5Util;
import org.apache.velocity.util.StringUtils;
import org.apache.tools.ant.DirectoryScanner;

/**
 * This class will convert a WebMacro template to
 * a Velocity template. Uses the ORO Regexp package to do the
 * rewrites. Note, it isn't 100% perfect, but will definitely get
 * you about 99.99% of the way to a converted system. Please
 * see the website documentation for more information on how to use
 * this class.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @version $Id: WebMacro.java 463298 2006-10-12 16:10:32Z henning $
 */
public class WebMacro
{
    /**
     *
     */
    protected static final String VM_EXT = ".vm";

    /**
     *
     */
    protected static final String WM_EXT = ".wm";

    /**
     * The regexes to use for line by line substition. The regexes
     * come in pairs. The first is the string to match, the second is
     * the substitution to make.
     */
    protected static String[] perLineREs =
    {
        "#if\\s*[(]\\s*(.*\\S)\\s*[)]\\s*(#begin|{)[ \\t]?",
        "#if( $1 )",

        "[ \\t]?(#end|})[ \\t]*\n(\\s*)#else\\s*(#begin|{)[ \\t]?(\\w)",
        "[ \\t]?(#end|})[ \\t]*\n(\\s*)#else\\s*(#begin|{)[ \\t]?",
        "$2#else",
        "(#end|})(\\s*#else)\\s*(#begin|{)[ \\t]?",
        "$1\n$2",

        "#foreach\\s+(\\$\\w+)\\s+in\\s+(\\$[^\\s#]+)\\s*(#begin|{)[ \\t]?",
        "#foreach( $1 in $2 )",

        "#set\\s+(\\$[^\\s=]+)\\s*=\\s*([\\S \\t]+)",
        "#set( $1 = $2 )",
        ")$1",

        "#parse\\s+([^\\s#]+)[ \\t]?",
        "#parse( $1 )",

        "#include\\s+([^\\s#]+)[ \\t]?",
        "#include( $1 )",

        "\\$\\(([^\\)]+)\\)",
        "${$1}",
        "${$1($2)}",

        "\\$_",
        "$l_",
        "${l$1}",

        "(#set\\s*\\([^;]+);(\\s*\\))",
        "$1$2",

        "(^|[^\\\\])\\$(\\w[^=\n;'\"]*);",
        "$1${$2}",

        "\\.wm",
        ".vm"
    };

    /**
     * Iterate through the set of find/replace regexes
     * that will convert a given WM template to a VM template
     * @param target
     */
    public void convert(String target)
    {
        File file = new File(target);

        if (!file.exists())
        {
            throw new RuntimeException("The specified template or directory does not exist");
        }

        if (file.isDirectory())
        {
            String basedir = file.getAbsolutePath();
            String newBasedir = basedir + VM_EXT;

            DirectoryScanner ds = new DirectoryScanner();
            ds.setBasedir(basedir);
            ds.addDefaultExcludes();
            ds.scan();
            String[] files = ds.getIncludedFiles();

            for (int i = 0; i < files.length; i++)
            {
                writeTemplate(files[i], basedir, newBasedir);
            }
        }
        else
        {
            writeTemplate(file.getAbsolutePath(), "", "");
        }
    }

    /**
     * Write out the converted template to the given named file
     * and base directory.
     */
    private boolean writeTemplate(String file, String basedir,
                                  String newBasedir)
    {
        if (file.indexOf(WM_EXT) < 0)
        {
            return false;
        }

        System.out.println("Converting " + file + "...");

        String template = file;
        String newTemplate = convertName(file);

        if (basedir.length() > 0)
        {
            String templateDir = newBasedir + extractPath(file);
            File outputDirectory = new File(templateDir);

            template = basedir + File.separator + file;


            if (! outputDirectory.exists())
            {
                outputDirectory.mkdirs();
            }

            newTemplate = newBasedir + File.separator + convertName(file);
        }

        String convertedTemplate = convertTemplate(template);

        FileWriter fw = null;
        try
        {
            fw = new FileWriter(newTemplate);
            fw.write(convertedTemplate);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fw != null)
            {
                try
                {
                    fw.close();
                }
                catch (IOException io)
                {
                }
            }
        }

        return true;
    }

    /**
     * Gets the path segment of the full path to a file (i.e. one
     * which originally included the file name).
     */
    private final String extractPath(String file)
    {
        int lastSepPos = file.lastIndexOf(File.separator);
        return (lastSepPos == -1 ? "" :
                File.separator + file.substring(0, lastSepPos));
    }

    /**
     * Simple extension conversion of .wm to .vm
     */
    private String convertName(String name)
    {
        return (name.indexOf(WM_EXT) < 0)
                ? name
                : name.substring(0, name.indexOf(WM_EXT)) + VM_EXT;
    }

    /**
     * How to use this little puppy :-)
     */
    private static final void usage()
    {
        System.err.println("Usage: convert-wm <template.wm | directory>");
    }

    /**
     * Apply find/replace regexes to our WM template
     * @param template
     * @return Returns the template with all regexprs applied.
     */
    public String convertTemplate(String template)
    {
        String contents = StringUtils.fileContentsToString(template);

        if (!contents.endsWith("\n"))
        {
            contents += "\n";
        }

        Perl5Util perl = new Perl5Util();
        for (int i = 0; i < perLineREs.length; i += 2)
        {
            contents = perl.substitute(makeSubstRE(i), contents);
        }

        if (perl.match("m/javascript/i", contents))
        {
            contents = perl.substitute("s/\n}/\n#end/g", contents);
        }
        else
        {
            contents = perl.substitute("s/(\n\\s*)}/$1#end/g", contents);
            contents = perl.substitute("s/#end\\s*\n\\s*#else/#else/g",
                                       contents);
        }

        return contents;
    }

    /**
     * Makes a Perl 5 regular expression for use by ORO.
     */
    private final String makeSubstRE(int i)
    {
        return ("s/" + perLineREs[i] + '/' + perLineREs[i + 1] + "/g");
    }

    /**
     * Main hook for the conversion process.
     * @param args
     */
    public static void main(String[] args)
    {
        if (args.length > 0)
        {
            for (int x=0; x < args.length; x++)
            {
                WebMacro converter = new WebMacro();
                converter.convert(args[x]);
            }
        }
        else
        {
            usage();
        }
    }
}
