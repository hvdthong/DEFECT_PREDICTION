package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

/**
 * @author Jacek Ambroziak
 * @author Morten Jorgensen
 */
final class DocumentCall extends FunctionCall {

    private Expression _arg1 = null;
    private Expression _arg2 = null;
    private Type       _arg1Type;

    /**
     * Default function call constructor
     */
    public DocumentCall(QName fname, Vector arguments) {
        super(fname, arguments);
    }

    /**
     * Type checks the arguments passed to the document() function. The first
     * argument can be any type (we must cast it to a string) and contains the
     * URI of the document
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        final int ac = argumentCount();
        if ((ac < 1) || (ac > 2)) {
            ErrorMsg msg = new ErrorMsg(ErrorMsg.ILLEGAL_ARG_ERR, this);
            throw new TypeCheckError(msg);
        }
        if (getStylesheet() == null) {
            ErrorMsg msg = new ErrorMsg(ErrorMsg.ILLEGAL_ARG_ERR, this);
            throw new TypeCheckError(msg);
        }

        _arg1 = argument(0);

            ErrorMsg msg = new ErrorMsg(ErrorMsg.DOCUMENT_ARG_ERR, this);
            throw new TypeCheckError(msg);
        }

        _arg1Type = _arg1.typeCheck(stable);
        if ((_arg1Type != Type.NodeSet) && (_arg1Type != Type.String)) {
            _arg1 = new CastExpr(_arg1, Type.String);
        }

        if (ac == 2) {
            _arg2 = argument(1);

                ErrorMsg msg = new ErrorMsg(ErrorMsg.DOCUMENT_ARG_ERR, this);
                throw new TypeCheckError(msg);
            }

            final Type arg2Type = _arg2.typeCheck(stable);

            if (arg2Type.identicalTo(Type.Node)) {
                _arg2 = new CastExpr(_arg2, Type.NodeSet);
            } else if (arg2Type.identicalTo(Type.NodeSet)) {
            } else {
                ErrorMsg msg = new ErrorMsg(ErrorMsg.DOCUMENT_ARG_ERR, this);
                throw new TypeCheckError(msg);
            }
        }

        return _type = Type.NodeSet;
    }
    
    /**
     * Translates the document() function call to a call to LoadDocument()'s
     * static method document().
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();
        final int ac = argumentCount();

        final int domField = cpg.addFieldref(classGen.getClassName(),
                                             DOM_FIELD,
                                             DOM_INTF_SIG);
          
        String docParamList = null;
        if (ac == 1) {
           docParamList = "("+OBJECT_SIG+STRING_SIG+TRANSLET_SIG+DOM_INTF_SIG
                         +")"+NODE_ITERATOR_SIG;
           docParamList = "("+OBJECT_SIG+NODE_ITERATOR_SIG+STRING_SIG
                         +TRANSLET_SIG+DOM_INTF_SIG+")"+NODE_ITERATOR_SIG;  
        }
        final int docIdx = cpg.addMethodref(LOAD_DOCUMENT_CLASS, "documentF",
                                            docParamList);


        _arg1.translate(classGen, methodGen);
        if (_arg1Type == Type.NodeSet) {
            _arg1.startIterator(classGen, methodGen);
        }

        if (ac == 2) {
            _arg2.translate(classGen, methodGen);
            _arg2.startIterator(classGen, methodGen);       
        }
    
        il.append(new PUSH(cpg, getStylesheet().getSystemId()));
        il.append(classGen.loadTranslet());
        il.append(DUP);
        il.append(new GETFIELD(domField));
        il.append(new INVOKESTATIC(docIdx));
    }

}
