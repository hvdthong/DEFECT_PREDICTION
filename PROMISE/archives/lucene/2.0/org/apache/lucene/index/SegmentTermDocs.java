import java.io.IOException;
import org.apache.lucene.util.BitVector;
import org.apache.lucene.store.IndexInput;

class SegmentTermDocs implements TermDocs {
  protected SegmentReader parent;
  protected IndexInput freqStream;
  protected int count;
  protected int df;
  protected BitVector deletedDocs;
  int doc = 0;
  int freq;

  private int skipInterval;
  private int numSkips;
  private int skipCount;
  private IndexInput skipStream;
  private int skipDoc;
  private long freqPointer;
  private long proxPointer;
  private long skipPointer;
  private boolean haveSkipped;

  protected SegmentTermDocs(SegmentReader parent) {
    this.parent = parent;
    this.freqStream = (IndexInput) parent.freqStream.clone();
    this.deletedDocs = parent.deletedDocs;
    this.skipInterval = parent.tis.getSkipInterval();
  }

  public void seek(Term term) throws IOException {
    TermInfo ti = parent.tis.get(term);
    seek(ti);
  }

  public void seek(TermEnum termEnum) throws IOException {
    TermInfo ti;
    
      ti = ((SegmentTermEnum) termEnum).termInfo();
      ti = parent.tis.get(termEnum.term());
      
    seek(ti);
  }

  void seek(TermInfo ti) throws IOException {
    count = 0;
    if (ti == null) {
      df = 0;
    } else {
      df = ti.docFreq;
      doc = 0;
      skipDoc = 0;
      skipCount = 0;
      numSkips = df / skipInterval;
      freqPointer = ti.freqPointer;
      proxPointer = ti.proxPointer;
      skipPointer = freqPointer + ti.skipOffset;
      freqStream.seek(freqPointer);
      haveSkipped = false;
    }
  }

  public void close() throws IOException {
    freqStream.close();
    if (skipStream != null)
      skipStream.close();
  }

  public final int doc() { return doc; }
  public final int freq() { return freq; }

  protected void skippingDoc() throws IOException {
  }

  public boolean next() throws IOException {
    while (true) {
      if (count == df)
        return false;

      int docCode = freqStream.readVInt();
      else

      count++;

      if (deletedDocs == null || !deletedDocs.get(doc))
        break;
      skippingDoc();
    }
    return true;
  }

  /** Optimized implementation. */
  public int read(final int[] docs, final int[] freqs)
          throws IOException {
    final int length = docs.length;
    int i = 0;
    while (i < length && count < df) {

      final int docCode = freqStream.readVInt();
      else
      count++;

      if (deletedDocs == null || !deletedDocs.get(doc)) {
        docs[i] = doc;
        freqs[i] = freq;
        ++i;
      }
    }
    return i;
  }

  /** Overridden by SegmentTermPositions to skip in prox stream. */
  protected void skipProx(long proxPointer) throws IOException {}

  /** Optimized implementation. */
  public boolean skipTo(int target) throws IOException {

      if (skipStream == null)

        skipStream.seek(skipPointer);
        haveSkipped = true;
      }

      int lastSkipDoc = skipDoc;
      long lastFreqPointer = freqStream.getFilePointer();
      long lastProxPointer = -1;
      int numSkipped = -1 - (count % skipInterval);

      while (target > skipDoc) {
        lastSkipDoc = skipDoc;
        lastFreqPointer = freqPointer;
        lastProxPointer = proxPointer;
        
        if (skipDoc != 0 && skipDoc >= doc)
          numSkipped += skipInterval;
        
        if(skipCount >= numSkips)
          break;

        skipDoc += skipStream.readVInt();
        freqPointer += skipStream.readVInt();
        proxPointer += skipStream.readVInt();

        skipCount++;
      }
      
      if (lastFreqPointer > freqStream.getFilePointer()) {
        freqStream.seek(lastFreqPointer);
        skipProx(lastProxPointer);

        doc = lastSkipDoc;
        count += numSkipped;
      }

    }

    do {
      if (!next())
        return false;
    } while (target > doc);
    return true;
  }

}
