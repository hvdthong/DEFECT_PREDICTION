package org.apache.xalan.extensions;

import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.functions.FuncExtFunction;
import org.apache.xpath.functions.Function;

/**
 * When {@link org.apache.xalan.templates.StylesheetHandler} creates 
 * an {@link org.apache.xpath.XPath}, the ExpressionVisitor
 * visits the XPath expression. For any extension functions it 
 * encounters, it instructs StylesheetRoot to register the
 * extension namespace. 
 * 
 * This mechanism is required to locate extension functions
 * that may be embedded within an expression.
 */
public class ExpressionVisitor extends XPathVisitor
{
  private StylesheetRoot m_sroot;
  
  /**
   * The constructor sets the StylesheetRoot variable which
   * is used to register extension namespaces.
   * @param sroot the StylesheetRoot that is being constructed.
   */
  public ExpressionVisitor (StylesheetRoot sroot)
  {
    m_sroot = sroot;
  }
  
  /**
   * If the function is an extension function, register the namespace.
   * 
   * @param owner The current XPath object that owns the expression.
   * @param function The function currently being visited.
   * 
   * @return true to continue the visit in the subtree, if any.
   */
  public boolean visitFunction(ExpressionOwner owner, Function func)
	{
    if (func instanceof FuncExtFunction)
    {
      String namespace = ((FuncExtFunction)func).getNamespace();
      m_sroot.getExtensionNamespacesManager().registerExtension(namespace);      
    }
		return true;
	}
}
