import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.util.UnicodeUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

final class FreqProxTermsWriter extends TermsHashConsumer {

  public TermsHashConsumerPerThread addThread(TermsHashPerThread perThread) {
    return new FreqProxTermsWriterPerThread(perThread);
  }

  void createPostings(RawPostingList[] postings, int start, int count) {
    final int end = start + count;
    for(int i=start;i<end;i++)
      postings[i] = new PostingList();
  }

  private static int compareText(final char[] text1, int pos1, final char[] text2, int pos2) {
    while(true) {
      final char c1 = text1[pos1++];
      final char c2 = text2[pos2++];
      if (c1 != c2) {
        if (0xffff == c2)
          return 1;
        else if (0xffff == c1)
          return -1;
        else
          return c1-c2;
      } else if (0xffff == c1)
        return 0;
    }
  }

  void closeDocStore(DocumentsWriter.FlushState state) {}
  void abort() {}



  public void flush(Map threadsAndFields, final DocumentsWriter.FlushState state) throws IOException {

    List allFields = new ArrayList();

    Iterator it = threadsAndFields.entrySet().iterator();
    while(it.hasNext()) {

      Map.Entry entry = (Map.Entry) it.next();

      Collection fields = (Collection) entry.getValue();

      Iterator fieldsIt = fields.iterator();

      while(fieldsIt.hasNext()) {
        FreqProxTermsWriterPerField perField = (FreqProxTermsWriterPerField) fieldsIt.next();
        if (perField.termsHashPerField.numPostings > 0)
          allFields.add(perField);
      }
    }

    Collections.sort(allFields);
    final int numAllFields = allFields.size();

    final TermInfosWriter termsOut = new TermInfosWriter(state.directory,
                                                         state.segmentName,
                                                         fieldInfos,
                                                         state.docWriter.writer.getTermIndexInterval());

    final IndexOutput freqOut = state.directory.createOutput(state.segmentFileName(IndexFileNames.FREQ_EXTENSION));
    final IndexOutput proxOut;

    if (fieldInfos.hasProx())
      proxOut = state.directory.createOutput(state.segmentFileName(IndexFileNames.PROX_EXTENSION));
    else
      proxOut = null;

    final DefaultSkipListWriter skipListWriter = new DefaultSkipListWriter(termsOut.skipInterval,
                                                                           termsOut.maxSkipLevels,
                                                                           state.numDocsInRAM, freqOut, proxOut);

    int start = 0;
    while(start < numAllFields) {
      final FieldInfo fieldInfo = ((FreqProxTermsWriterPerField) allFields.get(start)).fieldInfo;
      final String fieldName = fieldInfo.name;

      int end = start+1;
      while(end < numAllFields && ((FreqProxTermsWriterPerField) allFields.get(end)).fieldInfo.name.equals(fieldName))
        end++;
      
      FreqProxTermsWriterPerField[] fields = new FreqProxTermsWriterPerField[end-start];
      for(int i=start;i<end;i++) {
        fields[i-start] = (FreqProxTermsWriterPerField) allFields.get(i);

        fieldInfo.storePayloads |= fields[i-start].hasPayloads;
      }

      appendPostings(state, fields, termsOut, freqOut, proxOut, skipListWriter);

      for(int i=0;i<fields.length;i++) {
        TermsHashPerField perField = fields[i].termsHashPerField;
        int numPostings = perField.numPostings;
        perField.reset();
        perField.shrinkHash(numPostings);
        fields[i].reset();
      }

      start = end;
    }

    it = threadsAndFields.entrySet().iterator();
    while(it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      FreqProxTermsWriterPerThread perThread = (FreqProxTermsWriterPerThread) entry.getKey();
      perThread.termsHashPerThread.reset(true);
    }

    freqOut.close();
    if (proxOut != null) {
      state.flushedFiles.add(state.segmentFileName(IndexFileNames.PROX_EXTENSION));
      proxOut.close();
    }
    termsOut.close();
    
    state.flushedFiles.add(state.segmentFileName(IndexFileNames.FIELD_INFOS_EXTENSION));
    state.flushedFiles.add(state.segmentFileName(IndexFileNames.FREQ_EXTENSION));
    state.flushedFiles.add(state.segmentFileName(IndexFileNames.TERMS_EXTENSION));
    state.flushedFiles.add(state.segmentFileName(IndexFileNames.TERMS_INDEX_EXTENSION));
  }

  final byte[] copyByteBuffer = new byte[4096];

  /** Copy numBytes from srcIn to destIn */
  void copyBytes(IndexInput srcIn, IndexOutput destIn, long numBytes) throws IOException {
    while(numBytes > 0) {
      final int chunk;
      if (numBytes > 4096)
        chunk = 4096;
      else
        chunk = (int) numBytes;
      srcIn.readBytes(copyByteBuffer, 0, chunk);
      destIn.writeBytes(copyByteBuffer, 0, chunk);
      numBytes -= chunk;
    }
  }

