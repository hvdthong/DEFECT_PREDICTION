package org.apache.xalan.templates;

import org.w3c.dom.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.StringTokenizer;

import org.apache.xml.utils.QName;
import org.apache.xml.utils.NameSpace;
import org.apache.xpath.XPathContext;
import org.apache.xml.utils.StringToStringTable;
import org.apache.xml.utils.NameSpace;
import org.apache.xml.utils.StringVector;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xalan.transformer.ResultTreeHandler;

import java.io.*;

import java.util.*;

import javax.xml.transform.TransformerException;

/**
 * <meta name="usage" content="advanced"/>
 * Implement a Literal Result Element.
 */
public class ElemUnknown extends ElemLiteralResult
{

  /**
   * Copy an unknown element to the result tree
   *
   * @param transformer non-null reference to the the current transform-time state.
   *
   * @throws TransformerException
   */
  public void execute(
          TransformerImpl transformer)
            throws TransformerException{}
}
