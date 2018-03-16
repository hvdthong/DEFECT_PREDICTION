package org.apache.tools.ant.taskdefs.optional.sitraka.bytecode;

import java.io.*;

import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPool;
import org.apache.tools.ant.taskdefs.optional.depend.constantpool.Utf8CPInfo;

import org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.attributes.*;

/**
 * field structure
 *
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class FieldInfo {
	
	protected ConstantPool constantPool;
	protected int access_flags;
	protected int name_index;
	protected int descriptor_index;
	protected AttributeInfoList attributes;
	protected boolean synthetic;
	protected boolean deprecated;
	protected boolean constant;
	
	public FieldInfo(ConstantPool pool){
		constantPool = pool;
	}
	
	public void read(DataInputStream dis) throws IOException {
		access_flags = dis.readShort();
		name_index = dis.readShort();
		descriptor_index = dis.readShort();
		attributes = new AttributeInfoList(constantPool);
		attributes.read(dis);
		constant = attributes.getAttribute(AttributeInfo.CONSTANT_VALUE) != null;
		deprecated = attributes.getAttribute(AttributeInfo.DEPRECATED) != null;
		synthetic = attributes.getAttribute(AttributeInfo.SYNTHETIC) != null;
	}
	
	public int getAccessFlags(){
		return access_flags;
	}
	
	public String getName(){
		return Utils.getUTF8Value(constantPool, name_index);
	}
	
	public String getDescriptor(){
		return Utils.getUTF8Value(constantPool, descriptor_index);
	}
	
	public boolean isDeprecated(){
		return deprecated;
	}
	
	public boolean isSynthetic(){
		return synthetic;
	}
	
	public boolean isConstant(){
		return constant;
	}

	public String getAccess(){
		return Utils.getFieldAccess(access_flags);
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Field: ");
		sb.append(getAccess()).append(" ");
		Utils.descriptor2java(getDescriptor(),0,sb);
		sb.append(" ").append(getName());
		return sb.toString();
	}
}


