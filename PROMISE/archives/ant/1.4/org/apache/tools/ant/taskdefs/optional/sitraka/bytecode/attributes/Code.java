package org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.attributes;

import java.io.*;

import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPool;
import org.apache.tools.ant.taskdefs.optional.depend.constantpool.Utf8CPInfo;

/**
 * Code structure.
 *
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class Code extends AttributeInfo {
	
	protected int length;
	
	protected int max_stack;
	
	protected int max_locals;
	
	protected byte[] code;
	
	protected ExceptionInfo[] exceptions;
		
	protected LineNumberTable lineNumberTable;
	
	public Code(int attr_index, ConstantPool pool){
		super(attr_index, pool);
	}
	
	public void read(DataInputStream dis) throws IOException {
		length = dis.readInt();
		max_stack = dis.readShort();
		max_locals = dis.readShort();
		
		int bytecode_len = dis.readInt();
		dis.skip(bytecode_len);
		
		int exception_count = dis.readShort();
		exceptions = new ExceptionInfo[exception_count];
		for (int i = 0; i < exception_count; i++){
			exceptions[i] = new ExceptionInfo(constantPool);
			exceptions[i].read(dis);
		}
		
		AttributeInfoList attributes = new AttributeInfoList(constantPool);
		attributes.read(dis);
		lineNumberTable = (LineNumberTable)attributes.getAttribute(AttributeInfo.LINE_NUMBER_TABLE);
	}
	
	public int getMaxStack(){
		return max_stack;
	}
	
	public int getMaxLocals(){
		return max_locals;
	}
	
	public byte[] getCode(){
		return code;
	}
	
	public ExceptionInfo[] getExceptions(){
		return exceptions;
	}
	
	public LineNumberTable getLineNumberTable(){
		return lineNumberTable;
	}
	
	public static class ExceptionInfo {
		protected ConstantPool constantPool;
		protected int startPC;
		protected int endPC;
		protected int handlerPC;
		protected int catchType;
		public ExceptionInfo(ConstantPool pool){
			constantPool = pool;
		}
		public void read(DataInputStream dis) throws IOException {
			startPC = dis.readShort();
			endPC = dis.readShort();
			handlerPC = dis.readShort();
			catchType = dis.readShort();
		}
		public int getStartPC(){
			return startPC;
		}
		public int getEndPC(){
			return endPC;
		}
		public int getHandlerPC(){
			return handlerPC;
		}
		public int getCatchType(){
			return catchType;
		}
	}
	
}


