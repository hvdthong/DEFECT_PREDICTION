package org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.attributes;

import java.io.*;

import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPool;

/**
 * unknown structure attribute
 *
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class Unknown extends AttributeInfo {
	public Unknown(int attr_index, ConstantPool pool){
		super(attr_index, pool);
	}
}
