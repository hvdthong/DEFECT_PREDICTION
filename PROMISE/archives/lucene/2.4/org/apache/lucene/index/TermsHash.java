import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Arrays;
import java.io.IOException;

import org.apache.lucene.util.ArrayUtil;

/** This class implements {@link InvertedDocConsumer}, which
 *  is passed each token produced by the analyzer on each
 *  field.  It stores these tokens in a hash table, and
 *  allocates separate byte streams per token.  Consumers of
 *  this class, eg {@link FreqProxTermsWriter} and {@link
 *  TermVectorsTermsWriter}, write their own byte streams
 *  under each term.
 */

final class TermsHash extends InvertedDocConsumer {

  final TermsHashConsumer consumer;
  final TermsHash nextTermsHash;
  final int bytesPerPosting;
  final int postingsFreeChunk;
  final DocumentsWriter docWriter;
  
  private TermsHash primaryTermsHash;

  private RawPostingList[] postingsFreeList = new RawPostingList[1];
  private int postingsFreeCount;
  private int postingsAllocCount;
  boolean trackAllocations;

  public TermsHash(final DocumentsWriter docWriter, boolean trackAllocations, final TermsHashConsumer consumer, final TermsHash nextTermsHash) {
    this.docWriter = docWriter;
    this.consumer = consumer;
    this.nextTermsHash = nextTermsHash;
    this.trackAllocations = trackAllocations;

    bytesPerPosting = consumer.bytesPerPosting() + 4*DocumentsWriter.POINTER_NUM_BYTE;
    postingsFreeChunk = (int) (DocumentsWriter.BYTE_BLOCK_SIZE / bytesPerPosting);
  }

  InvertedDocConsumerPerThread addThread(DocInverterPerThread docInverterPerThread) {
    return new TermsHashPerThread(docInverterPerThread, this, nextTermsHash, null);
  }

  TermsHashPerThread addThread(DocInverterPerThread docInverterPerThread, TermsHashPerThread primaryPerThread) {
    return new TermsHashPerThread(docInverterPerThread, this, nextTermsHash, primaryPerThread);
  }

  void setFieldInfos(FieldInfos fieldInfos) {
    this.fieldInfos = fieldInfos;
    consumer.setFieldInfos(fieldInfos);
  }

  synchronized public void abort() {
    consumer.abort();
    if (nextTermsHash != null)
      nextTermsHash.abort();
  }

  void shrinkFreePostings(Map threadsAndFields, DocumentsWriter.FlushState state) {

    assert postingsFreeCount == postingsAllocCount: Thread.currentThread().getName() + ": postingsFreeCount=" + postingsFreeCount + " postingsAllocCount=" + postingsAllocCount + " consumer=" + consumer;

    final int newSize = ArrayUtil.getShrinkSize(postingsFreeList.length, postingsAllocCount);
    if (newSize != postingsFreeList.length) {
      RawPostingList[] newArray = new RawPostingList[newSize];
      System.arraycopy(postingsFreeList, 0, newArray, 0, postingsFreeCount);
      postingsFreeList = newArray;
    }
  }

  synchronized void closeDocStore(DocumentsWriter.FlushState state) throws IOException {
    consumer.closeDocStore(state);
    if (nextTermsHash != null)
      nextTermsHash.closeDocStore(state);
  }

  synchronized void flush(Map threadsAndFields, final DocumentsWriter.FlushState state) throws IOException {
    Map childThreadsAndFields = new HashMap();
    Map nextThreadsAndFields;

    if (nextTermsHash != null)
      nextThreadsAndFields = new HashMap();
    else
      nextThreadsAndFields = null;

    Iterator it = threadsAndFields.entrySet().iterator();
    while(it.hasNext()) {

      Map.Entry entry = (Map.Entry) it.next();

      TermsHashPerThread perThread = (TermsHashPerThread) entry.getKey();

      Collection fields = (Collection) entry.getValue();

      Iterator fieldsIt = fields.iterator();
      Collection childFields = new HashSet();
      Collection nextChildFields;

      if (nextTermsHash != null)
        nextChildFields = new HashSet();
      else
        nextChildFields = null;

      while(fieldsIt.hasNext()) {
        TermsHashPerField perField = (TermsHashPerField) fieldsIt.next();
        childFields.add(perField.consumer);
        if (nextTermsHash != null)
          nextChildFields.add(perField.nextPerField);
      }

      childThreadsAndFields.put(perThread.consumer, childFields);
      if (nextTermsHash != null)
        nextThreadsAndFields.put(perThread.nextPerThread, nextChildFields);
    }
    
    consumer.flush(childThreadsAndFields, state);

    shrinkFreePostings(threadsAndFields, state);
    
    if (nextTermsHash != null)
      nextTermsHash.flush(nextThreadsAndFields, state);
  }

  synchronized public boolean freeRAM() {

    if (!trackAllocations)
      return false;

    boolean any;
    final int numToFree;
    if (postingsFreeCount >= postingsFreeChunk)
      numToFree = postingsFreeChunk;
    else
      numToFree = postingsFreeCount;
    any = numToFree > 0;
    if (any) {
      Arrays.fill(postingsFreeList, postingsFreeCount-numToFree, postingsFreeCount, null);
      postingsFreeCount -= numToFree;
      postingsAllocCount -= numToFree;
      docWriter.bytesAllocated(-numToFree * bytesPerPosting);
      any = true;
    }

    if (nextTermsHash != null)
      any |= nextTermsHash.freeRAM();

    return any;
  }

  synchronized public void recyclePostings(final RawPostingList[] postings, final int numPostings) {

    assert postings.length >= numPostings;

    assert postingsFreeCount + numPostings <= postingsFreeList.length;
    System.arraycopy(postings, 0, postingsFreeList, postingsFreeCount, numPostings);
    postingsFreeCount += numPostings;
  }

  synchronized public void getPostings(final RawPostingList[] postings) {

    assert docWriter.writer.testPoint("TermsHash.getPostings start");

    assert postingsFreeCount <= postingsFreeList.length;
    assert postingsFreeCount <= postingsAllocCount: "postingsFreeCount=" + postingsFreeCount + " postingsAllocCount=" + postingsAllocCount;

    final int numToCopy;
    if (postingsFreeCount < postings.length)
      numToCopy = postingsFreeCount;
    else
      numToCopy = postings.length;
    final int start = postingsFreeCount-numToCopy;
    assert start >= 0;
    assert start + numToCopy <= postingsFreeList.length;
    assert numToCopy <= postings.length;
    System.arraycopy(postingsFreeList, start,
                     postings, 0, numToCopy);

    if (numToCopy != postings.length) {
      final int extra = postings.length - numToCopy;
      final int newPostingsAllocCount = postingsAllocCount + extra;

      consumer.createPostings(postings, numToCopy, extra);
      assert docWriter.writer.testPoint("TermsHash.getPostings after create");
      postingsAllocCount += extra;

      if (trackAllocations)
        docWriter.bytesAllocated(extra * bytesPerPosting);

      if (newPostingsAllocCount > postingsFreeList.length)
        postingsFreeList = new RawPostingList[ArrayUtil.getNextSize(newPostingsAllocCount)];
    }

    postingsFreeCount -= numToCopy;

    if (trackAllocations)
      docWriter.bytesUsed(postings.length * bytesPerPosting);
  }
}
