import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexOutput;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

final class DocumentWriter {
  private Analyzer analyzer;
  private Directory directory;
  private Similarity similarity;
  private FieldInfos fieldInfos;
  private int maxFieldLength;
  private int termIndexInterval = IndexWriter.DEFAULT_TERM_INDEX_INTERVAL;
  private PrintStream infoStream;

  /** This ctor used by test code only.
   *
   * @param directory The directory to write the document information to
   * @param analyzer The analyzer to use for the document
   * @param similarity The Similarity function
   * @param maxFieldLength The maximum number of tokens a field may have
   */ 
  DocumentWriter(Directory directory, Analyzer analyzer,
                 Similarity similarity, int maxFieldLength) {
    this.directory = directory;
    this.analyzer = analyzer;
    this.similarity = similarity;
    this.maxFieldLength = maxFieldLength;
  }

  DocumentWriter(Directory directory, Analyzer analyzer, IndexWriter writer) {
    this.directory = directory;
    this.analyzer = analyzer;
    this.similarity = writer.getSimilarity();
    this.maxFieldLength = writer.getMaxFieldLength();
    this.termIndexInterval = writer.getTermIndexInterval();
  }

  final void addDocument(String segment, Document doc)
          throws CorruptIndexException, IOException {
    fieldInfos = new FieldInfos();
    fieldInfos.add(doc);
    
    fieldStoresPayloads = new BitSet(fieldInfos.size());
    
    Arrays.fill(fieldBoosts, doc.getBoost());

    try {
    
      invertDocument(doc);
    
      Posting[] postings = sortPostingTable();
    
      fieldInfos.write(directory, segment + ".fnm");

      FieldsWriter fieldsWriter =
        new FieldsWriter(directory, segment, fieldInfos);
      try {
        fieldsWriter.addDocument(doc);
      } finally {
        fieldsWriter.close();
      }
    
      /*
      for (int i = 0; i < postings.length; i++) {
        Posting posting = postings[i];
        System.out.print(posting.term);
        System.out.print(" freq=" + posting.freq);
        System.out.print(" pos=");
        System.out.print(posting.positions[0]);
        for (int j = 1; j < posting.freq; j++)
	  System.out.print("," + posting.positions[j]);
        System.out.println("");
      }
       */

      writePostings(postings, segment);

      writeNorms(segment);
    } finally {
      IOException ex = null;
      
      Iterator it = openTokenStreams.iterator();
      while (it.hasNext()) {
        try {
          ((TokenStream) it.next()).close();
        } catch (IOException e) {
          if (ex != null) {
            ex = e;
          }
        }
      }
      openTokenStreams.clear();
      
      if (ex != null) {
        throw ex;
      }
    }
  }

  private final Hashtable postingTable = new Hashtable();
  private int[] fieldLengths;
  private int[] fieldPositions;
  private int[] fieldOffsets;
  private float[] fieldBoosts;
  
  private BitSet fieldStoresPayloads;
  
  private List openTokenStreams = new LinkedList();

