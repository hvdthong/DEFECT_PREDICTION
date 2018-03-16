package org.apache.xpath.functions;

import org.apache.xml.dtm.DTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;

/**
 * <meta name="usage" content="advanced"/>
 * Execute the Lang() function.
 */
public class FuncLang extends FunctionOneArg
{

  /**
   * Execute the function.  The function must return
   * a valid object.
   * @param xctxt The current execution context.
   * @return A valid XObject.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public XObject execute(XPathContext xctxt) throws javax.xml.transform.TransformerException
  {

    String lang = m_arg0.execute(xctxt).str();
    int parent = xctxt.getCurrentNode();
    boolean isLang = false;
    DTM dtm = xctxt.getDTM(parent);

    while (DTM.NULL != parent)
    {
      if (DTM.ELEMENT_NODE == dtm.getNodeType(parent))
      {

        if (DTM.NULL != langAttr)
        {
          String langVal = dtm.getNodeValue(langAttr);
          if (langVal.toLowerCase().startsWith(lang.toLowerCase()))
          {
            int valLen = lang.length();

            if ((langVal.length() == valLen)
                    || (langVal.charAt(valLen) == '-'))
            {
              isLang = true;
            }
          }

          break;
        }
      }

      parent = dtm.getParent(parent);
    }

    return isLang ? XBoolean.S_TRUE : XBoolean.S_FALSE;
  }
}
