package org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.attributes;

import java.io.*;

import org.apache.tools.ant.taskdefs.optional.depend.constantpool.*;
import org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.*;

/**
 * Attribute info structure that provides base methods
 *
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
public abstract class AttributeInfo {

	public final static String SOURCE_FILE = "SourceFile";

	public final static String CONSTANT_VALUE = "ConstantValue";

	public final static String CODE = "Code";

	public final static String EXCEPTIONS = "Exceptions";

	public final static String LINE_NUMBER_TABLE = "LineNumberTable";

	public final static String LOCAL_VARIABLE_TABLE = "LocalVariableTable";

	public final static String INNER_CLASSES = "InnerClasses";

	public final static String SOURCE_DIR = "SourceDir";

	public final static String SYNTHETIC = "Synthetic";

	public final static String DEPRECATED = "Deprecated";

	public final static String UNKNOWN = "Unknown";
	
	protected int name_index;
	
	protected ConstantPool constantPool;
	
	protected AttributeInfo(int attr_index, ConstantPool pool){
		name_index = attr_index;
		constantPool = pool;
	}
	
	/**
	 * @param dis
	 * @throws IOException
	 */
	protected void read(DataInputStream dis) throws IOException {
		int len = dis.readInt();
		dis.skipBytes(len);
	}
	
	public String getName(){
		return Utils.getUTF8Value(constantPool, name_index);
	}
	
	/**
	 * @param attr_index
	 * @param dis
	 * @param pool
	 */
	public static AttributeInfo newAttribute(int attr_index, DataInputStream dis, ConstantPool pool) throws IOException {
		AttributeInfo attr = null;
		final String name = Utils.getUTF8Value(pool, attr_index);
		if (SOURCE_FILE.equals(name)){
			attr = new SourceFile(attr_index, pool);
		} else if (CONSTANT_VALUE.equals(name)){
			attr = new ConstantValue(attr_index, pool);
		} else if (CODE.equals(name)){
			attr = new Code(attr_index, pool);
		} else if (EXCEPTIONS.equals(name)){
			attr = new Exceptions(attr_index, pool);
		} else if (LINE_NUMBER_TABLE.equals(name)){
			attr = new LineNumberTable(attr_index, pool);
		} else if (LOCAL_VARIABLE_TABLE.equals(name)){
			attr = new LocalVariableTable(attr_index, pool);
		} else if (INNER_CLASSES.equals(name)){
			attr = new InnerClasses(attr_index, pool);
		} else if (SOURCE_DIR.equals(name)){
			attr = new SourceDir(attr_index, pool);
		} else if (SYNTHETIC.equals(name)){
			attr = new Synthetic(attr_index, pool);
		} else if (DEPRECATED.equals(name)){
			attr = new Deprecated(attr_index, pool);
		} else {
			attr = new Unknown(attr_index, pool);
		}
		attr.read(dis);
		return attr;
	}
}
