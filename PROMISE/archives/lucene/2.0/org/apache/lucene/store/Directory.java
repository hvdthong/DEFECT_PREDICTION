import java.io.IOException;

/** A Directory is a flat list of files.  Files may be written once, when they
 * are created.  Once a file is created it may only be opened for read, or
 * deleted.  Random access is permitted both when reading and writing.
 *
 * <p> Java's i/o APIs not used directly, but rather all i/o is
 * through this API.  This permits things such as: <ul>
 * <li> implementation of RAM-based indices;
 * <li> implementation indices stored in a database, via JDBC;
 * <li> implementation of an index as a single file;
 * </ul>
 *
 * @author Doug Cutting
 */
public abstract class Directory {
  /** Returns an array of strings, one for each file in the directory. */
  public abstract String[] list()
       throws IOException;

  /** Returns true iff a file with the given name exists. */
  public abstract boolean fileExists(String name)
       throws IOException;

  /** Returns the time the named file was last modified. */
  public abstract long fileModified(String name)
       throws IOException;

  /** Set the modified time of an existing file to now. */
  public abstract void touchFile(String name)
       throws IOException;

  /** Removes an existing file in the directory. */
  public abstract void deleteFile(String name)
       throws IOException;

  /** Renames an existing file in the directory.
    If a file already exists with the new name, then it is replaced.
    This replacement should be atomic. */
  public abstract void renameFile(String from, String to)
       throws IOException;

  /** Returns the length of a file in the directory. */
  public abstract long fileLength(String name)
       throws IOException;


  /** Creates a new, empty file in the directory with the given name.
      Returns a stream writing this file. */
  public abstract IndexOutput createOutput(String name) throws IOException;


  /** Returns a stream reading an existing file. */
  public abstract IndexInput openInput(String name)
    throws IOException;

  /** Construct a {@link Lock}.
   * @param name the name of the lock file
   */
  public abstract Lock makeLock(String name);

  /** Closes the store. */
  public abstract void close()
       throws IOException;
}
