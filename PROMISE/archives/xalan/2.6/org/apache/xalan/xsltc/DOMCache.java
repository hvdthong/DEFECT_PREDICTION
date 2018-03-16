package org.apache.xalan.xsltc;


/**
 * @author Morten Jorgensen
 */
public interface DOMCache {

    /**
     * This method is responsible for:
     *
     * (1) building the DOMImpl tree
     *
     *      Parser  _parser = new Parser();
     *      DOMImpl _dom = new DOMImpl();
     *      _parser.setDocumentHandler(_dom.getBuilder());
     *      _parser.setDTDHandler(_dom.getBuilder());
     *      _parser.parse(uri);
     *
     * (2) giving the translet an early opportunity to extract anything from
     *     the DOMImpl that it would like
     *
     *      translet.documentPrepass(_dom);
     *
     * (3) setting the document URI:
     *
     *      _dom.setDocumentURI(uri);
     * 
     * @param baseURI The base URI used by the document call.
     * @param href The href argument passed to the document function.
     * @param translet A reference to the translet requesting the document
     */
    public DOM retrieveDocument(String baseURI, String href, Translet translet);

}
