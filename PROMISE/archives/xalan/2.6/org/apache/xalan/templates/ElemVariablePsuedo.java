package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;

import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPath;

public class ElemVariablePsuedo extends ElemVariable
{
  XUnresolvedVariableSimple m_lazyVar;
	
  /**
   * Set the "select" attribute.
   * If the variable-binding element has a select attribute,
   * then the value of the attribute must be an expression and
   * the value of the variable is the object that results from
   * evaluating the expression. In this case, the content
   * of the variable must be empty.
   *
   * @param v Value to set for the "select" attribute.
   */
  public void setSelect(XPath v)
  {
    super.setSelect(v);
    m_lazyVar = new XUnresolvedVariableSimple(this);
  }
  
  /**
   * Execute a variable declaration and push it onto the variable stack.
   *
   * @param transformer non-null reference to the the current transform-time state.
   *
   * @throws TransformerException
   */
  public void execute(TransformerImpl transformer) throws TransformerException
  {


    transformer.getXPathContext().getVarStack().setLocalVariable(m_index, m_lazyVar);
  }

}

