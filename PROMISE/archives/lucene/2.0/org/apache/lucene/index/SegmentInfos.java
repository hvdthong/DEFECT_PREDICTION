import java.util.Vector;
import java.io.IOException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.util.Constants;

final class SegmentInfos extends Vector {
  
  /** The file format version, a negative number. */
  /* Works since counter, the old 1st entry, is always >= 0 */
  public static final int FORMAT = -1;
  
  /**
   * counts how often the index has been changed by adding or deleting docs.
   * starting with the current time in milliseconds forces to create unique version numbers.
   */
  private long version = System.currentTimeMillis();

  public final SegmentInfo info(int i) {
    return (SegmentInfo) elementAt(i);
  }

  public final void read(Directory directory) throws IOException {
    
    IndexInput input = directory.openInput(IndexFileNames.SEGMENTS);
    try {
      int format = input.readInt();
        if (format < FORMAT)
          throw new IOException("Unknown format version: " + format);
      }
        counter = format;
      }
      
        SegmentInfo si =
          new SegmentInfo(input.readString(), input.readInt(), directory);
        addElement(si);
      }
      
        if (input.getFilePointer() >= input.length())
        else
      }
    }
    finally {
      input.close();
    }
  }

  public final void write(Directory directory) throws IOException {
    IndexOutput output = directory.createOutput("segments.new");
    try {
      for (int i = 0; i < size(); i++) {
        SegmentInfo si = info(i);
        output.writeString(si.name);
        output.writeInt(si.docCount);
      }         
    }
    finally {
      output.close();
    }

    directory.renameFile("segments.new", IndexFileNames.SEGMENTS);
  }

  /**
   * version number when this SegmentInfos was generated.
   */
  public long getVersion() {
    return version;
  }

  /**
   * Current version number from segments file.
   */
  public static long readCurrentVersion(Directory directory)
    throws IOException {
      
    IndexInput input = directory.openInput(IndexFileNames.SEGMENTS);
    int format = 0;
    long version = 0;
    try {
      format = input.readInt();
      if(format < 0){
        if (format < FORMAT)
          throw new IOException("Unknown format version: " + format);
      }
    }
    finally {
      input.close();
    }
     
    if(format < 0)
      return version;


    SegmentInfos sis = new SegmentInfos();
    sis.read(directory);
    return sis.getVersion();
  }
}
