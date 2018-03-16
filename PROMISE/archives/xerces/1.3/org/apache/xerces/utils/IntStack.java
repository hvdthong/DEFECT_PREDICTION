package org.apache.xerces.utils;

/**
 * A simple integer based stack.
 *
 * @author  Andy Clark, IBM
 *
 * @version $Id: IntStack.java 316817 2001-01-25 07:19:01Z andyc $
 */
public final class IntStack {


    /** Stack depth. */
    private int fDepth;

    /** Stack data. */
    private int[] fData;


    /** Returns the size of the stack. */
    public int size() {
        return fDepth;
    }

    /** Pushes a value onto the stack. */
    public void push(int value) {
        ensureCapacity(fDepth + 1);
        fData[fDepth++] = value;
    }

    /** Peeks at the top of the stack. */
    public int peek() {
        return fData[fDepth - 1];
    }

    /** Pops a value off of the stack. */
    public int pop() {
        return fData[--fDepth];
    }

    /** Clears the stack. */
    public void clear() {
        fDepth = 0;
    }


    /** Prints the stack. */
    public void print() {
        System.out.print('(');
        System.out.print(fDepth);
        System.out.print(") {");
        for (int i = 0; i < fDepth; i++) {
            if (i == 3) {
                System.out.print(" ...");
                break;
            }
            System.out.print(' ');
            System.out.print(fData[i]);
            if (i < fDepth - 1) {
                System.out.print(',');
            }
        }
        System.out.print(" }");
        System.out.println();
    }


    /** Ensures capacity. */
    private boolean ensureCapacity(int size) {
        try {
            return fData[size] != 0;
        }
        catch (NullPointerException e) {
            fData = new int[32];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            int[] newdata = new int[fData.length * 2];
            System.arraycopy(fData, 0, newdata, 0, fData.length);
            fData = newdata;
        }
        return true;
    }

