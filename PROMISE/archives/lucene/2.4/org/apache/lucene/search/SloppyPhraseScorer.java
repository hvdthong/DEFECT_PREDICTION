import org.apache.lucene.index.TermPositions;

import java.io.IOException;
import java.util.HashMap;

final class SloppyPhraseScorer extends PhraseScorer {
    private int slop;
    private PhrasePositions repeats[];
    private boolean checkedRepeats;

    SloppyPhraseScorer(Weight weight, TermPositions[] tps, int[] offsets, Similarity similarity,
                       int slop, byte[] norms) {
        super(weight, tps, offsets, similarity, norms);
        this.slop = slop;
    }

    /**
     * Score a candidate doc for all slop-valid position-combinations (matches) 
     * encountered while traversing/hopping the PhrasePositions.
     * <br> The score contribution of a match depends on the distance: 
     * <br> - highest score for distance=0 (exact match).
     * <br> - score gets lower as distance gets higher.
     * <br>Example: for query "a b"~2, a document "x a b a y" can be scored twice: 
     * once for "a b" (distance=0), and once for "b a" (distance=2).
     * <br>Pssibly not all valid combinations are encountered, because for efficiency  
     * we always propagate the least PhrasePosition. This allows to base on 
     * PriorityQueue and move forward faster. 
     * As result, for example, document "a b c b a"
     * would score differently for queries "a b c"~4 and "c b a"~4, although 
     * they really are equivalent. 
     * Similarly, for doc "a b c b a f g", query "c b"~2 
     * would get same score as "g f"~2, although "c b"~2 could be matched twice.
     * We may want to fix this in the future (currently not, for performance reasons).
     */
    protected final float phraseFreq() throws IOException {
        int end = initPhrasePositions();
        
        float freq = 0.0f;
        boolean done = (end<0);
        while (!done) {
            PhrasePositions pp = (PhrasePositions) pq.pop();
            int start = pp.position;
            int next = ((PhrasePositions) pq.top()).position;

            boolean tpsDiffer = true;
            for (int pos = start; pos <= next || !tpsDiffer; pos = pp.position) {
                if (pos<=next && tpsDiffer)
                if (!pp.nextPosition()) {
                    break;
                }
                PhrasePositions pp2 = null;
                tpsDiffer = !pp.repeats || (pp2 = termPositionsDiffer(pp))==null;
                if (pp2!=null && pp2!=pp) {
                }
            }

            int matchLength = end - start;
            if (matchLength <= slop)

            if (pp.position > end)
                end = pp.position;
        }

        return freq;
    }
    
    private PhrasePositions flip(PhrasePositions pp, PhrasePositions pp2) {
      int n=0;
      PhrasePositions pp3;
      while ((pp3=(PhrasePositions)pq.pop()) != pp2) {
        tmpPos[n++] = pp3;
      }
      for (n--; n>=0; n--) {
        pq.insert(tmpPos[n]);
      }
      pq.put(pp);
      return pp2;
    }

    /**
     * Init PhrasePositions in place.
     * There is a one time initialization for this scorer:
     * <br>- Put in repeats[] each pp that has another pp with same position in the doc.
     * <br>- Also mark each such pp by pp.repeats = true.
     * <br>Later can consult with repeats[] in termPositionsDiffer(pp), making that check efficient.
     * In particular, this allows to score queries with no repetitions with no overhead due to this computation.
     * <br>- Example 1 - query with no repetitions: "ho my"~2
     * <br>- Example 2 - query with repetitions: "ho my my"~2
     * <br>- Example 3 - query with repetitions: "my ho my"~2
     * <br>Init per doc w/repeats in query, includes propagating some repeating pp's to avoid false phrase detection.  
     * @return end (max position), or -1 if any term ran out (i.e. done) 
     * @throws IOException 
     */
    private int initPhrasePositions() throws IOException {
        int end = 0;
        
        if (checkedRepeats && repeats==null) {
            pq.clear();
            for (PhrasePositions pp = first; pp != null; pp = pp.next) {
                pp.firstPosition();
                if (pp.position > end)
                    end = pp.position;
            }
            return end;
        }
        
        for (PhrasePositions pp = first; pp != null; pp = pp.next)
            pp.firstPosition();
        
        if (!checkedRepeats) {
            checkedRepeats = true;
            HashMap m = null;
            for (PhrasePositions pp = first; pp != null; pp = pp.next) {
                int tpPos = pp.position + pp.offset;
                for (PhrasePositions pp2 = pp.next; pp2 != null; pp2 = pp2.next) {
                    int tpPos2 = pp2.position + pp2.offset;
                    if (tpPos2 == tpPos) { 
                        if (m == null)
                            m = new HashMap();
                        pp.repeats = true;
                        pp2.repeats = true;
                        m.put(pp,null);
                        m.put(pp2,null);
                    }
                }
            }
            if (m!=null)
                repeats = (PhrasePositions[]) m.keySet().toArray(new PhrasePositions[0]);
        }
        
        if (repeats!=null) {
            for (int i = 0; i < repeats.length; i++) {
                PhrasePositions pp = repeats[i];
                PhrasePositions pp2;
                while ((pp2 = termPositionsDiffer(pp)) != null) {
                } 
            }
        }
      
        pq.clear();
        for (PhrasePositions pp = first; pp != null; pp = pp.next) {
            if (pp.position > end)
                end = pp.position;
        }

        if (repeats!=null) {
          tmpPos = new PhrasePositions[pq.size()];
        }
        return end;
    }

    /**
     * We disallow two pp's to have the same TermPosition, thereby verifying multiple occurrences 
     * in the query of the same word would go elsewhere in the matched doc.
     * @return null if differ (i.e. valid) otherwise return the higher offset PhrasePositions
     * out of the first two PPs found to not differ.
     */
    private PhrasePositions termPositionsDiffer(PhrasePositions pp) {
        int tpPos = pp.position + pp.offset;
        for (int i = 0; i < repeats.length; i++) {
            PhrasePositions pp2 = repeats[i];
            if (pp2 == pp)
                continue;
            int tpPos2 = pp2.position + pp2.offset;
            if (tpPos2 == tpPos)
        }
        return null; 
    }
}
