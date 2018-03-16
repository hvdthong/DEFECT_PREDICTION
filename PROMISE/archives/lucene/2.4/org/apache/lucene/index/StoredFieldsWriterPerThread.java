import java.io.IOException;
import org.apache.lucene.store.IndexOutput;

final class StoredFieldsWriterPerThread extends DocFieldConsumerPerThread {

  final FieldsWriter localFieldsWriter;
  final StoredFieldsWriter storedFieldsWriter;
  final DocumentsWriter.DocState docState;

  StoredFieldsWriter.PerDoc doc;

  public StoredFieldsWriterPerThread(DocFieldProcessorPerThread docFieldProcessorPerThread, StoredFieldsWriter storedFieldsWriter) throws IOException {
    this.storedFieldsWriter = storedFieldsWriter;
    this.docState = docFieldProcessorPerThread.docState;
    localFieldsWriter = new FieldsWriter((IndexOutput) null, (IndexOutput) null, storedFieldsWriter.fieldInfos);
  }

  public void startDocument() {
    if (doc != null) {
      doc.reset();
      doc.docID = docState.docID;
    }
  }

  public DocumentsWriter.DocWriter finishDocument() {
    try {
      return doc;
    } finally {
      doc = null;
    }
  }

  public void abort() {
    if (doc != null) {
      doc.abort();
      doc = null;
    }
  }

  public DocFieldConsumerPerField addField(FieldInfo fieldInfo) {
    return new StoredFieldsWriterPerField(this, fieldInfo);
  }
}
