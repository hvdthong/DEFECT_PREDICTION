package org.apache.xalan.xsltc;


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
     */
    public DOM retrieveDocument(String uri, int mask, Translet translet);

}
