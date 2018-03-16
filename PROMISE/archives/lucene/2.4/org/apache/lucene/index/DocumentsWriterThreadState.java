import java.io.IOException;

/** Used by DocumentsWriter to maintain per-thread state.
 *  We keep a separate Posting hash and other state for each
 *  thread and then merge postings hashes from all threads
 *  when writing the segment. */
final class DocumentsWriterThreadState {

  final DocConsumerPerThread consumer;
  final DocumentsWriter.DocState docState;

  final DocumentsWriter docWriter;

  public DocumentsWriterThreadState(DocumentsWriter docWriter) throws IOException {
    this.docWriter = docWriter;
    docState = new DocumentsWriter.DocState();
    docState.maxFieldLength = docWriter.maxFieldLength;
    docState.infoStream = docWriter.infoStream;
    docState.similarity = docWriter.similarity;
    docState.docWriter = docWriter;
    consumer = docWriter.consumer.addThread(this);
  }

  void doAfterFlush() {
    numThreads = 0;
    doFlushAfter = false;
  }
}
