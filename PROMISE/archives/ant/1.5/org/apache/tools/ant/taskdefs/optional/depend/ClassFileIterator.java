package org.apache.tools.ant.taskdefs.optional.depend;

/**
 * Iterator interface for iterating over a set of class files
 *
 * @author Conor MacNeill
 */
public interface ClassFileIterator {

    /**
     * Get the next class file in the iteration
     *
     * @return the next class file in the iterationr
     */
    ClassFile getNextClassFile();
}

