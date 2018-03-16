import java.io.IOException;

import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.RAMOutputStream;

/**
 * This abstract class writes skip lists with multiple levels.
 * 
 * Example for skipInterval = 3:
 *                                                     c            (skip level 2)
 *                 c                 c                 c            (skip level 1) 
 *     x     x     x     x     x     x     x     x     x     x      (skip level 0)
 * d d d d d d d d d d d d d d d d d d d d d d d d d d d d d d d d  (posting list)
 *     3     6     9     12    15    18    21    24    27    30     (df)
 * 
 * d - document
 * x - skip data
 * c - skip data with child pointer
 * 
 * Skip level i contains every skipInterval-th entry from skip level i-1.
 * Therefore the number of entries on level i is: floor(df / ((skipInterval ^ (i + 1))).
 * 
 * Each skip entry on a level i>0 contains a pointer to the corresponding skip entry in list i-1.
 * This guarantess a logarithmic amount of skips to find the target document.
 * 
 * While this class takes care of writing the different skip levels,
 * subclasses must define the actual format of the skip data.
 * 
 */
abstract class MultiLevelSkipListWriter {
  private int numberOfSkipLevels;
  
  private int skipInterval;
  
  private RAMOutputStream[] skipBuffer;

  protected MultiLevelSkipListWriter(int skipInterval, int maxSkipLevels, int df) {
    this.skipInterval = skipInterval;
    
    numberOfSkipLevels = df == 0 ? 0 : (int) Math.floor(Math.log(df) / Math.log(skipInterval));
    
    if (numberOfSkipLevels > maxSkipLevels) {
      numberOfSkipLevels = maxSkipLevels;
    }
  }
  
  protected void init() {
    skipBuffer = new RAMOutputStream[numberOfSkipLevels];
    for (int i = 0; i < numberOfSkipLevels; i++) {
      skipBuffer[i] = new RAMOutputStream();
    }
  }

  protected void resetSkip() {
    if (skipBuffer == null) {
      init();
    } else {
      for (int i = 0; i < skipBuffer.length; i++) {
        skipBuffer[i].reset();
      }
    }      
  }

  /**
   * Subclasses must implement the actual skip data encoding in this method.
   *  
   * @param level the level skip data shall be writting for
   * @param skipBuffer the skip buffer to write to
   */
  protected abstract void writeSkipData(int level, IndexOutput skipBuffer) throws IOException;
  
  /**
   * Writes the current skip data to the buffers. The current document frequency determines
   * the max level is skip data is to be written to. 
   * 
   * @param df the current document frequency 
   * @throws IOException
   */
  void bufferSkip(int df) throws IOException {
    int numLevels;
   
    for (numLevels = 0; (df % skipInterval) == 0 && numLevels < numberOfSkipLevels; df /= skipInterval) {
      numLevels++;
    }
    
    long childPointer = 0;
    
    for (int level = 0; level < numLevels; level++) {
      writeSkipData(level, skipBuffer[level]);
      
      long newChildPointer = skipBuffer[level].getFilePointer();
      
      if (level != 0) {
        skipBuffer[level].writeVLong(childPointer);
      }
      
      childPointer = newChildPointer;
    }
  }

  /**
   * Writes the buffered skip lists to the given output.
   * 
   * @param output the IndexOutput the skip lists shall be written to 
   * @return the pointer the skip list starts
   */
  long writeSkip(IndexOutput output) throws IOException {
    long skipPointer = output.getFilePointer();
    if (skipBuffer == null || skipBuffer.length == 0) return skipPointer;
    
    for (int level = numberOfSkipLevels - 1; level > 0; level--) {
      long length = skipBuffer[level].getFilePointer();
      if (length > 0) {
        output.writeVLong(length);
        skipBuffer[level].writeTo(output);
      }
    }
    skipBuffer[0].writeTo(output);
    
    return skipPointer;
  }

}
