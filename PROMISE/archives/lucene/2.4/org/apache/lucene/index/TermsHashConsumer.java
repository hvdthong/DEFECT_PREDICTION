import java.io.IOException;
import java.util.Map;

abstract class TermsHashConsumer {
  abstract int bytesPerPosting();
  abstract void createPostings(RawPostingList[] postings, int start, int count);
  abstract TermsHashConsumerPerThread addThread(TermsHashPerThread perThread);
  abstract void flush(Map threadsAndFields, final DocumentsWriter.FlushState state) throws IOException;
  abstract void abort();
  abstract void closeDocStore(DocumentsWriter.FlushState state) throws IOException;

  FieldInfos fieldInfos;

  void setFieldInfos(FieldInfos fieldInfos) {
    this.fieldInfos = fieldInfos;
  }
}
