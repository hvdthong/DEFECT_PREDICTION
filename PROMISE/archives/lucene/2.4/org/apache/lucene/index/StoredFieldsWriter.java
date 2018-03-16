import java.util.Map;
import java.io.IOException;
import org.apache.lucene.store.RAMOutputStream;
import org.apache.lucene.util.ArrayUtil;

/** This is a DocFieldConsumer that writes stored fields. */
final class StoredFieldsWriter extends DocFieldConsumer {

  FieldsWriter fieldsWriter;
  final DocumentsWriter docWriter;
  int lastDocID;

  PerDoc[] docFreeList = new PerDoc[1];
  int freeCount;

  public StoredFieldsWriter(DocumentsWriter docWriter) {
    this.docWriter = docWriter;
  }

  public DocFieldConsumerPerThread addThread(DocFieldProcessorPerThread docFieldProcessorPerThread) throws IOException {
    return new StoredFieldsWriterPerThread(docFieldProcessorPerThread, this);
  }

  synchronized public void flush(Map threadsAndFields, DocumentsWriter.FlushState state) throws IOException {

    if (state.numDocsInStore > 0) {
      initFieldsWriter();

      fill(state.numDocsInStore - docWriter.getDocStoreOffset());
    }

    if (fieldsWriter != null)
      fieldsWriter.flush();
  }
  
  private void initFieldsWriter() throws IOException {
    if (fieldsWriter == null) {
      final String docStoreSegment = docWriter.getDocStoreSegment();
      if (docStoreSegment != null) {
        assert docStoreSegment != null;
        fieldsWriter = new FieldsWriter(docWriter.directory,
                                        docStoreSegment,
                                        fieldInfos);
        docWriter.addOpenFile(docStoreSegment + "." + IndexFileNames.FIELDS_EXTENSION);
        docWriter.addOpenFile(docStoreSegment + "." + IndexFileNames.FIELDS_INDEX_EXTENSION);
        lastDocID = 0;
      }
    }
  }

  synchronized public void closeDocStore(DocumentsWriter.FlushState state) throws IOException {
    final int inc = state.numDocsInStore - lastDocID;
    if (inc > 0) {
      initFieldsWriter();
      fill(state.numDocsInStore - docWriter.getDocStoreOffset());
    }

    if (fieldsWriter != null) {
      fieldsWriter.close();
      fieldsWriter = null;
      lastDocID = 0;
      assert state.docStoreSegmentName != null;
      state.flushedFiles.add(state.docStoreSegmentName + "." + IndexFileNames.FIELDS_EXTENSION);
      state.flushedFiles.add(state.docStoreSegmentName + "." + IndexFileNames.FIELDS_INDEX_EXTENSION);

      state.docWriter.removeOpenFile(state.docStoreSegmentName + "." + IndexFileNames.FIELDS_EXTENSION);
      state.docWriter.removeOpenFile(state.docStoreSegmentName + "." + IndexFileNames.FIELDS_INDEX_EXTENSION);

      if (4+state.numDocsInStore*8 != state.directory.fileLength(state.docStoreSegmentName + "." + IndexFileNames.FIELDS_INDEX_EXTENSION))
        throw new RuntimeException("after flush: fdx size mismatch: " + state.numDocsInStore + " docs vs " + state.directory.fileLength(state.docStoreSegmentName + "." + IndexFileNames.FIELDS_INDEX_EXTENSION) + " length in bytes of " + state.docStoreSegmentName + "." + IndexFileNames.FIELDS_INDEX_EXTENSION);
    }
  }

  int allocCount;

  synchronized PerDoc getPerDoc() {
    if (freeCount == 0) {
      allocCount++;
      if (allocCount > docFreeList.length) {
        assert allocCount == 1+docFreeList.length;
        docFreeList = new PerDoc[ArrayUtil.getNextSize(allocCount)];
      }
      return new PerDoc();
    } else
      return docFreeList[--freeCount];
  }

  synchronized void abort() {
    if (fieldsWriter != null) {
      try {
        fieldsWriter.close();
      } catch (Throwable t) {
      }
      fieldsWriter = null;
      lastDocID = 0;
    }
  }

  /** Fills in any hole in the docIDs */
  void fill(int docID) throws IOException {
    final int docStoreOffset = docWriter.getDocStoreOffset();

    final int end = docID+docStoreOffset;
    while(lastDocID < end) {
      fieldsWriter.skipDocument();
      lastDocID++;
    }
  }

  synchronized void finishDocument(PerDoc perDoc) throws IOException {
    assert docWriter.writer.testPoint("StoredFieldsWriter.finishDocument start");
    initFieldsWriter();

    fill(perDoc.docID);

    fieldsWriter.flushDocument(perDoc.numStoredFields, perDoc.fdt);
    lastDocID++;
    perDoc.reset();
    free(perDoc);
    assert docWriter.writer.testPoint("StoredFieldsWriter.finishDocument end");
  }

  public boolean freeRAM() {
    return false;
  }

  synchronized void free(PerDoc perDoc) {
    assert freeCount < docFreeList.length;
    assert 0 == perDoc.numStoredFields;
    assert 0 == perDoc.fdt.length();
    assert 0 == perDoc.fdt.getFilePointer();
    docFreeList[freeCount++] = perDoc;
  }

  class PerDoc extends DocumentsWriter.DocWriter {

    RAMOutputStream fdt = new RAMOutputStream();
    int numStoredFields;

    void reset() {
      fdt.reset();
      numStoredFields = 0;
    }

    void abort() {
      reset();
      free(this);
    }

    public long sizeInBytes() {
      return fdt.sizeInBytes();
    }

    public void finish() throws IOException {
      finishDocument(this);
    }
  }
}
