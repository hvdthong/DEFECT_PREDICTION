import java.util.Map;
import java.io.IOException;

abstract class InvertedDocConsumer {

  /** Add a new thread */
  abstract InvertedDocConsumerPerThread addThread(DocInverterPerThread docInverterPerThread);

  /** Abort (called after hitting AbortException) */
  abstract void abort();

  /** Flush a new segment */
  abstract void flush(Map threadsAndFields, DocumentsWriter.FlushState state) throws IOException;

  /** Close doc stores */
  abstract void closeDocStore(DocumentsWriter.FlushState state) throws IOException;

  /** Attempt to free RAM, returning true if any RAM was
   *  freed */
  abstract boolean freeRAM();

  FieldInfos fieldInfos;

  void setFieldInfos(FieldInfos fieldInfos) {
    this.fieldInfos = fieldInfos;
  }
}
