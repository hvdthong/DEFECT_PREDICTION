import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.store.IndexInput;

/**
 * Implements the skip list reader for the default posting list format
 * that stores positions and payloads.
 *
 */
class DefaultSkipListReader extends MultiLevelSkipListReader {
  private boolean currentFieldStoresPayloads;
  private long freqPointer[];
  private long proxPointer[];
  private int payloadLength[];
  
  private long lastFreqPointer;
  private long lastProxPointer;
  private int lastPayloadLength;
                           

  DefaultSkipListReader(IndexInput skipStream, int maxSkipLevels, int skipInterval) {
    super(skipStream, maxSkipLevels, skipInterval);
    freqPointer = new long[maxSkipLevels];
    proxPointer = new long[maxSkipLevels];
    payloadLength = new int[maxSkipLevels];
  }
  
  void init(long skipPointer, long freqBasePointer, long proxBasePointer, int df, boolean storesPayloads) {
    super.init(skipPointer, df);
    this.currentFieldStoresPayloads = storesPayloads;
    lastFreqPointer = freqBasePointer;
    lastProxPointer = proxBasePointer;

    Arrays.fill(freqPointer, freqBasePointer);
    Arrays.fill(proxPointer, proxBasePointer);
    Arrays.fill(payloadLength, 0);
  }

  /** Returns the freq pointer of the doc to which the last call of 
   * {@link MultiLevelSkipListReader#skipTo(int)} has skipped.  */
  long getFreqPointer() {
    return lastFreqPointer;
  }

  /** Returns the prox pointer of the doc to which the last call of 
   * {@link MultiLevelSkipListReader#skipTo(int)} has skipped.  */
  long getProxPointer() {
    return lastProxPointer;
  }
  
  /** Returns the payload length of the payload stored just before 
   * the doc to which the last call of {@link MultiLevelSkipListReader#skipTo(int)} 
   * has skipped.  */
  int getPayloadLength() {
    return lastPayloadLength;
  }
  
  protected void seekChild(int level) throws IOException {
    super.seekChild(level);
    freqPointer[level] = lastFreqPointer;
    proxPointer[level] = lastProxPointer;
    payloadLength[level] = lastPayloadLength;
  }
  
  protected void setLastSkipData(int level) {
    super.setLastSkipData(level);
    lastFreqPointer = freqPointer[level];
    lastProxPointer = proxPointer[level];
    lastPayloadLength = payloadLength[level];
  }


  protected int readSkipData(int level, IndexInput skipStream) throws IOException {
    int delta;
    if (currentFieldStoresPayloads) {
      delta = skipStream.readVInt();
      if ((delta & 1) != 0) {
        payloadLength[level] = skipStream.readVInt();
      }
      delta >>>= 1;
    } else {
      delta = skipStream.readVInt();
    }
    freqPointer[level] += skipStream.readVInt();
    proxPointer[level] += skipStream.readVInt();
    
    return delta;
  }
}
