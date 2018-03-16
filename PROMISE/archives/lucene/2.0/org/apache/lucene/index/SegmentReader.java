import java.io.IOException;
import java.util.*;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BitVector;
import org.apache.lucene.search.DefaultSimilarity;

/**
 * @version $Id: SegmentReader.java 405870 2006-05-12 21:04:00Z dnaber $
 */
class SegmentReader extends IndexReader {
  private String segment;

  FieldInfos fieldInfos;
  private FieldsReader fieldsReader;

  TermInfosReader tis;
  TermVectorsReader termVectorsReaderOrig = null;
  ThreadLocal termVectorsLocal = new ThreadLocal();

  BitVector deletedDocs = null;
  private boolean deletedDocsDirty = false;
  private boolean normsDirty = false;
  private boolean undeleteAll = false;

  IndexInput freqStream;
  IndexInput proxStream;

  CompoundFileReader cfsReader = null;

  private class Norm {
    public Norm(IndexInput in, int number)
    {
      this.in = in;
      this.number = number;
    }

    private IndexInput in;
    private byte[] bytes;
    private boolean dirty;
    private int number;

    private void reWrite() throws IOException {
      IndexOutput out = directory().createOutput(segment + ".tmp");
      try {
        out.writeBytes(bytes, maxDoc());
      } finally {
        out.close();
      }
      String fileName;
      if(cfsReader == null)
          fileName = segment + ".f" + number;
      else{
          fileName = segment + ".s" + number;
      }
      directory().renameFile(segment + ".tmp", fileName);
      this.dirty = false;
    }
  }

  private Hashtable norms = new Hashtable();

  /** The class which implements SegmentReader. */
  private static Class IMPL;
  static {
    try {
      String name =
        System.getProperty("org.apache.lucene.SegmentReader.class",
                           SegmentReader.class.getName());
      IMPL = Class.forName(name);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("cannot load SegmentReader class: " + e, e);
    } catch (SecurityException se) {
      try {
        IMPL = Class.forName(SegmentReader.class.getName());
      } catch (ClassNotFoundException e) {
        throw new RuntimeException("cannot load default SegmentReader class: " + e, e);
      }
    }
  }

  protected SegmentReader() { super(null); }

  public static SegmentReader get(SegmentInfo si) throws IOException {
    return get(si.dir, si, null, false, false);
  }

  public static SegmentReader get(SegmentInfos sis, SegmentInfo si,
                                  boolean closeDir) throws IOException {
    return get(si.dir, si, sis, closeDir, true);
  }

  public static SegmentReader get(Directory dir, SegmentInfo si,
                                  SegmentInfos sis,
                                  boolean closeDir, boolean ownDir)
    throws IOException {
    SegmentReader instance;
    try {
      instance = (SegmentReader)IMPL.newInstance();
    } catch (Exception e) {
      throw new RuntimeException("cannot load SegmentReader class: " + e, e);
    }
    instance.init(dir, sis, closeDir, ownDir);
    instance.initialize(si);
    return instance;
  }

