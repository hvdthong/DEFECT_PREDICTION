package org.apache.xml.serializer;

import javax.xml.transform.Transformer;

import org.w3c.dom.Node;
/**
 * This interface is meant to be used by a base interface to
 * TransformState, but which as only the setters which have non Xalan
 * specific types in their signature, so that there are no dependancies
 * of the serializer on Xalan.
 * 
 * @see org.apache.xalan.transformer.TransformState
 */
public interface TransformStateSetter
{


  /**
   * Set the current node.
   *
   * @param Node The current node.
   */
  void setCurrentNode(Node n);

  /**
   * Reset the state on the given transformer object.
   *
   * @param Transformer
   */
  void resetState(Transformer transformer);

}
