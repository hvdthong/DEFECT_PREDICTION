import java.io.IOException;
import java.util.Collection;

abstract class DocConsumer {
  abstract DocConsumerPerThread addThread(DocumentsWriterThreadState perThread) throws IOException;
  abstract void flush(final Collection threads, final DocumentsWriter.FlushState state) throws IOException;
  abstract void closeDocStore(final DocumentsWriter.FlushState state) throws IOException;
  abstract void abort();
  abstract boolean freeRAM();
}
