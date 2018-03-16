import java.io.IOException;

abstract class TermsHashConsumerPerThread {
  abstract void startDocument() throws IOException;
  abstract DocumentsWriter.DocWriter finishDocument() throws IOException;
  abstract public TermsHashConsumerPerField addField(TermsHashPerField termsHashPerField, FieldInfo fieldInfo);
  abstract public void abort();
}
