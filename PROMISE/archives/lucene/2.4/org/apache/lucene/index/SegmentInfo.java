import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.util.BitVector;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

final class SegmentInfo {




   


  





  public SegmentInfo(String name, int docCount, Directory dir) {
    this.name = name;
    this.docCount = docCount;
    this.dir = dir;
    delGen = NO;
    isCompoundFile = CHECK_DIR;
    preLockless = true;
    hasSingleNormFile = false;
    docStoreOffset = -1;
    docStoreSegment = name;
    docStoreIsCompoundFile = false;
    delCount = 0;
    hasProx = true;
  }

  public SegmentInfo(String name, int docCount, Directory dir, boolean isCompoundFile, boolean hasSingleNormFile) { 
    this(name, docCount, dir, isCompoundFile, hasSingleNormFile, -1, null, false, true);
  }

  public SegmentInfo(String name, int docCount, Directory dir, boolean isCompoundFile, boolean hasSingleNormFile,
                     int docStoreOffset, String docStoreSegment, boolean docStoreIsCompoundFile, boolean hasProx) { 
    this(name, docCount, dir);
    this.isCompoundFile = (byte) (isCompoundFile ? YES : NO);
    this.hasSingleNormFile = hasSingleNormFile;
    preLockless = false;
    this.docStoreOffset = docStoreOffset;
    this.docStoreSegment = docStoreSegment;
    this.docStoreIsCompoundFile = docStoreIsCompoundFile;
    this.hasProx = hasProx;
    delCount = 0;
    assert docStoreOffset == -1 || docStoreSegment != null: "dso=" + docStoreOffset + " dss=" + docStoreSegment + " docCount=" + docCount;
  }

  /**
   * Copy everything from src SegmentInfo into our instance.
   */
  void reset(SegmentInfo src) {
    clearFiles();
    name = src.name;
    docCount = src.docCount;
    dir = src.dir;
    preLockless = src.preLockless;
    delGen = src.delGen;
    docStoreOffset = src.docStoreOffset;
    docStoreIsCompoundFile = src.docStoreIsCompoundFile;
    if (src.normGen == null) {
      normGen = null;
    } else {
      normGen = new long[src.normGen.length];
      System.arraycopy(src.normGen, 0, normGen, 0, src.normGen.length);
    }
    isCompoundFile = src.isCompoundFile;
    hasSingleNormFile = src.hasSingleNormFile;
    delCount = src.delCount;
  }

  /**
   * Construct a new SegmentInfo instance by reading a
   * previously saved SegmentInfo from input.
   *
   * @param dir directory to load from
   * @param format format of the segments info file
   * @param input input handle to read segment info from
   */
  SegmentInfo(Directory dir, int format, IndexInput input) throws IOException {
    this.dir = dir;
    name = input.readString();
    docCount = input.readInt();
    if (format <= SegmentInfos.FORMAT_LOCKLESS) {
      delGen = input.readLong();
      if (format <= SegmentInfos.FORMAT_SHARED_DOC_STORE) {
        docStoreOffset = input.readInt();
        if (docStoreOffset != -1) {
          docStoreSegment = input.readString();
          docStoreIsCompoundFile = (1 == input.readByte());
        } else {
          docStoreSegment = name;
          docStoreIsCompoundFile = false;
        }
      } else {
        docStoreOffset = -1;
        docStoreSegment = name;
        docStoreIsCompoundFile = false;
      }
      if (format <= SegmentInfos.FORMAT_SINGLE_NORM_FILE) {
        hasSingleNormFile = (1 == input.readByte());
      } else {
        hasSingleNormFile = false;
      }
      int numNormGen = input.readInt();
      if (numNormGen == NO) {
        normGen = null;
      } else {
        normGen = new long[numNormGen];
        for(int j=0;j<numNormGen;j++) {
          normGen[j] = input.readLong();
        }
      }
      isCompoundFile = input.readByte();
      preLockless = (isCompoundFile == CHECK_DIR);
      if (format <= SegmentInfos.FORMAT_DEL_COUNT) {
        delCount = input.readInt();
        assert delCount <= docCount;
      } else
        delCount = -1;
      if (format <= SegmentInfos.FORMAT_HAS_PROX)
        hasProx = input.readByte() == 1;
      else
        hasProx = true;
    } else {
      delGen = CHECK_DIR;
      normGen = null;
      isCompoundFile = CHECK_DIR;
      preLockless = true;
      hasSingleNormFile = false;
      docStoreOffset = -1;
      docStoreIsCompoundFile = false;
      docStoreSegment = null;
      delCount = -1;
      hasProx = true;
    }
  }
  
