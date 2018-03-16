import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.Map;

class ReadOnlyMultiSegmentReader extends MultiSegmentReader {
  ReadOnlyMultiSegmentReader(Directory directory, SegmentInfos sis, boolean closeDirectory) throws IOException {
    super(directory, sis, closeDirectory, true);
  }

  ReadOnlyMultiSegmentReader(Directory directory, SegmentInfos infos, boolean closeDirectory, SegmentReader[] oldReaders, int[] oldStarts, Map oldNormsCache) throws IOException {
    super(directory, infos, closeDirectory, oldReaders, oldStarts, oldNormsCache, true);
  }

  protected void acquireWriteLock() {
    ReadOnlySegmentReader.noWrite();
  }
}
