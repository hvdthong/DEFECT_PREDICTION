package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XMLChar;

import java.util.Vector;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Erwin Bolwidt <ejb@klomp.org>
 */
final class CallTemplate extends Instruction {
    
    /**
     * Name of template to call.
     */
    private QName _name;
    
    /** 
     * The array of effective parameters in this CallTemplate. An object in 
     * this array can be either a WithParam or a Param if no WithParam 
     * exists for a particular parameter.
     */
    private Object[] _parameters = null;
        
    /**
     * The corresponding template which this CallTemplate calls.
     */
    private Template _calleeTemplate = null;
    
    public void display(int indent) {
	indent(indent);
	System.out.print("CallTemplate");
	Util.println(" name " + _name);
	displayContents(indent + IndentIncrement);
    }
		
    public boolean hasWithParams() {
	return elementCount() > 0;
    }

    public void parseContents(Parser parser) {
        final String name = getAttribute("name");
        if (name.length() > 0) {
            if (!XMLChar.isValidQName(name)) {
                ErrorMsg err = new ErrorMsg(ErrorMsg.INVALID_QNAME_ERR, name, this);
                parser.reportError(Constants.ERROR, err);           
            }                
            _name = parser.getQNameIgnoreDefaultNs(name);
        }
        else {
            reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "name");		
        }
	parseChildren(parser);
    }
		
    /**
     * Verify that a template with this name exists.
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	final Template template = stable.lookupTemplate(_name);
	if (template != null) {
	    typeCheckContents(stable);
	}
	else {
	    ErrorMsg err = new ErrorMsg(ErrorMsg.TEMPLATE_UNDEF_ERR,_name,this);
	    throw new TypeCheckError(err);
	}
	return Type.Void;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final Stylesheet stylesheet = classGen.getStylesheet();
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	if (stylesheet.hasLocalParams() || hasContents()) {
	    _calleeTemplate = getCalleeTemplate();
	    
	    if (_calleeTemplate != null) {
	    	buildParameterList();
	    }
	    else {
	        final int push = cpg.addMethodref(TRANSLET_CLASS, 
					          PUSH_PARAM_FRAME,
					          PUSH_PARAM_FRAME_SIG);
	        il.append(classGen.loadTranslet());
	        il.append(new INVOKEVIRTUAL(push));
	        translateContents(classGen, methodGen);
	    }
	}

	final String className = stylesheet.getClassName();
        String methodName = Util.escape(_name.toString());

	il.append(classGen.loadTranslet());
	il.append(methodGen.loadDOM());
	il.append(methodGen.loadIterator());
	il.append(methodGen.loadHandler());
	il.append(methodGen.loadCurrentNode());
        
	StringBuffer methodSig = new StringBuffer("(" + DOM_INTF_SIG 
            + NODE_ITERATOR_SIG + TRANSLET_OUTPUT_SIG + NODE_SIG);
	
	if (_calleeTemplate != null) {
	    Vector calleeParams = _calleeTemplate.getParameters();
	    int numParams = _parameters.length;
	    
	    for (int i = 0; i < numParams; i++) {
	        SyntaxTreeNode node = (SyntaxTreeNode)_parameters[i];
                
                if (node instanceof Param) {
                    il.append(ACONST_NULL);
                }
                    node.translate(classGen, methodGen);
                }
            }
        }

	methodSig.append(")V");
	il.append(new INVOKEVIRTUAL(cpg.addMethodref(className,
						     methodName,
						     methodSig.toString())));
	
	if (_calleeTemplate == null && (stylesheet.hasLocalParams() || hasContents())) {
	    final int pop = cpg.addMethodref(TRANSLET_CLASS,
					     POP_PARAM_FRAME,
					     POP_PARAM_FRAME_SIG);
	    il.append(classGen.loadTranslet());
	    il.append(new INVOKEVIRTUAL(pop));
	}
    }
    
    /**
     * Return the simple named template which this CallTemplate calls.
     * Return false if there is no matched template or the matched
     * template is not a simple named template.
     */
    public Template getCalleeTemplate() {
    	Stylesheet stylesheet = getXSLTC().getStylesheet();
    	Vector templates = stylesheet.getAllValidTemplates();
        
    	int size = templates.size();
    	for (int i = 0; i < size; i++) {
    	    Template t = (Template)templates.elementAt(i);
    	    if (t.getName() == _name && t.isSimpleNamedTemplate()) {
    	    	return t;
    	    }
    	}
    	return null;
    }
    
    /**
     * Build the list of effective parameters in this CallTemplate.
     * The parameters of the called template are put into the array first.
     * Then we visit the WithParam children of this CallTemplate and replace
     * the Param with a corresponding WithParam having the same name.
     */
    private void buildParameterList() {   	
    	Vector defaultParams = _calleeTemplate.getParameters();
    	int numParams = defaultParams.size();
    	_parameters = new Object[numParams];
    	for (int i = 0; i < numParams; i++) {
    	    _parameters[i] = defaultParams.elementAt(i);
    	}
    		    	
    	int count = elementCount();
    	for (int i = 0; i < count; i++) {
    	    Object node = elementAt(i);
            
    	    if (node instanceof WithParam) {
    	    	WithParam withParam = (WithParam)node;
    	    	QName name = withParam.getName();
                
    	    	for (int k = 0; k < numParams; k++) {
    	    	    Object object = _parameters[k];
    	    	    if (object instanceof Param 
    	    	        && ((Param)object).getName() == name) {
    	    	        withParam.setDoParameterOptimization(true);
    	    	        _parameters[k] = withParam;
    	    	        break;
    	    	    }
    	    	    else if (object instanceof WithParam 
    	    	        && ((WithParam)object).getName() == name) {
    	    	        withParam.setDoParameterOptimization(true);
    	    	        _parameters[k] = withParam;    	    	        
    	    	        break;
    	    	    }
    	    	}    	    	
    	    }
    	}
     }
}
    
