package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.*;

final class Attribute extends Instruction {
    private QName _name;
	
    public void display(int indent) {
	indent(indent);
	Util.println("Attribute " + _name);
	displayContents(indent + IndentIncrement);
    }

    public void parseContents(Parser parser) {
	_name = parser.getQName(getAttribute("name"));
	parseChildren(parser);
    }
}
