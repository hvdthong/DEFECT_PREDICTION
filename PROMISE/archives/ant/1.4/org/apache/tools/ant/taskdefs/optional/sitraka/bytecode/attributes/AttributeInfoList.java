package org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.attributes;

import java.io.*;

import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPool;
import org.apache.tools.ant.taskdefs.optional.depend.constantpool.Utf8CPInfo;

/**
 * list of attributes
 *
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class AttributeInfoList {
	
	protected AttributeInfo[] attributes;
	
	protected ConstantPool constantPool;
	
	public AttributeInfoList(ConstantPool pool){
		constantPool = pool;
	}
	
	public void read(DataInputStream dis) throws IOException {
		final int attributes_count = dis.readUnsignedShort();
		attributes = new AttributeInfo[attributes_count];
		for (int i = 0; i < attributes_count; i++){
			int attr_id = dis.readShort();
			attributes[i] = AttributeInfo.newAttribute(attr_id, dis, constantPool);
		}
	}
	
	public AttributeInfo[] getAttributes(){
		return attributes;
	}
	
	public AttributeInfo getAttribute(String name){
		for (int i = 0; i < attributes.length; i++){
			if (name.equals(attributes[i].getName())){
				return attributes[i];
			}
		}
		return null;
	}
	
	public int size(){
		return attributes.length;
	}
	
}
