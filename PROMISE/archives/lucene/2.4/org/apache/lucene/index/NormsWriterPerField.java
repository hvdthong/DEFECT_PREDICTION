import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.search.Similarity;

/** Taps into DocInverter, as an InvertedDocEndConsumer,
 *  which is called at the end of inverting each field.  We
 *  just look at the length for the field (docState.length)
 *  and record the norm. */

final class NormsWriterPerField extends InvertedDocEndConsumerPerField implements Comparable {

  final NormsWriterPerThread perThread;
  final FieldInfo fieldInfo;
  final DocumentsWriter.DocState docState;

  int[] docIDs = new int[1];
  byte[] norms = new byte[1];
  int upto;

  final DocInverter.FieldInvertState fieldState;

  public void reset() {
    docIDs = ArrayUtil.shrink(docIDs, upto);
    norms = ArrayUtil.shrink(norms, upto);
    upto = 0;
  }

  public NormsWriterPerField(final DocInverterPerField docInverterPerField, final NormsWriterPerThread perThread, final FieldInfo fieldInfo) {
    this.perThread = perThread;
    this.fieldInfo = fieldInfo;
    docState = perThread.docState;
    fieldState = docInverterPerField.fieldState;
  }

  void abort() {
    upto = 0;
  }

  public int compareTo(Object other) {
    return fieldInfo.name.compareTo(((NormsWriterPerField) other).fieldInfo.name);
  }
  
  void finish() {
    assert docIDs.length == norms.length;
    if (fieldInfo.isIndexed && !fieldInfo.omitNorms) {
      if (docIDs.length <= upto) {
        assert docIDs.length == upto;
        docIDs = ArrayUtil.grow(docIDs, 1+upto);
        norms = ArrayUtil.grow(norms, 1+upto);
      }
      final float norm = fieldState.boost * docState.similarity.lengthNorm(fieldInfo.name, fieldState.length);
      norms[upto] = Similarity.encodeNorm(norm);
      docIDs[upto] = docState.docID;
      upto++;
    }
  }
}
