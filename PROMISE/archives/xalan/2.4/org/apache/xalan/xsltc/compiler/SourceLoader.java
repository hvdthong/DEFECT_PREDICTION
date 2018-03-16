package org.apache.xalan.xsltc.compiler;

import org.xml.sax.InputSource;

public interface SourceLoader {

    /**
     * This interface is used to plug external document loaders into XSLTC
     * (used with the <xsl:include> and <xsl:import> elements.
     *
     * @param href The URI of the document to load
     * @param context The URI of the currently loaded document
     * @param xsltc The compiler that resuests the document
     * @return An InputSource with the loaded document
     */
    public InputSource loadSource(String href, String context, XSLTC xsltc);

}
