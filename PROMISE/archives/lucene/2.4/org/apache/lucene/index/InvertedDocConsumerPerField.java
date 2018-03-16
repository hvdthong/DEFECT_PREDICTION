import org.apache.lucene.document.Fieldable;
import org.apache.lucene.analysis.Token;
import java.io.IOException;

abstract class InvertedDocConsumerPerField {

  abstract boolean start(Fieldable[] fields, int count) throws IOException;

  abstract void add(Token token) throws IOException;

  abstract void finish() throws IOException;

  abstract void abort();
}
