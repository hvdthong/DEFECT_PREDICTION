public final class Constants {

  /** The value of <tt>System.getProperty("java.version")<tt>. **/
  public static final String JAVA_VERSION = System.getProperty("java.version");
  /** True iff this is Java version 1.1. */
  public static final boolean JAVA_1_1 = JAVA_VERSION.startsWith("1.1.");
  /** True iff this is Java version 1.2. */
  public static final boolean JAVA_1_2 = JAVA_VERSION.startsWith("1.2.");
  /** True iff this is Java version 1.3. */
  public static final boolean JAVA_1_3 = JAVA_VERSION.startsWith("1.3.");
 
  /** The value of <tt>System.getProperty("os.name")<tt>. **/
  public static final String OS_NAME = System.getProperty("os.name");
  /** True iff running on Linux. */
  public static final boolean LINUX = OS_NAME.startsWith("Linux");
  /** True iff running on Windows. */
  public static final boolean WINDOWS = OS_NAME.startsWith("Windows");
  /** True iff running on SunOS. */
  public static final boolean SUN_OS = OS_NAME.startsWith("SunOS");
}
