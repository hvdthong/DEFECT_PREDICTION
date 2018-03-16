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
 * @version  $Id: FloatDatatypeValidator.java 316047 2000-08-31 19:52:32Z jeffreyr $
 */

public class FloatDatatypeValidator extends AbstractDatatypeValidator {
    private Locale    fLocale               = null;
    private float[]   fEnumFloats           = null;
    private String    fPattern              = null;
    private float     fMaxInclusive         = Float.MAX_VALUE;
    private float     fMaxExclusive         = Float.MAX_VALUE;
    private float     fMinInclusive         = Float.MIN_VALUE;
    private float     fMinExclusive         = Float.MIN_VALUE;
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

        fDerivedByList = derivedByList; 

        if ( facets != null  )  {
            if ( fDerivedByList == false ) {
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
                                System.out.println("Internal Error parsing enumerated values for real type");
                            }
                    }
                }

            }
    }


    /**
     * validate that a string matches the real datatype
     * @param content A string containing the content to be validated
     * @exception throws InvalidDatatypeException if the content is
     *  is not a W3C real type
     */

    public Object validate(String content, Object state) 
    throws InvalidDatatypeValueException {
        if ( fDerivedByList == false  ) {
            checkContent(  content );
        } else {
           StringTokenizer parsedList = new StringTokenizer( content );
           try {
               while ( parsedList.hasMoreTokens() ) {
                   checkContent( parsedList.nextToken() );
               }
           } catch ( NoSuchElementException e ) {
               e.printStackTrace();
           }
        }
        return null;
    }

    /*
     * check that a facet is in range, assumes that facets are compatible -- compatibility ensured by setFacets
     */
    private void boundsCheck(float f) throws InvalidDatatypeValueException {
        boolean inUpperBound = false;
        boolean inLowerBound = false;

        if ( isMaxInclusiveDefined ) {
            inUpperBound = ( f <= fMaxInclusive );
        } else if ( isMaxExclusiveDefined ) {
            inUpperBound = ( f <  fMaxExclusive );
        }

        if ( isMinInclusiveDefined ) {
            inLowerBound = ( f >= fMinInclusive );
        } else if ( isMinExclusiveDefined ) {
            inLowerBound = ( f >  fMinExclusive );
        }

            getErrorString(DatatypeMessageProvider.OutOfBounds,
                           DatatypeMessageProvider.MSG_NONE,
        }
    }

    private void enumCheck(float v) throws InvalidDatatypeValueException {
        for (int i = 0; i < fEnumFloats.length; i++) {
            if (v == fEnumFloats[i]) return;
        }
        throw new InvalidDatatypeValueException(
                                               getErrorString(DatatypeMessageProvider.NotAnEnumValue,
                                                              DatatypeMessageProvider.MSG_NONE,
                                                              new Object [] { new Float(v)}));
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

    private  void checkContent( String content )throws InvalidDatatypeValueException {
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
            boundsCheck(f);

            if (((fFacetsDefined & DatatypeValidator.FACET_ENUMERATION ) != 0 ) )
                enumCheck(f);

    }

}
