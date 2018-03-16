package org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.attributes;

import java.io.*;

import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPool;
import org.apache.tools.ant.taskdefs.optional.depend.constantpool.Utf8CPInfo;

/**
 * exceptions structure
 *
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class Exceptions extends AttributeInfo {
	public Exceptions(int attr_index, ConstantPool pool){
		super(attr_index, pool);
	}
}
