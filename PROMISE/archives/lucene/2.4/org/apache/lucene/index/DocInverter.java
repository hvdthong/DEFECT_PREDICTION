import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;
import java.io.IOException;

/** This is a DocFieldConsumer that inverts each field,
 *  separately, from a Document, and accepts a
 *  InvertedTermsConsumer to process those terms. */

final class DocInverter extends DocFieldConsumer {

  final InvertedDocConsumer consumer;
  final InvertedDocEndConsumer endConsumer;

  public DocInverter(InvertedDocConsumer consumer, InvertedDocEndConsumer endConsumer) {
    this.consumer = consumer;
    this.endConsumer = endConsumer;
  }

  void setFieldInfos(FieldInfos fieldInfos) {
    super.setFieldInfos(fieldInfos);
    consumer.setFieldInfos(fieldInfos);
    endConsumer.setFieldInfos(fieldInfos);
  }

  void flush(Map threadsAndFields, DocumentsWriter.FlushState state) throws IOException {

    Map childThreadsAndFields = new HashMap();
    Map endChildThreadsAndFields = new HashMap();

    Iterator it = threadsAndFields.entrySet().iterator();
    while(it.hasNext()) {

      Map.Entry entry = (Map.Entry) it.next();

      DocInverterPerThread perThread = (DocInverterPerThread) entry.getKey();

      Collection fields = (Collection) entry.getValue();

      Iterator fieldsIt = fields.iterator();
      Collection childFields = new HashSet();
      Collection endChildFields = new HashSet();
      while(fieldsIt.hasNext()) {
        DocInverterPerField perField = (DocInverterPerField) fieldsIt.next();
        childFields.add(perField.consumer);
        endChildFields.add(perField.endConsumer);
      }

      childThreadsAndFields.put(perThread.consumer, childFields);
      endChildThreadsAndFields.put(perThread.endConsumer, endChildFields);
    }
    
    consumer.flush(childThreadsAndFields, state);
    endConsumer.flush(endChildThreadsAndFields, state);
  }

  public void closeDocStore(DocumentsWriter.FlushState state) throws IOException {
    consumer.closeDocStore(state);
    endConsumer.closeDocStore(state);
  }

  void abort() {
    consumer.abort();
    endConsumer.abort();
  }

  public boolean freeRAM() {
    return consumer.freeRAM();
  }

  public DocFieldConsumerPerThread addThread(DocFieldProcessorPerThread docFieldProcessorPerThread) {
    return new DocInverterPerThread(docFieldProcessorPerThread, this);
  }

  final static class FieldInvertState {
    int position;
    int length;
    int offset;
    float boost;

    void reset(float docBoost) {
      position = 0;
      length = 0;
      offset = 0;
      boost = docBoost;
    }
  }
}
