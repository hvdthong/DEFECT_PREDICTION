import java.io.IOException;
import org.apache.lucene.index.*;

final class ExactPhraseScorer extends PhraseScorer {

  ExactPhraseScorer(Weight weight, TermPositions[] tps, int[] offsets, Similarity similarity,
                    byte[] norms) {
    super(weight, tps, offsets, similarity, norms);
  }

  protected final float phraseFreq() throws IOException {
    pq.clear();
    for (PhrasePositions pp = first; pp != null; pp = pp.next) {
      pp.firstPosition();
    }

    int freq = 0;
	    do {
	      if (!first.nextPosition())
	        return (float)freq;
	    } while (first.position < last.position);
	      firstToLast();
      }
    } while (last.nextPosition());
  
    return (float)freq;
  }
}
