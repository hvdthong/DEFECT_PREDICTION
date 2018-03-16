package org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.attributes;

import java.io.*;

import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPool;
import org.apache.tools.ant.taskdefs.optional.depend.constantpool.Utf8CPInfo;

/**
 * line number table structure
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class LineNumberTable extends AttributeInfo {
	
	protected int count;
	
	protected int[] varTable;
	
	public LineNumberTable(int attr_index, ConstantPool pool){
		super(attr_index, pool);
	}
	
	protected void read(DataInputStream dis) throws IOException {
		int len = dis.readInt();
		count = dis.readShort();
		varTable = new int[ count * 2 ];
		for (int i = 0; i < varTable.length; i++){
			varTable[i] = dis.readShort();
		}
	}
	
	public int getStartPC(int i){
		return varTable[2*i];
	}
	
	public int getLineNumber(int i){
		return varTable[2*i + 1];
	}
	
	public int length(){
		return count;
	}
	
}

