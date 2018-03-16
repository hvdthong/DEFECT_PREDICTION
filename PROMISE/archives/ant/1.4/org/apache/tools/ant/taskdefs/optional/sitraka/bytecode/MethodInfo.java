package org.apache.tools.ant.taskdefs.optional.sitraka.bytecode;

import java.io.*;

import org.apache.tools.ant.taskdefs.optional.depend.constantpool.*;
import org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.attributes.*;

/**
 * Method info structure.
 * @todo give a more appropriate name to methods.
 *
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class MethodInfo {
	protected ConstantPool constantPool;
	protected int access_flags;
	protected int name_index;
	protected int descriptor_index;
	protected Code code;
	protected boolean deprecated;
	protected boolean synthetic;
	protected Exceptions exceptions;
	public MethodInfo(ConstantPool pool){
		constantPool = pool;
	}
	
	public void read(DataInputStream dis) throws IOException {
		access_flags = dis.readShort();
		name_index = dis.readShort();
		descriptor_index = dis.readShort();
		AttributeInfoList attrlist = new AttributeInfoList(constantPool);
		attrlist.read(dis);
		code = (Code)attrlist.getAttribute(AttributeInfo.CODE);
		synthetic = attrlist.getAttribute(AttributeInfo.SYNTHETIC) != null;
		deprecated = attrlist.getAttribute(AttributeInfo.DEPRECATED) != null;
		exceptions = (Exceptions)attrlist.getAttribute(AttributeInfo.EXCEPTIONS);
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
	
	public String getFullSignature(){
		return getReturnType() + " " + getShortSignature();
	}
	
	public String getShortSignature(){
		StringBuffer buf = new StringBuffer(getName());
		buf.append("(");
		String[] params = getParametersType();
		for (int i = 0; i < params.length; i++){
			buf.append(params[i]);
			if (i != params.length - 1){
				buf.append(", ");
			}
		}
		buf.append(")");
		return buf.toString();
	}
	
	public String getReturnType(){
		return Utils.getMethodReturnType(getDescriptor());
	}
	
	public String[] getParametersType(){
		return Utils.getMethodParams(getDescriptor());
	}
	
	public Code getCode(){
		return code;
	}
	
	public int getNumberOfLines(){
		int len = -1;
		if (code != null){
			LineNumberTable lnt = code.getLineNumberTable();
			if (lnt != null){
				len = lnt.length();
			}
		}
		return len;
	}
	
	public boolean isDeprecated(){
		return deprecated;
	}
	
	public boolean isSynthetic(){
		return synthetic;
	}
    
	public String getAccess(){
		return Utils.getMethodAccess(access_flags);
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Method: ").append(getAccess()).append(" ");
		sb.append(getFullSignature());
		sb.append(" synthetic:").append(synthetic);
		sb.append(" deprecated:").append(deprecated);
		return sb.toString();
	}
}