  private final void invertDocument(Document doc)
          throws IOException {
    Iterator fieldIterator = doc.getFields().iterator();
    while (fieldIterator.hasNext()) {
      Fieldable field = (Fieldable) fieldIterator.next();
      String fieldName = field.name();
      int fieldNumber = fieldInfos.fieldNumber(fieldName);

      if (length>0) position+=analyzer.getPositionIncrementGap(fieldName);

      if (field.isIndexed()) {
          String stringValue = field.stringValue();
          if(field.isStoreOffsetWithTermVector())
            addPosition(fieldName, stringValue, position++, null, new TermVectorOffsetInfo(offset, offset + stringValue.length()));
          else
            addPosition(fieldName, stringValue, position++, null, null);
          offset += stringValue.length();
          length++;
        } else 
          TokenStream stream = field.tokenStreamValue();
          
          if (stream == null) {
            if (field.readerValue() != null)
              reader = field.readerValue();
            else if (field.stringValue() != null)
              reader = new StringReader(field.stringValue());
            else
              throw new IllegalArgumentException
                      ("field must have either String or Reader value");
  
            stream = analyzer.tokenStream(fieldName, reader);
          }
          
          openTokenStreams.add(stream);
          
          stream.reset();
          

          Token lastToken = null;
          for (Token t = stream.next(); t != null; t = stream.next()) {
            position += (t.getPositionIncrement() - 1);
              
            Payload payload = t.getPayload();
            if (payload != null) {
              fieldStoresPayloads.set(fieldNumber);
            }
              
            TermVectorOffsetInfo termVectorOffsetInfo;
            if (field.isStoreOffsetWithTermVector()) {
              termVectorOffsetInfo = new TermVectorOffsetInfo(offset + t.startOffset(), offset + t.endOffset());
            } else {
              termVectorOffsetInfo = null;
            }
            addPosition(fieldName, t.termText(), position++, payload, termVectorOffsetInfo);
              
            lastToken = t;
            if (++length >= maxFieldLength) {
              if (infoStream != null)
                infoStream.println("maxFieldLength " +maxFieldLength+ " reached, ignoring following tokens");
              break;
            }
          }
            
          if(lastToken != null)
            offset += lastToken.endOffset() + 1;
        }

        fieldBoosts[fieldNumber] *= field.getBoost();
        fieldOffsets[fieldNumber] = offset;
      }
    }
    
    for (int i = fieldStoresPayloads.nextSetBit(0); i >= 0; i = fieldStoresPayloads.nextSetBit(i+1)) { 
    	fieldInfos.fieldInfo(i).storePayloads = true;
    }
  }


  private final void addPosition(String field, String text, int position, Payload payload, TermVectorOffsetInfo offset) {
    termBuffer.set(field, text);
    Posting ti = (Posting) postingTable.get(termBuffer);
      int freq = ti.freq;
        int[] positions = ti.positions;
        System.arraycopy(positions, 0, newPositions, 0, freq);
        ti.positions = newPositions;
        
        if (ti.payloads != null) {
          Payload[] payloads = ti.payloads;
          System.arraycopy(payloads, 0, newPayloads, 0, payloads.length);
          ti.payloads = newPayloads;
        }
      }

      if (payload != null) {
        if (ti.payloads == null) {
          ti.payloads = new Payload[ti.positions.length];
        }
        ti.payloads[freq] = payload;
      }
      
      if (offset != null) {
        if (ti.offsets.length == freq){
          TermVectorOffsetInfo [] newOffsets = new TermVectorOffsetInfo[freq*2];
          TermVectorOffsetInfo [] offsets = ti.offsets;
          System.arraycopy(offsets, 0, newOffsets, 0, freq);
          ti.offsets = newOffsets;
        }
        ti.offsets[freq] = offset;
      }
      Term term = new Term(field, text, false);
      postingTable.put(term, new Posting(term, position, payload, offset));
    }
  }

  private final Posting[] sortPostingTable() {
    Posting[] array = new Posting[postingTable.size()];
    Enumeration postings = postingTable.elements();
    for (int i = 0; postings.hasMoreElements(); i++)
      array[i] = (Posting) postings.nextElement();

    quickSort(array, 0, array.length - 1);

    return array;
  }

  private static final void quickSort(Posting[] postings, int lo, int hi) {
    if (lo >= hi)
      return;

    int mid = (lo + hi) / 2;

    if (postings[lo].term.compareTo(postings[mid].term) > 0) {
      Posting tmp = postings[lo];
      postings[lo] = postings[mid];
      postings[mid] = tmp;
    }

    if (postings[mid].term.compareTo(postings[hi].term) > 0) {
      Posting tmp = postings[mid];
      postings[mid] = postings[hi];
      postings[hi] = tmp;

      if (postings[lo].term.compareTo(postings[mid].term) > 0) {
        Posting tmp2 = postings[lo];
        postings[lo] = postings[mid];
        postings[mid] = tmp2;
      }
    }

    int left = lo + 1;
    int right = hi - 1;

    if (left >= right)
      return;

    Term partition = postings[mid].term;

    for (; ;) {
      while (postings[right].term.compareTo(partition) > 0)
        --right;

      while (left < right && postings[left].term.compareTo(partition) <= 0)
        ++left;

      if (left < right) {
        Posting tmp = postings[left];
        postings[left] = postings[right];
        postings[right] = tmp;
        --right;
      } else {
        break;
      }
    }

    quickSort(postings, lo, left);
    quickSort(postings, left + 1, hi);
  }

  private final void writePostings(Posting[] postings, String segment)
          throws CorruptIndexException, IOException {
    IndexOutput freq = null, prox = null;
    TermInfosWriter tis = null;
    TermVectorsWriter termVectorWriter = null;
    try {
      freq = directory.createOutput(segment + ".frq");
      prox = directory.createOutput(segment + ".prx");
      tis = new TermInfosWriter(directory, segment, fieldInfos,
                                termIndexInterval);
      TermInfo ti = new TermInfo();
      String currentField = null;
      boolean currentFieldHasPayloads = false;
      
      for (int i = 0; i < postings.length; i++) {
        Posting posting = postings[i];

        String termField = posting.term.field();
        if (currentField != termField) {
          currentField = termField;
          FieldInfo fi = fieldInfos.fieldInfo(currentField);
          currentFieldHasPayloads = fi.storePayloads;
          if (fi.storeTermVector) {
            if (termVectorWriter == null) {
              termVectorWriter =
                new TermVectorsWriter(directory, segment, fieldInfos);
              termVectorWriter.openDocument();
            }
            termVectorWriter.openField(currentField);

          } else if (termVectorWriter != null) {
            termVectorWriter.closeField();
          }
        }
        
        ti.set(1, freq.getFilePointer(), prox.getFilePointer(), -1);
        tis.add(posting.term, ti);

        int postingFreq = posting.freq;
        else {
        }

        int[] positions = posting.positions;
        Payload[] payloads = posting.payloads;
        int lastPayloadLength = -1;
        
        
          int position = positions[j];
          int delta = position - lastPosition;
          if (currentFieldHasPayloads) {
            int payloadLength = 0;
            Payload payload = null;
            if (payloads != null) {
              payload = payloads[j];
              if (payload != null) {
                payloadLength = payload.length;
              }
            }
            if (payloadLength == lastPayloadLength) {
              prox.writeVInt(delta * 2);
            } else {
              prox.writeVInt(delta * 2 + 1);
              prox.writeVInt(payloadLength);
              lastPayloadLength = payloadLength;
            }
            if (payloadLength > 0) {
              prox.writeBytes(payload.data, payload.offset, payload.length);
            }
          } else {
            prox.writeVInt(delta);
          }
          lastPosition = position;
        }
        if (termVectorWriter != null && termVectorWriter.isFieldOpen()) {
            termVectorWriter.addTerm(posting.term.text(), postingFreq, posting.positions, posting.offsets);
        }
      }
      if (termVectorWriter != null)
        termVectorWriter.closeDocument();
    } finally {
      IOException keep = null;
      if (freq != null) try { freq.close(); } catch (IOException e) { if (keep == null) keep = e; }
      if (prox != null) try { prox.close(); } catch (IOException e) { if (keep == null) keep = e; }
      if (tis  != null) try {  tis.close(); } catch (IOException e) { if (keep == null) keep = e; }
      if (termVectorWriter  != null) try {  termVectorWriter.close(); } catch (IOException e) { if (keep == null) keep = e; }
      if (keep != null) throw (IOException) keep.fillInStackTrace();
    }
  }

  private final void writeNorms(String segment) throws IOException { 
    for(int n = 0; n < fieldInfos.size(); n++){
      FieldInfo fi = fieldInfos.fieldInfo(n);
      if(fi.isIndexed && !fi.omitNorms){
        float norm = fieldBoosts[n] * similarity.lengthNorm(fi.name, fieldLengths[n]);
        IndexOutput norms = directory.createOutput(segment + ".f" + n);
        try {
          norms.writeByte(Similarity.encodeNorm(norm));
        } finally {
          norms.close();
        }
      }
    }
  }
  
  /** If non-null, a message will be printed to this if maxFieldLength is reached.
   */
  void setInfoStream(PrintStream infoStream) {
    this.infoStream = infoStream;
  }

  int getNumFields() {
    return fieldInfos.size();
  }
}

  TermVectorOffsetInfo [] offsets;
  

  Posting(Term t, int position, Payload payload, TermVectorOffsetInfo offset) {
    term = t;
    freq = 1;
    positions = new int[1];
    positions[0] = position;
    
    if (payload != null) {
      payloads = new Payload[1];
      payloads[0] = payload;
    } else 
      payloads = null;    
    

    if(offset != null){
      offsets = new TermVectorOffsetInfo[1];
      offsets[0] = offset;
    } else
      offsets = null;
  }
}
