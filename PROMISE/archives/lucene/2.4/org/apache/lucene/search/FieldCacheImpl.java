import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Expert: The default cache implementation, storing all values in memory.
 * A WeakHashMap is used for storage.
 *
 * <p>Created: May 19, 2004 4:40:36 PM
 *
 * @since   lucene 1.4
 * @version $Id: FieldCacheImpl.java 695514 2008-09-15 15:42:11Z otis $
 */
class FieldCacheImpl
implements FieldCache {
	
  /** Expert: Internal cache. */
  abstract static class Cache {
    private final Map readerCache = new WeakHashMap();
    
    protected abstract Object createValue(IndexReader reader, Object key)
        throws IOException;

    public Object get(IndexReader reader, Object key) throws IOException {
      Map innerCache;
      Object value;
      synchronized (readerCache) {
        innerCache = (Map) readerCache.get(reader);
        if (innerCache == null) {
          innerCache = new HashMap();
          readerCache.put(reader, innerCache);
          value = null;
        } else {
          value = innerCache.get(key);
        }
        if (value == null) {
          value = new CreationPlaceholder();
          innerCache.put(key, value);
        }
      }
      if (value instanceof CreationPlaceholder) {
        synchronized (value) {
          CreationPlaceholder progress = (CreationPlaceholder) value;
          if (progress.value == null) {
            progress.value = createValue(reader, key);
            synchronized (readerCache) {
              innerCache.put(key, progress.value);
            }
          }
          return progress.value;
        }
      }
      return value;
    }
  }

  static final class CreationPlaceholder {
    Object value;
  }

  /** Expert: Every composite-key in the internal cache is of this type. */
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

  private static final ByteParser BYTE_PARSER = new ByteParser() {
    public byte parseByte(String value) {
      return Byte.parseByte(value);
    }
  };

  private static final ShortParser SHORT_PARSER = new ShortParser() {
    public short parseShort(String value) {
      return Short.parseShort(value);
    }
  };

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

  public byte[] getBytes (IndexReader reader, String field) throws IOException {
    return getBytes(reader, field, BYTE_PARSER);
  }

  public byte[] getBytes(IndexReader reader, String field, ByteParser parser)
      throws IOException {
    return (byte[]) bytesCache.get(reader, new Entry(field, parser));
  }

  Cache bytesCache = new Cache() {

    protected Object createValue(IndexReader reader, Object entryKey)
        throws IOException {
      Entry entry = (Entry) entryKey;
      String field = entry.field;
      ByteParser parser = (ByteParser) entry.custom;
      final byte[] retArray = new byte[reader.maxDoc()];
      TermDocs termDocs = reader.termDocs();
      TermEnum termEnum = reader.terms (new Term (field));
      try {
        do {
          Term term = termEnum.term();
          if (term==null || term.field() != field) break;
          byte termval = parser.parseByte(term.text());
          termDocs.seek (termEnum);
          while (termDocs.next()) {
            retArray[termDocs.doc()] = termval;
          }
        } while (termEnum.next());
      } finally {
        termDocs.close();
        termEnum.close();
      }
      return retArray;
    }
  };
  
  public short[] getShorts (IndexReader reader, String field) throws IOException {
    return getShorts(reader, field, SHORT_PARSER);
  }

  public short[] getShorts(IndexReader reader, String field, ShortParser parser)
      throws IOException {
    return (short[]) shortsCache.get(reader, new Entry(field, parser));
  }

  Cache shortsCache = new Cache() {

    protected Object createValue(IndexReader reader, Object entryKey)
        throws IOException {
      Entry entry = (Entry) entryKey;
      String field = entry.field;
      ShortParser parser = (ShortParser) entry.custom;
      final short[] retArray = new short[reader.maxDoc()];
      TermDocs termDocs = reader.termDocs();
      TermEnum termEnum = reader.terms (new Term (field));
      try {
        do {
          Term term = termEnum.term();
          if (term==null || term.field() != field) break;
          short termval = parser.parseShort(term.text());
          termDocs.seek (termEnum);
          while (termDocs.next()) {
            retArray[termDocs.doc()] = termval;
          }
        } while (termEnum.next());
      } finally {
        termDocs.close();
        termEnum.close();
      }
      return retArray;
    }
  };
  
  public int[] getInts (IndexReader reader, String field) throws IOException {
    return getInts(reader, field, INT_PARSER);
  }

  public int[] getInts(IndexReader reader, String field, IntParser parser)
      throws IOException {
    return (int[]) intsCache.get(reader, new Entry(field, parser));
  }

  Cache intsCache = new Cache() {

    protected Object createValue(IndexReader reader, Object entryKey)
        throws IOException {
      Entry entry = (Entry) entryKey;
      String field = entry.field;
      IntParser parser = (IntParser) entry.custom;
      final int[] retArray = new int[reader.maxDoc()];
      TermDocs termDocs = reader.termDocs();
      TermEnum termEnum = reader.terms (new Term (field));
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
      return retArray;
    }
  };


  public float[] getFloats (IndexReader reader, String field)
    throws IOException {
    return getFloats(reader, field, FLOAT_PARSER);
  }

  public float[] getFloats(IndexReader reader, String field, FloatParser parser)
      throws IOException {
    return (float[]) floatsCache.get(reader, new Entry(field, parser));
  }

  Cache floatsCache = new Cache() {

    protected Object createValue(IndexReader reader, Object entryKey)
        throws IOException {
      Entry entry = (Entry) entryKey;
      String field = entry.field;
      FloatParser parser = (FloatParser) entry.custom;
      final float[] retArray = new float[reader.maxDoc()];
      TermDocs termDocs = reader.termDocs();
      TermEnum termEnum = reader.terms (new Term (field));
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
      return retArray;
    }
  };

  public String[] getStrings(IndexReader reader, String field)
      throws IOException {
    return (String[]) stringsCache.get(reader, field);
  }

  Cache stringsCache = new Cache() {

    protected Object createValue(IndexReader reader, Object fieldKey)
        throws IOException {
      String field = ((String) fieldKey).intern();
      final String[] retArray = new String[reader.maxDoc()];
      TermDocs termDocs = reader.termDocs();
      TermEnum termEnum = reader.terms (new Term (field));
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
      return retArray;
    }
  };

  public StringIndex getStringIndex(IndexReader reader, String field)
      throws IOException {
    return (StringIndex) stringsIndexCache.get(reader, field);
  }

  Cache stringsIndexCache = new Cache() {

    protected Object createValue(IndexReader reader, Object fieldKey)
        throws IOException {
      String field = ((String) fieldKey).intern();
      final int[] retArray = new int[reader.maxDoc()];
      String[] mterms = new String[reader.maxDoc()+1];
      TermDocs termDocs = reader.termDocs();
      TermEnum termEnum = reader.terms (new Term (field));

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
      return value;
    }
  };

  /** The pattern used to detect integer values in a field */
  /** removed for java 1.3 compatibility
   protected static final Pattern pIntegers = Pattern.compile ("[0-9\\-]+");
   **/

  /** The pattern used to detect float values in a field */
  /**
   * removed for java 1.3 compatibility
   * protected static final Object pFloats = Pattern.compile ("[0-9+\\-\\.eEfFdD]+");
   */

  public Object getAuto(IndexReader reader, String field) throws IOException {
    return autoCache.get(reader, field);
  }

  Cache autoCache = new Cache() {

    protected Object createValue(IndexReader reader, Object fieldKey)
        throws IOException {
      String field = ((String)fieldKey).intern();
      TermEnum enumerator = reader.terms (new Term (field));
      try {
        Term term = enumerator.term();
        if (term == null) {
          throw new RuntimeException ("no terms in field " + field + " - cannot determine sort type");
        }
        Object ret = null;
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
              } catch (NumberFormatException nfe3) {
                ret = getStringIndex (reader, field);
              }
          }          
        } else {
          throw new RuntimeException ("field \"" + field + "\" does not appear to be indexed");
        }
        return ret;
      } finally {
        enumerator.close();
      }
    }
  };

  public Comparable[] getCustom(IndexReader reader, String field,
      SortComparator comparator) throws IOException {
    return (Comparable[]) customCache.get(reader, new Entry(field, comparator));
  }

  Cache customCache = new Cache() {

    protected Object createValue(IndexReader reader, Object entryKey)
        throws IOException {
      Entry entry = (Entry) entryKey;
      String field = entry.field;
      SortComparator comparator = (SortComparator) entry.custom;
      final Comparable[] retArray = new Comparable[reader.maxDoc()];
      TermDocs termDocs = reader.termDocs();
      TermEnum termEnum = reader.terms (new Term (field));
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
      return retArray;
    }
  };
  
}