  void setNumFields(int numFields) {
    if (normGen == null) {
      normGen = new long[numFields];

      if (preLockless) {
        
      } else {
        for(int i=0;i<numFields;i++) {
          normGen[i] = NO;
        }
      }
    }
  }

  /** Returns total size in bytes of all of files used by
   *  this segment. */
  long sizeInBytes() throws IOException {
    if (sizeInBytes == -1) {
      List files = files();
      final int size = files.size();
      sizeInBytes = 0;
      for(int i=0;i<size;i++) {
        final String fileName = (String) files.get(i);
        if (docStoreOffset == -1 || !IndexFileNames.isDocStoreFile(fileName))
          sizeInBytes += dir.fileLength(fileName);
      }
    }
    return sizeInBytes;
  }

  boolean hasDeletions()
    throws IOException {
    if (delGen == NO) {
      return false;
    } else if (delGen >= YES) {
      return true;
    } else {
      return dir.fileExists(getDelFileName());
    }
  }

  void advanceDelGen() {
    if (delGen == NO) {
      delGen = YES;
    } else {
      delGen++;
    }
    clearFiles();
  }

  void clearDelGen() {
    delGen = NO;
    clearFiles();
  }

  public Object clone () {
    SegmentInfo si = new SegmentInfo(name, docCount, dir);
    si.isCompoundFile = isCompoundFile;
    si.delGen = delGen;
    si.delCount = delCount;
    si.preLockless = preLockless;
    si.hasSingleNormFile = hasSingleNormFile;
    if (normGen != null) {
      si.normGen = (long[]) normGen.clone();
    }
    si.docStoreOffset = docStoreOffset;
    si.docStoreSegment = docStoreSegment;
    si.docStoreIsCompoundFile = docStoreIsCompoundFile;
    return si;
  }

  String getDelFileName() {
    if (delGen == NO) {
      return null;
    } else {
      return IndexFileNames.fileNameFromGeneration(name, "." + IndexFileNames.DELETES_EXTENSION, delGen); 
    }
  }

