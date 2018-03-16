package org.apache.tools.ant.taskdefs.optional.sitraka.bytecode;

import java.io.*;

import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPool;
import org.apache.tools.ant.taskdefs.optional.depend.constantpool.Utf8CPInfo;

/**
 * field structure list
 *
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class FieldInfoList {
	
	protected ConstantPool constantPool;
	
	protected FieldInfo[] fields;
	
	public FieldInfoList(ConstantPool pool){
		constantPool = pool;
	}
	
	public void read(DataInputStream dis) throws IOException {
		int numFields = dis.readShort();
		fields = new FieldInfo[numFields];
		for (int i = 0; i < fields.length; i++){
			fields[i] = new FieldInfo(constantPool);
			fields[i].read(dis);
		}
	}
	
	public String getFieldName(int index){
		return getField(index).getName();
	}
	
	public FieldInfo getField(int index){
		return fields[index];
	}
	
	public int length(){
		return fields.length;
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Fields: ").append(fields.length).append("\n");
		for (int i = 0; i < fields.length; i++){
			sb.append("\t");
			sb.append(getField(i).toString());
		}
		return sb.toString();
	}
	
}
