package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class Text extends Instruction {

    private String _text;
    private boolean _escaping = true;
    private boolean _ignore = false;
    private boolean _textElement = false;

    /**
     * Create a blank Text syntax tree node.
     */
    public Text() {
	_textElement = true;
    }

    /**
     * Create text syntax tree node.
     * @param text is the text to put in the node.
     */
    public Text(String text) {
	_text = text;
    }

    /**
     * Returns the text wrapped inside this node
     * @return The text wrapped inside this node
     */
    protected String getText() {
	return _text;
    }

    /**
     * Set the text for this node. Appends the given text to any already
     * existing text (using string concatenation, so use only when needed).
     * @param text is the text to wrap inside this node.
     */
    protected void setText(String text) {
	if (_text == null)
	    _text = text;
	else
	    _text = _text + text;
    }

    public void display(int indent) {
	indent(indent);
	Util.println("Text");
	indent(indent + IndentIncrement);
	Util.println(_text);
    }
		
    public void parseContents(Parser parser) {
        final String str = getAttribute("disable-output-escaping");
	if ((str != null) && (str.equals("yes"))) _escaping = false;

	parseChildren(parser);

	if (_text == null) {
	    if (_textElement) {
		_text = EMPTYSTRING;
	    }
	    else {
		_ignore = true;
	    }
	}
	else if (_textElement) {
	    if (_text.length() == 0) _ignore = true;
	}
	else if (getParent() instanceof LiteralElement) {
	    LiteralElement element = (LiteralElement)getParent();
	    String space = element.getAttribute("xml:space");
	    if ((space == null) || (!space.equals("preserve")))
		if (_text.trim().length() == 0) _ignore = true;
	}
	else {
	    if (_text.trim().length() == 0) _ignore = true;
	}
    }

    public void ignore() {
	_ignore = true;
    }

    public boolean isTextElement() {
	return _textElement;
    }

    protected boolean contextDependent() {
	return false;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	if (!_ignore) {
	    final int esc = cpg.addInterfaceMethodref(OUTPUT_HANDLER,
						      "setEscaping", "(Z)Z");
	    if (!_escaping) {
		il.append(methodGen.loadHandler());
		il.append(new PUSH(cpg, false));
		il.append(new INVOKEINTERFACE(esc, 2));
	    }

	    final int characters = cpg.addInterfaceMethodref(OUTPUT_HANDLER,
							     "characters",
							     "(" + STRING_SIG + ")V");
	    il.append(methodGen.loadHandler());
	    il.append(new PUSH(cpg, _text));
	    il.append(new INVOKEINTERFACE(characters, 2));

	    if (!_escaping) {
		il.append(methodGen.loadHandler());
		il.append(SWAP);
		il.append(new INVOKEINTERFACE(esc, 2));
		il.append(POP);
	    }
	}
	translateContents(classGen, methodGen);
    }
}
