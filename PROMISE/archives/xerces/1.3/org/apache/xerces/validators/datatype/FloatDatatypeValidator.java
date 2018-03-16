package org.apache.xerces.validators.datatype;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.utils.regex.RegularExpression;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

/**
 *
 * @author Ted Leung
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @version  $Id: FloatDatatypeValidator.java 316786 2001-01-17 20:46:07Z elena $
 */

public class FloatDatatypeValidator extends AbstractDatatypeValidator {
    private Locale    fLocale               = null;
    private float[]   fEnumFloats           = null;
    private String    fPattern              = null;
    private float     fMaxInclusive         = Float.POSITIVE_INFINITY ;
    private float     fMaxExclusive         = Float.POSITIVE_INFINITY;
    private float     fMinInclusive         = Float.NEGATIVE_INFINITY;
    private float     fMinExclusive         = Float.NEGATIVE_INFINITY;
    private int       fFacetsDefined        = 0;

    private boolean   isMaxExclusiveDefined = false;
    private boolean   isMaxInclusiveDefined = false;
    private boolean   isMinExclusiveDefined = false;
    private boolean   isMinInclusiveDefined = false;
    private DatatypeMessageProvider fMessageProvider = new DatatypeMessageProvider();
    private RegularExpression      fRegex    = null;



    public FloatDatatypeValidator () throws InvalidDatatypeFacetException{
    }

