import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Map.Entry;

/** Holds buffered deletes, by docID, term or query.  We
 *  hold two instances of this class: one for the deletes
 *  prior to the last flush, the other for deletes after
 *  the last flush.  This is so if we need to abort
 *  (discard all buffered docs) we can also discard the
 *  buffered deletes yet keep the deletes done during
 *  previously flushed segments. */
class BufferedDeletes {
  int numTerms;
  HashMap terms = new HashMap();
  HashMap queries = new HashMap();
  List docIDs = new ArrayList();

  final static class Num {
    private int num;

    Num(int num) {
      this.num = num;
    }

    int getNum() {
      return num;
    }

    void setNum(int num) {
      if (num > this.num)
        this.num = num;
    }
  }



  void update(BufferedDeletes in) {
    numTerms += in.numTerms;
    terms.putAll(in.terms);
    queries.putAll(in.queries);
    docIDs.addAll(in.docIDs);
    in.terms.clear();
    in.numTerms = 0;
    in.queries.clear();
    in.docIDs.clear();
  }
    
  void clear() {
    terms.clear();
    queries.clear();
    docIDs.clear();
    numTerms = 0;
  }

  boolean any() {
    return terms.size() > 0 || docIDs.size() > 0 || queries.size() > 0;
  }

  synchronized void remap(MergeDocIDRemapper mapper,
                          SegmentInfos infos,
                          int[][] docMaps,
                          int[] delCounts,
                          MergePolicy.OneMerge merge,
                          int mergeDocCount) {

    final HashMap newDeleteTerms;

    if (terms.size() > 0) {
      newDeleteTerms = new HashMap();
      Iterator iter = terms.entrySet().iterator();
      while(iter.hasNext()) {
        Entry entry = (Entry) iter.next();
        Num num = (Num) entry.getValue();
        newDeleteTerms.put(entry.getKey(),
                           new Num(mapper.remap(num.getNum())));
      }
    } else
      newDeleteTerms = null;

    final List newDeleteDocIDs;

    if (docIDs.size() > 0) {
      newDeleteDocIDs = new ArrayList(docIDs.size());
      Iterator iter = docIDs.iterator();
      while(iter.hasNext()) {
        Integer num = (Integer) iter.next();
        newDeleteDocIDs.add(new Integer(mapper.remap(num.intValue())));
      }
    } else
      newDeleteDocIDs = null;

    final HashMap newDeleteQueries;
    
    if (queries.size() > 0) {
      newDeleteQueries = new HashMap(queries.size());
      Iterator iter = queries.entrySet().iterator();
      while(iter.hasNext()) {
        Entry entry = (Entry) iter.next();
        Integer num = (Integer) entry.getValue();
        newDeleteQueries.put(entry.getKey(),
                             new Integer(mapper.remap(num.intValue())));
      }
    } else
      newDeleteQueries = null;

    if (newDeleteTerms != null)
      terms = newDeleteTerms;
    if (newDeleteDocIDs != null)
      docIDs = newDeleteDocIDs;
    if (newDeleteQueries != null)
      queries = newDeleteQueries;
  }
}
