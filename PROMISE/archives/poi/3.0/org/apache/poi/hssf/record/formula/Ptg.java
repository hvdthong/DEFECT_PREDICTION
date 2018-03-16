   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at


   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.hssf.record.formula;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

import org.apache.poi.hssf.model.Workbook;
import org.apache.poi.hssf.record.RecordInputStream;

/**
 *
 * @author  andy
 * @author avik
 * @author Jason Height (jheight at chariot dot net dot au)
 */

public abstract class Ptg
{

        
    /* convert infix order ptg list to rpn order ptg list
     * @return List ptgs in RPN order
     * @param infixPtgs List of ptgs in infix order
     */
    
    /* DO NOT REMOVE
     *we keep this method in case we wish to change the way we parse
     *It needs a getPrecedence in OperationsPtg
    
    public static List ptgsToRpn(List infixPtgs) {
        java.util.Stack operands = new java.util.Stack();
        java.util.List retval = new java.util.Stack();
        
        java.util.ListIterator i = infixPtgs.listIterator();
        Object p;
        OperationPtg o ;
        boolean weHaveABracket = false;
        while (i.hasNext()) {
            p=i.next();
            if (p instanceof OperationPtg) {
                if (p instanceof ParenthesisPtg) {
                    if (!weHaveABracket) {
                        operands.push(p);
                        weHaveABracket = true;
                    } else {
                        o = (OperationPtg) operands.pop();
                        while (!(o instanceof ParenthesisPtg)) { 
                            retval.add(o);
                        }
                        weHaveABracket = false;
                    }
                } else {
                    
                        retval.add(operands.pop());
                    }
                    operands.push(p);
                }
            } else {
                retval.add(p);
            }
        }
        while (!operands.isEmpty()) {
            if (operands.peek() instanceof ParenthesisPtg ){
            } else {
                retval.add(operands.pop());
            }   
        }
        return retval;
    }
    */

    public static Stack createParsedExpressionTokens(short size,  RecordInputStream in )
    {
        Stack stack = new Stack();
        int pos = 0;
        List arrayPtgs = null;
        while ( pos < size )
        {
            Ptg ptg = Ptg.createPtg( in );
            if (ptg instanceof ArrayPtg) {
            	if (arrayPtgs == null)
            		arrayPtgs = new ArrayList(5);
            	arrayPtgs.add(ptg);
            	pos += 8;
            } else pos += ptg.getSize();
            stack.push( ptg );
        }
        if (arrayPtgs != null) {
        	for (int i=0;i<arrayPtgs.size();i++) {
        		ArrayPtg p = (ArrayPtg)arrayPtgs.get(i);
        		p.readTokenValues(in);
        	}
        }
        return stack;
    }
    