  /* Walk through all unique text tokens (Posting
   * instances) found in this field and serialize them
   * into a single RAM segment. */
  void appendPostings(final DocumentsWriter.FlushState flushState,
                      FreqProxTermsWriterPerField[] fields,
                      TermInfosWriter termsOut,
                      IndexOutput freqOut,
                      IndexOutput proxOut,
                      DefaultSkipListWriter skipListWriter)
    throws CorruptIndexException, IOException {

    final int fieldNumber = fields[0].fieldInfo.number;
    int numFields = fields.length;

    final FreqProxFieldMergeState[] mergeStates = new FreqProxFieldMergeState[numFields];

    for(int i=0;i<numFields;i++) {
      FreqProxFieldMergeState fms = mergeStates[i] = new FreqProxFieldMergeState(fields[i]);

      assert fms.field.fieldInfo == fields[0].fieldInfo;

      boolean result = fms.nextTerm();
      assert result;
    }

    final int skipInterval = termsOut.skipInterval;
    final boolean currentFieldOmitTf = fields[0].fieldInfo.omitTf;

    final boolean currentFieldStorePayloads = currentFieldOmitTf ? false : fields[0].fieldInfo.storePayloads;
  
    FreqProxFieldMergeState[] termStates = new FreqProxFieldMergeState[numFields];

    while(numFields > 0) {

      termStates[0] = mergeStates[0];
      int numToMerge = 1;

      for(int i=1;i<numFields;i++) {
        final char[] text = mergeStates[i].text;
        final int textOffset = mergeStates[i].textOffset;
        final int cmp = compareText(text, textOffset, termStates[0].text, termStates[0].textOffset);

        if (cmp < 0) {
          termStates[0] = mergeStates[i];
          numToMerge = 1;
        } else if (cmp == 0)
          termStates[numToMerge++] = mergeStates[i];
      }

      int df = 0;
      int lastPayloadLength = -1;

      int lastDoc = 0;

      final char[] text = termStates[0].text;
      final int start = termStates[0].textOffset;

      final long freqPointer = freqOut.getFilePointer();
      final long proxPointer;
      if (proxOut != null)
        proxPointer = proxOut.getFilePointer();
      else
        proxPointer = 0;

      skipListWriter.resetSkip();

      while(numToMerge > 0) {
        
        if ((++df % skipInterval) == 0) {
          skipListWriter.setSkipData(lastDoc, currentFieldStorePayloads, lastPayloadLength);
          skipListWriter.bufferSkip(df);
        }

        FreqProxFieldMergeState minState = termStates[0];
        for(int i=1;i<numToMerge;i++)
          if (termStates[i].docID < minState.docID)
            minState = termStates[i];

        final int doc = minState.docID;
        final int termDocFreq = minState.termFreq;

        assert doc < flushState.numDocsInRAM;
        assert doc > lastDoc || df == 1;

        final ByteSliceReader prox = minState.prox;

        if (!currentFieldOmitTf) {
          assert proxOut != null;
          for(int j=0;j<termDocFreq;j++) {
            final int code = prox.readVInt();
            if (currentFieldStorePayloads) {
              final int payloadLength;
              if ((code & 1) != 0) {
                payloadLength = prox.readVInt();
              } else
                payloadLength = 0;
              if (payloadLength != lastPayloadLength) {
                proxOut.writeVInt(code|1);
                proxOut.writeVInt(payloadLength);
                lastPayloadLength = payloadLength;
              } else
                proxOut.writeVInt(code & (~1));
              if (payloadLength > 0)
                copyBytes(prox, proxOut, payloadLength);
            } else {
              assert 0 == (code & 1);
              proxOut.writeVInt(code>>1);
            }
          
          final int newDocCode = (doc-lastDoc)<<1;

          if (1 == termDocFreq) {
            freqOut.writeVInt(newDocCode|1);
           } else {
            freqOut.writeVInt(newDocCode);
            freqOut.writeVInt(termDocFreq);
          }
        } else {
          freqOut.writeVInt(doc-lastDoc);
        }

        lastDoc = doc;

        if (!minState.nextDoc()) {

          int upto = 0;
          for(int i=0;i<numToMerge;i++)
            if (termStates[i] != minState)
              termStates[upto++] = termStates[i];
          numToMerge--;
          assert upto == numToMerge;


          if (!minState.nextTerm()) {
            upto = 0;
            for(int i=0;i<numFields;i++)
              if (mergeStates[i] != minState)
                mergeStates[upto++] = mergeStates[i];
            numFields--;
            assert upto == numFields;
          }
        }
      }

      assert df > 0;


      long skipPointer = skipListWriter.writeSkip(freqOut);

      termInfo.set(df, freqPointer, proxPointer, (int) (skipPointer - freqPointer));

      UnicodeUtil.UTF16toUTF8(text, start, termsUTF8);

      termsOut.add(fieldNumber,
                   termsUTF8.result,
                   termsUTF8.length,
                   termInfo);
    }
  }


  final UnicodeUtil.UTF8Result termsUTF8 = new UnicodeUtil.UTF8Result();

  void files(Collection files) {}

  static final class PostingList extends RawPostingList {
  }

  int bytesPerPosting() {
    return RawPostingList.BYTES_SIZE + 4 * DocumentsWriter.INT_NUM_BYTE;
  }
}
