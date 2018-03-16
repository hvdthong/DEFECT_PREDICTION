package org.apache.xalan.xsltc.compiler;

import javax.xml.parsers.*;

import org.xml.sax.*;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class CallTemplate extends Instruction {
    private QName _name;

    public void display(int indent) {
	indent(indent);
	System.out.print("CallTemplate");
	Util.println(" name " + _name);
	displayContents(indent + IndentIncrement);
    }
		
    public boolean hasWithParams() {
	return elementCount() > 0;
    }

    public void parseContents(Parser parser) {
	_name = parser.getQNameIgnoreDefaultNs(getAttribute("name"));
	parseChildren(parser);
    }
		
    /**
     * Verify that a template with this name exists.
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	final Template template = stable.lookupTemplate(_name);
	if (template != null) {
	    typeCheckContents(stable);
	}
	else {
	    ErrorMsg err = new ErrorMsg(ErrorMsg.TEMPLATE_UNDEF_ERR,_name,this);
	    throw new TypeCheckError(err);
	}
	return Type.Void;
    }

    /**
     * Translate call-template.
     * A parameter frame is pushed only if some template in the stylesheet
     * uses parameters.
     * TODO: optimize by checking if the callee has parameters.
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final Stylesheet stylesheet = classGen.getStylesheet();
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();


	if (stylesheet.hasLocalParams() || hasContents()) {
	    final int push = cpg.addMethodref(TRANSLET_CLASS, 
					      PUSH_PARAM_FRAME,
					      PUSH_PARAM_FRAME_SIG);
	    il.append(classGen.loadTranslet());
	    il.append(new INVOKEVIRTUAL(push));
	    translateContents(classGen, methodGen);
	}

	final String className = stylesheet.getClassName();
	String methodName = Util.escape(_name.toString());

	il.append(classGen.loadTranslet());
	il.append(methodGen.loadDOM());
	il.append(methodGen.loadIterator());
	il.append(methodGen.loadHandler());
	il.append(methodGen.loadCurrentNode());
	il.append(new INVOKEVIRTUAL(cpg.addMethodref(className,
						     methodName,
						     "("
						     + DOM_INTF_SIG
						     + NODE_ITERATOR_SIG
						     + TRANSLET_OUTPUT_SIG
						     + NODE_SIG
						     +")V")));
	

	if (stylesheet.hasLocalParams() || hasContents()) {
	    final int pop = cpg.addMethodref(TRANSLET_CLASS,
					     POP_PARAM_FRAME,
					     POP_PARAM_FRAME_SIG);
	    il.append(classGen.loadTranslet());
	    il.append(new INVOKEVIRTUAL(pop));
	}
    }
} 
