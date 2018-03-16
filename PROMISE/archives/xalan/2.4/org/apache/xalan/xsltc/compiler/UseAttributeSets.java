package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.StringTokenizer;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class UseAttributeSets extends Instruction {

    private final static String ATTR_SET_NOT_FOUND =
	"";

    private final Vector _sets = new Vector(2);

    /**
     * Constructur - define initial attribute sets to use
     */
    public UseAttributeSets(String setNames, Parser parser) {
	setParser(parser);
	addAttributeSets(setNames);
    }

    /**
     * This method is made public to enable an AttributeSet object to merge
     * itself with another AttributeSet (including any other AttributeSets
     * the two may inherit from).
     */
    public void addAttributeSets(String setNames) {
	if ((setNames != null) && (!setNames.equals(Constants.EMPTYSTRING))) {
	    final StringTokenizer tokens = new StringTokenizer(setNames);
	    while (tokens.hasMoreTokens()) {
		final QName qname = 
		    getParser().getQNameIgnoreDefaultNs(tokens.nextToken());
		_sets.add(qname);
	    }
	}
    }

    /**
     * Do nada.
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	return Type.Void;
    }

    /**
     * Generate a call to the method compiled for this attribute set
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {

	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	final SymbolTable symbolTable = getParser().getSymbolTable();

	for (int i=0; i<_sets.size(); i++) {
	    final QName name = (QName)_sets.elementAt(i);
	    final AttributeSet attrs = symbolTable.lookupAttributeSet(name);
	    if (attrs != null) {
		final String methodName = attrs.getMethodName();
		il.append(classGen.loadTranslet());
		il.append(methodGen.loadHandler());
		il.append(methodGen.loadIterator());
		final int method = cpg.addMethodref(classGen.getClassName(),
						    methodName, ATTR_SET_SIG);
		il.append(new INVOKESPECIAL(method));
	    }
	    else {
		final Parser parser = getParser();
		final String atrs = name.toString();
		reportError(this, parser, ErrorMsg.ATTRIBSET_UNDEF_ERR, atrs);
	    }
	}
    }
}
