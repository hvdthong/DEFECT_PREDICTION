import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.HashMap;

/**
 * Expert: The default cache implementation, storing all values in memory.
 * A WeakHashMap is used for storage.
 *
 * <p>Created: May 19, 2004 4:40:36 PM
 *
 * @author  Tim Jones (Nacimiento Software)
 * @since   lucene 1.4
 * @version $Id: FieldCacheImpl.java 391895 2006-04-06 04:02:09Z yonik $
 */
class FieldCacheImpl
implements FieldCache {

  /** Expert: Every key in the internal cache is of this type. */
  static class Entry {

    /** Creates one of these objects. */
    Entry (String field, int type, Locale locale) {
      this.field = field.intern();
      this.type = type;
      this.custom = null;
      this.locale = locale;
    }

    /** Creates one of these objects for a custom comparator. */
    Entry (String field, Object custom) {
      this.field = field.intern();
      this.type = SortField.CUSTOM;
      this.custom = custom;
      this.locale = null;
    }

    /** Two of these are equal iff they reference the same field and type. */
    public boolean equals (Object o) {
      if (o instanceof Entry) {
        Entry other = (Entry) o;
        if (other.field == field && other.type == type) {
          if (other.locale == null ? locale == null : other.locale.equals(locale)) {
            if (other.custom == null) {
              if (custom == null) return true;
            } else if (other.custom.equals (custom)) {
              return true;
            }
          }
        }
      }
      return false;
    }

    /** Composes a hashcode based on the field and type. */
    public int hashCode() {
      return field.hashCode() ^ type ^ (custom==null ? 0 : custom.hashCode()) ^ (locale==null ? 0 : locale.hashCode());
    }
  }

  private static final IntParser INT_PARSER = new IntParser() {
      public int parseInt(String value) {
        return Integer.parseInt(value);
      }
    };

  private static final FloatParser FLOAT_PARSER = new FloatParser() {
      public float parseFloat(String value) {
        return Float.parseFloat(value);
      }
    };

  /** The internal cache. Maps Entry to array of interpreted term values. **/
  final Map cache = new WeakHashMap();

  /** See if an object is in the cache. */
  Object lookup (IndexReader reader, String field, int type, Locale locale) {
    Entry entry = new Entry (field, type, locale);
    synchronized (this) {
      HashMap readerCache = (HashMap)cache.get(reader);
      if (readerCache == null) return null;
      return readerCache.get (entry);
    }
  }

  /** See if a custom object is in the cache. */
  Object lookup (IndexReader reader, String field, Object comparer) {
    Entry entry = new Entry (field, comparer);
    synchronized (this) {
      HashMap readerCache = (HashMap)cache.get(reader);
      if (readerCache == null) return null;
      return readerCache.get (entry);
    }
  }

  /** Put an object into the cache. */
  Object store (IndexReader reader, String field, int type, Locale locale, Object value) {
    Entry entry = new Entry (field, type, locale);
    synchronized (this) {
      HashMap readerCache = (HashMap)cache.get(reader);
      if (readerCache == null) {
        readerCache = new HashMap();
        cache.put(reader,readerCache);
      }
      return readerCache.put (entry, value);
    }
  }

  /** Put a custom object into the cache. */
  Object store (IndexReader reader, String field, Object comparer, Object value) {
    Entry entry = new Entry (field, comparer);
    synchronized (this) {
      HashMap readerCache = (HashMap)cache.get(reader);
      if (readerCache == null) {
        readerCache = new HashMap();
        cache.put(reader, readerCache);
      }
      return readerCache.put (entry, value);
    }
  }

  public int[] getInts (IndexReader reader, String field) throws IOException {
    return getInts(reader, field, INT_PARSER);
  }

  public int[] getInts (IndexReader reader, String field, IntParser parser)
  throws IOException {
    field = field.intern();
    Object ret = lookup (reader, field, parser);
    if (ret == null) {
      final int[] retArray = new int[reader.maxDoc()];
      TermDocs termDocs = reader.termDocs();
      TermEnum termEnum = reader.terms (new Term (field, ""));
      try {
        do {
          Term term = termEnum.term();
          if (term==null || term.field() != field) break;
          int termval = parser.parseInt(term.text());
          termDocs.seek (termEnum);
          while (termDocs.next()) {
            retArray[termDocs.doc()] = termval;
          }
        } while (termEnum.next());
      } finally {
        termDocs.close();
        termEnum.close();
      }
      store (reader, field, parser, retArray);
      return retArray;
    }
    return (int[]) ret;
  }

  public float[] getFloats (IndexReader reader, String field)
    throws IOException {
    return getFloats(reader, field, FLOAT_PARSER);
  }

  public float[] getFloats (IndexReader reader, String field,
                            FloatParser parser) throws IOException {
    field = field.intern();
    Object ret = lookup (reader, field, parser);
    if (ret == null) {
      final float[] retArray = new float[reader.maxDoc()];
      TermDocs termDocs = reader.termDocs();
      TermEnum termEnum = reader.terms (new Term (field, ""));
      try {
        do {
          Term term = termEnum.term();
          if (term==null || term.field() != field) break;
          float termval = parser.parseFloat(term.text());
          termDocs.seek (termEnum);
          while (termDocs.next()) {
            retArray[termDocs.doc()] = termval;
          }
        } while (termEnum.next());
      } finally {
        termDocs.close();
        termEnum.close();
      }
      store (reader, field, parser, retArray);
      return retArray;
    }
    return (float[]) ret;
  }

  public String[] getStrings (IndexReader reader, String field)
  throws IOException {
    field = field.intern();
    Object ret = lookup (reader, field, SortField.STRING, null);
    if (ret == null) {
      final String[] retArray = new String[reader.maxDoc()];
      TermDocs termDocs = reader.termDocs();
      TermEnum termEnum = reader.terms (new Term (field, ""));
      try {
        do {
          Term term = termEnum.term();
          if (term==null || term.field() != field) break;
          String termval = term.text();
          termDocs.seek (termEnum);
          while (termDocs.next()) {
            retArray[termDocs.doc()] = termval;
          }
        } while (termEnum.next());
      } finally {
        termDocs.close();
        termEnum.close();
      }
      store (reader, field, SortField.STRING, null, retArray);
      return retArray;
    }
    return (String[]) ret;
  }

  public StringIndex getStringIndex (IndexReader reader, String field)
  throws IOException {
    field = field.intern();
    Object ret = lookup (reader, field, STRING_INDEX, null);
    if (ret == null) {
      final int[] retArray = new int[reader.maxDoc()];
      String[] mterms = new String[reader.maxDoc()+1];
      TermDocs termDocs = reader.termDocs();
      TermEnum termEnum = reader.terms (new Term (field, ""));

      mterms[t++] = null;

      try {
        do {
          Term term = termEnum.term();
          if (term==null || term.field() != field) break;

          if (t >= mterms.length) throw new RuntimeException ("there are more terms than " +
                  "documents in field \"" + field + "\", but it's impossible to sort on " +
                  "tokenized fields");
          mterms[t] = term.text();

          termDocs.seek (termEnum);
          while (termDocs.next()) {
            retArray[termDocs.doc()] = t;
          }

          t++;
        } while (termEnum.next());
      } finally {
        termDocs.close();
        termEnum.close();
      }

      if (t == 0) {
        mterms = new String[1];
      } else if (t < mterms.length) {
        String[] terms = new String[t];
        System.arraycopy (mterms, 0, terms, 0, t);
        mterms = terms;
      }

      StringIndex value = new StringIndex (retArray, mterms);
      store (reader, field, STRING_INDEX, null, value);
      return value;
    }
    return (StringIndex) ret;
  }

  /** The pattern used to detect integer values in a field */
  /** removed for java 1.3 compatibility
   protected static final Pattern pIntegers = Pattern.compile ("[0-9\\-]+");
   **/

  /** The pattern used to detect float values in a field */
  /**
   * removed for java 1.3 compatibility
   * protected static final Object pFloats = Pattern.compile ("[0-9+\\-\\.eEfFdD]+");
   */

  public Object getAuto (IndexReader reader, String field)
  throws IOException {
    field = field.intern();
    Object ret = lookup (reader, field, SortField.AUTO, null);
    if (ret == null) {
      TermEnum enumerator = reader.terms (new Term (field, ""));
      try {
        Term term = enumerator.term();
        if (term == null) {
          throw new RuntimeException ("no terms in field " + field + " - cannot determine sort type");
        }
        if (term.field() == field) {
          String termtext = term.text().trim();

          /**
           * Java 1.4 level code:

           if (pIntegers.matcher(termtext).matches())
           return IntegerSortedHitQueue.comparator (reader, enumerator, field);

           else if (pFloats.matcher(termtext).matches())
           return FloatSortedHitQueue.comparator (reader, enumerator, field);
           */

          try {
            Integer.parseInt (termtext);
            ret = getInts (reader, field);
          } catch (NumberFormatException nfe1) {
            try {
              Float.parseFloat (termtext);
              ret = getFloats (reader, field);
            } catch (NumberFormatException nfe2) {
              ret = getStringIndex (reader, field);
            }
          }
          if (ret != null) {
            store (reader, field, SortField.AUTO, null, ret);
          }
        } else {
          throw new RuntimeException ("field \"" + field + "\" does not appear to be indexed");
        }
      } finally {
        enumerator.close();
      }

    }
    return ret;
  }

  public Comparable[] getCustom (IndexReader reader, String field, SortComparator comparator)
  throws IOException {
    field = field.intern();
    Object ret = lookup (reader, field, comparator);
    if (ret == null) {
      final Comparable[] retArray = new Comparable[reader.maxDoc()];
      TermDocs termDocs = reader.termDocs();
      TermEnum termEnum = reader.terms (new Term (field, ""));
      try {
        do {
          Term term = termEnum.term();
          if (term==null || term.field() != field) break;
          Comparable termval = comparator.getComparable (term.text());
          termDocs.seek (termEnum);
          while (termDocs.next()) {
            retArray[termDocs.doc()] = termval;
          }
        } while (termEnum.next());
      } finally {
        termDocs.close();
        termEnum.close();
      }
      store (reader, field, comparator, retArray);
      return retArray;
    }
    return (Comparable[]) ret;
  }

}

