import java.util.Comparator;

/**
 * Compares {@link org.apache.lucene.index.TermVectorEntry}s first by frequency and then by
 * the term (case-sensitive)
 *
 **/
public class TermVectorEntryFreqSortedComparator implements Comparator {
  public int compare(Object object, Object object1) {
    int result = 0;
    TermVectorEntry entry = (TermVectorEntry) object;
    TermVectorEntry entry1 = (TermVectorEntry) object1;
    result = entry1.getFrequency() - entry.getFrequency();
    if (result == 0)
    {
      result = entry.getTerm().compareTo(entry1.getTerm());
      if (result == 0)
      {
        result = entry.getField().compareTo(entry1.getField());
      }
    }
    return result;
  }
}
