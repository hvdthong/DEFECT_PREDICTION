import java.io.Reader;

/** Used by DocumentsWriter to implemented a StringReader
 *  that can be reset to a new string; we use this when
 *  tokenizing the string value from a Field. */
final class ReusableStringReader extends Reader {
  int upto;
  int left;
  String s;
  void init(String s) {
    this.s = s;
    left = s.length();
    this.upto = 0;
  }
  public int read(char[] c) {
    return read(c, 0, c.length);
  }
  public int read(char[] c, int off, int len) {
    if (left > len) {
      s.getChars(upto, upto+len, c, off);
      upto += len;
      left -= len;
      return len;
    } else if (0 == left) {
      return -1;
    } else {
      s.getChars(upto, upto+left, c, off);
      int r = left;
      left = 0;
      upto = s.length();
      return r;
    }
  }
  public void close() {};
}

