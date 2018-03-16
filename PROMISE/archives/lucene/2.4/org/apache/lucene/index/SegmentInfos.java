import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.ChecksumIndexOutput;
import org.apache.lucene.store.ChecksumIndexInput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

final class SegmentInfos extends Vector {

  /** The file format version, a negative number. */
  /* Works since counter, the old 1st entry, is always >= 0 */
  public static final int FORMAT = -1;

  /** This format adds details used for lockless commits.  It differs
   * slightly from the previous format in that file names
   * are never re-used (write once).  Instead, each file is
   * written to the next generation.  For example,
   * segments_1, segments_2, etc.  This allows us to not use
   * a commit lock.  See <a
   * formats</a> for details.
   */
  public static final int FORMAT_LOCKLESS = -2;

  /** This format adds a "hasSingleNormFile" flag into each segment info.
   * for details.
   */
  public static final int FORMAT_SINGLE_NORM_FILE = -3;

  /** This format allows multiple segments to share a single
   * vectors and stored fields file. */
  public static final int FORMAT_SHARED_DOC_STORE = -4;

  /** This format adds a checksum at the end of the file to
   *  ensure all bytes were successfully written. */
  public static final int FORMAT_CHECKSUM = -5;

  /** This format adds the deletion count for each segment.
   *  This way IndexWriter can efficiently report numDocs(). */
  public static final int FORMAT_DEL_COUNT = -6;

  /** This format adds the boolean hasProx to record if any
   *  fields in the segment store prox information (ie, have
   *  omitTf==false) */
  public static final int FORMAT_HAS_PROX = -7;

  /* This must always point to the most recent file format. */
  static final int CURRENT_FORMAT = FORMAT_HAS_PROX;
  
  /**
   * counts how often the index has been changed by adding or deleting docs.
   * starting with the current time in milliseconds forces to create unique version numbers.
   */
  private long version = System.currentTimeMillis();


  /**
   * If non-null, information about loading segments_N files
   * will be printed here.  @see #setInfoStream.
   */
  private static PrintStream infoStream;

  public final SegmentInfo info(int i) {
    return (SegmentInfo) get(i);
  }

  /**
   * Get the generation (N) of the current segments_N file
   * from a list of files.
   *
   * @param files -- array of file names to check
   */
  public static long getCurrentSegmentGeneration(String[] files) {
    if (files == null) {
      return -1;
    }
    long max = -1;
    for (int i = 0; i < files.length; i++) {
      String file = files[i];
      if (file.startsWith(IndexFileNames.SEGMENTS) && !file.equals(IndexFileNames.SEGMENTS_GEN)) {
        long gen = generationFromSegmentsFileName(file);
        if (gen > max) {
          max = gen;
        }
      }
    }
    return max;
  }

  /**
   * Get the generation (N) of the current segments_N file
   * in the directory.
   *
   * @param directory -- directory to search for the latest segments_N file
   */
  public static long getCurrentSegmentGeneration(Directory directory) throws IOException {
    String[] files = directory.list();
    if (files == null)
      throw new IOException("cannot read directory " + directory + ": list() returned null");
    return getCurrentSegmentGeneration(files);
  }

  /**
   * Get the filename of the current segments_N file
   * from a list of files.
   *
   * @param files -- array of file names to check
   */

  public static String getCurrentSegmentFileName(String[] files) throws IOException {
    return IndexFileNames.fileNameFromGeneration(IndexFileNames.SEGMENTS,
                                                 "",
                                                 getCurrentSegmentGeneration(files));
  }

  /**
   * Get the filename of the current segments_N file
   * in the directory.
   *
   * @param directory -- directory to search for the latest segments_N file
   */
  public static String getCurrentSegmentFileName(Directory directory) throws IOException {
    return IndexFileNames.fileNameFromGeneration(IndexFileNames.SEGMENTS,
                                                 "",
                                                 getCurrentSegmentGeneration(directory));
  }

  /**
   * Get the segments_N filename in use by this segment infos.
   */
  public String getCurrentSegmentFileName() {
    return IndexFileNames.fileNameFromGeneration(IndexFileNames.SEGMENTS,
                                                 "",
                                                 lastGeneration);
  }

  /**
   * Parse the generation off the segments file name and
   * return it.
   */
  public static long generationFromSegmentsFileName(String fileName) {
    if (fileName.equals(IndexFileNames.SEGMENTS)) {
      return 0;
    } else if (fileName.startsWith(IndexFileNames.SEGMENTS)) {
      return Long.parseLong(fileName.substring(1+IndexFileNames.SEGMENTS.length()),
                            Character.MAX_RADIX);
    } else {
      throw new IllegalArgumentException("fileName \"" + fileName + "\" is not a segments file");
    }
  }


