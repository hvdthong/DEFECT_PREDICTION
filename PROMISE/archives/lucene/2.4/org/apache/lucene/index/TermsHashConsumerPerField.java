import java.io.IOException;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.analysis.Token;

abstract class TermsHashConsumerPerField {
  abstract boolean start(Fieldable[] fields, int count) throws IOException;
  abstract void finish() throws IOException;
  abstract void skippingLongTerm(Token t) throws IOException;
  abstract void newTerm(Token t, RawPostingList p) throws IOException;
  abstract void addTerm(Token t, RawPostingList p) throws IOException;
  abstract int getStreamCount();
}
