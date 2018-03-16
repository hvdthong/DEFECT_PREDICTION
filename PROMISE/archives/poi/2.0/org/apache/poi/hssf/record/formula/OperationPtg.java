package org.apache.poi.hssf.record.formula;

import java.util.List;

/**
 * defines a Ptg that is an operation instead of an operand
 * @author  andy
 */

public abstract class OperationPtg extends Ptg
{
    public final static int TYPE_UNARY    = 0;
    public final static int TYPE_BINARY   = 1;
    public final static int TYPE_FUNCTION = 2;

    public abstract int getType();
    
    /**
     *  returns a string representation of the operations
     *  the length of the input array should equal the number returned by 
     *  @see #getNumberOfOperands
     *  
     */
    public abstract String toFormulaString(String[] operands);
    
    /**
     * The number of operands expected by the operations
     */
    public abstract int getNumberOfOperands();
    
    public byte getDefaultOperandClass() {return Ptg.CLASS_VALUE;}
    
}
