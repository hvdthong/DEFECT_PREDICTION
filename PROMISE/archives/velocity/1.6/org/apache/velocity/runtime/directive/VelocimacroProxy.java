import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.context.ProxyVMContext;
import org.apache.velocity.exception.MacroOverflowException;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.parser.ParserTreeConstants;
import org.apache.velocity.runtime.parser.node.ASTDirective;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;

/**
 *  VelocimacroProxy.java
 *
 *   a proxy Directive-derived object to fit with the current directive system
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: VelocimacroProxy.java 718424 2008-11-17 22:50:43Z nbubna $
 */
public class VelocimacroProxy extends Directive
{
    private String macroName;
    private String[] argArray = null;
    private String[] literalArgArray = null;
    private SimpleNode nodeTree = null;
    private int numMacroArgs = 0;
    private boolean preInit = false;
    private boolean strictArguments;
    private boolean localContextScope = false;
    private int maxCallDepth;

    /**
     * Return name of this Velocimacro.
     * @return The name of this Velocimacro.
     */
    public String getName()
    {
        return  macroName;
    }

    /**
     * Velocimacros are always LINE type directives.
     * @return The type of this directive.
     */
    public int getType()
    {
        return LINE;
    }

    /**
     * sets the directive name of this VM
     * 
     * @param name
     */
    public void setName(String name)
    {
        macroName = name;
    }

    /**
     * sets the array of arguments specified in the macro definition
     * 
     * @param arr
     */
    public void setArgArray(String[] arr)
    {
        argArray = arr;
        
        literalArgArray = new String[arr.length];
        for(int i = 0; i < arr.length; i++)
        {
            literalArgArray[i] = ".literal.$" + argArray[i];
        }

        /*
         * get the arg count from the arg array. remember that the arg array has the macro name as
         * it's 0th element
         */

        numMacroArgs = argArray.length - 1;
    }

    /**
     * @param tree
     */
    public void setNodeTree(SimpleNode tree)
    {
        nodeTree = tree;
    }

    /**
     * returns the number of ars needed for this VM
     * 
     * @return The number of ars needed for this VM
     */
    public int getNumArgs()
    {
        return numMacroArgs;
    }

    /**
     * Renders the macro using the context.
     * 
     * @param context Current rendering context
     * @param writer Writer for output
     * @param node AST that calls the macro
     * @return True if the directive rendered successfully.
     * @throws IOException
     * @throws MethodInvocationException
     * @throws MacroOverflowException
     */
    public boolean render(InternalContextAdapter context, Writer writer, Node node)
            throws IOException, MethodInvocationException, MacroOverflowException
    {

        final ProxyVMContext vmc = new ProxyVMContext(context, rsvc, localContextScope);

        int callArguments = node.jjtGetNumChildren();

        if (callArguments > 0)
        {
            for (int i = 1; i < argArray.length && i <= callArguments; i++)
            {
                Node macroCallArgument = node.jjtGetChild(i - 1);

                /*
                 * literalArgArray[i] is needed for "render literal if null" functionality.
                 * The value is used in ASTReference render-method.
                 * 
                 * The idea is to avoid generating the literal until absolutely necessary.
                 * 
                 * This makes VMReferenceMungeVisitor obsolete and it would not work anyway 
                 * when the macro AST is shared
                 */
                vmc.addVMProxyArg(context, argArray[i], literalArgArray[i], macroCallArgument);
            }
        }

        /*
         * check that we aren't already at the max call depth
         */
        if (maxCallDepth > 0 && maxCallDepth == vmc.getCurrentMacroCallDepth())
        {
            String templateName = vmc.getCurrentTemplateName();
            Object[] stack = vmc.getMacroNameStack();

            StringBuffer out = new StringBuffer(100)
                .append("Max calling depth of ").append(maxCallDepth)
                .append(" was exceeded in Template:").append(templateName)
                .append(" and Macro:").append(macroName)
                .append(" with Call Stack:");
            for (int i = 0; i < stack.length; i++)
            {
                if (i != 0)
                {
                    out.append("->");
                }
                out.append(stack[i]);
            }
            rsvc.getLog().error(out.toString());

            try
            {
                throw new MacroOverflowException(out.toString());
            }
            finally
            {
                while (vmc.getCurrentMacroCallDepth() > 0)
                {
                    vmc.popCurrentMacroName();
                }
            }
        }

        try
        {
            vmc.pushCurrentMacroName(macroName);
            nodeTree.render(vmc, writer);
            vmc.popCurrentMacroName();
            return true;
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            String msg = "VelocimacroProxy.render() : exception VM = #" + macroName + "()";
            rsvc.getLog().error(msg, e);
            throw new VelocityException(msg, e);
        }
    }

    /**
     * The major meat of VelocimacroProxy, init() checks the # of arguments.
     * 
     * @param rs
     * @param context
     * @param node
     * @throws TemplateInitException
     */
    public void init(RuntimeServices rs, InternalContextAdapter context, Node node)
            throws TemplateInitException
    {
        synchronized (this)
        {
            if (!preInit)
            {
                super.init(rs, context, node);

                strictArguments = rs.getConfiguration().getBoolean(
                        RuntimeConstants.VM_ARGUMENTS_STRICT, false);

                localContextScope = rsvc.getBoolean(RuntimeConstants.VM_CONTEXT_LOCALSCOPE, false);

                maxCallDepth = rsvc.getInt(RuntimeConstants.VM_MAX_DEPTH);

                nodeTree.init(context, rs);

                preInit = true;
            }
        }

        int i = node.jjtGetNumChildren();

        if (getNumArgs() != i)
        {

            for (Node parent = node.jjtGetParent(); parent != null;)
            {
                if ((parent instanceof ASTDirective)
                        && StringUtils.equals(((ASTDirective) parent).getDirectiveName(), "macro"))
                {
                    return;
                }
                parent = parent.jjtGetParent();
            }

            String msg = "VM #" + macroName + ": too "
                    + ((getNumArgs() > i) ? "few" : "many") + " arguments to macro. Wanted "
                    + getNumArgs() + " got " + i;

            if (strictArguments)
            {
                /**
                 * indicate col/line assuming it starts at 0 - this will be corrected one call up
                 */
                throw new TemplateInitException(msg, context.getCurrentTemplateName(), 0, 0);
            }
            else
            {
                rsvc.getLog().debug(msg);
                return;
            }
        }

        /* now validate that none of the arguments are plain words, (VELOCITY-614)
         * they should be string literals, references, inline maps, or inline lists */
        for (int n=0; n < i; n++)
        {
            Node child = node.jjtGetChild(n);
            if (child.getType() == ParserTreeConstants.JJTWORD)
            {
                /* indicate col/line assuming it starts at 0
                 * this will be corrected one call up  */
                throw new TemplateInitException("Invalid arg #"
                    + n + " in VM #" + macroName, context.getCurrentTemplateName(), 0, 0);
            }
        }
    }
}