  /**
   * Get the next segments_N filename that will be written.
   */
  public String getNextSegmentFileName() {
    long nextGeneration;

    if (generation == -1) {
      nextGeneration = 1;
    } else {
      nextGeneration = generation+1;
    }
    return IndexFileNames.fileNameFromGeneration(IndexFileNames.SEGMENTS,
                                                 "",
                                                 nextGeneration);
  }

  /**
   * Read a particular segmentFileName.  Note that this may
   * throw an IOException if a commit is in process.
   *
   * @param directory -- directory containing the segments file
   * @param segmentFileName -- segment file to load
   * @throws CorruptIndexException if the index is corrupt
   * @throws IOException if there is a low-level IO error
   */
  public final void read(Directory directory, String segmentFileName) throws CorruptIndexException, IOException {
    boolean success = false;

    clear();

    ChecksumIndexInput input = new ChecksumIndexInput(directory.openInput(segmentFileName));

    generation = generationFromSegmentsFileName(segmentFileName);

    lastGeneration = generation;

    try {
      int format = input.readInt();
        if (format < CURRENT_FORMAT)
          throw new CorruptIndexException("Unknown format version: " + format);
      }
        counter = format;
      }
      
        add(new SegmentInfo(directory, format, input));
      }
      
        if (input.getFilePointer() >= input.length())
        else
      }

      if (format <= FORMAT_CHECKSUM) {
        final long checksumNow = input.getChecksum();
        final long checksumThen = input.readLong();
        if (checksumNow != checksumThen)
          throw new CorruptIndexException("checksum mismatch in segments file");
      }
      success = true;
    }
    finally {
      input.close();
      if (!success) {
        clear();
      }
    }
  }

  /**
   * This version of read uses the retry logic (for lock-less
   * commits) to find the right segments file to load.
   * @throws CorruptIndexException if the index is corrupt
   * @throws IOException if there is a low-level IO error
   */
  public final void read(Directory directory) throws CorruptIndexException, IOException {

    generation = lastGeneration = -1;

    new FindSegmentsFile(directory) {

      protected Object doBody(String segmentFileName) throws CorruptIndexException, IOException {
        read(directory, segmentFileName);
        return null;
      }
    }.run();
  }

  ChecksumIndexOutput pendingOutput;

  private final void write(Directory directory) throws IOException {

    String segmentFileName = getNextSegmentFileName();

    if (generation == -1) {
      generation = 1;
    } else {
      generation++;
    }

    ChecksumIndexOutput output = new ChecksumIndexOutput(directory.createOutput(segmentFileName));

    boolean success = false;

    try {
      for (int i = 0; i < size(); i++) {
        info(i).write(output);
      }
      output.prepareCommit();
      success = true;
      pendingOutput = output;
    } finally {
      if (!success) {
        try {
          output.close();
        } catch (Throwable t) {
        }
        try {
          directory.deleteFile(segmentFileName);
        } catch (Throwable t) {
        }
      }
    }
  }

  /**
   * Returns a copy of this instance, also copying each
   * SegmentInfo.
   */
  
  public Object clone() {
    SegmentInfos sis = (SegmentInfos) super.clone();
    for(int i=0;i<sis.size();i++) {
      sis.set(i, sis.info(i).clone());
    }
    return sis;
  }

  /**
   * version number when this SegmentInfos was generated.
   */
  public long getVersion() {
    return version;
  }
  public long getGeneration() {
    return generation;
  }
  public long getLastGeneration() {
    return lastGeneration;
  }

  /**
   * Current version number from segments file.
   * @throws CorruptIndexException if the index is corrupt
   * @throws IOException if there is a low-level IO error
   */
  public static long readCurrentVersion(Directory directory)
    throws CorruptIndexException, IOException {

    return ((Long) new FindSegmentsFile(directory) {
        protected Object doBody(String segmentFileName) throws CorruptIndexException, IOException {

          IndexInput input = directory.openInput(segmentFileName);

          int format = 0;
          long version = 0;
          try {
            format = input.readInt();
            if(format < 0){
              if (format < CURRENT_FORMAT)
                throw new CorruptIndexException("Unknown format version: " + format);
            }
          }
          finally {
            input.close();
          }
     
          if(format < 0)
            return new Long(version);

          SegmentInfos sis = new SegmentInfos();
          sis.read(directory, segmentFileName);
          return new Long(sis.getVersion());
        }
      }.run()).longValue();
  }

  /** If non-null, information about retries when loading
   * the segments file will be printed to this.
   */
  public static void setInfoStream(PrintStream infoStream) {
    SegmentInfos.infoStream = infoStream;
  }

  /* Advanced configuration of retry logic in loading
     segments_N file */
  private static int defaultGenFileRetryCount = 10;
  private static int defaultGenFileRetryPauseMsec = 50;
  private static int defaultGenLookaheadCount = 10;

  /**
   * Advanced: set how many times to try loading the
   * segments.gen file contents to determine current segment
   * generation.  This file is only referenced when the
   * primary method (listing the directory) fails.
   */
  public static void setDefaultGenFileRetryCount(int count) {
    defaultGenFileRetryCount = count;
  }

  /**
   * @see #setDefaultGenFileRetryCount
   */
  public static int getDefaultGenFileRetryCount() {
    return defaultGenFileRetryCount;
  }

  /**
   * Advanced: set how many milliseconds to pause in between
   * attempts to load the segments.gen file.
   */
  public static void setDefaultGenFileRetryPauseMsec(int msec) {
    defaultGenFileRetryPauseMsec = msec;
  }

  /**
   * @see #setDefaultGenFileRetryPauseMsec
   */
  public static int getDefaultGenFileRetryPauseMsec() {
    return defaultGenFileRetryPauseMsec;
  }

  /**
   * Advanced: set how many times to try incrementing the
   * gen when loading the segments file.  This only runs if
   * the primary (listing directory) and secondary (opening
   * segments.gen file) methods fail to find the segments
   * file.
   */
  public static void setDefaultGenLookaheadCount(int count) {
    defaultGenLookaheadCount = count;
  }
  /**
   * @see #setDefaultGenLookaheadCount
   */
  public static int getDefaultGenLookahedCount() {
    return defaultGenLookaheadCount;
  }

  /**
   * @see #setInfoStream
   */
  public static PrintStream getInfoStream() {
    return infoStream;
  }

  private static void message(String message) {
    if (infoStream != null) {
      infoStream.println("SIS [" + Thread.currentThread().getName() + "]: " + message);
    }
  }

  /**
   * Utility class for executing code that needs to do
   * something with the current segments file.  This is
   * necessary with lock-less commits because from the time
   * you locate the current segments file name, until you
   * actually open it, read its contents, or check modified
   * time, etc., it could have been deleted due to a writer
   * commit finishing.
   */
  public abstract static class FindSegmentsFile {
    
    File fileDirectory;
    Directory directory;

    public FindSegmentsFile(File directory) {
      this.fileDirectory = directory;
    }

    public FindSegmentsFile(Directory directory) {
      this.directory = directory;
    }

    public Object run() throws CorruptIndexException, IOException {
      String segmentFileName = null;
      long lastGen = -1;
      long gen = 0;
      int genLookaheadCount = 0;
      IOException exc = null;
      boolean retry = false;

      int method = 0;

      

      while(true) {

        if (0 == method) {

          String[] files = null;

          long genA = -1;

          if (directory != null)
            files = directory.list();
          else
            files = fileDirectory.list();
          
          if (files != null)
            genA = getCurrentSegmentGeneration(files);

          message("directory listing genA=" + genA);

          long genB = -1;
          if (directory != null) {
            for(int i=0;i<defaultGenFileRetryCount;i++) {
              IndexInput genInput = null;
              try {
                genInput = directory.openInput(IndexFileNames.SEGMENTS_GEN);
              } catch (FileNotFoundException e) {
                message("segments.gen open: FileNotFoundException " + e);
                break;
              } catch (IOException e) {
                message("segments.gen open: IOException " + e);
              }

              if (genInput != null) {
                try {
                  int version = genInput.readInt();
                  if (version == FORMAT_LOCKLESS) {
                    long gen0 = genInput.readLong();
                    long gen1 = genInput.readLong();
                    message("fallback check: " + gen0 + "; " + gen1);
                    if (gen0 == gen1) {
                      genB = gen0;
                      break;
                    }
                  }
                } catch (IOException err2) {
                } finally {
                  genInput.close();
                }
              }
              try {
                Thread.sleep(defaultGenFileRetryPauseMsec);
              } catch (InterruptedException e) {
              }
            }
          }

          message(IndexFileNames.SEGMENTS_GEN + " check: genB=" + genB);

          if (genA > genB)
            gen = genA;
          else
            gen = genB;
          
          if (gen == -1) {
            String s;
            if (files != null) {
              s = "";
              for(int i=0;i<files.length;i++)
                s += " " + files[i];
            } else
              s = " null";
            throw new FileNotFoundException("no segments* file found in " + directory + ": files:" + s);
          }
        }

        if (1 == method || (0 == method && lastGen == gen && retry)) {

          method = 1;

          if (genLookaheadCount < defaultGenLookaheadCount) {
            gen++;
            genLookaheadCount++;
            message("look ahead increment gen to " + gen);
          }
        }

        if (lastGen == gen) {


          if (retry) {
            throw exc;
          } else {
            retry = true;
          }

        } else if (0 == method) {
          retry = false;
        }

        lastGen = gen;

        segmentFileName = IndexFileNames.fileNameFromGeneration(IndexFileNames.SEGMENTS,
                                                                "",
                                                                gen);

        try {
          Object v = doBody(segmentFileName);
          if (exc != null) {
            message("success on " + segmentFileName);
          }
          return v;
        } catch (IOException err) {

          if (exc == null) {
            exc = err;
          }

          message("primary Exception on '" + segmentFileName + "': " + err + "'; will retry: retry=" + retry + "; gen = " + gen);

          if (!retry && gen > 1) {

            String prevSegmentFileName = IndexFileNames.fileNameFromGeneration(IndexFileNames.SEGMENTS,
                                                                               "",
                                                                               gen-1);

            final boolean prevExists;
            if (directory != null)
              prevExists = directory.fileExists(prevSegmentFileName);
            else
              prevExists = new File(fileDirectory, prevSegmentFileName).exists();

            if (prevExists) {
              message("fallback to prior segment file '" + prevSegmentFileName + "'");
              try {
                Object v = doBody(prevSegmentFileName);
                if (exc != null) {
                  message("success on fallback " + prevSegmentFileName);
                }
                return v;
              } catch (IOException err2) {
                message("secondary Exception on '" + prevSegmentFileName + "': " + err2 + "'; will retry");
              }
            }
          }
        }
      }
    }

    /**
     * Subclass must implement this.  The assumption is an
     * IOException will be thrown if something goes wrong
     * during the processing that could have been caused by
     * a writer committing.
     */
    protected abstract Object doBody(String segmentFileName) throws CorruptIndexException, IOException;
  }

  /**
   * Returns a new SegmentInfos containg the SegmentInfo
   * instances in the specified range first (inclusive) to
   * last (exclusive), so total number of segments returned
   * is last-first.
   */
  public SegmentInfos range(int first, int last) {
    SegmentInfos infos = new SegmentInfos();
    infos.addAll(super.subList(first, last));
    return infos;
  }

  void updateGeneration(SegmentInfos other) {
    lastGeneration = other.lastGeneration;
    generation = other.generation;
    version = other.version;
  }

  public final void rollbackCommit(Directory dir) throws IOException {
    if (pendingOutput != null) {
      try {
        pendingOutput.close();
      } catch (Throwable t) {
      }

      try {
        final String segmentFileName = IndexFileNames.fileNameFromGeneration(IndexFileNames.SEGMENTS,
                                                                             "",
                                                                             generation);
        dir.deleteFile(segmentFileName);
      } catch (Throwable t) {
      }
      pendingOutput = null;
    }
  }

  /** Call this to start a commit.  This writes the new
   *  segments file, but writes an invalid checksum at the
   *  end, so that it is not visible to readers.  Once this
   *  is called you must call {@link #finishCommit} to complete
   *  the commit or {@link #rollbackCommit} to abort it. */
  public final void prepareCommit(Directory dir) throws IOException {
    if (pendingOutput != null)
      throw new IllegalStateException("prepareCommit was already called");
    write(dir);
  }

  public final void finishCommit(Directory dir) throws IOException {
    if (pendingOutput == null)
      throw new IllegalStateException("prepareCommit was not called");
    boolean success = false;
    try {
      pendingOutput.finishCommit();
      pendingOutput.close();
      pendingOutput = null;
      success = true;
    } finally {
      if (!success)
        rollbackCommit(dir);
    }


    final String fileName = IndexFileNames.fileNameFromGeneration(IndexFileNames.SEGMENTS,
                                                                  "",
                                                                  generation);
    success = false;
    try {
      dir.sync(fileName);
      success = true;
    } finally {
      if (!success) {
        try {
          dir.deleteFile(fileName);
        } catch (Throwable t) {
        }
      }
    }

    lastGeneration = generation;

    try {
      IndexOutput genOutput = dir.createOutput(IndexFileNames.SEGMENTS_GEN);
      try {
        genOutput.writeInt(FORMAT_LOCKLESS);
        genOutput.writeLong(generation);
        genOutput.writeLong(generation);
      } finally {
        genOutput.close();
      }
    } catch (Throwable t) {
    }
  }

  /** Writes & syncs to the Directory dir, taking care to
   *  remove the segments file on exception */
  public final void commit(Directory dir) throws IOException {
    prepareCommit(dir);
    finishCommit(dir);
  }

  synchronized String segString(Directory directory) {
    StringBuffer buffer = new StringBuffer();
    final int count = size();
    for(int i = 0; i < count; i++) {
      if (i > 0) {
        buffer.append(' ');
      }
      final SegmentInfo info = info(i);
      buffer.append(info.segString(directory));
      if (info.dir != directory)
        buffer.append("**");
    }
    return buffer.toString();
  }
}
