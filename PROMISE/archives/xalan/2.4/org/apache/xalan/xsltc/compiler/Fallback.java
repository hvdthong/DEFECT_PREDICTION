package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.xml.parsers.*;

import org.xml.sax.*;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class Fallback extends Instruction {

    private boolean _active = false;

    /**
     * This element never produces any data on the stack
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	if (_active) {
	    return(typeCheckContents(stable));
	}
	else {
	    return Type.Void;
	}
    }

    /**
     * Activate this fallback element
     */
    public void activate() {
	_active = true;
    }

    public String toString() {
	return("fallback");
    }

    /**
     * Parse contents only if this fallback element is put in place of
     * some unsupported element or non-XSLTC extension element
     */
    public void parseContents(Parser parser) {
	if (_active) parseChildren(parser);
    }

    /**
     * Translate contents only if this fallback element is put in place of
     * some unsupported element or non-XSLTC extension element
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	if (_active) translateContents(classGen, methodGen);
    }
}
