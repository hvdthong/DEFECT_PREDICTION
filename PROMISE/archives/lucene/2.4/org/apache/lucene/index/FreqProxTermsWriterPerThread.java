final class FreqProxTermsWriterPerThread extends TermsHashConsumerPerThread {
  final TermsHashPerThread termsHashPerThread;
  final DocumentsWriter.DocState docState;

  public FreqProxTermsWriterPerThread(TermsHashPerThread perThread) {
    docState = perThread.docState;
    termsHashPerThread = perThread;
  }
  
  public TermsHashConsumerPerField addField(TermsHashPerField termsHashPerField, FieldInfo fieldInfo) {
    return new FreqProxTermsWriterPerField(termsHashPerField, this, fieldInfo);
  }

  void startDocument() {
  }

  DocumentsWriter.DocWriter finishDocument() {
    return null;
  }

  public void abort() {}
}
