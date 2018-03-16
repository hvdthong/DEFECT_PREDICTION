package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class TransletOutput extends Instruction {

    private Expression _filename;

    private final static String MISSING_FILE_ATTR =
	"The <xsltc:output> element requires a 'file' attribute.";

    /**
     * Displays the contents of this <xsltc:output> element.
     */
    public void display(int indent) {
	indent(indent);
	Util.println("TransletOutput: " + _filename);
    }
		
    /**
     * Parse the contents of this <xsltc:output> element. The only attribute
     * we recognise is the 'file' attribute that contains teh output filename.
     */
    public void parseContents(Parser parser) {
	String filename = getAttribute("file");

	if ((filename == null) || (filename.equals(EMPTYSTRING))) {
	    reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "file");
	}

	_filename = AttributeValue.create(this, filename, parser);
	parseChildren(parser);
    }
    
    /**
     * Type checks the 'file' attribute (must be able to convert it to a str).
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	final Type type = _filename.typeCheck(stable);
	if (type instanceof StringType == false) {
	    _filename = new CastExpr(_filename, Type.String);
	}
	typeCheckContents(stable);
	return Type.Void;
    }
    
    /**
     * Compile code that opens the give file for output, dumps the contents of
     * the element to the file, then closes the file.
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	il.append(methodGen.loadHandler());
	
	final int open =  cpg.addMethodref(TRANSLET_CLASS,
					   "openOutputHandler",
					   "("+STRING_SIG+")"+
					   TRANSLET_OUTPUT_SIG);

	final int close =  cpg.addMethodref(TRANSLET_CLASS,
					    "closeOutputHandler",
					    "("+TRANSLET_OUTPUT_SIG+")V");

	il.append(classGen.loadTranslet());
	_filename.translate(classGen, methodGen);
	il.append(new INVOKEVIRTUAL(open));

	il.append(methodGen.storeHandler());
	
	translateContents(classGen, methodGen);

	il.append(classGen.loadTranslet());
	il.append(methodGen.loadHandler());
	il.append(new INVOKEVIRTUAL(close));

	il.append(methodGen.storeHandler());
    }
}

