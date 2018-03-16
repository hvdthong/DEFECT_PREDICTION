public class ToStringUtils {
  public static String boost(float boost) {
    if (boost != 1.0f) {
      return "^" + Float.toString(boost);
    } else return "";
  }
}
