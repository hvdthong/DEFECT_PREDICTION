package org.apache.xerces.validators.common;


import java.util.*;
import org.apache.xerces.validators.common.Grammar;
import org.apache.xerces.validators.common.GrammarResolver;
import org.apache.xerces.validators.datatype.DatatypeValidatorFactory;
import org.apache.xerces.validators.datatype.DatatypeValidatorFactoryImpl;


/**
 * This class embodies the representation of a Schema Grammar
 * pool.
 * This class is called from the validator.
 * Grammar pool maps to a set of Grammar Proxy classes.
 * 
 * @author Jeffrey Rodriguez
 * @version $Id: GrammarResolverImpl.java 316662 2000-12-01 02:52:10Z jeffreyr $
 */
public class GrammarResolverImpl implements GrammarResolver {


    /**
     *           Hashtable structure that represents a mapping
     *           between Namespace and a Grammar
     */

    private DatatypeValidatorFactoryImpl fDataTypeReg;

    /** Default constructor. */
    public GrammarResolverImpl() {
    }


    /**
     * 
     * @param nameSpaceKey
     *               Namespace key into Grammar pool
     * @return                           Grammar abstraction associated
     *         with NameSpace key.
     */
    public Grammar getGrammar( String nameSpaceKey ) {
        return(Grammar) ( fGrammarRegistry.get( nameSpaceKey ) ); 
    }

    public DatatypeValidatorFactory getDatatypeRegistry(){
            fDataTypeReg = new DatatypeValidatorFactoryImpl();
        }
        return fDataTypeReg;
    }

    /**
     * 
     * @return             Array of String key name spaces in Grammar pool
     */
    public String[] getNSKeysInPool() {
        int numberOfNSKeysInPool = fGrammarRegistry.size();
        String[] NSArray         = new String[numberOfNSKeysInPool];
        Enumeration enumOfKeys   = nameSpaceKeys();
        for (int i = 0; i<numberOfNSKeysInPool; i ++ ) {
            NSArray[i] = (String )( enumOfKeys.nextElement() );
        }
        return NSArray;
    }

    /**
     * 
     * @param nameSpaceKey
     *                Key to associate with Grammar
     *                abstraction
     * @param grammar Grammar abstraction
     *                used by validator.
     */
    public void putGrammar( String nameSpaceKey, Grammar grammar ){
        fGrammarRegistry.put( nameSpaceKey, grammar ); 
    }

    /**
     * 
     * @return         Length of grammar pool. Number of associations.
     */
    public int size() {
        return fGrammarRegistry.size();
    }

     /**
     * 
     * @return             Enumeration of String key name spaces in Grammar pool
     */

    public Enumeration nameSpaceKeys(){
        return fGrammarRegistry.keys();
    }


    /**
     * Removes association of Namespace key and Grammar from                         
     * Grammar pool
     * 
     * @param nameSpaceKey
     *               Name space key
     */
    public Grammar removeGrammar( String nameSpaceKey ) {
        if ( containsNameSpace( nameSpaceKey ) == true )
          fGrammarRegistry.remove( nameSpaceKey );
        return null;
    }



    /**
     *         Is Grammar abstraction in Grammar pool?
     * 
     * @param grammar Grammar Abstraction
     * @return true  - Yes there is at least one instance
     *         false - No
     */
    public boolean contains( Grammar grammar ){
        return fGrammarRegistry.contains( grammar );
    }

    /**
     *                Is Namespace key in Grammar pool
     * 
     * @param nameSpaceKey
     *               Namespace key
     * @return                Boolean- true - Namespace key association
     *         is in grammar pool.
     */
    public boolean containsNameSpace( String nameSpaceKey ){
        return fGrammarRegistry.containsKey( nameSpaceKey );
    }

    /**
     *         Reset internal Namespace/Grammar registry.
     */
    public void clearGrammarResolver() { 
        fGrammarRegistry.clear();
             fDataTypeReg.resetRegistry();
        }
       
    }






    /* Unit Test

    static final int NGRAMMARS  = 10;


    public static void main( String args[] ) 
    {
        SchemaGrammarResolver grammarPool     = SchemaGrammarResolver.instanceGrammarResolver();
        Grammar     testGrammars[]  = new Grammar[NGRAMMARS]; 
        String      testNameSpace[] = {

        for( int  i = 0; i< NGRAMMARS ; i++ ) {
        testGrammars[i] = new Grammar( testNameSpace[i] );
        }


        for( int i = 0; i<testGrammars.length; i++ ) {
            grammarPool.addGrammar( testNameSpace[i], testGrammars[i] );
        }
        String [] localNames = grammarPool.getNSKeysInPool();
        for( int i = 0; i<localNames.length; i++ ){
            System.out.println( "Key[" + i + "] =" + localNames[i] );
        }


         
        Grammar myTestGrammar = new Grammar( "testgrammar" );
        
        boolean isInPool = grammarPool.isGrammarInPool( myTestGrammar);
        System.out.println( "Grammar " + myTestGrammar.whatGrammarAmI()  + "Is in pool = " +  isInPool );
        
        grammarPool.addGrammar("myNSTest", myTestGrammar );
        isInPool = grammarPool.isGrammarInPool( myTestGrammar);
        System.out.println( "Just added Grammar " + myTestGrammar.whatGrammarAmI()  + "Is in pool = " +  isInPool );
                    
        isInPool = grammarPool.isNSInPool(myNSTest);

        System.out.println( "NS: " + myNSTest  + "Is in pool = " +  isInPool ); 
        grammarPool.addGrammar(myNSTest, new Grammar( myNSTest ));
        
        isInPool = grammarPool.isNSInPool(myNSTest);

        System.out.println( "NS: " + myNSTest  + "Is in pool = " +  isInPool ); 

        System.out.println( "Length of Grammar pool = " + grammarPool.length() );
               
        grammarPool.resetGrammarPool();
        System.out.println( "Length of Grammar pool now = " + grammarPool.length() );

        grammarPool.addGrammar("myNSTest", myTestGrammar );
        grammarPool.addGrammar("myNSTest", myTestGrammar );
        grammarPool.addGrammar("myNSTest", myTestGrammar );
        grammarPool.addGrammar("myNSTest", myTestGrammar );

        System.out.println( "Length of Grammar pool now better not be 5 = " + grammarPool.length() );
        for( int i = 0; i<testGrammars.length; i++ ) {
        grammarPool.addGrammar( testNameSpace[i], testGrammars[i] );
        }
        grammarPool.deleteGrammarForNS( "myNSTest" );
        System.out.println( "Length of Grammar pool now better not be 5 = " + grammarPool.length() );
        localNames = grammarPool.getNSKeysInPool();
        for( int i = 0; i<localNames.length; i++ ){
                   System.out.println( "Key[" + i + "] =" + localNames[i] );
           }
    }
    */

