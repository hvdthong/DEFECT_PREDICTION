import java.io.IOException;
import org.apache.lucene.document.Fieldable;

abstract class DocFieldConsumerPerField {
  /** Processes all occurrences of a single field */
  abstract void processFields(Fieldable[] fields, int count) throws IOException;
  abstract void abort();
}
