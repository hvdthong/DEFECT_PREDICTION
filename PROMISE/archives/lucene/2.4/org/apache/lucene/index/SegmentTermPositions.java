import org.apache.lucene.store.IndexInput;

import java.io.IOException;

final class SegmentTermPositions
extends SegmentTermDocs implements TermPositions {
  private IndexInput proxStream;
  private int proxCount;
  private int position;
  
  private int payloadLength;
  private boolean needToLoadPayload;
  
  private long lazySkipPointer = -1;
  private int lazySkipProxCount = 0;
  
  SegmentTermPositions(SegmentReader p) {
    super(p);
  }

  final void seek(TermInfo ti, Term term) throws IOException {
    super.seek(ti, term);
    if (ti != null)
      lazySkipPointer = ti.proxPointer;
    
    lazySkipProxCount = 0;
    proxCount = 0;
    payloadLength = 0;
    needToLoadPayload = false;
  }

  public final void close() throws IOException {
    super.close();
    if (proxStream != null) proxStream.close();
  }

  public final int nextPosition() throws IOException {
    if (currentFieldOmitTf)
      return 0;
    lazySkip();
    proxCount--;
    return position += readDeltaPosition();
  }

  private final int readDeltaPosition() throws IOException {
    int delta = proxStream.readVInt();
    if (currentFieldStoresPayloads) {
      if ((delta & 1) != 0) {
        payloadLength = proxStream.readVInt();
      } 
      delta >>>= 1;
      needToLoadPayload = true;
    }
    return delta;
  }
  
  protected final void skippingDoc() throws IOException {
    lazySkipProxCount += freq;
  }

  public final boolean next() throws IOException {
    lazySkipProxCount += proxCount;
    
      return true;
    }
    return false;
  }

  public final int read(final int[] docs, final int[] freqs) {
    throw new UnsupportedOperationException("TermPositions does not support processing multiple documents in one call. Use TermDocs instead.");
  }


  /** Called by super.skipTo(). */
  protected void skipProx(long proxPointer, int payloadLength) throws IOException {
    lazySkipPointer = proxPointer;
    lazySkipProxCount = 0;
    proxCount = 0;
    this.payloadLength = payloadLength;
    needToLoadPayload = false;
  }

  private void skipPositions(int n) throws IOException {
    assert !currentFieldOmitTf;
      readDeltaPosition();
      skipPayload();
    }      
  }
  
  private void skipPayload() throws IOException {
    if (needToLoadPayload && payloadLength > 0) {
      proxStream.seek(proxStream.getFilePointer() + payloadLength);
    }
    needToLoadPayload = false;
  }

  private void lazySkip() throws IOException {
    if (proxStream == null) {
      proxStream = (IndexInput)parent.proxStream.clone();
    }
    
    skipPayload();
      
    if (lazySkipPointer != -1) {
      proxStream.seek(lazySkipPointer);
      lazySkipPointer = -1;
    }
     
    if (lazySkipProxCount != 0) {
      skipPositions(lazySkipProxCount);
      lazySkipProxCount = 0;
    }
  }
  
  public int getPayloadLength() {
    return payloadLength;
  }

  public byte[] getPayload(byte[] data, int offset) throws IOException {
    if (!needToLoadPayload) {
      throw new IOException("Payload cannot be loaded more than once for the same term position.");
    }

    byte[] retArray;
    int retOffset;
    if (data == null || data.length - offset < payloadLength) {
      retArray = new byte[payloadLength];
      retOffset = 0;
    } else {
      retArray = data;
      retOffset = offset;
    }
    proxStream.readBytes(retArray, retOffset, payloadLength);
    needToLoadPayload = false;
    return retArray;
  }

  public boolean isPayloadAvailable() {
    return needToLoadPayload && payloadLength > 0;
  }

}
