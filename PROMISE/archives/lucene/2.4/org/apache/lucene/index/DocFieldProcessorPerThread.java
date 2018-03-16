import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;

/**
 * Gathers all Fieldables for a document under the same
 * name, updates FieldInfos, and calls per-field consumers
 * to process field by field.
 *
 * Currently, only a single thread visits the fields,
 * sequentially, for processing.
 */

final class DocFieldProcessorPerThread extends DocConsumerPerThread {

  float docBoost;
  int fieldGen;
  final DocFieldProcessor docFieldProcessor;
  final FieldInfos fieldInfos;
  final DocFieldConsumerPerThread consumer;

  DocFieldProcessorPerField[] fields = new DocFieldProcessorPerField[1];
  int fieldCount;

  DocFieldProcessorPerField[] fieldHash = new DocFieldProcessorPerField[2];
  int hashMask = 1;
  int totalFieldCount;

  final DocumentsWriter.DocState docState;
  
  public DocFieldProcessorPerThread(DocumentsWriterThreadState threadState, DocFieldProcessor docFieldProcessor) throws IOException {
    this.docState = threadState.docState;
    this.docFieldProcessor = docFieldProcessor;
    this.fieldInfos = docFieldProcessor.fieldInfos;
    this.consumer = docFieldProcessor.consumer.addThread(this);
  }

  public void abort() {
    for(int i=0;i<fieldHash.length;i++) {
      DocFieldProcessorPerField field = fieldHash[i];
      while(field != null) {
        final DocFieldProcessorPerField next = field.next;
        field.abort();
        field = next;
      }
    }
    consumer.abort();
  }

  public Collection fields() {
    Collection fields = new HashSet();
    for(int i=0;i<fieldHash.length;i++) {
      DocFieldProcessorPerField field = fieldHash[i];
      while(field != null) {
        fields.add(field.consumer);
        field = field.next;
      }
    }
    assert fields.size() == totalFieldCount;
    return fields;
  }

  /** If there are fields we've seen but did not see again
   *  in the last run, then free them up. */

  void trimFields(DocumentsWriter.FlushState state) {

    for(int i=0;i<fieldHash.length;i++) {
      DocFieldProcessorPerField perField = fieldHash[i];
      DocFieldProcessorPerField lastPerField = null;

      while (perField != null) {

        if (perField.lastGen == -1) {


          if (lastPerField == null)
            fieldHash[i] = perField.next;
          else
            lastPerField.next = perField.next;

          if (state.docWriter.infoStream != null)
            state.docWriter.infoStream.println("  purge field=" + perField.fieldInfo.name);

          totalFieldCount--;

        } else {
          perField.lastGen = -1;
          lastPerField = perField;
        }

        perField = perField.next;
      }
    }
  }

  private void rehash() {
    final int newHashSize = (int) (fieldHash.length*2);
    assert newHashSize > fieldHash.length;

    final DocFieldProcessorPerField newHashArray[] = new DocFieldProcessorPerField[newHashSize];

    int newHashMask = newHashSize-1;
    for(int j=0;j<fieldHash.length;j++) {
      DocFieldProcessorPerField fp0 = fieldHash[j];
      while(fp0 != null) {
        final int hashPos2 = fp0.fieldInfo.name.hashCode() & newHashMask;
        DocFieldProcessorPerField nextFP0 = fp0.next;
        fp0.next = newHashArray[hashPos2];
        newHashArray[hashPos2] = fp0;
        fp0 = nextFP0;
      }
    }

    fieldHash = newHashArray;
    hashMask = newHashMask;
  }

