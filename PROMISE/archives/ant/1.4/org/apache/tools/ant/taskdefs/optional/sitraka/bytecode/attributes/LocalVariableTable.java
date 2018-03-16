package org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.attributes;

import java.io.*;

import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPool;
import org.apache.tools.ant.taskdefs.optional.depend.constantpool.Utf8CPInfo;

/**
 * local variable table.
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class LocalVariableTable extends AttributeInfo {
	
	protected int count;
	
	protected int[] varTable;
	
	public LocalVariableTable(int attr_index, ConstantPool pool){
		super(attr_index, pool);
	}
	
	protected void read(DataInputStream dis) throws IOException {
		int len = dis.readInt();
		count = dis.readShort();
		varTable = new int[count * 5];
		for (int i = 0; i < varTable.length; i++){
			varTable[i] = dis.readShort();
		}
	}
	
	public int getStartPC(int index){
		return varTable[5*index];
	}
	public int getEndPC(int index){
		return varTable[5*index] + varTable[5*index + 1];
	}
	public String getVariableName(int index){
		int i = varTable[5*index + 2];
		return ((Utf8CPInfo)constantPool.getEntry(i)).getValue();
	}
	public String getType(int index){
		int i = varTable[5*index + 3];
		return ((Utf8CPInfo)constantPool.getEntry(i)).getValue();
	}
	public int getSlot(int index){
		return varTable[5*index + 4];
	}
	public int length(){
		return count;
	}
}

