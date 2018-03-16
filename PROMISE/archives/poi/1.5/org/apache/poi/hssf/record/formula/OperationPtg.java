package org.apache.poi.hssf.record.formula;

/**
 * defines a Ptg that is an operation instead of an operand
 * @author  andy
 */

public interface OperationPtg
{
    public final static int TYPE_UNARY    = 0;
    public final static int TYPE_BINARY   = 1;
    public final static int TYPE_FUNCTION = 2;

    public int getType();

    public int getNumberOfOperands();

    public String toFormulaString(Ptg [] operands);
}
