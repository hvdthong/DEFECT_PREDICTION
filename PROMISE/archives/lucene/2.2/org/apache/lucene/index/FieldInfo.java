final class FieldInfo {
  String name;
  boolean isIndexed;
  int number;

  boolean storeTermVector;
  boolean storeOffsetWithTermVector;
  boolean storePositionWithTermVector;

  

  FieldInfo(String na, boolean tk, int nu, boolean storeTermVector, 
            boolean storePositionWithTermVector,  boolean storeOffsetWithTermVector, 
            boolean omitNorms, boolean storePayloads) {
    name = na;
    isIndexed = tk;
    number = nu;
    this.storeTermVector = storeTermVector;
    this.storeOffsetWithTermVector = storeOffsetWithTermVector;
    this.storePositionWithTermVector = storePositionWithTermVector;
    this.omitNorms = omitNorms;
    this.storePayloads = storePayloads;
  }
}
