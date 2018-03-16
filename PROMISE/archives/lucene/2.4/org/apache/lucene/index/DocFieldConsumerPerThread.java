import java.io.IOException;

abstract class DocFieldConsumerPerThread {
  abstract void startDocument() throws IOException;
  abstract DocumentsWriter.DocWriter finishDocument() throws IOException;
  abstract DocFieldConsumerPerField addField(FieldInfo fi);
  abstract void abort();
}