   private void initialize(SegmentInfo si) throws IOException {
    segment = si.name;

    Directory cfsDir = directory();
    if (directory().fileExists(segment + ".cfs")) {
      cfsReader = new CompoundFileReader(directory(), segment + ".cfs");
      cfsDir = cfsReader;
    }

    fieldInfos = new FieldInfos(cfsDir, segment + ".fnm");
    fieldsReader = new FieldsReader(cfsDir, segment, fieldInfos);

    tis = new TermInfosReader(cfsDir, segment, fieldInfos);

    if (hasDeletions(si))
      deletedDocs = new BitVector(directory(), segment + ".del");

    freqStream = cfsDir.openInput(segment + ".frq");
    proxStream = cfsDir.openInput(segment + ".prx");
    openNorms(cfsDir);

      termVectorsReaderOrig = new TermVectorsReader(cfsDir, segment, fieldInfos);
    }
  }

   protected void finalize() {
     termVectorsLocal.set(null);
     super.finalize();
   }

  protected void doCommit() throws IOException {
      deletedDocs.write(directory(), segment + ".tmp");
      directory().renameFile(segment + ".tmp", segment + ".del");
    }
    if(undeleteAll && directory().fileExists(segment + ".del")){
      directory().deleteFile(segment + ".del");
    }
      Enumeration values = norms.elements();
      while (values.hasMoreElements()) {
        Norm norm = (Norm) values.nextElement();
        if (norm.dirty) {
          norm.reWrite();
        }
      }
    }
    deletedDocsDirty = false;
    normsDirty = false;
    undeleteAll = false;
  }

  protected void doClose() throws IOException {
    fieldsReader.close();
    tis.close();

    if (freqStream != null)
      freqStream.close();
    if (proxStream != null)
      proxStream.close();

    closeNorms();

    if (termVectorsReaderOrig != null)
      termVectorsReaderOrig.close();

    if (cfsReader != null)
      cfsReader.close();
  }

  static boolean hasDeletions(SegmentInfo si) throws IOException {
    return si.dir.fileExists(si.name + ".del");
  }

  public boolean hasDeletions() {
    return deletedDocs != null;
  }


  static boolean usesCompoundFile(SegmentInfo si) throws IOException {
    return si.dir.fileExists(si.name + ".cfs");
  }

  static boolean hasSeparateNorms(SegmentInfo si) throws IOException {
    String[] result = si.dir.list();
    String pattern = si.name + ".s";
    int patternLength = pattern.length();
    for(int i = 0; i < result.length; i++){
      if(result[i].startsWith(pattern) && Character.isDigit(result[i].charAt(patternLength)))
        return true;
    }
    return false;
  }

  protected void doDelete(int docNum) {
    if (deletedDocs == null)
      deletedDocs = new BitVector(maxDoc());
    deletedDocsDirty = true;
    undeleteAll = false;
    deletedDocs.set(docNum);
  }

  protected void doUndeleteAll() {
      deletedDocs = null;
      deletedDocsDirty = false;
      undeleteAll = true;
  }

  Vector files() throws IOException {
    Vector files = new Vector(16);

    for (int i = 0; i < IndexFileNames.INDEX_EXTENSIONS.length; i++) {
      String name = segment + "." + IndexFileNames.INDEX_EXTENSIONS[i];
      if (directory().fileExists(name))
        files.addElement(name);
    }

    for (int i = 0; i < fieldInfos.size(); i++) {
      FieldInfo fi = fieldInfos.fieldInfo(i);
      if (fi.isIndexed  && !fi.omitNorms){
        String name;
        if(cfsReader == null)
            name = segment + ".f" + i;
        else
            name = segment + ".s" + i;
        if (directory().fileExists(name))
            files.addElement(name);
      }
    }
    return files;
  }

  public TermEnum terms() {
    return tis.terms();
  }

  public TermEnum terms(Term t) throws IOException {
    return tis.terms(t);
  }

  public synchronized Document document(int n) throws IOException {
    if (isDeleted(n))
      throw new IllegalArgumentException
              ("attempt to access a deleted document");
    return fieldsReader.doc(n);
  }

  public synchronized boolean isDeleted(int n) {
    return (deletedDocs != null && deletedDocs.get(n));
  }

  public TermDocs termDocs() throws IOException {
    return new SegmentTermDocs(this);
  }

  public TermPositions termPositions() throws IOException {
    return new SegmentTermPositions(this);
  }

  public int docFreq(Term t) throws IOException {
    TermInfo ti = tis.get(t);
    if (ti != null)
      return ti.docFreq;
    else
      return 0;
  }

  public int numDocs() {
    int n = maxDoc();
    if (deletedDocs != null)
      n -= deletedDocs.count();
    return n;
  }

  public int maxDoc() {
    return fieldsReader.size();
  }

  /**
   * @see IndexReader#getFieldNames(IndexReader.FieldOption fldOption)
   */
  public Collection getFieldNames(IndexReader.FieldOption fieldOption) {

    Set fieldSet = new HashSet();
    for (int i = 0; i < fieldInfos.size(); i++) {
      FieldInfo fi = fieldInfos.fieldInfo(i);
      if (fieldOption == IndexReader.FieldOption.ALL) {
        fieldSet.add(fi.name);
      }
      else if (!fi.isIndexed && fieldOption == IndexReader.FieldOption.UNINDEXED) {
        fieldSet.add(fi.name);
      }
      else if (fi.isIndexed && fieldOption == IndexReader.FieldOption.INDEXED) {
        fieldSet.add(fi.name);
      }
      else if (fi.isIndexed && fi.storeTermVector == false && fieldOption == IndexReader.FieldOption.INDEXED_NO_TERMVECTOR) {
        fieldSet.add(fi.name);
      }
      else if (fi.storeTermVector == true &&
               fi.storePositionWithTermVector == false &&
               fi.storeOffsetWithTermVector == false &&
               fieldOption == IndexReader.FieldOption.TERMVECTOR) {
        fieldSet.add(fi.name);
      }
      else if (fi.isIndexed && fi.storeTermVector && fieldOption == IndexReader.FieldOption.INDEXED_WITH_TERMVECTOR) {
        fieldSet.add(fi.name);
      }
      else if (fi.storePositionWithTermVector && fi.storeOffsetWithTermVector == false && fieldOption == IndexReader.FieldOption.TERMVECTOR_WITH_POSITION) {
        fieldSet.add(fi.name);
      }
      else if (fi.storeOffsetWithTermVector && fi.storePositionWithTermVector == false && fieldOption == IndexReader.FieldOption.TERMVECTOR_WITH_OFFSET) {
        fieldSet.add(fi.name);
      }
      else if ((fi.storeOffsetWithTermVector && fi.storePositionWithTermVector) &&
                fieldOption == IndexReader.FieldOption.TERMVECTOR_WITH_POSITION_OFFSET) {
        fieldSet.add(fi.name);
      }
    }
    return fieldSet;
  }


  public synchronized boolean hasNorms(String field) {
    return norms.containsKey(field);
  }

  static byte[] createFakeNorms(int size) {
    byte[] ones = new byte[size];
    Arrays.fill(ones, DefaultSimilarity.encodeNorm(1.0f));
    return ones;
  }

  private byte[] ones;
  private byte[] fakeNorms() {
    if (ones==null) ones=createFakeNorms(maxDoc());
    return ones;
  }

  protected synchronized byte[] getNorms(String field) throws IOException {
    Norm norm = (Norm) norms.get(field);

      byte[] bytes = new byte[maxDoc()];
      norms(field, bytes, 0);
    }
    return norm.bytes;
  }

  public synchronized byte[] norms(String field) throws IOException {
    byte[] bytes = getNorms(field);
    if (bytes==null) bytes=fakeNorms();
    return bytes;
  }

  protected void doSetNorm(int doc, String field, byte value)
          throws IOException {
    Norm norm = (Norm) norms.get(field);
      return;
    normsDirty = true;

  }

  /** Read norms into a pre-allocated array. */
  public synchronized void norms(String field, byte[] bytes, int offset)
    throws IOException {

    Norm norm = (Norm) norms.get(field);
    if (norm == null) {
      System.arraycopy(fakeNorms(), 0, bytes, offset, maxDoc());
      return;
    }

      System.arraycopy(norm.bytes, 0, bytes, offset, maxDoc());
      return;
    }

    IndexInput normStream = (IndexInput) norm.in.clone();
      normStream.seek(0);
      normStream.readBytes(bytes, offset, maxDoc());
    } finally {
      normStream.close();
    }
  }


  private void openNorms(Directory cfsDir) throws IOException {
    for (int i = 0; i < fieldInfos.size(); i++) {
      FieldInfo fi = fieldInfos.fieldInfo(i);
      if (fi.isIndexed && !fi.omitNorms) {
        String fileName = segment + ".s" + fi.number;
        Directory d = directory();
        if(!d.fileExists(fileName)){
            fileName = segment + ".f" + fi.number;
            d = cfsDir;
        }
        norms.put(fi.name, new Norm(d.openInput(fileName), fi.number));
      }
    }
  }

  private void closeNorms() throws IOException {
    synchronized (norms) {
      Enumeration enumerator = norms.elements();
      while (enumerator.hasMoreElements()) {
        Norm norm = (Norm) enumerator.nextElement();
        norm.in.close();
      }
    }
  }
  
  /**
   * Create a clone from the initial TermVectorsReader and store it in the ThreadLocal.
   * @return TermVectorsReader
   */
  private TermVectorsReader getTermVectorsReader() {
    TermVectorsReader tvReader = (TermVectorsReader)termVectorsLocal.get();
    if (tvReader == null) {
      tvReader = (TermVectorsReader)termVectorsReaderOrig.clone();
      termVectorsLocal.set(tvReader);
    }
    return tvReader;
  }
  
  /** Return a term frequency vector for the specified document and field. The
   *  vector returned contains term numbers and frequencies for all terms in
   *  the specified field of this document, if the field had storeTermVector
   *  flag set.  If the flag was not set, the method returns null.
   * @throws IOException
   */
  public TermFreqVector getTermFreqVector(int docNumber, String field) throws IOException {
    FieldInfo fi = fieldInfos.fieldInfo(field);
    if (fi == null || !fi.storeTermVector || termVectorsReaderOrig == null) 
      return null;
    
    TermVectorsReader termVectorsReader = getTermVectorsReader();
    if (termVectorsReader == null)
      return null;
    
    return termVectorsReader.get(docNumber, field);
  }


  /** Return an array of term frequency vectors for the specified document.
   *  The array contains a vector for each vectorized field in the document.
   *  Each vector vector contains term numbers and frequencies for all terms
   *  in a given vectorized field.
   *  If no such fields existed, the method returns null.
   * @throws IOException
   */
  public TermFreqVector[] getTermFreqVectors(int docNumber) throws IOException {
    if (termVectorsReaderOrig == null)
      return null;
    
    TermVectorsReader termVectorsReader = getTermVectorsReader();
    if (termVectorsReader == null)
      return null;
    
    return termVectorsReader.get(docNumber);
  }
}
