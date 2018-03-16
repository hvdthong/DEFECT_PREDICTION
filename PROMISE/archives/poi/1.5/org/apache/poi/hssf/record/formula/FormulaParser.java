package org.apache.poi.hssf.record.formula;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

/**
 * EXPERIMENTAL code to parse formulas back and forth between RPN and not
 *
 * @author Avik Sengupta <lists@aviksengupta.com>
 */
public class FormulaParser {
    
    private String formulaString;
    private int pointer=0;
    
    private Stack operationsList = new java.util.Stack();
    private Stack operandsList = new java.util.Stack();
    private List result = new ArrayList();
    private int numParen;
    
    
    private static char TAB = '\t';
    private static char CR = '\n';
    
    
    
    public FormulaParser(String formula){
        formulaString = formula;
        pointer=0;
    }
    
    
    private void GetChar() {
        Look=formulaString.charAt(pointer++);
        System.out.println("Got char: "+Look);
    }
    
    
    private void Error(String s) {
        System.out.println("Error: "+s);
    }
    
    
    
    private void Abort(String s) {
        Error(s);
        throw new RuntimeException("Cannot Parse, sorry");
    }
    
    
    
    private void Expected(String s) {
        Abort(s + " Expected");
    }
    
    
    
    private boolean IsAlpha(char c) {
        return Character.isLetter(c);
    }
    
    
    
    private boolean IsDigit(char c) {
        System.out.println("Checking digit for"+c);
        return Character.isDigit(c);
        
    }
    
    
    
    private boolean  IsAlNum(char c) {
        return  (IsAlpha(c) || IsDigit(c));
    }
    
    
    
    private boolean IsAddop( char c) {
        return (c =='+' || c =='-');
    }
    
    
    
    private boolean IsWhite( char c) {
        return  (c ==' ' || c== TAB);
    }
    
    
    
    private void SkipWhite() {
        while (IsWhite(Look)) {
            GetChar();
        }
    }
    
    
    
    private void Match(char x) {
        if (Look != x) {
            Expected("" + x + "");
        }else {
            GetChar();
            SkipWhite();
        }
    }
    
    
    
    private String GetName() {
        String Token;
        Token = "";
        if (!IsAlpha(Look)) {
            Expected("Name");
        }
        while (IsAlNum(Look)) {
            Token = Token + Character.toUpperCase(Look);
            GetChar();
        }
        
        SkipWhite();
        return Token;
    }
    
    
    
    private String GetNum() {
        String Value ="";
        if  (!IsDigit(Look)) Expected("Integer");
        while (IsDigit(Look)){
            Value = Value + Look;
            GetChar();
        }
        SkipWhite();
        return Value;
    }
    
    
    
    private void  Emit(String s){
        System.out.print(TAB+s);
    }
    
    
    
    private void EmitLn(String s) {
        Emit(s);
        System.out.println();;
    }
    
    
    
    private void Ident() {
        String Name;
        Name = GetName();
        if (Look == '('){
            Match('(');
            Match(')');
        } else {
            
            if (cellRef) {
            }else {
            }
        }
    }

    
    
    
    private void Factor() {
        if (Look == '(' ) {
            Match('(');
            operationsList.add(new ParenthesisPtg());
            Expression();
            Match(')');
            operationsList.add(new ParenthesisPtg());
            return;
        } else if (IsAlpha(Look)){
            Ident();
        }else{
            IntPtg p = new IntPtg();
            p.setValue(Short.parseShort(GetNum()));
            operandsList.add(p);
        }
    }

    
    
    private void Multiply(){
        Match('*');
        Factor();
        operationsList.add(new MultiplyPtg());
    }
    
    
    
    private void Divide() {
        Match('/');
        Factor();
        operationsList.add(new DividePtg());
    }
    
    
    
    private void  Term(){
        Factor();
        while (Look == '*' || Look == '/' ) {
            if (Look == '*') Multiply();
            if (Look == '/') Divide();
        }
    }
    
    
    
    private void Add() {
        Match('+');
        Term();
        operationsList.add(new AddPtg());
    }
    
    
    
    private void Subtract() {
        Match('-');
        Term();
        operationsList.add(new SubtractPtg());
    }
    
    
    
    private void Expression() {
        if (IsAddop(Look)) {
        } else {
            Term();
        }
        while (IsAddop(Look)) {
            EmitLn("MOVE D0,-(SP)");
            if ( Look == '+' )  Add();
            if (Look == '-') Subtract();
        }
    }
    
    
    /**
procedure Assignment;
var Name: string[8];
begin
   Name := GetName;
   Match('=');
   Expression;
   EmitLn('LEA ' + Name + '(PC),A0');
   EmitLn('MOVE D0,(A0)')
end;
     **/
    
    
    private void  Init() {
        GetChar();
        SkipWhite();
    }
    
    public void parse() {
        Init();
        Expression();
        tokenToRPN();
    }
    
    private void tokenToRPN() {
        OperationPtg op;
        Ptg operand;
        int numOper = 0;
        int numOnStack = 0;
        result.add(operandsList.pop()); numOnStack++;
        
        while (!operationsList.isEmpty()) {
            op = (OperationPtg) operationsList.pop();
            if (op instanceof ParenthesisPtg) {
            }
            
            
            for (numOper = op.getNumberOfOperands();numOper>0;numOper--) {
                if (numOnStack==0) {
                } else {
                    numOnStack--;
                }
            }
            result.add(op);
            numOnStack++;
        }
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
           for (int i=0;i<result.size();i++) {
            buf.append( ( (Ptg)result.get(i)).toFormulaString());
            buf.append(' ');
        } 
        return buf.toString();
    }
    
    
    public static void main(String[] argv) {
        FormulaParser fp = new FormulaParser(argv[0]+";");
        fp.parse();
        System.out.println(fp.toString());
        
    }
    
} 
