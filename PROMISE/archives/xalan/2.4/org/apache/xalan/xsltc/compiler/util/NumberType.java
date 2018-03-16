package org.apache.xalan.xsltc.compiler.util;

import org.apache.xalan.xsltc.compiler.util.Type;

public abstract class NumberType extends Type {
    public boolean isNumber() {
	return true;
    }

    public boolean isSimple() {
	return true;
    }
}

