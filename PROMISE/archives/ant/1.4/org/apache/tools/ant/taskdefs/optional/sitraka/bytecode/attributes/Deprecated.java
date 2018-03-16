package org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.attributes;

import java.io.*;

import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPool;
import org.apache.tools.ant.taskdefs.optional.depend.constantpool.Utf8CPInfo;

/**
 * deprecated structure
 *
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class Deprecated extends AttributeInfo {
	public Deprecated(int attr_index, ConstantPool pool){
		super(attr_index, pool);
	}
}
