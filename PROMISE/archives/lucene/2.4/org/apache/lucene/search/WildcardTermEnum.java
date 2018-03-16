import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

/**
 * Subclass of FilteredTermEnum for enumerating all terms that match the
 * specified wildcard filter term.
 * <p>
 * Term enumerations are always ordered by Term.compareTo().  Each term in
 * the enumeration is greater than all that precede it.
 *
 * @version $Id: WildcardTermEnum.java 472959 2006-11-09 16:21:50Z yonik $
 */
public class WildcardTermEnum extends FilteredTermEnum {
  Term searchTerm;
  String field = "";
  String text = "";
  String pre = "";
  int preLen = 0;
  boolean endEnum = false;

  /**
   * Creates a new <code>WildcardTermEnum</code>.  Passing in a
   * {@link org.apache.lucene.index.Term Term} that does not contain a
   * <code>WILDCARD_CHAR</code> will cause an exception to be thrown.
   * <p>
   * After calling the constructor the enumeration is already pointing to the first 
   * valid term if such a term exists.
   */
  public WildcardTermEnum(IndexReader reader, Term term) throws IOException {
    super();
    searchTerm = term;
    field = searchTerm.field();
    text = searchTerm.text();

    int sidx = text.indexOf(WILDCARD_STRING);
    int cidx = text.indexOf(WILDCARD_CHAR);
    int idx = sidx;
    if (idx == -1) {
      idx = cidx;
    }
    else if (cidx >= 0) {
      idx = Math.min(idx, cidx);
    }

    pre = searchTerm.text().substring(0,idx);
    preLen = pre.length();
    text = text.substring(preLen);
    setEnum(reader.terms(new Term(searchTerm.field(), pre)));
  }

  protected final boolean termCompare(Term term) {
    if (field == term.field()) {
      String searchText = term.text();
      if (searchText.startsWith(pre)) {
        return wildcardEquals(text, 0, searchText, preLen);
      }
    }
    endEnum = true;
    return false;
  }

  public final float difference() {
    return 1.0f;
  }

  public final boolean endEnum() {
    return endEnum;
  }

  /********************************************
   * String equality with support for wildcards
   ********************************************/

  public static final char WILDCARD_STRING = '*';
  public static final char WILDCARD_CHAR = '?';

  /**
   * Determines if a word matches a wildcard pattern.
   * <small>Work released by Granta Design Ltd after originally being done on
   * company time.</small>
   */
  public static final boolean wildcardEquals(String pattern, int patternIdx,
    String string, int stringIdx)
  {
    int p = patternIdx;
    
    for (int s = stringIdx; ; ++p, ++s)
      {
        boolean sEnd = (s >= string.length());
        boolean pEnd = (p >= pattern.length());

        if (sEnd)
        {
          boolean justWildcardsLeft = true;

          int wildcardSearchPos = p;
          while (wildcardSearchPos < pattern.length() && justWildcardsLeft)
          {
            char wildchar = pattern.charAt(wildcardSearchPos);
            
            if (wildchar != WILDCARD_CHAR && wildchar != WILDCARD_STRING)
            {
              justWildcardsLeft = false;
            }
            else
            {
              if (wildchar == WILDCARD_CHAR) {
                return false;
              }
              
              wildcardSearchPos++;
            }
          }

          if (justWildcardsLeft)
          {
            return true;
          }
        }

        if (sEnd || pEnd)
        {
          break;
        }

        if (pattern.charAt(p) == WILDCARD_CHAR)
        {
          continue;
        }

        if (pattern.charAt(p) == WILDCARD_STRING)
        {
          ++p;
          for (int i = string.length(); i >= s; --i)
          {
            if (wildcardEquals(pattern, p, string, i))
            {
              return true;
            }
          }
          break;
        }
        if (pattern.charAt(p) != string.charAt(s))
        {
          break;
        }
      }
      return false;
  }

  public void close() throws IOException
  {
    super.close();
    searchTerm = null;
    field = null;
    text = null;
  }
}
