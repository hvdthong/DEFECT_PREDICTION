package org.apache.xalan.xsltc;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.dom.DOMImpl;

public interface DOMCache {

    /**
     * This method is responsible for:
     *
     * (1) building the DOMImpl tree
     *
     *      Parser  _parser = new Parser();
     *      DOMImpl _dom = new DOMImpl();
     *      _parser.setDocumentHandler(_dom.getBuilder());
     *      _parser.parse(uri);
     *
     * (2) building indicies for all ID elements (declared in DTD):
     *
     *      DTDMonitor _dtdMonitor = new DTDMonitor();
     *      _parser.setDTDHandler(_dtdMonitor);
     *      translet.setIndexSize(dom.getSize());
     *      dtd.buildIdIndex(dom, mask, translet);
     *
     * (3) passing unparsed entity URI elements from DTD to translet:
     *
     *      translet.setUnparsedEntityURIs(dtd.getUnparsedEntityURIs());
     *
     * (4) setting the document URI:
     *
     *      _dom.setDocumentURI(uri);
     */
    public DOMImpl retrieveDocument(String uri, int mask, Translet translet);

}
