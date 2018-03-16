package org.apache.xerces.validators.datatype;
import java.util.Hashtable;
import java.util.Vector;
import java.lang.reflect.*;
import org.apache.xerces.validators.datatype.*;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.validators.datatype.DatatypeValidatorFactory;
import org.apache.xerces.validators.datatype.InvalidDatatypeFacetException;


/**
 *
 * This class implements a factory of datatype validators. Internally the
 * DatatypeValidators are kept in three registries:<BR>
 * (i) DTDRegistry - stores DTD datatype validators
 * <ii> SchemaRegistry - stores Schema datatype validators
 * <iii> UserDefinedRegistry - stores Schema user defined datatypes.
 * <BR>
 * The above registries will be initialized on demand (for XML document with a DTD, only
 * DTDRegistry will be initialized).
 * <BR>
 * <B>Note: </B>Between multiple parse() calls, only _user_defined_ registry will be reset.
 * DTD registry and schema registry are initialized only once and are kept for the *life-time* of the parser .
 * <BR>
 * This implementation uses a Hahtable as a registry table but future implementation
 * should use a lighter object, maybe a Map class ( not use a derived Map class
 * because of JDK 1.1.8 no supporting Map).<BR>
 * <BR>
 * As the Parser parses an instance document it knows if validation needs
 * to be checked. If no validation is necessary we should not instantiate a
 * DatatypeValidatorFactoryImpl.<BR>
 * <BR>
 *
 * @author Elena Litani
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @version $Id: DatatypeValidatorFactoryImpl.java 317412 2001-08-01 12:46:57Z sandygao $
 */
public class DatatypeValidatorFactoryImpl implements DatatypeValidatorFactory {

    private static final boolean fDebug = false;
    private Hashtable fRegistry;
    private Hashtable fDTDDatatypeRegistry;
    private Hashtable fSchemaDatatypeRegistry;

    private byte fRegistryExpanded = 0;


    private byte fSchemaValidation = 0;

    public DatatypeValidatorFactoryImpl() {
        fRegistry = new Hashtable(30);

        fSchemaDatatypeRegistry = new Hashtable (40);
        fDTDDatatypeRegistry = new Hashtable (10);
    }

