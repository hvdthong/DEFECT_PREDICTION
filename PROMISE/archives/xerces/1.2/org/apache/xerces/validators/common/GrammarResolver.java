package org.apache.xerces.validators.common;
import java.util.*;
import org.apache.xerces.validators.common.Grammar;

/**
 * This class embodies the representation of a Grammar
 * pool Resolver.
 * This class is called from the validator.
 * 
 * 
 * @author Jeffrey Rodriguez
 */

public interface GrammarResolver {
    /**
     * initializeGrammarRegistry - Gets call to register initial
     * grammars such as DTD - Schema
     */

    /**
     * 
     * @param nameSpaceKey
     *               Namespace key into Grammar pool
     * @return                           Grammar abstraction associated
     *         with NameSpace key.
     */
    public Grammar getGrammar( String nameSpaceKey );

    /**
     * 
     * @return             Enumeration of String key name spaces in Grammar pool
     */

    public Enumeration nameSpaceKeys();

    /**
     * 
     * @param nameSpaceKey
     *                Key to associate with Grammar
     *                abstraction
     * @param grammar Grammar abstraction
     *                used by validator.
     */
    public void putGrammar( String nameSpaceKey, Grammar grammar );

    /**
     * Removes association of Namespace key and Grammar from                         
     * Grammar pool
     * 
     * @param nameSpaceKey
     *               Name space key
     */
    public Grammar removeGrammar( String nameSpaceKey ); 



    /**
     *         Is Grammar abstraction in Grammar pool?
     * 
     * @param grammar Grammar Abstraction
     * @return true  - Yes there is at least one instance
     *         false - No
     */
    public boolean contains( Grammar grammar );

    /**
     *                Is Namespace key in Grammar pool
     * 
     * @param nameSpaceKey
     *               Namespace key
     * @return                Boolean- true - Namespace key association
     *         is in grammar pool.
     */
    public boolean containsNameSpace( String nameSpaceKey ); 

    /**
     *         Reset internal Namespace/Grammar registry.
     */
    public void clearGrammarResolver();

    /**
     * 
     * @return         Length of grammar pool. Number of associations.
     */
    public int size(); 

}



