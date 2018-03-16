package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

/**
 * @author G. Todd Miller 
 */
final class FilteredAbsoluteLocationPath extends Expression {

    public FilteredAbsoluteLocationPath() {
	_path = null;
    }

    public FilteredAbsoluteLocationPath(Expression path) {
	_path = path;
	if (path != null) {
	    _path.setParent(this);
	}
    }

    public void setParser(Parser parser) {
	super.setParser(parser);
	if (_path != null) {
	    _path.setParser(parser);
	}
    }

    public Expression getPath() {
	return(_path);
    }
    
    public String toString() {
	return "FilteredAbsoluteLocationPath(" +
	    (_path != null ? _path.toString() : "null") + ')';
    }
	
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	if (_path != null) {
	    final Type ptype = _path.typeCheck(stable);
		_path = new CastExpr(_path, Type.NodeSet);
	    }
	}
	return _type = Type.NodeSet;	
    }
	
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	if (_path != null) {
	    final int initDFI = cpg.addMethodref(DUP_FILTERED_ITERATOR,
						"<init>",
						"("
						+ NODE_ITERATOR_SIG
						+ ")V");


            LocalVariableGen pathTemp =
               methodGen.addLocalVariable("filtered_absolute_location_path_tmp",
                                          Util.getJCRefType(NODE_ITERATOR_SIG),
                                          il.getEnd(), null);
	    _path.translate(classGen, methodGen);
            il.append(new ASTORE(pathTemp.getIndex()));

	    il.append(new NEW(cpg.addClass(DUP_FILTERED_ITERATOR)));
	    il.append(DUP);
            il.append(new ALOAD(pathTemp.getIndex()));

	    il.append(new INVOKESPECIAL(initDFI));
	}
	else {
	    final int git = cpg.addInterfaceMethodref(DOM_INTF,
						      "getIterator",
						      "()"+NODE_ITERATOR_SIG);
	    il.append(methodGen.loadDOM());
	    il.append(new INVOKEINTERFACE(git, 1));
	}
    }
}
