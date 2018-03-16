package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
final class Message extends Instruction {
    private boolean _terminate = false;

    public void parseContents(Parser parser) {
	String termstr = getAttribute("terminate");
	if (termstr != null) {
            _terminate = termstr.equals("yes");
	}
	parseChildren(parser);
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	typeCheckContents(stable);
	return Type.Void;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	il.append(classGen.loadTranslet());

        switch (elementCount()) {
            case 0:
                il.append(new PUSH(cpg, ""));
            break;
            case 1:
                SyntaxTreeNode child = (SyntaxTreeNode) elementAt(0);
                if (child instanceof Text) {
                    il.append(new PUSH(cpg, ((Text) child).getText()));
                    break;
                }
            default:
                il.append(methodGen.loadHandler());

                il.append(new NEW(cpg.addClass(STREAM_XML_OUTPUT)));
                il.append(methodGen.storeHandler());

                il.append(new NEW(cpg.addClass(STRING_WRITER)));
                il.append(DUP);
                il.append(DUP);
                il.append(new INVOKESPECIAL(
                    cpg.addMethodref(STRING_WRITER, "<init>", "()V")));

                il.append(methodGen.loadHandler());
                il.append(new INVOKESPECIAL(
                    cpg.addMethodref(STREAM_XML_OUTPUT, "<init>",
                                     "()V")));

                il.append(methodGen.loadHandler());
                il.append(SWAP);
                il.append(new INVOKEVIRTUAL(
                    cpg.addMethodref(OUTPUT_BASE, "setWriter",
                                     "("+WRITER_SIG+")V")));

                il.append(methodGen.loadHandler());
                il.append(new INVOKEVIRTUAL(
                    cpg.addMethodref(OUTPUT_BASE, "setEncoding",
                                     "("+STRING_SIG+")V")));

                il.append(methodGen.loadHandler());
                il.append(ICONST_1);
                il.append(new INVOKEVIRTUAL(
                    cpg.addMethodref(OUTPUT_BASE, "setOmitXMLDeclaration",
                                     "(Z)V")));

                il.append(methodGen.loadHandler());
                il.append(new INVOKEVIRTUAL(
                    cpg.addMethodref(OUTPUT_BASE, "startDocument",
                                     "()V")));

                translateContents(classGen, methodGen);

                il.append(methodGen.loadHandler());
                il.append(new INVOKEVIRTUAL(
                    cpg.addMethodref(OUTPUT_BASE, "endDocument",
                                     "()V")));

                il.append(new INVOKEVIRTUAL(
                    cpg.addMethodref(STRING_WRITER, "toString",
                                     "()" + STRING_SIG)));

                il.append(SWAP);
                il.append(methodGen.storeHandler());
            break;
        }

	il.append(new INVOKEVIRTUAL(cpg.addMethodref(TRANSLET_CLASS,
						     "displayMessage",
						     "("+STRING_SIG+")V")));

	if (_terminate == true) {
	    final int einit = cpg.addMethodref("java.lang.RuntimeException",
					       "<init>",
					       "(Ljava/lang/String;)V");
	    il.append(new NEW(cpg.addClass("java.lang.RuntimeException")));
	    il.append(DUP);
	    il.append(new PUSH(cpg,"Termination forced by an " +
			           "xsl:message instruction"));
	    il.append(new INVOKESPECIAL(einit));
	    il.append(ATHROW);
	}
    }

}
