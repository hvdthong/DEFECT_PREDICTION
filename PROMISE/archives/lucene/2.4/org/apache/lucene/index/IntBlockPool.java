final class IntBlockPool {

  public int[][] buffers = new int[10][];



  final private DocumentsWriter docWriter;
  final boolean trackAllocations;

  public IntBlockPool(DocumentsWriter docWriter, boolean trackAllocations) {
    this.docWriter = docWriter;
    this.trackAllocations = trackAllocations;
  }

  public void reset() {
    if (bufferUpto != -1) {
      if (bufferUpto > 0)
        docWriter.recycleIntBlocks(buffers, 1, 1+bufferUpto);

      bufferUpto = 0;
      intUpto = 0;
      intOffset = 0;
      buffer = buffers[0];
    }
  }

  public void nextBuffer() {
    if (1+bufferUpto == buffers.length) {
      int[][] newBuffers = new int[(int) (buffers.length*1.5)][];
      System.arraycopy(buffers, 0, newBuffers, 0, buffers.length);
      buffers = newBuffers;
    }
    buffer = buffers[1+bufferUpto] = docWriter.getIntBlock(trackAllocations);
    bufferUpto++;

    intUpto = 0;
    intOffset += DocumentsWriter.INT_BLOCK_SIZE;
  }
}

