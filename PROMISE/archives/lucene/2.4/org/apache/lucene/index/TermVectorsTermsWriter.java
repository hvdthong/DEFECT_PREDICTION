import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.RAMOutputStream;
import org.apache.lucene.util.ArrayUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

final class TermVectorsTermsWriter extends TermsHashConsumer {

  final DocumentsWriter docWriter;
  TermVectorsWriter termVectorsWriter;
  PerDoc[] docFreeList = new PerDoc[1];
  int freeCount;
  IndexOutput tvx;
  IndexOutput tvd;
  IndexOutput tvf;
  int lastDocID;

  public TermVectorsTermsWriter(DocumentsWriter docWriter) {
    this.docWriter = docWriter;
  }

  public TermsHashConsumerPerThread addThread(TermsHashPerThread termsHashPerThread) {
    return new TermVectorsTermsWriterPerThread(termsHashPerThread, this);
  }

  void createPostings(RawPostingList[] postings, int start, int count) {
    final int end = start + count;
    for(int i=start;i<end;i++)
      postings[i] = new PostingList();
  }

  synchronized void flush(Map threadsAndFields, final DocumentsWriter.FlushState state) throws IOException {

    if (tvx != null) {

      if (state.numDocsInStore > 0)
        fill(state.numDocsInStore - docWriter.getDocStoreOffset());

      tvx.flush();
      tvd.flush();
      tvf.flush();
    }

    Iterator it = threadsAndFields.entrySet().iterator();
    while(it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      Iterator it2 = ((Collection) entry.getValue()).iterator();
      while(it2.hasNext()) {
        TermVectorsTermsWriterPerField perField = (TermVectorsTermsWriterPerField) it2.next();
        perField.termsHashPerField.reset();
        perField.shrinkHash();
      }

      TermVectorsTermsWriterPerThread perThread = (TermVectorsTermsWriterPerThread) entry.getKey();
      perThread.termsHashPerThread.reset(true);
    }
  }

