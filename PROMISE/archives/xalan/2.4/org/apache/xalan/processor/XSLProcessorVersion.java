package org.apache.xalan.processor;

/**
 * <meta name="usage" content="general"/>
 * Administrative class to keep track of the version number of
 * the Xalan release.
 * <P>See also: org/apache/xalan/res/XSLTInfo.properties</P>
 * @deprecated To be replaced by org.apache.xalan.Version.getVersion()
 */
public class XSLProcessorVersion
{

  /**
   * Print the processor version to the command line.
   *
   * @param argv command line arguments, unused.
   */
  public static void main(String argv[])
  {
    System.out.println(S_VERSION);
  }

  /**
   * Constant name of product.
   */
  public static final String PRODUCT = "Xalan";

  /**
   * Implementation Language.
   */
  public static String LANGUAGE = "Java";

  /**
   * Major version number.
   * Version number. This changes only when there is a
   *          significant, externally apparent enhancement from
   *          the previous release. 'n' represents the n'th
   *          version.
   *
   *          Clients should carefully consider the implications
   *          of new versions as external interfaces and behaviour
   *          may have changed.
   */
  public static int VERSION = 2;

  /**
   * Release Number.
   * Release number. This changes when:
   *            -  a new set of functionality is to be added, eg,
   *               implementation of a new W3C specification.
   *            -  API or behaviour change.
   *            -  its designated as a reference release.
   */
  public static int RELEASE = 4;

  /**
   * Maintenance Drop Number.
   * Optional identifier used to designate maintenance
   *          drop applied to a specific release and contains
   *          fixes for defects reported. It maintains compatibility
   *          with the release and contains no API changes.
   *          When missing, it designates the final and complete
   *          development drop for a release.
   */
  public static int MAINTENANCE = 0;

  /**
   * Development Drop Number.
   * Optional identifier designates development drop of
   *          a specific release. D01 is the first development drop
   *          of a new release.
   *
   *          Development drops are works in progress towards a
   *          compeleted, final release. A specific development drop
   *          may not completely implement all aspects of a new
   *          feature, which may take several development drops to
   *          complete. At the point of the final drop for the
   *          release, the D suffix will be omitted.
   *
   *          Each 'D' drops can contain functional enhancements as
   *          well as defect fixes. 'D' drops may not be as stable as
   *          the final releases.
   */
  public static int DEVELOPMENT = 0;
  
  /**
   * Version String like <CODE>"<B>Xalan</B> <B>Language</B> 
   * v.r[.dd| <B>D</B>nn]"</CODE>.
   * <P>Semantics of the version string are identical to the Xerces project.</P>
   */
  public static String S_VERSION = PRODUCT+" "+LANGUAGE+" "
                                   +VERSION+"."+RELEASE+"."
                                   +(DEVELOPMENT > 0 ? ("D"+DEVELOPMENT) 
                                     : (""+MAINTENANCE));

}
