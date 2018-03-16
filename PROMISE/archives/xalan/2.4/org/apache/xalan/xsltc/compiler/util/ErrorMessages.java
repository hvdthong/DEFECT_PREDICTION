package org.apache.xalan.xsltc.compiler.util;

import java.util.Vector;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class ErrorMessages extends ResourceBundle {

    private static final String errorMessages[] = { 
	"More than one stylesheet defined in the same file.",
	"Template ''{0}'' already defined in this stylesheet.",
	"Template ''{0}'' not defined in this stylesheet.",
	"Variable ''{0}'' is multiply defined in the same scope.",
	"Variable or parameter ''{0}'' is undefined.",
	"Cannot find class ''{0}''.",
	"Cannot find external method ''{0}'' (must be public).",
	"Cannot convert argument/return type in call to method ''{0}''",
	"File or URI ''{0}'' not found.",
	"Invalid URI ''{0}''.",
	"Cannot open file or URI ''{0}''.",
	"<xsl:stylesheet> or <xsl:transform> element expected.",
	"Namespace prefix ''{0}'' is undeclared.",
	"Unable to resolve call to function ''{0}''.",
	"Argument to ''{0}'' must be a literal string.",
	"Error parsing XPath expression ''{0}''.",
	"Required attribute ''{0}'' is missing.",
	"Illegal character ''{0}'' in XPath expression.",
	"Illegal name ''{0}'' for processing instruction.",
	"Attribute ''{0}'' outside of element.",
	"Illegal attribute ''{0}''.",
	"Circular import/include. Stylesheet ''{0}'' already loaded.",
	"Result-tree fragments cannot be sorted (<xsl:sort> elements are "+
	"ignored). You must sort the nodes when creating the result tree.",
	"Decimal formatting ''{0}'' is already defined.",
	"XSL version ''{0}'' is not supported by XSLTC.",
	"Circular variable/parameter reference in ''{0}''.",
	"Unknown operator for binary expression.",
	"Illegal argument(s) for function call.",
	"Second argument to document() function must be a node-set.",
	"At least one <xsl:when> element required in <xsl:choose>.",
	"Only one <xsl:otherwise> element allowed in <xsl:choose>.",
	"<xsl:otherwise> can only be used within <xsl:choose>.",
	"<xsl:when> can only be used within <xsl:choose>.",
	"Only <xsl:when> and <xsl:otherwise> elements allowed in <xsl:choose>.",
	"<xsl:attribute-set> is missing the 'name' attribute.",
	"Illegal child element.",
	"You cannot call an element ''{0}''",
	"You cannot call an attribute ''{0}''",
	"Text data outside of top-level <xsl:stylesheet> element.",
	"JAXP parser not configured correctly",
	"Unrecoverable XSLTC-internal error: ''{0}''",
	"Unsupported XSL element ''{0}''.",
	"Unrecognised XSLTC extension ''{0}''.",
	"The input document is not a stylesheet "+
	"(the XSL namespace is not declared in the root element).",
	"Could not find stylesheet target ''{0}''.",
	"Not implemented: ''{0}''.",
	"The input document does not contain an XSL stylesheet.",
	"Could not parse element ''{0}''",
	"The use-attribute of <key> must be node, node-set, string or number.",
	"Output XML document version should be 1.0",
	"Unknown operator for relational expression",
	"Attempting to use non-existing attribute set ''{0}''.",
	"Cannot parse attribute value template ''{0}''.",
	"Unknown data-type in signature for class ''{0}''.",
	"Cannot convert data-type ''{0}'' to ''{1}''.",

	"This Templates does not contain a valid translet class definition.",
	"This Templates does not contain a class with the name ''{0}''.",
	"Could not load the translet class ''{0}''.",
	"Translet class loaded, but unable to create translet instance.",
	"Attempting to set ErrorListener for ''{0}'' to null",
	"Only StreamSource, SAXSource and DOMSOurce are supported by XSLTC",
	"Source object passed to ''{0}'' has no contents.",
	"Could not compile stylesheet",
	"TransformerFactory does not recognise attribute ''{0}''.",
	"setResult() must be called prior to startDocument().",
	"The transformer has no encapsulated translet object.",
	"No defined output handler for transformation result.",
	"Result object passed to ''{0}'' is invalid.",
	"Attempting to access invalid Transformer property ''{0}''.",
	"Could not crate SAX2DOM adapter: ''{0}''.",
	"XSLTCSource.build() called without systemId being set.",

	"The -i option must be used with the -o option.",

	"SYNOPSIS\n" +
	"   java org.apache.xalan.xsltc.cmdline.Compile [-o <output>]\n" +
	"      [-d <directory>] [-j <jarfile>] [-p <package>]\n" +
	"      [-n] [-x] [-s] [-u] [-v] [-h] { <stylesheet> | -i }\n\n" +
	"OPTIONS\n" +
	"   -o <output>    assigns the name <output> to the generated\n" +
	"                  translet. By default the translet name\n" +
	"                  is taken from the <stylesheet> name. This option\n"+
	"                  is ignored if compiling multiple stylesheets.\n" +
	"   -d <directory> specifies a destination directory for translet\n" +
	"   -j <jarfile>   packages translet classes into a jar file of the\n"+
 	"                  name specified as <jarfile>\n"+
	"   -p <package>   specifies a package name prefix for all generated\n"+
	"                  translet classes.\n" +
	"   -n             disables template inlining to reduce method\n" +
	"                  length.\n"+
	"   -x             turns on additional debugging message output\n" +
	"   -s             disables calling System.exit\n" +
	"   -u             interprets <stylesheet> arguments as URLs\n" +
	"   -i             forces compiler to read stylesheet from stdin\n" +
	"   -v             prints the version of the compiler\n" +
	"   -h             prints this usage statement\n",  
 
	"SYNOPSIS \n" +
	"   java org.apache.xalan.xsltc.cmdline.Transform [-j <jarfile>]\n"+
        "      [-x] [-s] [-n <iterations>] {-u <document_url> | <document>}\n" +  
        "      <class> [<param1>=<value1> ...]\n\n" +
        "   uses the translet <class> to transform an XML document \n"+
	"   specified as <document>. The translet <class> is either in\n"+
	"   the user's CLASSPATH or in the optionally specified <jarfile>.\n"+
	"OPTIONS\n" +
	"   -j <jarfile>    specifies a jarfile from which to load translet\n"+
	"   -x              turns on additional debugging message output\n" +
	"   -s              disables calling System.exit\n" +
	"   -n <iterations> runs the transformation <iterations> times and\n" +
	"                   displays profiling information\n" +
	"   -u <document_url> specifies XML input document as a URL\n", 


	"<xsl:sort> can only be used within <xsl:for-each> or <xsl:apply-templates>.",
	"Output encoding ''{0}'' is not supported on this JVM.",
	"Syntax error in ''{0}''.",
	"Cannot find external constructor ''{0}''.",
	"First argument to non-static Java function ''{0}'' is not valid object ref."
    };

    private static Vector _keys;

    static {
	_keys = new Vector();
	_keys.addElement(ErrorMsg.ERROR_MESSAGES_KEY);
	_keys.addElement(ErrorMsg.COMPILER_ERROR_KEY);
	_keys.addElement(ErrorMsg.COMPILER_WARNING_KEY);
	_keys.addElement(ErrorMsg.RUNTIME_ERROR_KEY);
    }

    public Enumeration getKeys() {
	return _keys.elements();
    }

    public Object handleGetObject(String key) {
	if (key == null) return null;
	if (key.equals(ErrorMsg.ERROR_MESSAGES_KEY))
	    return errorMessages;
 	else if (key.equals(ErrorMsg.COMPILER_ERROR_KEY))
	    return "Compiler error(s): ";
	else if (key.equals(ErrorMsg.COMPILER_WARNING_KEY))
	    return "Compiler warning(s): ";	    
 	else if (key.equals(ErrorMsg.RUNTIME_ERROR_KEY))
	    return "Translet error(s): ";
	return(null);
    }

}
