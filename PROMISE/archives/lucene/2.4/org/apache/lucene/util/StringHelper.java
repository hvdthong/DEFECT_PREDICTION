public abstract class StringHelper {

  /**
   * Compares two byte[] arrays, element by element, and returns the
   * number of elements common to both arrays.
   *
   * @param bytes1 The first byte[] to compare
   * @param bytes2 The second byte[] to compare
   * @return The number of common elements.
   */
  public static final int bytesDifference(byte[] bytes1, int len1, byte[] bytes2, int len2) {
    int len = len1 < len2 ? len1 : len2;
    for (int i = 0; i < len; i++)
      if (bytes1[i] != bytes2[i])
        return i;
    return len;
  }

  /**
   * Compares two strings, character by character, and returns the
   * first position where the two strings differ from one another.
   *
   * @param s1 The first string to compare
   * @param s2 The second string to compare
   * @return The first position where the two strings differ.
   */
  public static final int stringDifference(String s1, String s2) {
    int len1 = s1.length();
    int len2 = s2.length();
    int len = len1 < len2 ? len1 : len2;
    for (int i = 0; i < len; i++) {
      if (s1.charAt(i) != s2.charAt(i)) {
	      return i;
      }
    }
    return len;
  }

  private StringHelper() {
  }
}
