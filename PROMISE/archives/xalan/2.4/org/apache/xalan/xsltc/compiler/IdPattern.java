package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.*;

final class IdPattern extends IdKeyPattern {

    public IdPattern(String id) {
	super("##id",id);
    }

}
