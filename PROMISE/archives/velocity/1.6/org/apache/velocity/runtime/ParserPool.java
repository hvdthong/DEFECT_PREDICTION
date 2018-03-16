import org.apache.velocity.runtime.parser.Parser;


/**
 * Provides instances of parsers as needed.  get() will return a new parser if
 * available.  If a parser is acquired from the pool, put() should be called
 * with that parser to make it available again for reuse.
 *
 * @author <a href="mailto:sergek@lokitech.com">Serge Knystautas</a>
 * @version $Id: RuntimeInstance.java 384374 2006-03-08 23:19:30Z nbubna $
 * @since 1.5
 */
public interface ParserPool
{
    /**
     * Initialize the pool so that it can begin serving parser instances.
     * @param svc
     */
    void initialize(RuntimeServices svc);

    /**
     * Retrieve an instance of a parser pool.
     * @return A parser object.
     */
    Parser get();

    /**
     * Return the parser to the pool so that it may be reused.
     * @param parser
     */
    void put(Parser parser);
}
