package org.apache.xml.serializer;

/**
 * This class defines the constants which are the names of the four default
 * output methods.
 * <p>
 * Three default output methods are defined: XML, HTML, and TEXT. 
 * These constants can be used as an argument to the
 * OutputPropertiesFactory.getDefaultMethodProperties() method to get
 * the properties to create a serializer.
 * 
 * This class is a public API.
 * 
 * @see OutputPropertiesFactory
 * @see Serializer
 * 
 * @xsl.usage general
 */
public final class Method
{
    /**
     * A private constructor to prevent the creation of such a class.
     */
    private Method() {
        
    }

  /**
   * The output method type for XML documents: <tt>xml</tt>.
   */
  public static final String XML = "xml";

  /**
   * The output method type for HTML documents: <tt>html</tt>.
   */
  public static final String HTML = "html";

  /**
   * The output method for XHTML documents,
   * this method type is not currently supported: <tt>xhtml</tt>.
   */
  public static final String XHTML = "xhtml";

  /**
   * The output method type for text documents: <tt>text</tt>.
   */
  public static final String TEXT = "text";
  
  /**
   * The "internal" method, just used when no method is 
   * specified in the style sheet, and a serializer of this type wraps either an
   * XML or HTML type (depending on the first tag in the output being html or
   * not)
   */  
  public static final String UNKNOWN = "";
}
