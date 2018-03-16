package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public abstract class Pattern extends Expression {
    /**
     * Returns the type of a pattern, which is always a <code>NodeType</code>.
     * A <code>NodeType</code> has a number of subtypes defined by 
     * <code>NodeType._type</code> corresponding to each type of node.
     */
    public abstract Type typeCheck(SymbolTable stable) throws TypeCheckError;

    /**
     * Translate this node into JVM bytecodes. Patterns are translated as
     * boolean expressions with true/false lists. Before calling 
     * <code>translate</code> on a pattern, make sure that the node being 
     * matched is on top of the stack. After calling <code>translate</code>, 
     * make sure to backpatch both true and false lists. True lists are the 
     * default, in the sense that they always <em>"fall through"</em>. If this
     * is not the intended semantics (e.g., see 
     * {@link org.apache.xalan.xsltc.compiler.AlternativePattern#translate})
     * then a GOTO must be appended to the instruction list after calling
     * <code>translate</code>. 
     */
    public abstract void translate(ClassGenerator classGen,
				   MethodGenerator methodGen);

    /**
     * Returns the priority of this pattern (section 5.5 in the XSLT spec).
     */
    public abstract double getPriority();
}
