package org.apache.log4j.or.sax;

import org.apache.log4j.or.ObjectRenderer;

import org.xml.sax.Attributes;

/**
   Render <code>org.xml.sax.Attributes</code> objects.

   @author Ceki G&uuml;lc&uuml;
   @since 1.2 */
public class AttributesRenderer implements ObjectRenderer {

  public
  AttributesRenderer() {
  }


  /**
     Render a {@link org.xml.sax.Attributes}.
  */
  public
  String  doRender(Object o) {
    if(o instanceof Attributes) {
      StringBuffer sbuf = new StringBuffer();
      Attributes a = (Attributes) o;
      int len = a.getLength();
      boolean first = true;
      for(int i = 0; i < len; i++) {
	if(first) {
	  first = false;
	} else {
	  sbuf.append(", ");
	}
	sbuf.append(a.getQName(i));
	sbuf.append('=');
	sbuf.append(a.getValue(i));
      }
      return sbuf.toString();
    } else {
      return o.toString();
    }
  }
}

