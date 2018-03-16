import java.io.IOException;
import org.apache.lucene.index.*;

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

  final boolean nextPosition() throws IOException {
      position = tp.nextPosition() - offset;
      return true;
    } else
      return false;
  }
}
