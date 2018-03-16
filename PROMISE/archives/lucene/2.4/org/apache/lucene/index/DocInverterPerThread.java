import java.io.IOException;

import org.apache.lucene.analysis.Token;

/** This is a DocFieldConsumer that inverts each field,
 *  separately, from a Document, and accepts a
 *  InvertedTermsConsumer to process those terms. */

final class DocInverterPerThread extends DocFieldConsumerPerThread {
  final DocInverter docInverter;
  final InvertedDocConsumerPerThread consumer;
  final InvertedDocEndConsumerPerThread endConsumer;
  final Token localToken = new Token();
  final DocumentsWriter.DocState docState;

  final DocInverter.FieldInvertState fieldState = new DocInverter.FieldInvertState();

  final ReusableStringReader stringReader = new ReusableStringReader();

  public DocInverterPerThread(DocFieldProcessorPerThread docFieldProcessorPerThread, DocInverter docInverter) {
    this.docInverter = docInverter;
    docState = docFieldProcessorPerThread.docState;
    consumer = docInverter.consumer.addThread(this);
    endConsumer = docInverter.endConsumer.addThread(this);
  }

  public void startDocument() throws IOException {
    consumer.startDocument();
    endConsumer.startDocument();
  }

  public DocumentsWriter.DocWriter finishDocument() throws IOException {
    endConsumer.finishDocument();
    return consumer.finishDocument();
  }

  void abort() {
    try {
      consumer.abort();
    } finally {
      endConsumer.abort();
    }
  }

  public DocFieldConsumerPerField addField(FieldInfo fi) {
    return new DocInverterPerField(this, fi);
  }
}
