public class SpacePad {


  static public void main(String[] args) {
    StringBuffer sbuf = new StringBuffer();

    for(int i = 0; i < 35; i++) {
      sbuf.setLength(0);
      sbuf.append("\"");
      spacePad(sbuf, i);
      sbuf.append("\"");
      System.out.println(sbuf.toString());
    }
    
    sbuf.setLength(0);
    sbuf.append("\"");
    spacePad(sbuf, 67);
    sbuf.append("\"");
    System.out.println(sbuf.toString());
    
  }
  static
  public
  void spacePad(StringBuffer sbuf, int length) {
    while(length >= 32) {
      sbuf.append(SPACES[5]);
      length -= 32;
    }
    
    for(int i = 4; i >= 0; i--) {	
      if((length & (1<<i)) != 0) {
	sbuf.append(SPACES[i]);
      }
    }
  }
}
