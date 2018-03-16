package org.apache.tools.ant.taskdefs.optional.depend;

import java.util.*;
import java.io.*;

/**
 * An iterator which iterates through the contents of a java directory.
 * 
 * The iterator should be created with the directory at the root of the
 * Java namespace.
 * 
 * @author Conor MacNeill
 */
public class DirectoryIterator implements ClassFileIterator {

    /**
     * This is a stack of current iterators supporting the depth first
     * traversal of the directory tree.
     */
    private Stack enumStack;

    /**
     * The current directory iterator. As directories encounter lower level
     * directories, the current iterator is pushed onto the iterator
     * stack and a new iterator over the sub directory becomes the current
     * directory. This implements a depth first traversal of the directory namespace.
     */
    private Enumeration currentEnum;

    /**
     * The length of the root directory. This is used to remove the root directory
     * from full paths.
     */
    int rootLength;

    /**
     * Creates a directory iterator.
     * 
     * The directory iterator is created to scan the root directory. If the
     * changeInto flag is given, then the entries returned will be relative to this
     * directory and not the current directory.
     * 
     * @param rootDirectory the root if the directory namespace which is to be iterated over
     * @param changeInto if true then the returned entries will be relative to the rootDirectory
     * and not the current directory.
     * 
     * @throws IOException if there is a problem reading the directory information.
     */
    public DirectoryIterator(File rootDirectory, boolean changeInto) throws IOException {
        super();

        enumStack = new Stack();

        if (rootDirectory.isAbsolute() || changeInto) {
            rootLength = rootDirectory.getPath().length() + 1;
        } else {
            rootLength = 0;
        } 

        Vector filesInRoot = getDirectoryEntries(rootDirectory);

        currentEnum = filesInRoot.elements();
    }

    /**
     * Get a vector covering all the entries (files and subdirectories in a directory).
     * 
     * @param directory the directory to be scanned.
     * 
     * @return a vector containing File objects for each entry in the directory.
     */
    private Vector getDirectoryEntries(File directory) {
        Vector files = new Vector();

        String[] filesInDir = directory.list();

        if (filesInDir != null) {
            int length = filesInDir.length;

            for (int i = 0; i < length; ++i) {
                files.addElement(new File(directory, filesInDir[i]));
            } 
        } 

        return files;
    } 

    /**
     * Template method to allow subclasses to supply elements for the iteration.
     * 
     * The directory iterator maintains a stack of iterators covering each level
     * in the directory hierarchy. The current iterator covers the current directory
     * being scanned. If the next entry in that directory is a subdirectory, the current
     * iterator is pushed onto the stack and a new iterator is created for the
     * subdirectory. If the entry is a file, it is returned as the next element and the
     * iterator remains valid. If there are no more entries in the current directory,
     * the topmost iterator on the statck is popped off to become the current iterator.
     * 
     * @return the next ClassFile in the iteration.
     */
    public ClassFile getNextClassFile() {
        ClassFile nextElement = null;

        try {
            while (nextElement == null) {
                if (currentEnum.hasMoreElements()) {
                    File element = (File) currentEnum.nextElement();

                    if (element.isDirectory()) {

                        enumStack.push(currentEnum);

                        Vector files = getDirectoryEntries(element);

                        currentEnum = files.elements();
                    } else {

                        FileInputStream inFileStream = new FileInputStream(element);

                        if (element.getName().endsWith(".class")) {

                            ClassFile javaClass = new ClassFile();

                            javaClass.read(inFileStream);

                            nextElement = javaClass;
                        } 
                    } 
                } else {
                    if (enumStack.empty()) {
                        break;
                    } else {
                        currentEnum = (Enumeration) enumStack.pop();
                    } 
                } 
            } 
        } catch (IOException e) {
            nextElement = null;
        } 

        return nextElement;
    } 

}

