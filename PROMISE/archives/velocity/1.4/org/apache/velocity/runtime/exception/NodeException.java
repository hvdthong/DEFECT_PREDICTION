import org.apache.velocity.runtime.parser.node.Node;

public class NodeException extends Exception
{
    public NodeException(String exceptionMessage, Node node)
    {
        super(exceptionMessage + ": " + node.literal() + 
                " [line " + node.getLine() + ",column " +
                    node.getColumn() + "]");
    }        
}
