package org.apache.xml.utils;

/**
 * Primary constants used by the XSLT Processor
 * @xsl.usage advanced
 */
public class Constants
{

  /** 
   * Mnemonics for standard XML Namespace URIs, as Java Strings:
   * <ul>
   * URI permanantly assigned to the "xml:" prefix. This is used for some
   * features built into the XML specification itself, such as xml:space 
   * and xml:lang. It was defined by the W3C's XML Namespaces spec.</li>
   * URI which indicates that a name may be an XSLT directive. In most
   * XSLT stylesheets, this is bound to the "xsl:" prefix. It's defined
   * by the W3C's XSLT Recommendation.</li>
   * used in early prototypes of XSLT processors for much the same purpose
   * as S_XSLNAMESPACEURL. It is now considered obsolete, and the version
   * of XSLT which it signified is not fully compatable with the final
   * XSLT Recommendation, so what it really signifies is a badly obsolete
   * stylesheet.</li>
   * </ul> */
  public static final String 

  /** Authorship mnemonics, as Java Strings. Not standardized, 
   * as far as I know.
   * <ul>
   * <li>S_VENDOR -- the name of the organization/individual who published
   * this XSLT processor. </li>
   * <li>S_VENDORURL -- URL where one can attempt to retrieve more
   * information about this publisher and product.</li>
   * </ul>
   */
  public static final String 
	S_VENDOR = "Apache Software Foundation", 

  /** S_BUILTIN_EXTENSIONS_URL is a mnemonic for the XML Namespace 
   * built-in XSLT Extensions. When used in stylesheets, this is often 
   * bound to the "xalan:" prefix.
   */
  public static final String 

  /**
   * The old built-in extension url. It is still supported for
   * backward compatibility.
   */
  public static final String 
  
  /**
   * Xalan extension namespaces.
   */
  public static final String 
  
  /**
   * EXSLT extension namespaces.
   */
  public static final String
    
    
  /**
   * The minimum version of XSLT supported by this processor.
   */
  public static final double XSLTVERSUPPORTED = 1.0;
}
