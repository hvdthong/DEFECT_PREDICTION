import java.io.IOException;

abstract class InvertedDocConsumerPerThread {
  abstract void startDocument() throws IOException;
  abstract InvertedDocConsumerPerField addField(DocInverterPerField docInverterPerField, FieldInfo fieldInfo);
  abstract DocumentsWriter.DocWriter finishDocument() throws IOException;
  abstract void abort();
}