    /**
     * Initializes fDTDRegistry with (9) DTD related datatypes .
     */
    public void initializeDTDRegistry() {


            try {
                fDTDDatatypeRegistry.put("string",            new StringDatatypeValidator() );
                fDTDDatatypeRegistry.put("ID",                new IDDatatypeValidator());
                fDTDDatatypeRegistry.put("IDREF",             new IDREFDatatypeValidator());
                fDTDDatatypeRegistry.put("ENTITY",            new ENTITYDatatypeValidator());
                fDTDDatatypeRegistry.put("NOTATION",          new NOTATIONDatatypeValidator());

                createDTDDatatypeValidator( "IDREFS", new IDREFDatatypeValidator(), null , true );

                createDTDDatatypeValidator( "ENTITIES", new ENTITYDatatypeValidator(),  null, true );

                Hashtable facets = new Hashtable(2);
                facets.put(AbstractStringValidator.FACET_SPECIAL_TOKEN,
                           AbstractStringValidator.SPECIAL_TOKEN_NMTOKEN);
                facets.put(SchemaSymbols.ELT_WHITESPACE, SchemaSymbols.ATT_COLLAPSE);
                createDTDDatatypeValidator("NMTOKEN", new StringDatatypeValidator(), facets, false );

                createDTDDatatypeValidator("NMTOKENS",  getDatatypeValidator( "NMTOKEN" ), null, true );
                fRegistryExpanded = 1;
            }
            catch ( InvalidDatatypeFacetException ex ) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * Initializes fSchemaDatatypeRegistry with schema primitive and derived datatypes.
     * See W3C Schema Datatype REC.
     * If DTD registry is not initialized yet, this method will initialize it as well.
     */
    public void expandRegistryToFullSchemaSet() {
        fSchemaValidation = 1;
        if ( fRegistryExpanded != 2 ) {
            DatatypeValidator v;
            try {
                fSchemaDatatypeRegistry.put("anySimpleType",     new AnySimpleType());
                fSchemaDatatypeRegistry.put("boolean",           new BooleanDatatypeValidator());
                fSchemaDatatypeRegistry.put("float",             new FloatDatatypeValidator());
                fSchemaDatatypeRegistry.put("double",            new DoubleDatatypeValidator());
                fSchemaDatatypeRegistry.put("decimal",           new DecimalDatatypeValidator());
                fSchemaDatatypeRegistry.put("hexBinary",         new HexBinaryDatatypeValidator());
                fSchemaDatatypeRegistry.put("base64Binary",      new Base64BinaryDatatypeValidator());
                fSchemaDatatypeRegistry.put("anyURI",            new AnyURIDatatypeValidator());
                fSchemaDatatypeRegistry.put("QName",             new QNameDatatypeValidator());
                fSchemaDatatypeRegistry.put("duration",          new DurationDatatypeValidator());
                fSchemaDatatypeRegistry.put("gDay",              new DayDatatypeValidator());
                fSchemaDatatypeRegistry.put("time",              new TimeDatatypeValidator());
                fSchemaDatatypeRegistry.put("dateTime",          new DateTimeDatatypeValidator());
                fSchemaDatatypeRegistry.put("date",              new DateDatatypeValidator());
                fSchemaDatatypeRegistry.put("gMonthDay",         new MonthDayDatatypeValidator());
                fSchemaDatatypeRegistry.put("gYearMonth",        new YearMonthDatatypeValidator());
                fSchemaDatatypeRegistry.put("gYear",             new YearDatatypeValidator());
                fSchemaDatatypeRegistry.put("gMonth",            new MonthDatatypeValidator());

                if ( fRegistryExpanded == 0 ) {
                }

                Hashtable facets = new Hashtable (2);
                facets.put(SchemaSymbols.ELT_WHITESPACE, SchemaSymbols.ATT_REPLACE);
                createSchemaDatatypeValidator("normalizedString", getDatatypeValidator("string"), facets, false);


                facets.clear();
                facets.put(SchemaSymbols.ELT_WHITESPACE, SchemaSymbols.ATT_COLLAPSE);
                createSchemaDatatypeValidator("token", getDatatypeValidator("string"), facets, false);

                facets.clear();
                facets.put(SchemaSymbols.ELT_WHITESPACE, SchemaSymbols.ATT_COLLAPSE);
                facets.put(SchemaSymbols.ELT_PATTERN , "([a-zA-Z]{2}|[iI]-[a-zA-Z]+|[xX]-[a-zA-Z]+)(-[a-zA-Z]+)*" );
                createSchemaDatatypeValidator("language", getDatatypeValidator("string") , facets, false );

                facets.clear();
                facets.put(SchemaSymbols.ELT_WHITESPACE, SchemaSymbols.ATT_COLLAPSE);
                facets.put(AbstractStringValidator.FACET_SPECIAL_TOKEN,
                           AbstractStringValidator.SPECIAL_TOKEN_NAME);
                createSchemaDatatypeValidator("Name", getDatatypeValidator("string"), facets, false );

                facets.clear();
                facets.put(SchemaSymbols.ELT_WHITESPACE, SchemaSymbols.ATT_COLLAPSE);
                facets.put(AbstractStringValidator.FACET_SPECIAL_TOKEN,
                           AbstractStringValidator.SPECIAL_TOKEN_NCNAME);
                createSchemaDatatypeValidator("NCName", getDatatypeValidator("string"), facets, false );

                facets.clear();
                facets.put(SchemaSymbols.ELT_FRACTIONDIGITS, "0");
                createSchemaDatatypeValidator("integer", getDatatypeValidator("decimal"), facets, false);


                facets.clear();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE , "0" );
                createSchemaDatatypeValidator("nonPositiveInteger",
                                              getDatatypeValidator("integer"), facets, false );


                facets.clear();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE , "-1" );
                createSchemaDatatypeValidator("negativeInteger",
                                              getDatatypeValidator( "nonPositiveInteger"), facets, false );

                facets.clear();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE , "9223372036854775807");
                facets.put(SchemaSymbols.ELT_MININCLUSIVE,  "-9223372036854775808");
                createSchemaDatatypeValidator("long", getDatatypeValidator( "integer"), facets, false );

                facets.clear();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE , "2147483647");
                facets.put(SchemaSymbols.ELT_MININCLUSIVE,  "-2147483648");
                createSchemaDatatypeValidator("int", getDatatypeValidator( "long"), facets,false );

