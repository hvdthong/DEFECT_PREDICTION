package org.apache.xerces.validators.datatype;
import java.util.Hashtable;
import java.util.Vector;
import java.lang.reflect.*;
import org.apache.xerces.validators.datatype.*;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.validators.datatype.DatatypeValidatorFactory;
import org.apache.xerces.validators.datatype.InvalidDatatypeFacetException;


/**
 * This class implements a factory of Datatype Validators. Internally the
 * DatatypeValidators are kept in a registry.<BR>
 * There is one instance of DatatypeValidatorFactoryImpl per Parser.<BR>
 * There is one datatype Registry per instance of DatatypeValidatorFactoryImpl,
 * such registry is first allocated with the number DatatypeValidators needed.<BR>
 * e.g.
 * If Parser finds an XML document with a DTD, a registry of DTD validators (only
 * 9 validators) get initialized in the registry.
 * The initialization process consist of instantiating the Datatype and
 * facets and registering the Datatype into registry table.
 * This implementatio uses a Hahtable as a registry table but future implementation
 * should use a lighter object, maybe a Map class ( not use a derived Map class
 * because of JDK 1.1.8 no supporting Map).<BR>
 * <BR>
 * As the Parser parses an instance document it knows if validation needs
 * to be checked. If no validation is necesary we should not instantiate a
 * DatatypeValidatorFactoryImpl.<BR>
 * If validation is needed, we need to instantiate a DatatypeValidatorFactoryImpl.<BR>
 * 
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @version $Id: DatatypeValidatorFactoryImpl.java 316715 2000-12-11 21:11:27Z ericye $
 */
public class DatatypeValidatorFactoryImpl implements DatatypeValidatorFactory {

    private static final boolean fDebug = false;
    private Hashtable fRegistry;
    private boolean   fRegistryExpanded = false;


    public DatatypeValidatorFactoryImpl() {
    }

