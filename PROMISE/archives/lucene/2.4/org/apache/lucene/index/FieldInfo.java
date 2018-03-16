final class FieldInfo {
  String name;
  boolean isIndexed;
  int number;

  boolean storeTermVector;
  boolean storeOffsetWithTermVector;
  boolean storePositionWithTermVector;

  

  FieldInfo(String na, boolean tk, int nu, boolean storeTermVector, 
            boolean storePositionWithTermVector,  boolean storeOffsetWithTermVector, 
            boolean omitNorms, boolean storePayloads, boolean omitTf) {
    name = na;
    isIndexed = tk;
    number = nu;
    this.storeTermVector = storeTermVector;
    this.storeOffsetWithTermVector = storeOffsetWithTermVector;
    this.storePositionWithTermVector = storePositionWithTermVector;
    this.omitNorms = omitNorms;
    this.storePayloads = storePayloads;
    this.omitTf = omitTf;
  }

  public Object clone() {
    return new FieldInfo(name, isIndexed, number, storeTermVector, storePositionWithTermVector,
                         storeOffsetWithTermVector, omitNorms, storePayloads, omitTf);
  }

  void update(boolean isIndexed, boolean storeTermVector, boolean storePositionWithTermVector, 
              boolean storeOffsetWithTermVector, boolean omitNorms, boolean storePayloads, boolean omitTf) {
    if (this.isIndexed != isIndexed) {
    }
    if (this.storeTermVector != storeTermVector) {
    }
    if (this.storePositionWithTermVector != storePositionWithTermVector) {
    }
    if (this.storeOffsetWithTermVector != storeOffsetWithTermVector) {
    }
    if (this.omitNorms != omitNorms) {
    }
    if (this.omitTf != omitTf) {
    }
    if (this.storePayloads != storePayloads) {
      this.storePayloads = true;
    }
  }

  void update(FieldInfo other) {
    if (isIndexed != other.isIndexed) {
    }
    if (storeTermVector != other.storeTermVector) {
    }
    if (storePositionWithTermVector != other.storePositionWithTermVector) {
    }
    if (storeOffsetWithTermVector != other.storeOffsetWithTermVector) {
    }
    if (omitNorms != other.omitNorms) {
    }
    if (this.omitTf != omitTf) {
    }
    if (storePayloads != other.storePayloads) {
      storePayloads = true;
    }
  }
}