                facets.clear();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE , "32767");
                facets.put(SchemaSymbols.ELT_MININCLUSIVE,  "-32768");
                createSchemaDatatypeValidator("short", getDatatypeValidator( "int"), facets, false );

                facets.clear();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE , "127");
                facets.put(SchemaSymbols.ELT_MININCLUSIVE,  "-128");
                createSchemaDatatypeValidator("byte",
                                              getDatatypeValidator( "short"), facets, false );

                facets.clear();
                facets.put(SchemaSymbols.ELT_MININCLUSIVE, "0" );
                createSchemaDatatypeValidator("nonNegativeInteger",
                                              getDatatypeValidator( "integer"), facets, false );

                facets.clear();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE, "18446744073709551615" );
                createSchemaDatatypeValidator("unsignedLong",
                                              getDatatypeValidator( "nonNegativeInteger"), facets, false );


                facets.clear();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE, "4294967295" );
                createSchemaDatatypeValidator("unsignedInt",
                                              getDatatypeValidator( "unsignedLong"), facets, false );


                facets.clear();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE, "65535" );
                createSchemaDatatypeValidator("unsignedShort",
                                              getDatatypeValidator( "unsignedInt"), facets, false );


                facets.clear();
                facets.put(SchemaSymbols.ELT_MAXINCLUSIVE, "255" );
                createSchemaDatatypeValidator("unsignedByte",
                                              getDatatypeValidator( "unsignedShort"), facets, false );

                facets.clear();
                facets.put(SchemaSymbols.ELT_MININCLUSIVE, "1" );
                createSchemaDatatypeValidator("positiveInteger",
                                              getDatatypeValidator( "nonNegativeInteger"), facets, false );

                ((IDDatatypeValidator)getDatatypeValidator("ID")).setTokenType(AbstractStringValidator.SPECIAL_TOKEN_IDNCNAME);
                ((IDREFDatatypeValidator)getDatatypeValidator("IDREF")).setTokenType(AbstractStringValidator.SPECIAL_TOKEN_IDREFNCNAME);
                QNameDatatypeValidator.setNCNameValidator(getDatatypeValidator("NCName"));

                fRegistryExpanded = 2;
            }
            catch ( InvalidDatatypeFacetException ex ) {
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
        fRegistry.clear();
        fSchemaValidation = 0;
    }

    public DatatypeValidator createDatatypeValidator(String typeName,
                                                     DatatypeValidator base, Hashtable facets, boolean list ) throws InvalidDatatypeFacetException {
        if ( base == null ) {
            return null;
        }
        DatatypeValidator simpleType = createSchemaValidator(typeName, base, facets, list);
        registerUserDefinedValidator(typeName, simpleType);
        return simpleType;
    }


    public DatatypeValidator createDatatypeValidator(String typeName, Vector validators) {
        DatatypeValidator simpleType = null;
        if ( validators!=null ) {
            simpleType = new UnionDatatypeValidator(validators);
        }
        if ( simpleType !=null ) {
            registerUserDefinedValidator(typeName, simpleType);
        }
        return simpleType;
    }


    /**
     * Searches different datatype registries depending on validation mode (schema or dtd)
     *
     * @param type
     * @return
     */
    public DatatypeValidator getDatatypeValidator(String type) {
        AbstractDatatypeValidator simpleType = null;
        if ( type == null ) {
            return null;
        }
        simpleType = (AbstractDatatypeValidator) fDTDDatatypeRegistry.get(type);
        if ( simpleType == null && fSchemaValidation == 1 ) {
            simpleType = (AbstractDatatypeValidator) fSchemaDatatypeRegistry.get(type);
            if ( simpleType == null ) {
                return(DatatypeValidator) fRegistry.get(type);
            }

        }

        return(DatatypeValidator)simpleType;

    }


    private DatatypeValidator createSchemaDatatypeValidator(String typeName,
                                                            DatatypeValidator base, Hashtable facets, boolean list ) throws InvalidDatatypeFacetException {
        DatatypeValidator primitive = createSchemaValidator(typeName, base, facets, list);
        registerSchemaValidator(typeName, primitive);
        return primitive;
    }

    private DatatypeValidator createDTDDatatypeValidator(String typeName,
                                                         DatatypeValidator base, Hashtable facets, boolean list ) throws InvalidDatatypeFacetException {
        DatatypeValidator primitive = createSchemaValidator(typeName, base, facets, list);
        registerDTDValidator(typeName, primitive);
        return primitive;
    }

    private DatatypeValidator createSchemaValidator (String typeName,
                                                     DatatypeValidator base, Hashtable facets, boolean list ) throws InvalidDatatypeFacetException{

        DatatypeValidator simpleType = null;
        if ( list ) {
            simpleType = new ListDatatypeValidator(base, facets, list);
        }
        else {
            try {
                String value = (String)facets.get(SchemaSymbols.ELT_WHITESPACE);

                if ( value != null && !(base instanceof StringDatatypeValidator) ) {
                    if ( !value.equals(SchemaSymbols.ATT_COLLAPSE) )
                        throw new InvalidDatatypeFacetException( "whiteSpace value '" + value +
                                                                 "' for this type must be 'collapse'.");
                    facets.remove(SchemaSymbols.ELT_WHITESPACE);
                }

                Class validatorDef = base.getClass();

                Class [] validatorArgsClass = new Class[] {
                    org.apache.xerces.validators.datatype.DatatypeValidator.class,
                    java.util.Hashtable.class,
                    boolean.class};

                Object [] validatorArgs     = new Object[] { base, facets, Boolean.FALSE};
                Constructor validatorConstructor = validatorDef.getConstructor( validatorArgsClass );
                simpleType = ( DatatypeValidator ) createDatatypeValidator ( validatorConstructor, validatorArgs );
            }
            catch ( NoSuchMethodException e ) {
                e.printStackTrace();
            }

        }
        return simpleType;
    }

    private void registerUserDefinedValidator (String typeName, DatatypeValidator simpleType) {
        if ( simpleType != null ) {
            fRegistry.put(typeName, simpleType);
        }
    }
    private void registerSchemaValidator (String typeName, DatatypeValidator simpleType) {
        if ( simpleType != null ) {
            fSchemaDatatypeRegistry.put(typeName, simpleType);
        }
    }
    private void registerDTDValidator (String typeName, DatatypeValidator simpleType) {
        if ( simpleType != null ) {
            fDTDDatatypeRegistry.put(typeName, simpleType);
        }
    }



    private static Object createDatatypeValidator(Constructor validatorConstructor,
                                                  Object[] arguments) throws InvalidDatatypeFacetException {
        Object validator = null;
        try {
            validator = validatorConstructor.newInstance(arguments);
        }
        catch ( InstantiationException e ) {
            if ( fDebug ) {
                e.printStackTrace();
            }
        }
        catch ( IllegalAccessException e ) {
            if ( fDebug ) {
                e.printStackTrace();
            }
        }
        catch ( IllegalArgumentException e ) {
            if ( fDebug ) {
                e.printStackTrace();
            }
        }
        catch ( InvocationTargetException e ) {
            if ( fDebug ) {
                System.out.println("!! The original error message is: " + e.getTargetException().getMessage() );
                e.getTargetException().printStackTrace();
            }
            else {
                throw new InvalidDatatypeFacetException( e.getTargetException().getMessage() );
            }
        }

        return validator;
    }

}

