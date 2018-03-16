package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class NamespaceAlias extends TopLevelElement {

    private String sPrefix;
    private String rPrefix;
	
    /*
     * The namespace alias definitions given here have an impact only on
     * literal elements and literal attributes.
     */
    public void parseContents(Parser parser) {
	sPrefix = getAttribute("stylesheet-prefix");
	rPrefix = getAttribute("result-prefix");
	parser.getSymbolTable().addPrefixAlias(sPrefix,rPrefix);
    }
	
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	return Type.Void;
    }
	
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    }
}
