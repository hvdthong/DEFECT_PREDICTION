package org.apache.xalan.templates;

import org.apache.xpath.XPathVisitor;

/**
 * A derivation from this class can be passed to a class that implements 
 * the XSLTVisitable interface, to have the appropriate method called 
 * for each component of an XSLT stylesheet.  Aside from possible other uses,
 * the main intention is to provide a reasonable means to perform expression 
 * rewriting.
 */
public class XSLTVisitor extends XPathVisitor
{
	/**
	 * Visit an XSLT instruction.  Any element that isn't called by one 
	 * of the other visit methods, will be called by this method.
	 * 
	 * @param elem The xsl instruction element object.
	 * @return true if the sub expressions should be traversed.
	 */
	public boolean visitInstruction(ElemTemplateElement elem)
	{
		return true;
	}
	
	/**
	 * Visit an XSLT stylesheet instruction.
	 * 
	 * @param elem The xsl instruction element object.
	 * @return true if the sub expressions should be traversed.
	 */
	public boolean visitStylesheet(ElemTemplateElement elem)
	{
		return true;
	}

	
	/**
	 * Visit an XSLT top-level instruction.
	 * 
	 * @param elem The xsl instruction element object.
	 * @return true if the sub expressions should be traversed.
	 */
	public boolean visitTopLevelInstruction(ElemTemplateElement elem)
	{
		return true;
	}
	
	/**
	 * Visit an XSLT top-level instruction.
	 * 
	 * @param elem The xsl instruction element object.
	 * @return true if the sub expressions should be traversed.
	 */
	public boolean visitTopLevelVariableOrParamDecl(ElemTemplateElement elem)
	{
		return true;
	}

	
	/**
	 * Visit an XSLT variable or parameter declaration.
	 * 
	 * @param elem The xsl instruction element object.
	 * @return true if the sub expressions should be traversed.
	 */
	public boolean visitVariableOrParamDecl(ElemVariable elem)
	{
		return true;
	}
	
	/**
	 * Visit a LiteralResultElement.
	 * 
	 * @param elem The literal result object.
	 * @return true if the sub expressions should be traversed.
	 */
	public boolean visitLiteralResultElement(ElemLiteralResult elem)
	{
		return true;
	}
	
	/**
	 * Visit an Attribute Value Template (at the top level).
	 * 
	 * @param elem The attribute value template object.
	 * @return true if the sub expressions should be traversed.
	 */
	public boolean visitAVT(AVT elem)
	{
		return true;
	}


	/**
	 * Visit an extension element.
	 * @param elem The extension object.
	 * @return true if the sub expressions should be traversed.
	 */
	public boolean visitExtensionElement(ElemExtensionCall elem)
	{
		return true;
	}

}

