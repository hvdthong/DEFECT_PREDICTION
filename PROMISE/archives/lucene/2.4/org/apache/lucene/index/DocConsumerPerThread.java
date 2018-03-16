import java.io.IOException;

abstract class DocConsumerPerThread {

  /** Process the document. If there is
   *  something for this document to be done in docID order,
   *  you should encapsulate that as a
   *  DocumentsWriter.DocWriter and return it.
   *  DocumentsWriter then calls finish() on this object
   *  when it's its turn. */
  abstract DocumentsWriter.DocWriter processDocument() throws IOException;

  abstract void abort();
}
