import java.io.IOException;
import org.apache.lucene.index.*;

/**
 * Position of a term in a document that takes into account the term offset within the phrase. 
 */
final class PhrasePositions {

  PhrasePositions(TermPositions t, int o) {
    tp = t;
    offset = o;
  }

    if (!tp.next()) {
      return false;
    }
    doc = tp.doc();
    position = 0;
    return true;
  }

  final boolean skipTo(int target) throws IOException {
    if (!tp.skipTo(target)) {
      return false;
    }
    doc = tp.doc();
    position = 0;
    return true;
  }


  final void firstPosition() throws IOException {
    nextPosition();
  }

  /**
   * Go to next location of this term current document, and set 
   * <code>position</code> as <code>location - offset</code>, so that a 
   * matching exact phrase is easily identified when all PhrasePositions 
   * have exactly the same <code>position</code>.
   */
  final boolean nextPosition() throws IOException {
      position = tp.nextPosition() - offset;
      return true;
    } else
      return false;
  }
}
