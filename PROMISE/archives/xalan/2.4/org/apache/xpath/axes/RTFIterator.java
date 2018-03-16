package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xpath.compiler.Compiler;

public class RTFIterator extends OneStepIteratorForward {

	/**
	 * Constructor for RTFIterator
	 */
	RTFIterator(Compiler compiler, int opPos, int analysis)
		throws TransformerException {
		super(compiler, opPos, analysis);
	}

	/**
	 * Constructor for RTFIterator
	 */
	public RTFIterator(int axis) {
		super(axis);
	}

}

