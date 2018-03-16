package org.apache.tools.ant.taskdefs.optional.sitraka.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ClassCPInfo;
import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPool;
import org.apache.tools.ant.taskdefs.optional.depend.constantpool.Utf8CPInfo;
import org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.attributes.AttributeInfo;


/**
 * Object representing a class.
 *
 * Information are kept to the strict minimum for JProbe reports so
 * that not too many objects are created for a class, otherwise the
 * JVM can quickly run out of memory when analyzing a great deal of
 * classes and keeping them in memory for global analysis.
 *
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
public final class ClassFile {

    private MethodInfo[] methods;

    private String sourceFile;

    private String fullname;

    private int access_flags;

    public ClassFile(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        ConstantPool constantPool = new ConstantPool();

        int minor = dis.readShort();
        int major = dis.readShort();

        constantPool.read(dis);
        constantPool.resolve();

        access_flags = dis.readShort();
        int this_class = dis.readShort();
        fullname = ((ClassCPInfo) constantPool.getEntry(this_class)).getClassName().replace('/', '.');
        int super_class = dis.readShort();

        int count = dis.readShort();

        int numFields = dis.readShort();
        for (int i = 0; i < numFields; i++) {
            dis.skip(2 * 3);
            int attributes_count = dis.readUnsignedShort();
            for (int j = 0; j < attributes_count; j++) {
                int len = dis.readInt();
                dis.skipBytes(len);
            }
        }

        int method_count = dis.readShort();
        methods = new MethodInfo[method_count];
        for (int i = 0; i < method_count; i++) {
            methods[i] = new MethodInfo();
            methods[i].read(constantPool, dis);
        }

        int attributes_count = dis.readUnsignedShort();
        for (int j = 0; j < attributes_count; j++) {
            int attr_id = dis.readShort();
            int len = dis.readInt();
            String attr_name = Utils.getUTF8Value(constantPool, attr_id);
            if (AttributeInfo.SOURCE_FILE.equals(attr_name)) {
                int name_index = dis.readShort();
                sourceFile = ((Utf8CPInfo) constantPool.getEntry(name_index)).getValue();
            } else {
                dis.skipBytes(len);
            }
        }
    }

    public int getAccess() {
        return access_flags;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public MethodInfo[] getMethods() {
        return methods;
    }

    public String getFullName() {
        return fullname;
    }

    public String getName() {
        String name = getFullName();
        int pos = name.lastIndexOf('.');
        if (pos == -1) {
            return "";
        }
        return name.substring(pos + 1);
    }

    public String getPackage() {
        String name = getFullName();
        int pos = name.lastIndexOf('.');
        if (pos == -1) {
            return "";
        }
        return name.substring(0, pos);
    }

}





