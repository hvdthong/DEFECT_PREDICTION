final class NormsWriterPerThread extends InvertedDocEndConsumerPerThread {
  final NormsWriter normsWriter;
  final DocumentsWriter.DocState docState;

  public NormsWriterPerThread(DocInverterPerThread docInverterPerThread, NormsWriter normsWriter) {
    this.normsWriter = normsWriter;
    docState = docInverterPerThread.docState;
  }

  InvertedDocEndConsumerPerField addField(DocInverterPerField docInverterPerField, final FieldInfo fieldInfo) {
    return new NormsWriterPerField(docInverterPerField, this, fieldInfo);
  }

  void abort() {}

  void startDocument() {}
  void finishDocument() {}

  boolean freeRAM() {
    return false;
  }
}
