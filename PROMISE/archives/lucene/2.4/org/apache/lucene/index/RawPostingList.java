abstract class RawPostingList {
  final static int BYTES_SIZE = DocumentsWriter.OBJECT_HEADER_BYTES + 3*DocumentsWriter.INT_NUM_BYTE;
  int textStart;
  int intStart;
  int byteStart;
}
