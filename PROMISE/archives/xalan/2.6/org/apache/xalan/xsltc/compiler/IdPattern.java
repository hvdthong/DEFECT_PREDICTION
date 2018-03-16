package org.apache.xalan.xsltc.compiler;


/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
final class IdPattern extends IdKeyPattern {

    public IdPattern(String id) {
	super("##id",id);
    }

}
