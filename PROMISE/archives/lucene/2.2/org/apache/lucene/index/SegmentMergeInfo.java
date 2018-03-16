import java.io.IOException;

final class SegmentMergeInfo {
  Term term;
  int base;
  TermEnum termEnum;
  IndexReader reader;

  SegmentMergeInfo(int b, TermEnum te, IndexReader r)
    throws IOException {
    base = b;
    reader = r;
    termEnum = te;
    term = te.term();
  }

  int[] getDocMap() {
    if (docMap == null) {
    if (reader.hasDeletions()) {
      int maxDoc = reader.maxDoc();
      docMap = new int[maxDoc];
      int j = 0;
      for (int i = 0; i < maxDoc; i++) {
        if (reader.isDeleted(i))
          docMap[i] = -1;
        else
          docMap[i] = j++;
      }
    }
  }
    return docMap;
  }

  TermPositions getPositions() throws IOException {
    if (postings == null) {
      postings = reader.termPositions();
    }
    return postings;
  }

  final boolean next() throws IOException {
    if (termEnum.next()) {
      term = termEnum.term();
      return true;
    } else {
      term = null;
      return false;
    }
  }

  final void close() throws IOException {
    termEnum.close();
    if (postings != null) {
    postings.close();
  }
}
}

