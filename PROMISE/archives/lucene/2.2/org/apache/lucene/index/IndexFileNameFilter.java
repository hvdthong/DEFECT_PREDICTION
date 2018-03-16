import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;

/**
 * Filename filter that accept filenames and extensions only created by Lucene.
 * 
 * @author Daniel Naber / Bernhard Messer
 * @version $rcs = ' $Id: Exp $ ' ;
 */
public class IndexFileNameFilter implements FilenameFilter {

  static IndexFileNameFilter singleton = new IndexFileNameFilter();
  private HashSet extensions;
  private HashSet extensionsInCFS;

  public IndexFileNameFilter() {
    extensions = new HashSet();
    for (int i = 0; i < IndexFileNames.INDEX_EXTENSIONS.length; i++) {
      extensions.add(IndexFileNames.INDEX_EXTENSIONS[i]);
    }
    extensionsInCFS = new HashSet();
    for (int i = 0; i < IndexFileNames.INDEX_EXTENSIONS_IN_COMPOUND_FILE.length; i++) {
      extensionsInCFS.add(IndexFileNames.INDEX_EXTENSIONS_IN_COMPOUND_FILE[i]);
    }
  }

  /* (non-Javadoc)
   * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
   */
  public boolean accept(File dir, String name) {
    int i = name.lastIndexOf('.');
    if (i != -1) {
      String extension = name.substring(1+i);
      if (extensions.contains(extension)) {
        return true;
      } else if (extension.startsWith("f") &&
                 extension.matches("f\\d+")) {
        return true;
      } else if (extension.startsWith("s") &&
                 extension.matches("s\\d+")) {
        return true;
      }
    } else {
      if (name.equals(IndexFileNames.DELETABLE)) return true;
      else if (name.startsWith(IndexFileNames.SEGMENTS)) return true;
    }
    return false;
  }

  /**
   * Returns true if this is a file that would be contained
   * in a CFS file.  This function should only be called on
   * files that pass the above "accept" (ie, are already
   * known to be a Lucene index file).
   */
  public boolean isCFSFile(String name) {
    int i = name.lastIndexOf('.');
    if (i != -1) {
      String extension = name.substring(1+i);
      if (extensionsInCFS.contains(extension)) {
        return true;
      }
      if (extension.startsWith("f") &&
          extension.matches("f\\d+")) {
        return true;
      }
    }
    return false;
  }

  public static IndexFileNameFilter getFilter() {
    return singleton;
  }
}