    /**
     * Initializes registry with primitive and derived
     * Simple types.
     * 
     * This method does not clear the registry to clear
     * the registry you have to call resetRegistry.
     * 
     * The net effect of this method is to start with
     * a the smallest set of datatypes needed by the
     * validator.
     * 
     * If we start with DTD's then we initialize the
     * table to only the 9 validators needed by DTD Validation.
     * 
     * If we start with Schema's then we initialize to
     * to full set of validators.
     * 
     * @param registrySet
     */
    public void initializeDTDRegistry() {

        if (fRegistry == null) {
            fRegistry = new Hashtable();
            fRegistryExpanded = false;
        }



            try {
                fRegistry.put("string",            new StringDatatypeValidator() );
                fRegistry.put("ID",                new IDDatatypeValidator());
                fRegistry.put("IDREF",             new IDREFDatatypeValidator());
                fRegistry.put("ENTITY",            new ENTITYDatatypeValidator());
                fRegistry.put("NOTATION",          new NOTATIONDatatypeValidator());

                createDatatypeValidator( "IDREFS", new IDREFDatatypeValidator(), null , true );

                createDatatypeValidator( "ENTITIES", new ENTITYDatatypeValidator(),  null, true );

                Hashtable facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_PATTERN , "\\c+" );
                createDatatypeValidator("NMTOKEN", new StringDatatypeValidator(), facets, false );

                createDatatypeValidator("NMTOKENS",  
                                        getDatatypeValidator( "NMTOKEN" ), null, true );

            } catch (InvalidDatatypeFacetException ex) {
                ex.printStackTrace();
            }
        }
    }


    public void expandRegistryToFullSchemaSet() {

        if (fRegistry == null) {
            fRegistryExpanded = false;
        }
        if (fRegistryExpanded == false) {
            DatatypeValidator v;
            try {
                fRegistry.put("boolean",           new BooleanDatatypeValidator()  );
                fRegistry.put("float",             new FloatDatatypeValidator());
                fRegistry.put("double",            new DoubleDatatypeValidator());
                fRegistry.put("decimal",           new DecimalDatatypeValidator());
                fRegistry.put("timeDuration",      new TimeDurationDatatypeValidator());
                fRegistry.put("recurringDuration", new RecurringDurationDatatypeValidator());
                fRegistry.put("binary",            new BinaryDatatypeValidator());
                fRegistry.put("uriReference",      new URIReferenceDatatypeValidator());
                fRegistry.put("QName",             new QNameDatatypeValidator()); 

                if (fRegistry.get("IDREF") == null) {
                }

                Hashtable facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_PATTERN , "([a-zA-Z]{2}|[iI]-[a-zA-Z]+|[xX]-[a-zA-Z]+)(-[a-zA-Z]+)*" );

                createDatatypeValidator("language", new StringDatatypeValidator() , facets,
                                        false );






                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_PATTERN , "\\i\\c*" );
                createDatatypeValidator("Name", new StringDatatypeValidator(), facets, false );

                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_PATTERN , "[\\i-[:]][\\c-[:]]*"  );
                createDatatypeValidator("NCName", new StringDatatypeValidator(), facets, false );

                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_SCALE, "0");
                createDatatypeValidator("integer", new DecimalDatatypeValidator(), facets, false);


                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE , "0" );
                createDatatypeValidator("nonPositiveInteger", 
                                        getDatatypeValidator("integer"), facets, false );


                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE , "-1" );
                createDatatypeValidator("negativeInteger", 
                                        getDatatypeValidator( "nonPositiveInteger"), facets, false );

                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE , "9223372036854775807");
                facets.put(SchemaSymbols.ELT_MININCLUSIVE,  "-9223372036854775808");
                createDatatypeValidator("long", getDatatypeValidator( "integer"), facets, false );

                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE , "2147483647");
                facets.put(SchemaSymbols.ELT_MININCLUSIVE,  "-2147483648");
                createDatatypeValidator("int", getDatatypeValidator( "long"), facets,false );

                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE , "32767");
                facets.put(SchemaSymbols.ELT_MININCLUSIVE,  "-32768");
                createDatatypeValidator("short", getDatatypeValidator( "int"), facets, false );

                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE , "127");
                facets.put(SchemaSymbols.ELT_MININCLUSIVE,  "-128");
                createDatatypeValidator("byte",
                                        getDatatypeValidator( "short"), facets, false );

                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_MININCLUSIVE, "0" );
                createDatatypeValidator("nonNegativeInteger", 
                                        getDatatypeValidator( "integer"), facets, false );

                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE, "18446744073709551615" );
                createDatatypeValidator("unsignedLong",
                                        getDatatypeValidator( "nonNegativeInteger"), facets, false );


                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE, "4294967295" );
                createDatatypeValidator("unsignedInt",
                                        getDatatypeValidator( "unsignedLong"), facets, false );


                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE, "65535" );
                createDatatypeValidator("unsignedShort", 
                                        getDatatypeValidator( "unsignedInt"), facets, false );


                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE, "255" );
                createDatatypeValidator("unsignedByte",
                                        getDatatypeValidator( "unsignedShort"), facets, false );

                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_MININCLUSIVE, "1" );
                createDatatypeValidator("positiveInteger",
                                        getDatatypeValidator( "nonNegativeInteger"), facets, false );


                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_DURATION, "P0Y" );
                facets.put(SchemaSymbols.ELT_PERIOD,   "P0Y" );
                createDatatypeValidator("timeInstant", 
                                        getDatatypeValidator( "recurringDuration"),facets, false );

                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_DURATION, "P0Y" );
                createDatatypeValidator("time", 
                                        getDatatypeValidator( "recurringDuration"), facets, false );

                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_PERIOD,   "P0Y" );
                createDatatypeValidator("timePeriod", 
                                        getDatatypeValidator( "recurringDuration"), facets, false );


                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_DURATION, "PT24H" );
                createDatatypeValidator("date",
                                        getDatatypeValidator( "timePeriod"), facets, false );


                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_DURATION, "P1M" );
                createDatatypeValidator("month",
                                        getDatatypeValidator( "timePeriod"), facets, false );

                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_DURATION, "P1Y" );
                createDatatypeValidator("year", 
                                        getDatatypeValidator( "timePeriod"), facets, false );

                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_DURATION, "P100Y" );
                createDatatypeValidator("century", 
                                        getDatatypeValidator( "timePeriod"), facets, false );

                facets = new Hashtable();
                facets.put(SchemaSymbols.ELT_PERIOD, "P1Y" );
                facets.put(SchemaSymbols.ELT_DURATION, "PT24H" );
                createDatatypeValidator("recurringDate",
                                        getDatatypeValidator( "recurringDuration"),facets, false );
                fRegistryExpanded = true;
            } catch (InvalidDatatypeFacetException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * An optimization option that we should write in the future is to separate the static list
     * of Datatype Validators from the dynamic part where anonymous, and user derived datatype are
     * kept, then when we resetRegistry only the dynamic part of the registry should be cleared.
     * So we don't end up clearing the static part of the table over and over every time that we
     * do a parse cycle.
     */
    public void resetRegistry() {
        if (fRegistry != null) {
            fRegistry.clear();
            fRegistryExpanded = false;
        }
    }

    public DatatypeValidator createDatatypeValidator(String typeName, 
                                                     DatatypeValidator base, Hashtable facets, boolean list ) throws InvalidDatatypeFacetException {

        DatatypeValidator simpleType = null;

        if (this.fDebug == true) {
            System.out.println("type name = " + typeName );
        }

        if (base != null) {
            if (list) {
                simpleType = new ListDatatypeValidator(base, facets, list);    
            } else {
                try {
                    Class validatorDef = base.getClass();

                    Class [] validatorArgsClass = new Class[] {  
                        org.apache.xerces.validators.datatype.DatatypeValidator.class,
                        java.util.Hashtable.class,
                        boolean.class};



                    Object [] validatorArgs     = new Object[] {
                        base, facets, Boolean.FALSE};




                    Constructor validatorConstructor =
                    validatorDef.getConstructor( validatorArgsClass );


                    simpleType = 
                    ( DatatypeValidator ) createDatatypeValidator (
                                                                  validatorConstructor, validatorArgs );
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            if (simpleType != null) {
            }

        }
    }


    private static Object createDatatypeValidator(Constructor validatorConstructor, 
                                                  Object[] arguments)  throws  InvalidDatatypeFacetException {
        Object validator = null;
        try {
            validator = validatorConstructor.newInstance(arguments);
            return validator;
        } catch (InstantiationException e) {
            if (fDebug) {
                e.printStackTrace();
            } else {
                return null;
            }
        } catch (IllegalAccessException e) {
            if (fDebug) {
                e.printStackTrace();
            } else {
                return null;
            }
        } catch (IllegalArgumentException e) {
            if (fDebug) {
                e.printStackTrace();
            } else {
                return null;
            }
        } catch (InvocationTargetException e) {
            if (fDebug) {
                System.out.println("!! The original error message is: " + e.getTargetException().getMessage() );
                e.getTargetException().printStackTrace();
            } else {
                throw new InvalidDatatypeFacetException( e.getTargetException().getMessage() );
            }
        }
        return validator;
    }

    public DatatypeValidator createDatatypeValidator(String typeName, Vector validators) {
         DatatypeValidator simpleType = null;
         if (validators!=null) {
             simpleType = new UnionDatatypeValidator(validators);
         }
         if (simpleType !=null) {
             addValidator(typeName, simpleType);
         }
         return simpleType;
    }


    public DatatypeValidator getDatatypeValidator(String type) {
        AbstractDatatypeValidator simpleType = null;
        if (fDebug) {
            System.out.println( "type = >" + type +"<");
            System.out.println( "fRegistry = >" + fRegistry +"<" );
            simpleType = (AbstractDatatypeValidator) fRegistry.get(type);
        }
        if (type != null && fRegistry != null
            && fRegistry.containsKey( type ) == true) {
            simpleType = (AbstractDatatypeValidator) fRegistry.get(type);

        }
        return(DatatypeValidator) simpleType;
    }

    private void addValidator(String name, DatatypeValidator v) {
        fRegistry.put(name,v);
    }
}

