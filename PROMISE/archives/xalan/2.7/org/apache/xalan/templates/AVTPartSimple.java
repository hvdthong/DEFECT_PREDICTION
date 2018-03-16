package org.apache.xalan.templates;

import org.apache.xml.utils.FastStringBuffer;
import org.apache.xpath.XPathContext;

/**
 * Simple string part of a complex AVT.
 * @xsl.usage internal
 */
public class AVTPartSimple extends AVTPart
{
    static final long serialVersionUID = -3744957690598727913L;

  /**
   * Simple string value;
   * @serial
   */
  private String m_val;

  /**
   * Construct a simple AVT part.
   * @param val A pure string section of an AVT.
   */
  public AVTPartSimple(String val)
  {
    m_val = val;
  }

  /**
   * Get the AVT part as the original string.
   *
   * @return the AVT part as the original string.
   */
  public String getSimpleString()
  {
    return m_val;
  }
  
  /**
   * This function is used to fixup variables from QNames to stack frame 
   * indexes at stylesheet build time.
   * @param vars List of QNames that correspond to variables.  This list 
   * should be searched backwards for the first qualified name that 
   * corresponds to the variable reference qname.  The position of the 
   * QName in the vector from the start of the vector will be its position 
   * in the stack frame (but variables above the globalsTop value will need 
   * to be offset to the current stack frame).
   */
  public void fixupVariables(java.util.Vector vars, int globalsSize)
  {
  }


  /**
   * Write the value into the buffer.
   *
   * @param xctxt An XPathContext object, providing infomation specific
   * to this invocation and this thread. Maintains SAX state, variables, 
   * error handler and  so on, so the transformation/XPath object itself
   * can be simultaneously invoked from multiple threads.
   * @param buf Buffer to write into.
   * @param context The current source tree context.
   * @param nsNode The current namespace context (stylesheet tree context).
   */
  public void evaluate(XPathContext xctxt, FastStringBuffer buf,
                       int context,
                       org.apache.xml.utils.PrefixResolver nsNode)
  {
    buf.append(m_val);
  }
  /**
   * @see XSLTVisitable#callVisitors(XSLTVisitor)
   */
  public void callVisitors(XSLTVisitor visitor)
  {
  }

}
