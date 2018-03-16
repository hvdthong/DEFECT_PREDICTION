package org.apache.tools.ant.taskdefs.optional.sitraka.bytecode;

import java.io.*;

import org.apache.tools.ant.taskdefs.optional.depend.constantpool.*;

/**
 * list of interfaces implemented in a class.
 *
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class InterfaceList {
	protected ConstantPool constantPool;
	protected int[] interfaces;
	
	public InterfaceList(ConstantPool pool){
		constantPool = pool;
	}
	public void read(DataInputStream dis) throws IOException {
		int count = dis.readShort();
		interfaces = new int[count];
		for (int i = 0; i < count; i++){
			interfaces[i] = dis.readShort();
		}
	}
	public int length(){
		return interfaces.length;
	}
	public String getInterface(int i){
		int index = interfaces[i];
		ClassCPInfo cp = (ClassCPInfo)constantPool.getEntry(index);
		return cp.getClassName().replace('/','.');
	}
	public String[] getInterfaces(){
		String[] classes = new String[interfaces.length];
		for (int i = 0; i < classes.length; i++){
			classes[i] = getInterface(i);
		}
		return classes;
	}
	public String toString(){
		StringBuffer sb = new StringBuffer("Interfaces: ");
		String[] names = getInterfaces();
		for (int i = 0; i < names.length; i++){
			sb.append(names[i]);
			if (i != names.length - 1){
				sb.append(", ");
			}
		}
		return sb.toString();
	}
}
