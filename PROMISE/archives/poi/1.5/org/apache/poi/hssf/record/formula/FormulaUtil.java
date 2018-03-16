package org.apache.poi.hssf.record.formula;

/**
 *
 * @author  andy
 */

public class FormulaUtil
{

    /** Creates new FormulaUtil */

    public FormulaUtil()
    {
    }

    public static Ptg [] parseFormula(String formula)
    {
        Ptg[]        ptg = null;
        StringBuffer f   = new StringBuffer(formula);

        if (isIntAddition(formula))
        {
            int loc = getLoc(formula, '+');

            System.out.println(formula.substring(0, loc).trim() + ","
                               + formula.substring(loc + 1,
                                                   formula.length()).trim());
            ptg = formulaAddTwoInts(Short
                .parseShort(formula.substring(0, loc).trim()), Short
                .parseShort(formula.substring(loc + 1, formula.length())
                    .trim()));
        }
        else if (isIntSubtraction(formula))
        {
            int loc = getLoc(formula, '-');

            ptg = formulaSubtractTwoInts(Short
                .parseShort(formula.substring(0, loc).trim()), Short
                .parseShort(formula.substring(loc + 1, formula.length())
                    .trim()));
        }
        else if (isIntMultiplication(formula))
        {
            int loc = getLoc(formula, '*');

            ptg = formulaMultiplyTwoInts(Short
                .parseShort(formula.substring(0, loc).trim()), Short
                .parseShort(formula.substring(loc + 1, formula.length())
                    .trim()));
        }
        else if (isIntDivision(formula))
        {
            int loc = getLoc(formula, '/');

            ptg = formulaDivideTwoInts(Short
                .parseShort(formula.substring(0, loc).trim()), Short
                .parseShort(formula.substring(loc + 1, formula.length())
                    .trim()));
        }
        else if (isIntPower(formula))
        {
            int loc = getLoc(formula, '^');

            ptg = formulaPowerTwoInts(Short
                .parseShort(formula.substring(0, loc).trim()), Short
                .parseShort(formula.substring(loc + 1, formula.length())
                    .trim()));
        }
        return ptg;
    }

    public static Ptg [] formulaAddTwoInts(short first, short second)
    {
        Ptg[] ptg = new Ptg[ 3 ];

        ptg[ 0 ] = createInteger(first);
        ptg[ 1 ] = createInteger(second);
        ptg[ 2 ] = createAdd();
        return ptg;
    }

    public static Ptg [] formulaSubtractTwoInts(short first, short second)
    {
        Ptg[] ptg = new Ptg[ 3 ];

        ptg[ 0 ] = createInteger(first);
        ptg[ 1 ] = createInteger(second);
        ptg[ 2 ] = createSubtract();
        return ptg;
    }

    public static Ptg [] formulaMultiplyTwoInts(short first, short second)
    {
        Ptg[] ptg = new Ptg[ 3 ];

        ptg[ 0 ] = createInteger(first);
        ptg[ 1 ] = createInteger(second);
        ptg[ 2 ] = createMultiply();
        return ptg;
    }

    public static Ptg [] formulaPowerTwoInts(short first, short second)
    {
        Ptg[] ptg = new Ptg[ 3 ];

        ptg[ 0 ] = createInteger(second);
        ptg[ 1 ] = createInteger(first);
        ptg[ 2 ] = createPower();
        return ptg;
    }

    public static Ptg [] formulaDivideTwoInts(short first, short second)
    {
        Ptg[] ptg = new Ptg[ 3 ];

        ptg[ 0 ] = createInteger(first);
        ptg[ 1 ] = createInteger(second);
        ptg[ 2 ] = createDivide();
        return ptg;
    }

    public static Ptg createInteger(short value)
    {
        IntPtg ptg = new IntPtg();

        ptg.setValue(value);
        return ptg;
    }

    public static Ptg createAdd()
    {
        AddPtg ptg = new AddPtg();

        return ptg;
    }

    public static Ptg createSubtract()
    {
        SubtractPtg ptg = new SubtractPtg();

        return ptg;
    }

    public static Ptg createMultiply()
    {
        MultiplyPtg ptg = new MultiplyPtg();

        return ptg;
    }

    public static Ptg createDivide()
    {
        DividePtg ptg = new DividePtg();

        return ptg;
    }

    public static Ptg createPower()
    {
        PowerPtg ptg = new PowerPtg();

        return ptg;
    }

    private static boolean isIntAddition(String formula)
    {
        StringBuffer buffer = new StringBuffer(formula);

        if (instr(formula, "+"))
        {
            return true;
        }
        return false;
    }

    private static boolean isIntSubtraction(String formula)
    {
        StringBuffer buffer = new StringBuffer(formula);

        if (instr(formula, "-"))
        {
            return true;
        }
        return false;
    }

    private static boolean isIntMultiplication(String formula)
    {
        StringBuffer buffer = new StringBuffer(formula);

        if (instr(formula, "*"))
        {
            return true;
        }
        return false;
    }

    private static boolean isIntDivision(String formula)
    {
        StringBuffer buffer = new StringBuffer(formula);

        if (instr(formula, "/"))
        {
            return true;
        }
        return false;
    }

    private static boolean isIntPower(String formula)
    {
        StringBuffer buffer = new StringBuffer(formula);

        if (instr(formula, "^"))
        {
            return true;
        }
        return false;
    }

    private static boolean instr(String matchin, String matchon)
    {
        int lenmatchin = matchin.length();
        int lenmatchon = matchon.length();
        int pos        = 0;

        if (lenmatchon > lenmatchin)
        {
            return false;
        }
        while (pos + lenmatchon < lenmatchin)
        {
            String sub = matchin.substring(pos, pos + lenmatchon);

            if (sub.equals(matchon))
            {
                return true;
            }
            pos++;
        }
        return false;
    }

    private static int getLoc(String matchin, char matchon)
    {
        int retval = -1;

        for (int pos = 0; pos < matchin.length(); pos++)
        {
            if (matchin.charAt(pos) == matchon)
            {
                retval = pos;
                break;
            }
        }
        return retval;
    }
}
