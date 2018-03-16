import java.io.IOException;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.analysis.Token;

final class FreqProxTermsWriterPerField extends TermsHashConsumerPerField implements Comparable {

  final FreqProxTermsWriterPerThread perThread;
  final TermsHashPerField termsHashPerField;
  final FieldInfo fieldInfo;
  final DocumentsWriter.DocState docState;
  final DocInverter.FieldInvertState fieldState;
  boolean omitTf;

  public FreqProxTermsWriterPerField(TermsHashPerField termsHashPerField, FreqProxTermsWriterPerThread perThread, FieldInfo fieldInfo) {
    this.termsHashPerField = termsHashPerField;
    this.perThread = perThread;
    this.fieldInfo = fieldInfo;
    docState = termsHashPerField.docState;
    fieldState = termsHashPerField.fieldState;
    omitTf = fieldInfo.omitTf;
  }

  int getStreamCount() {
    if (fieldInfo.omitTf)
      return 1;
    else
      return 2;
  }

  void finish() {}

  boolean hasPayloads;

  void skippingLongTerm(Token t) throws IOException {}

  public int compareTo(Object other0) {
    FreqProxTermsWriterPerField other = (FreqProxTermsWriterPerField) other0;
    return fieldInfo.name.compareTo(other.fieldInfo.name);
  }

  void reset() {
    omitTf = fieldInfo.omitTf;
  }

  boolean start(Fieldable[] fields, int count) {
    for(int i=0;i<count;i++)
      if (fields[i].isIndexed())
        return true;
    return false;
  }     

  final void writeProx(Token t, FreqProxTermsWriter.PostingList p, int proxCode) {
    final Payload payload = t.getPayload();    
    if (payload != null && payload.length > 0) {
      termsHashPerField.writeVInt(1, (proxCode<<1)|1);
      termsHashPerField.writeVInt(1, payload.length);
      termsHashPerField.writeBytes(1, payload.data, payload.offset, payload.length);
      hasPayloads = true;      
    } else
      termsHashPerField.writeVInt(1, proxCode<<1);
    p.lastPosition = fieldState.position;
  }

  final void newTerm(Token t, RawPostingList p0) {
    assert docState.testPoint("FreqProxTermsWriterPerField.newTerm start");
    FreqProxTermsWriter.PostingList p = (FreqProxTermsWriter.PostingList) p0;
    p.lastDocID = docState.docID;
    if (omitTf) {
      p.lastDocCode = docState.docID;
    } else {
      p.lastDocCode = docState.docID << 1;
      p.docFreq = 1;
      writeProx(t, p, fieldState.position);
    }
  }

  final void addTerm(Token t, RawPostingList p0) {

    assert docState.testPoint("FreqProxTermsWriterPerField.addTerm start");

    FreqProxTermsWriter.PostingList p = (FreqProxTermsWriter.PostingList) p0;

    assert omitTf || p.docFreq > 0;

    if (omitTf) {
      if (docState.docID != p.lastDocID) {
        assert docState.docID > p.lastDocID;
        termsHashPerField.writeVInt(0, p.lastDocCode);
        p.lastDocCode = docState.docID - p.lastDocID;
        p.lastDocID = docState.docID;
      }
    } else {
      if (docState.docID != p.lastDocID) {
        assert docState.docID > p.lastDocID;

        if (1 == p.docFreq)
          termsHashPerField.writeVInt(0, p.lastDocCode|1);
        else {
          termsHashPerField.writeVInt(0, p.lastDocCode);
          termsHashPerField.writeVInt(0, p.docFreq);
        }
        p.docFreq = 1;
        p.lastDocCode = (docState.docID - p.lastDocID) << 1;
        p.lastDocID = docState.docID;
        writeProx(t, p, fieldState.position);
      } else {
        p.docFreq++;
        writeProx(t, p, fieldState.position-p.lastPosition);
      }
    }
  }

  public void abort() {}
}