  public DocumentsWriter.DocWriter processDocument() throws IOException {

    consumer.startDocument();
    final Document doc = docState.doc;

    assert docFieldProcessor.docWriter.writer.testPoint("DocumentsWriter.ThreadState.init start");

    fieldCount = 0;
    
    final int thisFieldGen = fieldGen++;

    final List docFields = doc.getFields();
    final int numDocFields = docFields.size();


    for(int i=0;i<numDocFields;i++) {
      Fieldable field = (Fieldable) docFields.get(i);
      final String fieldName = field.name();

      final int hashPos = fieldName.hashCode() & hashMask;
      DocFieldProcessorPerField fp = fieldHash[hashPos];
      while(fp != null && !fp.fieldInfo.name.equals(fieldName))
        fp = fp.next;

      if (fp == null) {

        FieldInfo fi = fieldInfos.add(fieldName, field.isIndexed(), field.isTermVectorStored(),
                                      field.isStorePositionWithTermVector(), field.isStoreOffsetWithTermVector(),
                                      field.getOmitNorms(), false, field.getOmitTf());

        fp = new DocFieldProcessorPerField(this, fi);
        fp.next = fieldHash[hashPos];
        fieldHash[hashPos] = fp;
        totalFieldCount++;

        if (totalFieldCount >= fieldHash.length/2)
          rehash();
      } else
        fp.fieldInfo.update(field.isIndexed(), field.isTermVectorStored(),
                            field.isStorePositionWithTermVector(), field.isStoreOffsetWithTermVector(),
                            field.getOmitNorms(), false, field.getOmitTf());

      if (thisFieldGen != fp.lastGen) {

        fp.fieldCount = 0;

        if (fieldCount == fields.length) {
          final int newSize = fields.length*2;
          DocFieldProcessorPerField newArray[] = new DocFieldProcessorPerField[newSize];
          System.arraycopy(fields, 0, newArray, 0, fieldCount);
          fields = newArray;
        }

        fields[fieldCount++] = fp;
        fp.lastGen = thisFieldGen;
      }

      if (fp.fieldCount == fp.fields.length) {
        Fieldable[] newArray = new Fieldable[fp.fields.length*2];
        System.arraycopy(fp.fields, 0, newArray, 0, fp.fieldCount);
        fp.fields = newArray;
      }

      fp.fields[fp.fieldCount++] = field;
    }

    quickSort(fields, 0, fieldCount-1);

    for(int i=0;i<fieldCount;i++)
      fields[i].consumer.processFields(fields[i].fields, fields[i].fieldCount);

    if (docState.maxTermPrefix != null && docState.infoStream != null)
      docState.infoStream.println("WARNING: document contains at least one immense term (longer than the max length " + DocumentsWriter.MAX_TERM_LENGTH + "), all of which were skipped.  Please correct the analyzer to not produce such terms.  The prefix of the first immense term is: '" + docState.maxTermPrefix + "...'"); 

    return consumer.finishDocument();
  }

  void quickSort(DocFieldProcessorPerField[] array, int lo, int hi) {
    if (lo >= hi)
      return;
    else if (hi == 1+lo) {
      if (array[lo].fieldInfo.name.compareTo(array[hi].fieldInfo.name) > 0) {
        final DocFieldProcessorPerField tmp = array[lo];
        array[lo] = array[hi];
        array[hi] = tmp;
      }
      return;
    }

    int mid = (lo + hi) >>> 1;

    if (array[lo].fieldInfo.name.compareTo(array[mid].fieldInfo.name) > 0) {
      DocFieldProcessorPerField tmp = array[lo];
      array[lo] = array[mid];
      array[mid] = tmp;
    }

    if (array[mid].fieldInfo.name.compareTo(array[hi].fieldInfo.name) > 0) {
      DocFieldProcessorPerField tmp = array[mid];
      array[mid] = array[hi];
      array[hi] = tmp;

      if (array[lo].fieldInfo.name.compareTo(array[mid].fieldInfo.name) > 0) {
        DocFieldProcessorPerField tmp2 = array[lo];
        array[lo] = array[mid];
        array[mid] = tmp2;
      }
    }

    int left = lo + 1;
    int right = hi - 1;

    if (left >= right)
      return;

    DocFieldProcessorPerField partition = array[mid];

    for (; ;) {
      while (array[right].fieldInfo.name.compareTo(partition.fieldInfo.name) > 0)
        --right;

      while (left < right && array[left].fieldInfo.name.compareTo(partition.fieldInfo.name) <= 0)
        ++left;

      if (left < right) {
        DocFieldProcessorPerField tmp = array[left];
        array[left] = array[right];
        array[right] = tmp;
        --right;
      } else {
        break;
      }
    }

    quickSort(array, lo, left);
    quickSort(array, left + 1, hi);
  }
}
