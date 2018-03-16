import java.io.IOException;
import org.apache.lucene.document.Fieldable;

final class DocFieldConsumersPerField extends DocFieldConsumerPerField {

  final DocFieldConsumerPerField one;
  final DocFieldConsumerPerField two;
  final DocFieldConsumersPerThread perThread;

  public DocFieldConsumersPerField(DocFieldConsumersPerThread perThread, DocFieldConsumerPerField one, DocFieldConsumerPerField two) {
    this.perThread = perThread;
    this.one = one;
    this.two = two;
  }

  public void processFields(Fieldable[] fields, int count) throws IOException {
    one.processFields(fields, count);
    two.processFields(fields, count);
  }

  public void abort() {
    try {
      one.abort();
    } finally {
      two.abort();
    }
  }
}
