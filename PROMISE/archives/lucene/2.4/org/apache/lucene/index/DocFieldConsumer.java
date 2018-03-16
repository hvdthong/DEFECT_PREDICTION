import java.io.IOException;
import java.util.Map;

abstract class DocFieldConsumer {

  FieldInfos fieldInfos;

  /** Called when DocumentsWriter decides to create a new
   *  segment */
  abstract void flush(Map threadsAndFields, DocumentsWriter.FlushState state) throws IOException;

  /** Called when DocumentsWriter decides to close the doc
   *  stores */
  abstract void closeDocStore(DocumentsWriter.FlushState state) throws IOException;
  
  /** Called when an aborting exception is hit */
  abstract void abort();

  /** Add a new thread */
  abstract DocFieldConsumerPerThread addThread(DocFieldProcessorPerThread docFieldProcessorPerThread) throws IOException;

  /** Called when DocumentsWriter is using too much RAM.
   *  The consumer should free RAM, if possible, returning
   *  true if any RAM was in fact freed. */
  abstract boolean freeRAM();

  void setFieldInfos(FieldInfos fieldInfos) {
    this.fieldInfos = fieldInfos;
  }
}
