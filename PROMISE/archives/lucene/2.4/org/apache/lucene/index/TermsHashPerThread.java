import java.io.IOException;

final class TermsHashPerThread extends InvertedDocConsumerPerThread {

  final TermsHash termsHash;
  final TermsHashConsumerPerThread consumer;
  final TermsHashPerThread nextPerThread;

  final CharBlockPool charPool;
  final IntBlockPool intPool;
  final ByteBlockPool bytePool;
  final boolean primary;
  final DocumentsWriter.DocState docState;

  final RawPostingList freePostings[] = new RawPostingList[256];
  int freePostingsCount;

  public TermsHashPerThread(DocInverterPerThread docInverterPerThread, final TermsHash termsHash, final TermsHash nextTermsHash, final TermsHashPerThread primaryPerThread) {
    docState = docInverterPerThread.docState;

    this.termsHash = termsHash;
    this.consumer = termsHash.consumer.addThread(this);

    if (nextTermsHash != null) {
      charPool = new CharBlockPool(termsHash.docWriter);
      primary = true;
    } else {
      charPool = primaryPerThread.charPool;
      primary = false;
    }

    intPool = new IntBlockPool(termsHash.docWriter, termsHash.trackAllocations);
    bytePool = new ByteBlockPool(termsHash.docWriter.byteBlockAllocator, termsHash.trackAllocations);

    if (nextTermsHash != null)
      nextPerThread = nextTermsHash.addThread(docInverterPerThread, this);
    else
      nextPerThread = null;
  }

  InvertedDocConsumerPerField addField(DocInverterPerField docInverterPerField, final FieldInfo fieldInfo) {
    return new TermsHashPerField(docInverterPerField, this, nextPerThread, fieldInfo);
  }

  synchronized public void abort() {
    reset(true);
    consumer.abort();
    if (nextPerThread != null)
      nextPerThread.abort();
  }

  void morePostings() throws IOException {
    assert freePostingsCount == 0;
    termsHash.getPostings(freePostings);
    freePostingsCount = freePostings.length;
    assert noNullPostings(freePostings, freePostingsCount, "consumer=" + consumer);
  }

  private static boolean noNullPostings(RawPostingList[] postings, int count, String details) {
    for(int i=0;i<count;i++)
      assert postings[i] != null: "postings[" + i + "] of " + count + " is null: " + details;
    return true;
  }

  public void startDocument() throws IOException {
    consumer.startDocument();
    if (nextPerThread != null)
      nextPerThread.consumer.startDocument();
  }

  public DocumentsWriter.DocWriter finishDocument() throws IOException {
    final DocumentsWriter.DocWriter doc = consumer.finishDocument();

    final DocumentsWriter.DocWriter doc2;
    if (nextPerThread != null)
      doc2 = nextPerThread.consumer.finishDocument();
    else
      doc2 = null;
    if (doc == null)
      return doc2;
    else {
      doc.setNext(doc2);
      return doc;
    }
  }

  void reset(boolean recyclePostings) {
    intPool.reset();
    bytePool.reset();

    if (primary)
      charPool.reset();

    if (recyclePostings) {
      termsHash.recyclePostings(freePostings, freePostingsCount);
      freePostingsCount = 0;
    }
  }
}
