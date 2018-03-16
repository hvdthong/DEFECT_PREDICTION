final class IndexFileNames {

  /** Name of the index segment file */
  static final String SEGMENTS = "segments";
  
  /** Name of the index deletable file */
  static final String DELETABLE = "deletable";
  
  /**
   * This array contains all filename extensions used by Lucene's index files, with
   * one exception, namely the extension made up from <code>.f</code> + a number.
   * Also note that two of Lucene's files (<code>deletable</code> and
   * <code>segments</code>) don't have any filename extension.
   */
  static final String INDEX_EXTENSIONS[] = new String[] {
      "cfs", "fnm", "fdx", "fdt", "tii", "tis", "frq", "prx", "del",
      "tvx", "tvd", "tvf", "tvp" };
  
  /** File extensions of old-style index files */
  static final String COMPOUND_EXTENSIONS[] = new String[] {
    "fnm", "frq", "prx", "fdx", "fdt", "tii", "tis"
  };
  
  /** File extensions for term vector support */
  static final String VECTOR_EXTENSIONS[] = new String[] {
    "tvx", "tvd", "tvf"
  };
  
}
