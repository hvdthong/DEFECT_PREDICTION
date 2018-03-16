public final class LucenePackage {


  /** Return Lucene's package, including version information. */
  public static Package get() {
    return LucenePackage.class.getPackage();
  }
}
