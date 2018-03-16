import java.util.Map;

import org.apache.velocity.runtime.parser.node.ASTReference;

/**
 *  This class is a visitor used by the VM proxy to change the 
 *  literal representation of a reference in a VM.  The reason is
 *  to preserve the 'render literal if null' behavior w/o making
 *  the VMProxy stuff more complicated than it is already.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: VMReferenceMungeVisitor.java 75955 2004-03-03 23:23:08Z geirm $
 */ 
public class VMReferenceMungeVisitor extends BaseVisitor
{
    /**
     *  Map containing VM arg to instance-use reference
     *  Passed in with CTOR
     */
    private Map argmap = null;

    /**
     *  CTOR - takes a map of args to reference
     */
    public VMReferenceMungeVisitor( Map map )
    {
        argmap = map;
    }

    /**
     *  Visitor method - if the literal is right, will
     *  set the literal in the ASTReference node
     *
     *  @param node ASTReference to work on
     *  @param data Object to pass down from caller
     */
    public Object visit( ASTReference node, Object data)
    {
        /*
         *  see if there is an override value for this
         *  reference
         */
        String override = (String) argmap.get( node.literal().substring(1) );

        /*
         *  if so, set in the node
         */
        if( override != null)
        {
            node.setLiteral( override );
        }

        /*
         *  feed the children...
         */
        data = node.childrenAccept(this, data);   

        return data;
    }
}

