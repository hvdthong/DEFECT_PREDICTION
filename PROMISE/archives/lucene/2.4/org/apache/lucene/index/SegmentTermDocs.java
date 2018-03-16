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
  private int maxSkipLevels;
  private DefaultSkipListReader skipListReader;
  
  private long freqBasePointer;
  private long proxBasePointer;

  private long skipPointer;
  private boolean haveSkipped;
  
  protected boolean currentFieldStoresPayloads;
  protected boolean currentFieldOmitTf;
  
  protected SegmentTermDocs(SegmentReader parent) {
    this.parent = parent;
    this.freqStream = (IndexInput) parent.freqStream.clone();
    this.deletedDocs = parent.deletedDocs;
    this.skipInterval = parent.tis.getSkipInterval();
    this.maxSkipLevels = parent.tis.getMaxSkipLevels();
  }

  public void seek(Term term) throws IOException {
    TermInfo ti = parent.tis.get(term);
    seek(ti, term);
  }

  public void seek(TermEnum termEnum) throws IOException {
    TermInfo ti;
    Term term;
    
      SegmentTermEnum segmentTermEnum = ((SegmentTermEnum) termEnum);
      term = segmentTermEnum.term();
      ti = segmentTermEnum.termInfo();
      term = termEnum.term();
      ti = parent.tis.get(term);        
    }
    
    seek(ti, term);
  }

  void seek(TermInfo ti, Term term) throws IOException {
    count = 0;
    FieldInfo fi = parent.fieldInfos.fieldInfo(term.field);
    currentFieldOmitTf = (fi != null) ? fi.omitTf : false;
    currentFieldStoresPayloads = (fi != null) ? fi.storePayloads : false;
    if (ti == null) {
      df = 0;
    } else {
      df = ti.docFreq;
      doc = 0;
      freqBasePointer = ti.freqPointer;
      proxBasePointer = ti.proxPointer;
      skipPointer = freqBasePointer + ti.skipOffset;
      freqStream.seek(freqBasePointer);
      haveSkipped = false;
    }
  }

  public void close() throws IOException {
    freqStream.close();
    if (skipListReader != null)
      skipListReader.close();
  }

  public final int doc() { return doc; }
  public final int freq() { return freq; }

  protected void skippingDoc() throws IOException {
  }

  public boolean next() throws IOException {
    while (true) {
      if (count == df)
        return false;
      final int docCode = freqStream.readVInt();
      
      if (currentFieldOmitTf) {
        doc += docCode;
        freq = 1;
      } else {
        else
      }
      
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
    if (currentFieldOmitTf) {
      return readNoTf(docs, freqs, length);
    } else {
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
  }

  private final int readNoTf(final int[] docs, final int[] freqs, final int length) throws IOException {
    int i = 0;
    while (i < length && count < df) {
      doc += freqStream.readVInt();       
      count++;

      if (deletedDocs == null || !deletedDocs.get(doc)) {
        docs[i] = doc;
        freqs[i] = 1;
        ++i;
      }
    }
    return i;
  }
 
  
  /** Overridden by SegmentTermPositions to skip in prox stream. */
  protected void skipProx(long proxPointer, int payloadLength) throws IOException {}

  /** Optimized implementation. */
  public boolean skipTo(int target) throws IOException {
      if (skipListReader == null)

        skipListReader.init(skipPointer, freqBasePointer, proxBasePointer, df, currentFieldStoresPayloads);
        haveSkipped = true;
      }

      int newCount = skipListReader.skipTo(target); 
      if (newCount > count) {
        freqStream.seek(skipListReader.getFreqPointer());
        skipProx(skipListReader.getProxPointer(), skipListReader.getPayloadLength());

        doc = skipListReader.getDoc();
        count = newCount;
      }      
    }

    do {
      if (!next())
        return false;
    } while (target > doc);
    return true;
  }
}
