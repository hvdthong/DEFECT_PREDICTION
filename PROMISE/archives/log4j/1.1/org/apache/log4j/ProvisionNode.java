package org.apache.log4j;

import java.util.Vector;

class ProvisionNode extends Vector {
    
  ProvisionNode(Category cat) {
    super();
    this.addElement(cat);
  }
}
