package org.apache.xalan.xsltc.compiler.util;

import java.util.Vector;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Template;

/**
 * This class is used for named templates. Named template methods have access
 * to the DOM, the current iterator, the handler and the current node.
 */
public final class NamedMethodGenerator extends MethodGenerator {
    protected static int CURRENT_INDEX  = 4;

    public NamedMethodGenerator(int access_flags, Type return_type,
				Type[] arg_types, String[] arg_names,
				String method_name, String class_name,
				InstructionList il, ConstantPoolGen cp) {
	super(access_flags, return_type, arg_types, arg_names, method_name, 
	      class_name, il, cp);
    }

    public int getLocalIndex(String name) {
	if (name.equals("current")) {
	    return CURRENT_INDEX;
	}
	return super.getLocalIndex(name);
    }
}
