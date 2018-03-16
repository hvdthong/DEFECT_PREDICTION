package org.apache.poi.hssf.record.formula;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author  andy
 */

public abstract class Ptg
{

    /** Creates new Ptg */
    
    public Ptg()
    {
    }
    
    
    
    /*
    private static List ptgsToList(Class [] ptgs)
    {
        List         result = new ArrayList();
        Constructor constructor;

        for (int i = 0; i < ptgs.length; i++)
        {
            Class ptg = null;
 
            ptg = ptgs[ i ];
            try
            {
                
                constructor = ptg.getConstructor(new Class[]
                {
                    byte [].class, int.class
                });
            }
            catch (Exception illegalArgumentException)
            {
                throw new RuntimeException(
                    "Now that didn't work nicely at all (couldn't do that there list of ptgs)");
            }
            result.add(constructor);
        }
        return result;
    }*/
    

    public static Ptg createPtg(byte [] data, int offset)
    {
        byte id     = data[ offset + 0 ];
        Ptg  retval = null;

        System.out.println("PTG = " + Integer.toHexString(id) + " (" + id
                           + ")");
        switch (id)
        {

            case AddPtg.sid :
                retval = new AddPtg(data, offset);
                break;

            case SubtractPtg.sid :
                retval = new SubtractPtg(data, offset);
                break;

            case IntPtg.sid :
                retval = new IntPtg(data, offset);
                break;

            case DividePtg.sid :
                retval = new DividePtg(data, offset);
                break;

            case MultiplyPtg.sid :
                retval = new MultiplyPtg(data, offset);
                break;

            case PowerPtg.sid :
                retval = new PowerPtg(data, offset);
                break;

            case AreaPtg.sid :
                retval = new AreaPtg(data, offset);
                break;

            case MemErrPtg.sid :
                retval = new MemErrPtg(data, offset);
                break;

            case AttrPtg.sid :
                retval = new AttrPtg(data, offset);
                break;

            case ValueReferencePtg.sid :
                retval = new ValueReferencePtg(data, offset);
                break;


            case ValueVariableFunctionPtg.sid :
                retval = new ValueVariableFunctionPtg(data, offset);
                break;

            case NamePtg.sid :
                retval = new NamePtg(data, offset);
                break;

            case ExpPtg.sid :
                retval = new ExpPtg(data, offset);
                break;

            default :

                throw new RuntimeException("Unknown PTG = "
                                           + Integer.toHexString(( int ) id)
                                           + " (" + ( int ) id + ")");
        }
        return retval;
    }

    public abstract int getSize();

    public final byte [] getBytes()
    {
        int    size  = getSize();
        byte[] bytes = new byte[ size ];

        writeBytes(bytes, 0);
        return bytes;
    }

    public abstract void writeBytes(byte [] array, int offset);

    public abstract String toFormulaString();
    
    /**
     * Ptg's should override this
     */
    
    public int getPrecedence() {
        return 100;
    }
    
    public int getStringLength() {
        return 0;
    }
    
    
}
