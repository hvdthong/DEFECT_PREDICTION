import java.io.IOException;
import org.apache.lucene.store.IndexInput;

final class TermBuffer implements Cloneable {
  private static final char[] NO_CHARS = new char[0];

  private String field;
  private char[] text = NO_CHARS;
  private int textLength;

  public final int compareTo(TermBuffer other) {
      return compareChars(text, textLength, other.text, other.textLength);
    else
      return field.compareTo(other.field);
  }

  private static final int compareChars(char[] v1, int len1,
                                        char[] v2, int len2) {
    int end = Math.min(len1, len2);
    for (int k = 0; k < end; k++) {
      char c1 = v1[k];
      char c2 = v2[k];
      if (c1 != c2) {
        return c1 - c2;
      }
    }
    return len1 - len2;
  }

  private final void setTextLength(int newLength) {
    if (text.length < newLength) {
      char[] newText = new char[newLength];
      System.arraycopy(text, 0, newText, 0, textLength);
      text = newText;
    }
    textLength = newLength;
  }

  public final void read(IndexInput input, FieldInfos fieldInfos)
    throws IOException {
    int start = input.readVInt();
    int length = input.readVInt();
    int totalLength = start + length;
    setTextLength(totalLength);
    input.readChars(this.text, start, length);
    this.field = fieldInfos.fieldName(input.readVInt());
  }

  public final void set(Term term) {
    if (term == null) {
      reset();
      return;
    }

    setTextLength(term.text().length());
    term.text().getChars(0, term.text().length(), text, 0);

    this.field = term.field();
    this.term = term;
  }

  public final void set(TermBuffer other) {
    setTextLength(other.textLength);
    System.arraycopy(other.text, 0, text, 0, textLength);

    this.field = other.field;
    this.term = other.term;
  }

  public void reset() {
    this.field = null;
    this.textLength = 0;
    this.term = null;
  }

  public Term toTerm() {
      return null;

    if (term == null)
      term = new Term(field, new String(text, 0, textLength), false);

    return term;
  }

  protected Object clone() {
    TermBuffer clone = null;
    try {
      clone = (TermBuffer)super.clone();
    } catch (CloneNotSupportedException e) {}

    clone.text = new char[text.length];
    System.arraycopy(text, 0, clone.text, 0, textLength);

    return clone;
  }
}
