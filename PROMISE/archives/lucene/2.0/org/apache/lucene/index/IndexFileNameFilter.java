import java.io.File;
import java.io.FilenameFilter;

/**
 * Filename filter that accept filenames and extensions only created by Lucene.
 * 
 * @author Daniel Naber / Bernhard Messer
 * @version $rcs = ' $Id: Exp $ ' ;
 */
public class IndexFileNameFilter implements FilenameFilter {

  /* (non-Javadoc)
   * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
   */
  public boolean accept(File dir, String name) {
    for (int i = 0; i < IndexFileNames.INDEX_EXTENSIONS.length; i++) {
      if (name.endsWith("."+IndexFileNames.INDEX_EXTENSIONS[i]))
        return true;
    }
    if (name.equals(IndexFileNames.DELETABLE)) return true;
    else if (name.equals(IndexFileNames.SEGMENTS)) return true;
    else if (name.matches(".+\\.f\\d+")) return true;
    return false;
  }

}
