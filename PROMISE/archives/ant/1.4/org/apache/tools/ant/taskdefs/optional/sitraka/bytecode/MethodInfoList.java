package org.apache.tools.ant.taskdefs.optional.sitraka.bytecode;

import java.io.*;

import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPool;
import org.apache.tools.ant.taskdefs.optional.depend.constantpool.Utf8CPInfo;

/**
 * A list of method_info structures.
 *
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class MethodInfoList {
	/** pool containing all the information */
	protected ConstantPool constantPool;
	
	/** methods in this list */
	protected MethodInfo[] methods;
	
	
	public MethodInfoList(ConstantPool pool){
		constantPool = pool;
	}
	
	/** read the bytecode from the stream */
	public void read(DataInputStream dis) throws IOException {
		int count = dis.readShort();
		methods = new MethodInfo[count];
		for (int i = 0; i < count; i++){
			methods[i] = new MethodInfo(constantPool);
			methods[i].read(dis);
		}
	}
	
	/** the size of the list */
	public int length(){
		return methods.length;
	}
	
	/**
	 * get a method in the list.
	 * @param i the index of the method to retrieve
	 * @return the method matching the index.
	 */
	public MethodInfo getMethod(int i){
		return methods[i];
	}
	
	/**
	 * return the set of methods in this list. Mostly as a debugging purpose.
	 */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Methods: ").append(methods.length).append("\n");
		for (int i = 0; i < methods.length; i++){
			sb.append("\t");
			sb.append(getMethod(i).toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	
}



