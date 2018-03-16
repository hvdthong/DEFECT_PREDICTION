import java.io.IOException;

import org.apache.lucene.store.IndexInput;

final class SegmentTermPositions
extends SegmentTermDocs implements TermPositions {
  private IndexInput proxStream;
  private int proxCount;
  private int position;
  
  SegmentTermPositions(SegmentReader p) {
    super(p);
    this.proxStream = (IndexInput)parent.proxStream.clone();
  }

  final void seek(TermInfo ti) throws IOException {
    super.seek(ti);
    if (ti != null)
      proxStream.seek(ti.proxPointer);
    proxCount = 0;
  }

  public final void close() throws IOException {
    super.close();
    proxStream.close();
  }

  public final int nextPosition() throws IOException {
    proxCount--;
    return position += proxStream.readVInt();
  }

  protected final void skippingDoc() throws IOException {
      proxStream.readVInt();
  }

  public final boolean next() throws IOException {
      proxStream.readVInt();

      return true;
    }
    return false;
  }

  public final int read(final int[] docs, final int[] freqs) {
    throw new UnsupportedOperationException("TermPositions does not support processing multiple documents in one call. Use TermDocs instead.");
  }


  /** Called by super.skipTo(). */
  protected void skipProx(long proxPointer) throws IOException {
    proxStream.seek(proxPointer);
    proxCount = 0;
  }

}