  /**
   * Returns true if this field for this segment has saved a separate norms file (_<segment>_N.sX).
   *
   * @param fieldNumber the field index to check
   */
  boolean hasSeparateNorms(int fieldNumber)
    throws IOException {
    if ((normGen == null && preLockless) || (normGen != null && normGen[fieldNumber] == CHECK_DIR)) {
      String fileName = name + ".s" + fieldNumber;
      return dir.fileExists(fileName);
    } else if (normGen == null || normGen[fieldNumber] == NO) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Returns true if any fields in this segment have separate norms.
   */
  boolean hasSeparateNorms()
    throws IOException {
    if (normGen == null) {
      if (!preLockless) {
        return false;
      } else {
        String[] result = dir.list();
        if (result == null)
          throw new IOException("cannot read directory " + dir + ": list() returned null");
        
        String pattern;
        pattern = name + ".s";
        int patternLength = pattern.length();
        for(int i = 0; i < result.length; i++){
          if(result[i].startsWith(pattern) && Character.isDigit(result[i].charAt(patternLength)))
            return true;
        }
        return false;
      }
    } else {
      for(int i=0;i<normGen.length;i++) {
        if (normGen[i] >= YES) {
          return true;
        }
      }
      for(int i=0;i<normGen.length;i++) {
        if (normGen[i] == CHECK_DIR) {
          if (hasSeparateNorms(i)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  /**
   * Increment the generation count for the norms file for
   * this field.
   *
   * @param fieldIndex field whose norm file will be rewritten
   */
  void advanceNormGen(int fieldIndex) {
    if (normGen[fieldIndex] == NO) {
      normGen[fieldIndex] = YES;
    } else {
      normGen[fieldIndex]++;
    }
    clearFiles();
  }

  /**
   * Get the file name for the norms file for this field.
   *
   * @param number field index
   */
  String getNormFileName(int number) throws IOException {
    String prefix;

    long gen;
    if (normGen == null) {
      gen = CHECK_DIR;
    } else {
      gen = normGen[number];
    }
    
    if (hasSeparateNorms(number)) {
      prefix = ".s";
      return IndexFileNames.fileNameFromGeneration(name, prefix + number, gen);
    }

    if (hasSingleNormFile) {
      prefix = "." + IndexFileNames.NORMS_EXTENSION;
      return IndexFileNames.fileNameFromGeneration(name, prefix, WITHOUT_GEN);
    }
      
    prefix = ".f";
    return IndexFileNames.fileNameFromGeneration(name, prefix + number, WITHOUT_GEN);
  }

  /**
   * Mark whether this segment is stored as a compound file.
   *
   * @param isCompoundFile true if this is a compound file;
   * else, false
   */
  void setUseCompoundFile(boolean isCompoundFile) {
    if (isCompoundFile) {
      this.isCompoundFile = YES;
    } else {
      this.isCompoundFile = NO;
    }
    clearFiles();
  }

  /**
   * Returns true if this segment is stored as a compound
   * file; else, false.
   */
  boolean getUseCompoundFile() throws IOException {
    if (isCompoundFile == NO) {
      return false;
    } else if (isCompoundFile == YES) {
      return true;
    } else {
      return dir.fileExists(name + "." + IndexFileNames.COMPOUND_FILE_EXTENSION);
    }
  }

  int getDelCount() throws IOException {
    if (delCount == -1) {
      if (hasDeletions()) {
        final String delFileName = getDelFileName();
        delCount = new BitVector(dir, delFileName).count();
      } else
        delCount = 0;
    }
    assert delCount <= docCount;
    return delCount;
  }

  void setDelCount(int delCount) {
    this.delCount = delCount;
    assert delCount <= docCount;
  }

  int getDocStoreOffset() {
    return docStoreOffset;
  }
  
  boolean getDocStoreIsCompoundFile() {
    return docStoreIsCompoundFile;
  }
  
  void setDocStoreIsCompoundFile(boolean v) {
    docStoreIsCompoundFile = v;
    clearFiles();
  }
  
  String getDocStoreSegment() {
    return docStoreSegment;
  }
  
  void setDocStoreOffset(int offset) {
    docStoreOffset = offset;
    clearFiles();
  }
  
  /**
   * Save this segment's info.
   */
  void write(IndexOutput output)
    throws IOException {
    output.writeString(name);
    output.writeInt(docCount);
    output.writeLong(delGen);
    output.writeInt(docStoreOffset);
    if (docStoreOffset != -1) {
      output.writeString(docStoreSegment);
      output.writeByte((byte) (docStoreIsCompoundFile ? 1:0));
    }

    output.writeByte((byte) (hasSingleNormFile ? 1:0));
    if (normGen == null) {
      output.writeInt(NO);
    } else {
      output.writeInt(normGen.length);
      for(int j = 0; j < normGen.length; j++) {
        output.writeLong(normGen[j]);
      }
    }
    output.writeByte(isCompoundFile);
    output.writeInt(delCount);
    output.writeByte((byte) (hasProx ? 1:0));
  }

  void setHasProx(boolean hasProx) {
    this.hasProx = hasProx;
    clearFiles();
  }

  boolean getHasProx() {
    return hasProx;
  }

  private void addIfExists(List files, String fileName) throws IOException {
    if (dir.fileExists(fileName))
      files.add(fileName);
  }

  /*
   * Return all files referenced by this SegmentInfo.  The
   * returns List is a locally cached List so you should not
   * modify it.
   */

  public List files() throws IOException {

    if (files != null) {
      return files;
    }
    
    files = new ArrayList();
    
    boolean useCompoundFile = getUseCompoundFile();

    if (useCompoundFile) {
      files.add(name + "." + IndexFileNames.COMPOUND_FILE_EXTENSION);
    } else {
      final String[] exts = IndexFileNames.NON_STORE_INDEX_EXTENSIONS;
      for(int i=0;i<exts.length;i++)
        addIfExists(files, name + "." + exts[i]);
    }

    if (docStoreOffset != -1) {
      assert docStoreSegment != null;
      if (docStoreIsCompoundFile) {
        files.add(docStoreSegment + "." + IndexFileNames.COMPOUND_FILE_STORE_EXTENSION);
      } else {
        final String[] exts = IndexFileNames.STORE_INDEX_EXTENSIONS;
        for(int i=0;i<exts.length;i++)
          addIfExists(files, docStoreSegment + "." + exts[i]);
      }
    } else if (!useCompoundFile) {
      final String[] exts = IndexFileNames.STORE_INDEX_EXTENSIONS;
      for(int i=0;i<exts.length;i++)
        addIfExists(files, name + "." + exts[i]);
    }

    String delFileName = IndexFileNames.fileNameFromGeneration(name, "." + IndexFileNames.DELETES_EXTENSION, delGen);
    if (delFileName != null && (delGen >= YES || dir.fileExists(delFileName))) {
      files.add(delFileName);
    }

    if (normGen != null) {
      for(int i=0;i<normGen.length;i++) {
        long gen = normGen[i];
        if (gen >= YES) {
          files.add(IndexFileNames.fileNameFromGeneration(name, "." + IndexFileNames.SEPARATE_NORMS_EXTENSION + i, gen));
        } else if (NO == gen) {
          if (!hasSingleNormFile && !useCompoundFile) {
            String fileName = name + "." + IndexFileNames.PLAIN_NORMS_EXTENSION + i;
            if (dir.fileExists(fileName)) {
              files.add(fileName);
            }
          }
        } else if (CHECK_DIR == gen) {
          String fileName = null;
          if (useCompoundFile) {
            fileName = name + "." + IndexFileNames.SEPARATE_NORMS_EXTENSION + i;
          } else if (!hasSingleNormFile) {
            fileName = name + "." + IndexFileNames.PLAIN_NORMS_EXTENSION + i;
          }
          if (fileName != null && dir.fileExists(fileName)) {
            files.add(fileName);
          }
        }
      }
    } else if (preLockless || (!hasSingleNormFile && !useCompoundFile)) {
      String prefix;
      if (useCompoundFile)
        prefix = name + "." + IndexFileNames.SEPARATE_NORMS_EXTENSION;
      else
        prefix = name + "." + IndexFileNames.PLAIN_NORMS_EXTENSION;
      int prefixLength = prefix.length();
      String[] allFiles = dir.list();
      if (allFiles == null)
        throw new IOException("cannot read directory " + dir + ": list() returned null");
      for(int i=0;i<allFiles.length;i++) {
        String fileName = allFiles[i];
        if (fileName.length() > prefixLength && Character.isDigit(fileName.charAt(prefixLength)) && fileName.startsWith(prefix)) {
          files.add(fileName);
        }
      }
    }
    return files;
  }

  /* Called whenever any change is made that affects which
   * files this segment has. */
  private void clearFiles() {
    files = null;
    sizeInBytes = -1;
  }

  /** Used for debugging */
  public String segString(Directory dir) {
    String cfs;
    try {
      if (getUseCompoundFile())
        cfs = "c";
      else
        cfs = "C";
    } catch (IOException ioe) {
      cfs = "?";
    }

    String docStore;

    if (docStoreOffset != -1)
      docStore = "->" + docStoreSegment;
    else
      docStore = "";

    return name + ":" +
      cfs +
      (this.dir == dir ? "" : "x") +
      docCount + docStore;
  }

  /** We consider another SegmentInfo instance equal if it
   *  has the same dir and same name. */
  public boolean equals(Object obj) {
    SegmentInfo other;
    try {
      other = (SegmentInfo) obj;
    } catch (ClassCastException cce) {
      return false;
    }
    return other.dir == dir && other.name.equals(name);
  }

  public int hashCode() {
    return dir.hashCode() + name.hashCode();
  }
}
