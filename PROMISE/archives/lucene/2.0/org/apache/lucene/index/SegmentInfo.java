import org.apache.lucene.store.Directory;

final class SegmentInfo {

  public SegmentInfo(String name, int docCount, Directory dir) {
    this.name = name;
    this.docCount = docCount;
    this.dir = dir;
  }
}
