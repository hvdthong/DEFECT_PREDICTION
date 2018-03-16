import java.io.Writer;
import java.io.IOException;

import java.util.Iterator;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeConstants;

import org.apache.velocity.context.InternalContextAdapter;

import org.apache.velocity.runtime.parser.node.Node;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import org.apache.velocity.util.introspection.Info;

/**
 * Foreach directive used for moving through arrays,
 * or objects that provide an Iterator.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: Foreach.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class Foreach extends Directive
{
    /**
     * Return name of this directive.
     */
    public String getName()
    {
        return "foreach";
    }        
    
    /**
     * Return type of this directive.
     */
    public int getType()
    {
        return BLOCK;
    }        

    /**
     * The name of the variable to use when placing
     * the counter value into the context. Right
     * now the default is $velocityCount.
     */
    private String counterName;

    /**
     * What value to start the loop counter at.
     */
    private int counterInitialValue;

    /**
     * The reference name used to access each
     * of the elements in the list object. It
     * is the $item in the following:
     *
     * #foreach ($item in $list)
     *
     * This can be used class wide because
     * it is immutable.
     */
    private String elementKey;

    /**
     *  immutable, so create in init
     */
    protected Info uberInfo;

    /**
     *  simple init - init the tree and get the elementKey from
     *  the AST
     */
    public void init(RuntimeServices rs, InternalContextAdapter context, Node node)
        throws Exception
    {
        super.init(rs, context, node);

        counterName = rsvc.getString(RuntimeConstants.COUNTER_NAME);
        counterInitialValue = rsvc.getInt(RuntimeConstants.COUNTER_INITIAL_VALUE);
 
        /*
         *  this is really the only thing we can do here as everything
         *  else is context sensitive
         */

        elementKey = node.jjtGetChild(0).getFirstToken().image.substring(1);

        /*
         * make an uberinfo - saves new's later on
         */

        uberInfo = new Info(context.getCurrentTemplateName(),
                getLine(),getColumn());
    }

    /**
     *  renders the #foreach() block
     */
    public boolean render(InternalContextAdapter context,
                           Writer writer, Node node)
        throws IOException,  MethodInvocationException, ResourceNotFoundException,
        	ParseErrorException
    {        
        /*
         *  do our introspection to see what our collection is
         */

        Object listObject = node.jjtGetChild(2).value(context);

        if (listObject == null)
             return false;

        Iterator i = null;

        try
        {
            i = rsvc.getUberspect().getIterator(listObject, uberInfo);
        }
        catch(Exception ee)
        {
            System.out.println(ee);
        }

        if (i == null)
        {
            return false;
        }

        int counter = counterInitialValue;
        
        /*
         *  save the element key if there is one,
         *  and the loop counter
         */

        Object o = context.get(elementKey);
        Object ctr = context.get( counterName);

        while (i.hasNext())
        {
            context.put( counterName , new Integer(counter));
            context.put(elementKey,i.next());
            node.jjtGetChild(3).render(context, writer);
            counter++;
        }

        /*
         * restores the loop counter (if we were nested)
         * if we have one, else just removes
         */
        
        if (ctr != null)
        {
            context.put(counterName, ctr);
        }
        else
        {
            context.remove(counterName);
        }


        /*
         *  restores element key if exists
         *  otherwise just removes
         */

        if (o != null)
        {
            context.put(elementKey, o);
        }
        else
        {
            context.remove(elementKey);
        }

        return true;
    }
}
