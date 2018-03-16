package org.apache.tools.ant.taskdefs.optional.sitraka.bytecode.attributes;

/**
 * Attribute info structure that provides base methods
 *
 */
public interface AttributeInfo {

    String SOURCE_FILE = "SourceFile";

    String CONSTANT_VALUE = "ConstantValue";

    String CODE = "Code";

    String EXCEPTIONS = "Exceptions";

    String LINE_NUMBER_TABLE = "LineNumberTable";

    String LOCAL_VARIABLE_TABLE = "LocalVariableTable";

    String INNER_CLASSES = "InnerClasses";

    String SOURCE_DIR = "SourceDir";

    String SYNTHETIC = "Synthetic";

    String DEPRECATED = "Deprecated";

    String UNKNOWN = "Unknown";

}
