package org.apache.tools.ant.taskdefs.optional.depend;

/**
 * Iterator interface for iterating over a set of class files
 *
 */
public interface ClassFileIterator {

    /**
     * Get the next class file in the iteration
     *
     * @return the next class file in the iteration
     */
    ClassFile getNextClassFile();
}

