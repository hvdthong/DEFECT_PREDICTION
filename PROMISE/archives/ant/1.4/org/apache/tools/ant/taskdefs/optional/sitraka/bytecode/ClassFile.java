package org.apache.tools.ant.taskdefs.optional.sitraka.bytecode;

import java.io.*;

import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPool;
import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ClassCPInfo;
import org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.attributes.*;


/**
 * Object representing a class.
 *
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class ClassFile {
	
	protected ConstantPool constantPool;
	
	protected InterfaceList interfaces;
	
	protected FieldInfoList fields;
	
	protected MethodInfoList methods;
		
	protected String sourceDir;
	
	protected String sourceFile;
	
	protected int access_flags;
	
	protected int this_class;
	
	protected int super_class;
	
	protected boolean isSynthetic;
	
	protected boolean isDeprecated;
	
	public ClassFile(InputStream is) throws IOException {
		DataInputStream dis = new DataInputStream(is);
		constantPool = new ConstantPool();
			
		int minor = dis.readShort();
		int major = dis.readShort();
				
		constantPool.read(dis);
		constantPool.resolve();
		
		access_flags = dis.readShort();
		this_class = dis.readShort();
		super_class = dis.readShort();
		
		interfaces = new InterfaceList(constantPool);
		interfaces.read(dis);
		
		fields = new FieldInfoList(constantPool);
		fields.read(dis);
		
		methods = new MethodInfoList(constantPool);
		methods.read(dis);
		
		AttributeInfoList attributes = new AttributeInfoList(constantPool);
		attributes.read(dis);
		SourceFile srcFile = (SourceFile)attributes.getAttribute(AttributeInfo.SOURCE_FILE);
		if (srcFile != null){
			sourceFile = srcFile.getValue();
		}
		SourceDir srcDir = (SourceDir)attributes.getAttribute(AttributeInfo.SOURCE_DIR);
		if (srcDir != null){
			sourceDir = srcDir.getValue();
		}
		isSynthetic = attributes.getAttribute(AttributeInfo.SYNTHETIC) != null;
		isDeprecated = attributes.getAttribute(AttributeInfo.DEPRECATED) != null;
	}
	
	public int getAccess(){
		return access_flags;
	}
	public InterfaceList getInterfaces(){
		return interfaces;
	}
	public String getSourceFile(){
		return sourceFile;
	}
	public String getSourceDir(){
		return sourceDir;
	}
	public boolean isSynthetic() {
		return isSynthetic;
	}
	public boolean isDeprecated() {
		return isDeprecated;
	}
	public MethodInfoList getMethods(){
		return methods;
	}
	public FieldInfoList getFields(){
		return fields;
	}
	public String getSuperName(){
		return Utils.getUTF8Value(constantPool, super_class);
	}
	public String getFullName(){
		return ((ClassCPInfo)constantPool.getEntry(this_class)).getClassName().replace('/','.');
	}
	public String getName(){
		String name = getFullName();
		int pos = name.lastIndexOf('.');
		if (pos == -1){
			return "";
		}
		return name.substring(pos + 1);
	}
	public String getPackage(){
		String name = getFullName();
		int pos = name.lastIndexOf('.');
		if (pos == -1){
			return "";
		}
		return name.substring(0, pos);
	}
	
	/** dirty test method, move it into a testcase */
	public static void main(String[] args) throws Exception {
		System.out.println("loading classfile...");
		InputStream is = ClassLoader.getSystemResourceAsStream("java/util/Vector.class");
		ClassFile clazzfile = new ClassFile(is);
		System.out.println("Class name: " + clazzfile.getName());
		MethodInfoList methods = clazzfile.getMethods();
		for (int i = 0; i < methods.length(); i++){
			MethodInfo method = methods.getMethod(i);
			System.out.println("Method: " + method.getFullSignature());
			System.out.println("line: " +  method.getNumberOfLines());
			LineNumberTable lnt = method.getCode().getLineNumberTable();
		}
	}
	
}


 