  synchronized void closeDocStore(final DocumentsWriter.FlushState state) throws IOException {
    if (tvx != null) {
      fill(state.numDocsInStore - docWriter.getDocStoreOffset());
      tvx.close();
      tvf.close();
      tvd.close();
      tvx = null;
      assert state.docStoreSegmentName != null;
      if (4+state.numDocsInStore*16 != state.directory.fileLength(state.docStoreSegmentName + "." + IndexFileNames.VECTORS_INDEX_EXTENSION))
        throw new RuntimeException("after flush: tvx size mismatch: " + state.numDocsInStore + " docs vs " + state.directory.fileLength(state.docStoreSegmentName + "." + IndexFileNames.VECTORS_INDEX_EXTENSION) + " length in bytes of " + state.docStoreSegmentName + "." + IndexFileNames.VECTORS_INDEX_EXTENSION);

      state.flushedFiles.add(state.docStoreSegmentName + "." + IndexFileNames.VECTORS_INDEX_EXTENSION);
      state.flushedFiles.add(state.docStoreSegmentName + "." + IndexFileNames.VECTORS_FIELDS_EXTENSION);
      state.flushedFiles.add(state.docStoreSegmentName + "." + IndexFileNames.VECTORS_DOCUMENTS_EXTENSION);

      docWriter.removeOpenFile(state.docStoreSegmentName + "." + IndexFileNames.VECTORS_INDEX_EXTENSION);
      docWriter.removeOpenFile(state.docStoreSegmentName + "." + IndexFileNames.VECTORS_FIELDS_EXTENSION);
      docWriter.removeOpenFile(state.docStoreSegmentName + "." + IndexFileNames.VECTORS_DOCUMENTS_EXTENSION);

      lastDocID = 0;
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

  /** Fills in no-term-vectors for all docs we haven't seen
   *  since the last doc that had term vectors. */
  void fill(int docID) throws IOException {
    final int docStoreOffset = docWriter.getDocStoreOffset();
    final int end = docID+docStoreOffset;
    if (lastDocID < end) {
      final long tvfPosition = tvf.getFilePointer();
      while(lastDocID < end) {
        tvx.writeLong(tvd.getFilePointer());
        tvd.writeVInt(0);
        tvx.writeLong(tvfPosition);
        lastDocID++;
      }
    }
  }

  synchronized void initTermVectorsWriter() throws IOException {        
    if (tvx == null) {
      
      final String docStoreSegment = docWriter.getDocStoreSegment();

      if (docStoreSegment == null)
        return;

      assert docStoreSegment != null;

      tvx = docWriter.directory.createOutput(docStoreSegment + "." + IndexFileNames.VECTORS_INDEX_EXTENSION);
      tvd = docWriter.directory.createOutput(docStoreSegment +  "." + IndexFileNames.VECTORS_DOCUMENTS_EXTENSION);
      tvf = docWriter.directory.createOutput(docStoreSegment +  "." + IndexFileNames.VECTORS_FIELDS_EXTENSION);
      
      tvx.writeInt(TermVectorsReader.FORMAT_CURRENT);
      tvd.writeInt(TermVectorsReader.FORMAT_CURRENT);
      tvf.writeInt(TermVectorsReader.FORMAT_CURRENT);

      docWriter.addOpenFile(docStoreSegment + "." + IndexFileNames.VECTORS_INDEX_EXTENSION);
      docWriter.addOpenFile(docStoreSegment + "." + IndexFileNames.VECTORS_FIELDS_EXTENSION);
      docWriter.addOpenFile(docStoreSegment + "." + IndexFileNames.VECTORS_DOCUMENTS_EXTENSION);

      lastDocID = 0;
    }
  }

  synchronized void finishDocument(PerDoc perDoc) throws IOException {

    assert docWriter.writer.testPoint("TermVectorsTermsWriter.finishDocument start");

    initTermVectorsWriter();

    fill(perDoc.docID);

    tvx.writeLong(tvd.getFilePointer());
    tvx.writeLong(tvf.getFilePointer());
    tvd.writeVInt(perDoc.numVectorFields);
    if (perDoc.numVectorFields > 0) {
      for(int i=0;i<perDoc.numVectorFields;i++)
        tvd.writeVInt(perDoc.fieldNumbers[i]);
      assert 0 == perDoc.fieldPointers[0];
      long lastPos = perDoc.fieldPointers[0];
      for(int i=1;i<perDoc.numVectorFields;i++) {
        long pos = perDoc.fieldPointers[i];
        tvd.writeVLong(pos-lastPos);
        lastPos = pos;
      }
      perDoc.tvf.writeTo(tvf);
      perDoc.tvf.reset();
      perDoc.numVectorFields = 0;
    }

    assert lastDocID == perDoc.docID + docWriter.getDocStoreOffset();

    lastDocID++;

    free(perDoc);
    assert docWriter.writer.testPoint("TermVectorsTermsWriter.finishDocument end");
  }

  public boolean freeRAM() {
    return false;
  }

  public void abort() {
    if (tvx != null) {
      try {
        tvx.close();
      } catch (Throwable t) {
      }
      tvx = null;
    }
    if (tvd != null) {
      try {
        tvd.close();
      } catch (Throwable t) {
      }
      tvd = null;
    }
    if (tvf != null) {
      try {
        tvf.close();
      } catch (Throwable t) {
      }
      tvf = null;
    }
    lastDocID = 0;
  }

  synchronized void free(PerDoc doc) {
    assert freeCount < docFreeList.length;
    docFreeList[freeCount++] = doc;
  }

  class PerDoc extends DocumentsWriter.DocWriter {

    RAMOutputStream tvf = new RAMOutputStream();
    int numVectorFields;

    int[] fieldNumbers = new int[1];
    long[] fieldPointers = new long[1];

    void reset() {
      tvf.reset();
      numVectorFields = 0;
    }

    void abort() {
      reset();
      free(this);
    }

    void addField(final int fieldNumber) {
      if (numVectorFields == fieldNumbers.length) {
        fieldNumbers = ArrayUtil.grow(fieldNumbers);
        fieldPointers = ArrayUtil.grow(fieldPointers);
      }
      fieldNumbers[numVectorFields] = fieldNumber;
      fieldPointers[numVectorFields] = tvf.getFilePointer();
      numVectorFields++;
    }

    public long sizeInBytes() {
      return tvf.sizeInBytes();
    }

    public void finish() throws IOException {
      finishDocument(this);
    }
  }

  static final class PostingList extends RawPostingList {
  }

  int bytesPerPosting() {
    return RawPostingList.BYTES_SIZE + 3 * DocumentsWriter.INT_NUM_BYTE;
  }
}
