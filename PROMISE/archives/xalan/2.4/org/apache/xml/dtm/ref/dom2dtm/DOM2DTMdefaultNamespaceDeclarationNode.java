package org.apache.xml.dtm.ref.dom2dtm;

import org.w3c.dom.*;
import org.apache.xml.dtm.ref.dom2dtm.DOM2DTM;
import org.apache.xml.dtm.ref.*;
import org.apache.xml.dtm.*;

/** This is a kluge to let us shove a declaration for xml: into the
 * DOM2DTM model.  Basically, it creates a proxy node in DOM space to
 * carry the additional information. This is _NOT_ a full DOM
 * implementation, and shouldn't be one since it sits alongside the
 * DOM rather than becoming part of the DOM model.
 * 
 * (This used to be an internal class within DOM2DTM. Moved out because
 * I need to perform an instanceof operation on it to support a temporary
 * workaround in DTMManagerDefault.)
 * 
 * %REVIEW% What if the DOM2DTM was built around a DocumentFragment and
 * there isn't a single root element? I think this fails that case...
 * 
 * %REVIEW% An alternative solution would be to create the node _only_
 * in DTM space, but given how DOM2DTM is currently written I think
 * this is simplest.
 * */
public class DOM2DTMdefaultNamespaceDeclarationNode implements Attr
{
  final String NOT_SUPPORTED_ERR="Unsupported operation on pseudonode";
  
  Element pseudoparent;
  String prefix,uri,nodename;
  int handle;
  DOM2DTMdefaultNamespaceDeclarationNode(Element pseudoparent,String prefix,String uri,int handle)
  {
    this.pseudoparent=pseudoparent;
    this.prefix=prefix;
    this.uri=uri;
    this.handle=handle;
    this.nodename="xmlns:"+prefix;
  }
  public String getNodeName() {return nodename;}
  public String getName() {return nodename;}
  public String getPrefix() {return prefix;}
  public String getLocalName() {return prefix;}
  public String getNodeValue() {return uri;}
  public String getValue() {return uri;}
  public Element getOwnerElement() {return pseudoparent;}
  
  public boolean isSupported(String feature, String version) {return false;}
  public boolean hasChildNodes() {return false;}
  public boolean hasAttributes() {return false;}
  public Node getParentNode() {return null;}
  public Node getFirstChild() {return null;}
  public Node getLastChild() {return null;}
  public Node getPreviousSibling() {return null;}
  public Node getNextSibling() {return null;}
  public boolean getSpecified() {return false;}
  public void normalize() {return;}
  public NodeList getChildNodes() {return null;}
  public NamedNodeMap getAttributes() {return null;}
  public short getNodeType() {return Node.ATTRIBUTE_NODE;}
  public void setNodeValue(String value) {throw new DTMException(NOT_SUPPORTED_ERR);}
  public void setValue(String value) {throw new DTMException(NOT_SUPPORTED_ERR);}
  public void setPrefix(String value) {throw new DTMException(NOT_SUPPORTED_ERR);}
  public Node insertBefore(Node a, Node b) {throw new DTMException(NOT_SUPPORTED_ERR);}
  public Node replaceChild(Node a, Node b) {throw new DTMException(NOT_SUPPORTED_ERR);}
  public Node appendChild(Node a) {throw new DTMException(NOT_SUPPORTED_ERR);}
  public Node removeChild(Node a) {throw new DTMException(NOT_SUPPORTED_ERR);}
  public Document getOwnerDocument() {return pseudoparent.getOwnerDocument();}
  public Node cloneNode(boolean deep) {throw new DTMException(NOT_SUPPORTED_ERR);}
	
	/** Non-DOM method, part of the temporary kluge
	 * %REVIEW% This would be a pruning problem, but since it will always be
	 * added to the root element and we prune on elements, we shouldn't have 
	 * to worry.
	 */
	public int getHandleOfNode()		
	{
		return handle;
	}
}

