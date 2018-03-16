class ReadOnlySegmentReader extends SegmentReader {

  static void noWrite() {
    throw new UnsupportedOperationException("This IndexReader cannot make any changes to the index (it was opened with readOnly = true)");
  }
  
  protected void acquireWriteLock() {
    noWrite();
  }

  public boolean isDeleted(int n) {
    return deletedDocs != null && deletedDocs.get(n);
  }
}
