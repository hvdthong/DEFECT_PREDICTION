public class ToStringUtils {
  /** for printing boost only if not 1.0 */ 
  public static String boost(float boost) {
    if (boost != 1.0f) {
      return "^" + Float.toString(boost);
    } else return "";
  }

}
