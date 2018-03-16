import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.search.Similarity;


/** Writes norms.  Each thread X field accumulates the norms
 *  for the doc/fields it saw, then the flush method below
 *  merges all of these together into a single _X.nrm file.
 */

final class NormsWriter extends InvertedDocEndConsumer {

  private static final byte defaultNorm = Similarity.encodeNorm(1.0f);
  private FieldInfos fieldInfos;
  public InvertedDocEndConsumerPerThread addThread(DocInverterPerThread docInverterPerThread) {
    return new NormsWriterPerThread(docInverterPerThread, this);
  }

  public void abort() {}

  void files(Collection files) {}

  void setFieldInfos(FieldInfos fieldInfos) {
    this.fieldInfos = fieldInfos;
  }

  /** Produce _X.nrm if any document had a field with norms
   *  not disabled */
  public void flush(Map threadsAndFields, DocumentsWriter.FlushState state) throws IOException {

    final Map byField = new HashMap();

    final Iterator it = threadsAndFields.entrySet().iterator();
    while(it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();

      Collection fields = (Collection) entry.getValue();
      Iterator fieldsIt = fields.iterator();

      while(fieldsIt.hasNext()) {
        NormsWriterPerField perField = (NormsWriterPerField) fieldsIt.next();

        if (perField.upto > 0) {
          List l = (List) byField.get(perField.fieldInfo);
          if (l == null) {
            l = new ArrayList();
            byField.put(perField.fieldInfo, l);
          }
          l.add(perField);
        } else
          fieldsIt.remove();
      }
    }

    final String normsFileName = state.segmentName + "." + IndexFileNames.NORMS_EXTENSION;
    state.flushedFiles.add(normsFileName);
    IndexOutput normsOut = state.directory.createOutput(normsFileName);

    try {
      normsOut.writeBytes(SegmentMerger.NORMS_HEADER, 0, SegmentMerger.NORMS_HEADER.length);

      final int numField = fieldInfos.size();

      int normCount = 0;

      for(int fieldNumber=0;fieldNumber<numField;fieldNumber++) {

        final FieldInfo fieldInfo = fieldInfos.fieldInfo(fieldNumber);

        List toMerge = (List) byField.get(fieldInfo);
        int upto = 0;
        if (toMerge != null) {

          final int numFields = toMerge.size();

          normCount++;

          final NormsWriterPerField[] fields = new NormsWriterPerField[numFields];
          int[] uptos = new int[numFields];

          for(int j=0;j<numFields;j++)
            fields[j] = (NormsWriterPerField) toMerge.get(j);

          int numLeft = numFields;
              
          while(numLeft > 0) {

            assert uptos[0] < fields[0].docIDs.length : " uptos[0]=" + uptos[0] + " len=" + (fields[0].docIDs.length);

            int minLoc = 0;
            int minDocID = fields[0].docIDs[uptos[0]];

            for(int j=1;j<numLeft;j++) {
              final int docID = fields[j].docIDs[uptos[j]];
              if (docID < minDocID) {
                minDocID = docID;
                minLoc = j;
              }
            }

            assert minDocID < state.numDocsInRAM;

            for(;upto<minDocID;upto++)
              normsOut.writeByte(defaultNorm);

            normsOut.writeByte(fields[minLoc].norms[uptos[minLoc]]);
            (uptos[minLoc])++;
            upto++;

            if (uptos[minLoc] == fields[minLoc].upto) {
              fields[minLoc].reset();
              if (minLoc != numLeft-1) {
                fields[minLoc] = fields[numLeft-1];
                uptos[minLoc] = uptos[numLeft-1];
              }
              numLeft--;
            }
          }
          
          for(;upto<state.numDocsInRAM;upto++)
            normsOut.writeByte(defaultNorm);
        } else if (fieldInfo.isIndexed && !fieldInfo.omitNorms) {
          normCount++;
          for(;upto<state.numDocsInRAM;upto++)
            normsOut.writeByte(defaultNorm);
        }

        assert 4+normCount*state.numDocsInRAM == normsOut.getFilePointer() : ".nrm file size mismatch: expected=" + (4+normCount*state.numDocsInRAM) + " actual=" + normsOut.getFilePointer();
      }

    } finally {
      normsOut.close();
    }
  }

  void closeDocStore(DocumentsWriter.FlushState state) {}
}