    public FloatDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
                                    boolean derivedByList ) throws InvalidDatatypeFacetException {
        if ( base != null )


        if ( facets != null  )  {
            for (Enumeration e = facets.keys(); e.hasMoreElements();) {
                String key = (String) e.nextElement();

                if (key.equals(SchemaSymbols.ELT_PATTERN)) {
                    fFacetsDefined += DatatypeValidator.FACET_PATTERN;
                    fPattern = (String)facets.get(key);
                    if ( fPattern != null )
                        fRegex = new RegularExpression(fPattern, "X" );


                } else if (key.equals(SchemaSymbols.ELT_ENUMERATION)) {
                    fFacetsDefined += DatatypeValidator.FACET_ENUMERATION;
                } else if (key.equals(SchemaSymbols.ELT_MAXINCLUSIVE)) {
                    fFacetsDefined += DatatypeValidator.FACET_MAXINCLUSIVE;
                    String value = null;
                    try {
                        value  = ((String)facets.get(key));
                        fMaxInclusive = Float.valueOf(value).floatValue();
                    } catch (NumberFormatException ex ) {
                        throw new InvalidDatatypeFacetException( getErrorString(
                                                                                DatatypeMessageProvider.IllegalFacetValue, 
                                                                                DatatypeMessageProvider.MSG_NONE, new Object [] { value, key}));
                    }
                } else if (key.equals(SchemaSymbols.ELT_MAXEXCLUSIVE)) {
                    fFacetsDefined += DatatypeValidator.FACET_MAXEXCLUSIVE;
                    String value = null;
                    try {
                        value  = ((String)facets.get(key));
                        fMaxExclusive = Float.valueOf(value).floatValue();
                    } catch (NumberFormatException ex ) {
                        throw new InvalidDatatypeFacetException( getErrorString(
                                                                                DatatypeMessageProvider.IllegalFacetValue, 
                                                                                DatatypeMessageProvider.MSG_NONE, new Object [] { value, key}));
                    }
                } else if (key.equals(SchemaSymbols.ELT_MININCLUSIVE)) {
                    fFacetsDefined += DatatypeValidator.FACET_MININCLUSIVE;
                    String value = null;
                    try {
                        value  = ((String)facets.get(key));
                        fMinInclusive  = Float.valueOf(value).floatValue();
                    } catch (NumberFormatException ex ) {
                        throw new InvalidDatatypeFacetException( getErrorString(
                                                                                DatatypeMessageProvider.IllegalFacetValue, 
                                                                                DatatypeMessageProvider.MSG_NONE, new Object [] { value, key}));
                    }
                } else if (key.equals(SchemaSymbols.ELT_MINEXCLUSIVE)) {
                    fFacetsDefined += DatatypeValidator.FACET_MININCLUSIVE;
                    String value = null;
                    try {
                        value  = ((String)facets.get(key));
                        fMinExclusive  = Float.valueOf(value).floatValue();
                    } catch (NumberFormatException ex ) {
                        throw new InvalidDatatypeFacetException( getErrorString(
                                                                                DatatypeMessageProvider.IllegalFacetValue, 
                                                                                DatatypeMessageProvider.MSG_NONE, new Object [] { value, key}));
                    }
                } else {
                    throw new InvalidDatatypeFacetException( getErrorString(  DatatypeMessageProvider.MSG_FORMAT_FAILURE,
                                                                                DatatypeMessageProvider.MSG_NONE,
                                                                                null));
                }
            }
            isMaxExclusiveDefined = ((fFacetsDefined & 
                                        DatatypeValidator.FACET_MAXEXCLUSIVE ) != 0 )?true:false;
            isMaxInclusiveDefined = ((fFacetsDefined & 
                                        DatatypeValidator.FACET_MAXINCLUSIVE ) != 0 )?true:false;
            isMinExclusiveDefined = ((fFacetsDefined &
                                        DatatypeValidator.FACET_MINEXCLUSIVE ) != 0 )?true:false;
            isMinInclusiveDefined = ((fFacetsDefined &
                                        DatatypeValidator.FACET_MININCLUSIVE ) != 0 )?true:false;


            if ( isMaxExclusiveDefined && isMaxInclusiveDefined ) {
                throw new InvalidDatatypeFacetException(
                                                        "It is an error for both maxInclusive and maxExclusive to be specified for the same datatype." ); 
            }
            if ( isMinExclusiveDefined && isMinInclusiveDefined ) {
                throw new InvalidDatatypeFacetException(
                                                        "It is an error for both minInclusive and minExclusive to be specified for the same datatype." ); 
            }



            if ( (fFacetsDefined & DatatypeValidator.FACET_ENUMERATION ) != 0 ) {
                Vector v = (Vector) facets.get(SchemaSymbols.ELT_ENUMERATION);    
                if (v != null) {
                    fEnumFloats = new float[v.size()];
                    for (int i = 0; i < v.size(); i++)
                        try {
                            fEnumFloats[i] = Float.valueOf((String) v.elementAt(i)).floatValue();
                        } catch (InvalidDatatypeValueException idve) {
                            throw new InvalidDatatypeFacetException(
                                                                    getErrorString(DatatypeMessageProvider.InvalidEnumValue,
                                                                                    DatatypeMessageProvider.MSG_NONE,
                                                                                    new Object [] { v.elementAt(i)}));
                        } catch (NumberFormatException nfe) {
                            if( v.elementAt(i).equals("INF") ){
                                fEnumFloats[i] = Float.POSITIVE_INFINITY;
                            } else if( v.elementAt(i).equals("-INF")){
                                fEnumFloats[i] = Float.NEGATIVE_INFINITY;
                            } else if( v.elementAt(i).equals("NaN")) {
                                fEnumFloats[i] = Float.NaN;
                            } else {
                                throw new InvalidDatatypeFacetException( getErrorString(
                                    DatatypeMessageProvider.IllegalFacetValue, 
                                    DatatypeMessageProvider.MSG_NONE, new Object [] {v.elementAt(i), "enumeration"}));
                            }
                        }
                }
            }
    }


    /**
     * Validate string content to be a valid float as
     * defined 3.2.3. Datatype.
     * IEEE single-precision 32-bit floatin point type
     * [IEEE] 754-1985]. The basic value space of float
     * consists of the values mx2^e, where m is an integer
     * whose absolute value is less than 2^24, and e
     * is an integer between -149 and 104 inclusive.
     * 
     * @param content A string containing the content to be validated
     * @param state
     * @return 
     * @exception throws InvalidDatatypeException if the content is
     *                   is not a W3C real type
     * @exception InvalidDatatypeValueException
     */
    public Object validate(String content, Object state) 
    throws InvalidDatatypeValueException {
        checkContentEnum (content, state, null);
        return null;
    }

     /**
     * validate if the content is valid against base datatype and facets (if any)
     * this function might be called directly from UnionDatatype or ListDatatype 
     * 
     * @param content A string containing the content to be validated
     * @param enumeration A vector with enumeration strings  
     * @exception throws InvalidDatatypeException if the content is
     *  is not a W3C decimal type;
     * @exception throws InvalidDatatypeFacetException if enumeration is not float
     */
     protected void checkContentEnum(String content, Object state, Vector enumeration) 
                                                      throws InvalidDatatypeValueException{
             ((FloatDatatypeValidator)this.fBaseValidator).checkContentEnum( content, state, enumeration);
       }

       if ( (fFacetsDefined & DatatypeValidator.FACET_PATTERN ) != 0 ) {
               if ( fRegex == null || fRegex.matches( content) == false )
                   throw new InvalidDatatypeValueException("Value'"+content+
                                                           "does not match regular expression facet" + fPattern );
       }

        float f = 0;
        try {
                f = Float.valueOf(content).floatValue();
        } catch (NumberFormatException nfe) {
                if( content.equals("INF") ){
                    f=Float.POSITIVE_INFINITY;
                } else if( content.equals("-INF") ){
                    f=Float.NEGATIVE_INFINITY;
                } else if( content.equals("NaN" ) ) {
                    f=Float.NaN;
                } else {
                    throw new InvalidDatatypeValueException(
                                  getErrorString(DatatypeMessageProvider.NotFloat,
                                                 DatatypeMessageProvider.MSG_NONE,
                                                           new Object [] { content}));
                }
        }
        
        if (enumeration != null) {
            int size =  enumeration.size();
            float[]     enumFloats = new float[size];
            int i=0;
            try {
                for (; i < size; i++)
                    enumFloats[i] = Float.valueOf((String) enumeration.elementAt(i)).floatValue();
            
            } catch (NumberFormatException nfe) {
                if( enumeration.elementAt(i).equals("INF") ){
                    enumFloats[i] = Float.POSITIVE_INFINITY;
                } else if( enumeration.elementAt(i).equals("-INF")){
                    enumFloats[i] = Float.NEGATIVE_INFINITY;
                } else if( enumeration.elementAt(i).equals("NaN")) {
                    enumFloats[i] = Float.NaN;
                } else {
                    throw new InvalidDatatypeValueException(
                                    getErrorString(DatatypeMessageProvider.InvalidEnumValue,
                                                   DatatypeMessageProvider.MSG_NONE,
                                                   new Object [] { enumeration.elementAt(i)}));
                }
            }
            enumCheck(f, enumFloats);
        }

        boundsCheck(f);

        if (((fFacetsDefined & DatatypeValidator.FACET_ENUMERATION ) != 0 ) )
                enumCheck(f, fEnumFloats);
    }




    /*
     * check that a facet is in range, assumes that facets are compatible -- compatibility ensured by setFacets
     */
    private void boundsCheck(float d) throws InvalidDatatypeValueException {

        boolean minOk = false;
        boolean maxOk = false;
        String  upperBound =  (fMaxExclusive != Float.MAX_VALUE )? (   Float.toString( fMaxExclusive)) :
                              ( ( fMaxInclusive != Float.MAX_VALUE )? Float.toString( fMaxInclusive):"");

        String  lowerBound =  (fMinExclusive != Float.MIN_VALUE )? ( Float.toString( fMinExclusive ) ):
                              (( fMinInclusive != Float.MIN_VALUE )? Float.toString( fMinInclusive ):""); 
        String  lowerBoundIndicator = "";
        String  upperBoundIndicator = "";

        if ( isMaxInclusiveDefined) {
            maxOk = (d <= fMaxInclusive);
            upperBound          = Float.toString( fMaxInclusive );
            if ( upperBound != null ) {
                upperBoundIndicator = "<="; 
            } else {
                upperBound="";
            }
        } else if ( isMaxExclusiveDefined) {
            maxOk = (d < fMaxExclusive );
            upperBound = Float.toString(fMaxExclusive );
            if ( upperBound != null ) {
                upperBoundIndicator = "<";
            } else {
                upperBound = "";
            }
        } else {
            maxOk = (!isMaxInclusiveDefined && ! isMaxExclusiveDefined);
        }



        if ( isMinInclusiveDefined) {

            minOk = (d >=  fMinInclusive );
            lowerBound = Float.toString( fMinInclusive );
            if ( lowerBound != null ) {
                lowerBoundIndicator = "<=";
            } else {
                lowerBound = "";
            }
        } else if ( isMinExclusiveDefined) {
            minOk = (d > fMinExclusive);
            lowerBound = Float.toString( fMinExclusive  );
            if ( lowerBound != null ) {
                lowerBoundIndicator = "<";
            } else {
                lowerBound = "";
            }
        } else {
            minOk = (!isMinInclusiveDefined && !isMinExclusiveDefined);
        }

        if (!(minOk && maxOk))
            throw new InvalidDatatypeValueException (
                             getErrorString(DatatypeMessageProvider.OutOfBounds,
                                  DatatypeMessageProvider.MSG_NONE,
                                      new Object [] { Float.toString(d) ,  lowerBound ,
                                          upperBound, lowerBoundIndicator, upperBoundIndicator}));

    }

  
    /**
     * set the locate to be used for error messages
     */
    public void setLocale(Locale locale) {
        fLocale = locale;
    }

    public int compare( String content1, String content2){
        return 0;
    }


    



    public Hashtable getFacets(){
        return null;
    }
    /**
       * Returns a copy of this object.
       */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("clone() is not supported in "+this.getClass().getName());
    }


    private void enumCheck(float v, float[] enumFloats) throws InvalidDatatypeValueException {
       for (int i = 0; i < enumFloats.length; i++) {
           if (v == enumFloats[i]) return;
       }
       throw new InvalidDatatypeValueException(
                                              getErrorString(DatatypeMessageProvider.NotAnEnumValue,
                                                             DatatypeMessageProvider.MSG_NONE,
                                                             new Object [] { new Float(v)}));
   }


    private String getErrorString(int major, int minor, Object args[]) {
        try {
            return fMessageProvider.createMessage(fLocale, major, minor, args);
        } catch (Exception e) {
            return "Illegal Errorcode "+minor;
        }
    }


    private void setBasetype(DatatypeValidator base) {
        fBaseValidator =  base;
    }

    

}
