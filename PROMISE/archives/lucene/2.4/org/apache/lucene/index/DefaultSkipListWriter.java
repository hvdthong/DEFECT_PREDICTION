import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.store.IndexOutput;


/**
 * Implements the skip list writer for the default posting list format
 * that stores positions and payloads.
 *
 */
class DefaultSkipListWriter extends MultiLevelSkipListWriter {
  private int[] lastSkipDoc;
  private int[] lastSkipPayloadLength;
  private long[] lastSkipFreqPointer;
  private long[] lastSkipProxPointer;
  
  private IndexOutput freqOutput;
  private IndexOutput proxOutput;

  private int curDoc;
  private boolean curStorePayloads;
  private int curPayloadLength;
  private long curFreqPointer;
  private long curProxPointer;
  
  DefaultSkipListWriter(int skipInterval, int numberOfSkipLevels, int docCount, IndexOutput freqOutput, IndexOutput proxOutput) {
    super(skipInterval, numberOfSkipLevels, docCount);
    this.freqOutput = freqOutput;
    this.proxOutput = proxOutput;
    
    lastSkipDoc = new int[numberOfSkipLevels];
    lastSkipPayloadLength = new int[numberOfSkipLevels];
    lastSkipFreqPointer = new long[numberOfSkipLevels];
    lastSkipProxPointer = new long[numberOfSkipLevels];
  }

  /**
   * Sets the values for the current skip data. 
   */
  void setSkipData(int doc, boolean storePayloads, int payloadLength) {
    this.curDoc = doc;
    this.curStorePayloads = storePayloads;
    this.curPayloadLength = payloadLength;
    this.curFreqPointer = freqOutput.getFilePointer();
    if (proxOutput != null)
      this.curProxPointer = proxOutput.getFilePointer();
  }
  
  protected void resetSkip() {
    super.resetSkip();
    Arrays.fill(lastSkipDoc, 0);
    Arrays.fill(lastSkipFreqPointer, freqOutput.getFilePointer());
    if (proxOutput != null)
      Arrays.fill(lastSkipProxPointer, proxOutput.getFilePointer());
  }
  
  protected void writeSkipData(int level, IndexOutput skipBuffer) throws IOException {
    if (curStorePayloads) {
      int delta = curDoc - lastSkipDoc[level];
      if (curPayloadLength == lastSkipPayloadLength[level]) {
        skipBuffer.writeVInt(delta * 2);
      } else {
        skipBuffer.writeVInt(delta * 2 + 1);
        skipBuffer.writeVInt(curPayloadLength);
        lastSkipPayloadLength[level] = curPayloadLength;
      }
    } else {
      skipBuffer.writeVInt(curDoc - lastSkipDoc[level]);
    }
    skipBuffer.writeVInt((int) (curFreqPointer - lastSkipFreqPointer[level]));
    skipBuffer.writeVInt((int) (curProxPointer - lastSkipProxPointer[level]));

    lastSkipDoc[level] = curDoc;
    
    lastSkipFreqPointer[level] = curFreqPointer;
    lastSkipProxPointer[level] = curProxPointer;
  }

}
