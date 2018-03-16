import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.IndexInput;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Iterator;
import java.io.IOException;


/**
 * Combines multiple files into a single compound file.
 * The file format:<br>
 * <ul>
 *     <li>VInt fileCount</li>
 *     <li>{Directory}
 *         fileCount entries with the following structure:</li>
 *         <ul>
 *             <li>long dataOffset</li>
 *             <li>String fileName</li>
 *         </ul>
 *     <li>{File Data}
 *         fileCount entries with the raw data of the corresponding file</li>
 * </ul>
 *
 * The fileCount integer indicates how many files are contained in this compound
 * file. The {directory} that follows has that many entries. Each directory entry
 * contains a long pointer to the start of this file's data section, and a String
 * with that file's name.
 *
 *
 * @version $Id: CompoundFileWriter.java 690539 2008-08-30 17:33:06Z mikemccand $
 */
final class CompoundFileWriter {

    private static final class FileEntry {
        /** source file */
        String file;

        /** temporary holder for the start of directory entry for this file */
        long directoryOffset;

        /** temporary holder for the start of this file's data section */
        long dataOffset;
    }


    private Directory directory;
    private String fileName;
    private HashSet ids;
    private LinkedList entries;
    private boolean merged = false;
    private SegmentMerger.CheckAbort checkAbort;

    /** Create the compound stream in the specified file. The file name is the
     *  entire name (no extensions are added).
     *  @throws NullPointerException if <code>dir</code> or <code>name</code> is null
     */
    public CompoundFileWriter(Directory dir, String name) {
      this(dir, name, null);
    }

    CompoundFileWriter(Directory dir, String name, SegmentMerger.CheckAbort checkAbort) {
        if (dir == null)
            throw new NullPointerException("directory cannot be null");
        if (name == null)
            throw new NullPointerException("name cannot be null");
        this.checkAbort = checkAbort;
        directory = dir;
        fileName = name;
        ids = new HashSet();
        entries = new LinkedList();
    }

    /** Returns the directory of the compound file. */
    public Directory getDirectory() {
        return directory;
    }

    /** Returns the name of the compound file. */
    public String getName() {
        return fileName;
    }

    /** Add a source stream. <code>file</code> is the string by which the 
     *  sub-stream will be known in the compound stream.
     * 
     *  @throws IllegalStateException if this writer is closed
     *  @throws NullPointerException if <code>file</code> is null
     *  @throws IllegalArgumentException if a file with the same name
     *   has been added already
     */
    public void addFile(String file) {
        if (merged)
            throw new IllegalStateException(
                "Can't add extensions after merge has been called");

        if (file == null)
            throw new NullPointerException(
                "file cannot be null");

        if (! ids.add(file))
            throw new IllegalArgumentException(
                "File " + file + " already added");

        FileEntry entry = new FileEntry();
        entry.file = file;
        entries.add(entry);
    }

    /** Merge files with the extensions added up to now.
     *  All files with these extensions are combined sequentially into the
     *  compound stream. After successful merge, the source files
     *  are deleted.
     *  @throws IllegalStateException if close() had been called before or
     *   if no file has been added to this object
     */
    public void close() throws IOException {
        if (merged)
            throw new IllegalStateException(
                "Merge already performed");

        if (entries.isEmpty())
            throw new IllegalStateException(
                "No entries to merge have been defined");

        merged = true;

        IndexOutput os = null;
        try {
            os = directory.createOutput(fileName);

            os.writeVInt(entries.size());

            Iterator it = entries.iterator();
            long totalSize = 0;
            while(it.hasNext()) {
                FileEntry fe = (FileEntry) it.next();
                fe.directoryOffset = os.getFilePointer();
                os.writeString(fe.file);
                totalSize += directory.fileLength(fe.file);
            }

            final long finalLength = totalSize+os.getFilePointer();
            os.setLength(finalLength);

            byte buffer[] = new byte[16384];
            it = entries.iterator();
            while(it.hasNext()) {
                FileEntry fe = (FileEntry) it.next();
                fe.dataOffset = os.getFilePointer();
                copyFile(fe, os, buffer);
            }

            it = entries.iterator();
            while(it.hasNext()) {
                FileEntry fe = (FileEntry) it.next();
                os.seek(fe.directoryOffset);
                os.writeLong(fe.dataOffset);
            }

            assert finalLength == os.length();

            IndexOutput tmp = os;
            os = null;
            tmp.close();

        } finally {
            if (os != null) try { os.close(); } catch (IOException e) { }
        }
    }

    /** Copy the contents of the file with specified extension into the
     *  provided output stream. Use the provided buffer for moving data
     *  to reduce memory allocation.
     */
    private void copyFile(FileEntry source, IndexOutput os, byte buffer[])
    throws IOException
    {
        IndexInput is = null;
        try {
            long startPtr = os.getFilePointer();

            is = directory.openInput(source.file);
            long length = is.length();
            long remainder = length;
            int chunk = buffer.length;

            while(remainder > 0) {
                int len = (int) Math.min(chunk, remainder);
                is.readBytes(buffer, 0, len, false);
                os.writeBytes(buffer, len);
                remainder -= len;
                if (checkAbort != null)
                  checkAbort.work(80);
            }

            if (remainder != 0)
                throw new IOException(
                    "Non-zero remainder length after copying: " + remainder
                    + " (id: " + source.file + ", length: " + length
                    + ", buffer size: " + chunk + ")");

            long endPtr = os.getFilePointer();
            long diff = endPtr - startPtr;
            if (diff != length)
                throw new IOException(
                    "Difference in the output file offsets " + diff
                    + " does not match the original file length " + length);

        } finally {
            if (is != null) is.close();
        }
    }
}
