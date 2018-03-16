package org.apache.xalan.xsltc.cmdline;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import org.apache.xalan.xsltc.cmdline.getopt.GetOpt;
import org.apache.xalan.xsltc.cmdline.getopt.GetOptsException;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author G. Todd Miller
 * @author Morten Jorgensen
 */
public final class Compile {

    private static int VERSION_MAJOR = 1;
    private static int VERSION_MINOR = 4;
    private static int VERSION_DELTA = 0;
 

    private static boolean _allowExit = true;

    public static void printUsage() {
        StringBuffer vers = new StringBuffer("XSLTC version " + 
	    VERSION_MAJOR + "." + VERSION_MINOR + 
	    ((VERSION_DELTA > 0) ? ("."+VERSION_DELTA) : ("")));
	System.err.println(vers + "\n" + 
		new ErrorMsg(ErrorMsg.COMPILE_USAGE_STR));
	if (_allowExit) System.exit(-1);
    }

    /** 
     * This method implements the command line compiler. See the USAGE_STRING
     * constant for a description. It may make sense to move the command-line
     * handling to a separate package (ie. make one xsltc.cmdline.Compiler
     * class that contains this main() method and one xsltc.cmdline.Transform
     * class that contains the DefaultRun stuff).
     */
    public static void main(String[] args) {
	try {
	    boolean inputIsURL = false;
	    boolean useStdIn = false;
	    boolean classNameSet = false;
	    final GetOpt getopt = new GetOpt(args, "o:d:j:p:uxhsinv");
	    if (args.length < 1) printUsage();

	    final XSLTC xsltc = new XSLTC();
	    xsltc.init();

	    int c;
	    while ((c = getopt.getNextOption()) != -1) {
		switch(c) {
		case 'i':
		    useStdIn = true;
		    break;
		case 'o':
		    xsltc.setClassName(getopt.getOptionArg());
		    classNameSet = true;
		    break;
		case 'd':
		    xsltc.setDestDirectory(getopt.getOptionArg());
		    break;
		case 'p':
		    xsltc.setPackageName(getopt.getOptionArg());
		    break;
		case 'j':  
		    xsltc.setJarFileName(getopt.getOptionArg());
		    break;
		case 'x':
		    xsltc.setDebug(true);
		    break;
		case 'u':
		    inputIsURL = true;
		    break;
		case 's':
		    _allowExit = false;
		    break;
		case 'n':
		    break;
		case 'v':
		case 'h':
		default:
		    printUsage();
		    break; 
		}
	    }

	    boolean compileOK;

	    if (useStdIn) {
		if (!classNameSet) {
		    System.err.println(new ErrorMsg(ErrorMsg.COMPILE_STDIN_ERR));
		    if (_allowExit) System.exit(-1);
		}
		compileOK = xsltc.compile(System.in, xsltc.getClassName());
	    }
	    else {
		final String[] stylesheetNames = getopt.getCmdArgs();
		final Vector   stylesheetVector = new Vector();
		for (int i = 0; i < stylesheetNames.length; i++) {
		    final String name = stylesheetNames[i];
		    URL url;
		    if (inputIsURL)
			url = new URL(name);
		    else
			url = (new File(name)).toURL();
		    stylesheetVector.addElement(url);
		}
		compileOK = xsltc.compile(stylesheetVector);
	    }

	    if (compileOK) {
		xsltc.printWarnings();
		if (xsltc.getJarFileName() != null) xsltc.outputToJar();
		if (_allowExit) System.exit(0);
	    }
	    else {
		xsltc.printWarnings();
		xsltc.printErrors();
		if (_allowExit) System.exit(-1);
	    }
	}
	catch (GetOptsException ex) {
	    System.err.println(ex);
	}
	catch (Exception e) {
	    e.printStackTrace();
	    if (_allowExit) System.exit(-1);
	}
    }

}
