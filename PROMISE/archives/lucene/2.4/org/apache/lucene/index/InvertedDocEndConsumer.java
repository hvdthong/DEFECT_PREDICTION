import java.util.Map;
import java.io.IOException;

abstract class InvertedDocEndConsumer {
  abstract InvertedDocEndConsumerPerThread addThread(DocInverterPerThread docInverterPerThread);
  abstract void flush(Map threadsAndFields, DocumentsWriter.FlushState state) throws IOException;
  abstract void closeDocStore(DocumentsWriter.FlushState state) throws IOException;
  abstract void abort();
  abstract void setFieldInfos(FieldInfos fieldInfos);
}