    public static Ptg createPtg(RecordInputStream in)
    {
        byte id     = in.readByte();
        Ptg  retval = null;

        switch (id)
        {
                 retval = new ExpPtg(in);
                 break;
 
                 retval = new AddPtg(in);
                 break;
       	  
                 retval = new SubtractPtg(in);
                 break;
      	  
                 retval = new MultiplyPtg(in);
                 break;
        	  
        	                  retval = new DividePtg(in);
        	                  break;
        	  
                 retval = new PowerPtg(in);
                 break;
       	  
                 retval = new ConcatPtg(in);
        	                  break;
 
                 retval = new LessThanPtg(in);
        	                  break;
 
                 retval = new LessEqualPtg(in);
        	                  break;
 
                 retval = new EqualPtg(in);
        	                  break;
        	  
                 retval = new GreaterEqualPtg(in);
        	                  break;
        	  
                 retval = new GreaterThanPtg(in);
        	                  break;
 
                 retval = new NotEqualPtg(in);
        	                  break;
 
                 retval = new IntersectionPtg(in);
        	                  break;
                 retval = new UnionPtg(in);
        	                  break;
        	  
                 retval = new RangePtg(in);
        	                  break;
        	  
                 retval = new UnaryPlusPtg(in);
        	                  break;
        	  
                 retval = new UnaryMinusPtg(in);
        	                  break;
        	  
                 retval = new PercentPtg(in);
        	                  break;
        	  
                 retval = new ParenthesisPtg(in);
        	                  break;
 
                 retval = new MissingArgPtg(in);
        	                  break;
 
                retval = new StringPtg(in);
                break;
 
                 retval = new AttrPtg(in);
        	                  break;
        	  
                 retval = new ErrPtg(in);
        	                  break;
 
                retval = new BoolPtg(in);
                break;
 
                 retval = new IntPtg(in);
        	                  break;
 
        	                 retval = new NumberPtg(in);
        	                 break;
        	  
             	retval = new ArrayPtg(in);
             	break;
             	retval = new ArrayPtgV(in);
             	break;
             	retval = new ArrayPtgA(in);
             	break;
        	  
                 retval = new FuncPtg(in);
                 break;
        	  
                 retval = new FuncVarPtg(in);
        	                  break;
        	  
                 retval = new ReferencePtg(in);
        	                  break;
                 retval = new RefAPtg(in);
                 break;   
                 retval = new RefVPtg(in);
                 break;   
                 retval = new RefNAPtg(in);
                 break;
                 retval = new RefNPtg(in);
                 break;
                 retval = new RefNVPtg(in);
                 break;           	                  
        	  
                 retval = new AreaPtg(in);
        	                  break;
                 retval = new AreaVPtg(in);
                 break;
                 retval = new AreaAPtg(in);
                 break;
                 retval = new AreaNAPtg(in);
                  break;
                 retval = new AreaNPtg(in);
                 break;
                retval = new AreaNVPtg(in);
                break;
        	  
                 retval = new MemAreaPtg(in);
                 break;
        	  
                 retval = new MemErrPtg(in);
        	                  break;
        	  
                 retval = new MemFuncPtg(in);
                 break;
        	  
                 retval = new RefErrorPtg(in);
        	                  break;
        	  
                 retval = new AreaErrPtg(in);
        	                  break;
        	  
                 retval = new NamePtg(in);
                 break;
        	  
                 retval = new NameXPtg(in);
        	                  break;
 
                 retval = new Area3DPtg(in);
        	                  break;
 
                 retval = new Ref3DPtg(in);
        	                  break;
 
                 retval = new DeletedRef3DPtg(in);
        	                  break;
        	  
                 retval = new DeletedArea3DPtg(in);
                 break;

            default :

                 throw new java.lang.UnsupportedOperationException(" Unknown Ptg in Formula: 0x"+
                        Integer.toHexString(( int ) id) + " (" + ( int ) id + ")");
        }
        
        if (id > 0x60) {
            retval.setClass(CLASS_ARRAY);
        } else if (id > 0x40) {
            retval.setClass(CLASS_VALUE);
        } else {
            retval.setClass(CLASS_REF);
        }

       return retval;
        
    }
    
    public static int serializePtgStack(Stack expression, byte[] array, int offset) {
    	int pos = 0;
    	int size = 0;
    	if (expression != null)
    		size = expression.size();

    	List arrayPtgs = null;
    	
    	for (int k = 0; k < size; k++) {
    		Ptg ptg = ( Ptg ) expression.get(k);
    		
    		ptg.writeBytes(array, pos + offset);
    		if (ptg instanceof ArrayPtg) {
    		  if (arrayPtgs == null)
    			  arrayPtgs = new ArrayList(5);
    		  arrayPtgs.add(ptg);
    		  pos += 8;
    		} else pos += ptg.getSize();
    	}
    	if (arrayPtgs != null) {
    		for (int i=0;i<arrayPtgs.size();i++) {
    			ArrayPtg p = (ArrayPtg)arrayPtgs.get(i);
    			pos += p.writeTokenValueBytes(array, pos + offset);
    		}
    	}
    	return pos;
    }

    public abstract int getSize();

    public final byte [] getBytes()
    {
        int    size  = getSize();
        byte[] bytes = new byte[ size ];

        writeBytes(bytes, 0);
        return bytes;
    }
    /** write this Ptg to a byte array*/
    public abstract void writeBytes(byte [] array, int offset);
    
    /**
     * return a string representation of this token alone
     */
    public abstract String toFormulaString(Workbook book);
    /**
     * dump a debug representation (hexdump) to a string
     */
    public String toDebugString() {
        byte[] ba = new byte[getSize()];
        String retval=null;
        writeBytes(ba,0);        
        try {
            retval = org.apache.poi.util.HexDump.dump(ba,0,0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval;
    }
    
    /** Overridden toString method to ensure object hash is not printed.
     * This helps get rid of gratuitous diffs when comparing two dumps
     * Subclasses may output more relevant information by overriding this method
     **/
    public String toString(){
        return this.getClass().toString();
    }
    
    public static final byte CLASS_REF = 0x00;
    public static final byte CLASS_VALUE = 0x20;
    public static final byte CLASS_ARRAY = 0x40;
    
    
    public void setClass(byte thePtgClass) {
        ptgClass = thePtgClass;
    }
    
    /** returns the class (REF/VALUE/ARRAY) for this Ptg */
    public byte getPtgClass() {
        return ptgClass;
    }
    
    public abstract byte getDefaultOperandClass();

    public abstract Object clone();

    
    
}
