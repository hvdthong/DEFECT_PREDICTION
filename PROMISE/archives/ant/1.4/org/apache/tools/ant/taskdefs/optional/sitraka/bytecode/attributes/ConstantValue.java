package org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.attributes;

import java.io.*;

import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPool;
import org.apache.tools.ant.taskdefs.optional.depend.constantpool.Utf8CPInfo;

/**
 * constant value structure
 *
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class ConstantValue extends AttributeInfo {
	
	protected int name_index;
	
	public ConstantValue(int attr_index, ConstantPool pool){
		super(attr_index, pool);
	}
	protected void read(DataInputStream dis) throws IOException {
		if (len != 2){
			throw new IllegalStateException("Constant value length should be 2 but was " + len);
		}
		name_index = dis.readShort();
	}
	
	public String getValue(){
		return ((Utf8CPInfo)constantPool.getEntry(name_index)).getValue();
	}
}
