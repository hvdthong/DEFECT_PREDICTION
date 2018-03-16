final class CharBlockPool {

  public char[][] buffers = new char[10][];
  int numBuffer;


  final private DocumentsWriter docWriter;

  public CharBlockPool(DocumentsWriter docWriter) {
    this.docWriter = docWriter;
  }

  public void reset() {
    docWriter.recycleCharBlocks(buffers, 1+bufferUpto);
    bufferUpto = -1;
    charUpto = DocumentsWriter.CHAR_BLOCK_SIZE;
    charOffset = -DocumentsWriter.CHAR_BLOCK_SIZE;
  }

  public void nextBuffer() {
    if (1+bufferUpto == buffers.length) {
      char[][] newBuffers = new char[(int) (buffers.length*1.5)][];
      System.arraycopy(buffers, 0, newBuffers, 0, buffers.length);
      buffers = newBuffers;
    }
    buffer = buffers[1+bufferUpto] = docWriter.getCharBlock();
    bufferUpto++;

    charUpto = 0;
    charOffset += DocumentsWriter.CHAR_BLOCK_SIZE;
  }
}

