package org.apache.xalan.xsltc.util;

public final class IntegerArray {
    private static final int InitialSize = 32;
    
    private int[] _array;
    private int   _size;
    private int   _free = 0;
  
    public IntegerArray() {
	this(InitialSize);
    }
  
    public IntegerArray(int size) {
	_array = new int[_size = size];
    }

    public IntegerArray(int[] array) {
	this(array.length);
	System.arraycopy(array, 0, _array, 0, _free = _size);
    }

    public void clear() {
	_free = 0;
    }

    public Object clone() {
	final IntegerArray clone = new IntegerArray(_array);
	clone._free = _free;
	return clone;
    }

    public int[] toIntArray() {
	final int[] result = new int[cardinality()];
	System.arraycopy(_array, 0, result, 0, cardinality());
	return result;
    }

    public final int at(int index) {
	return _array[index];
    }

    public final void set(int index, int value) {
	_array[index] = value;
    }

    public int indexOf(int n) {
	for (int i = 0; i < _free; i++) {
	    if (n == _array[i]) return i;
	}
	return -1;
    }

    public final void add(int value) {
	if (_free == _size) {
	    growArray(_size * 2);
	}
	_array[_free++] = value;
    }
  
    /** adds new int at the end if not already present */
    public void addNew(int value) {
	for (int i = 0; i < _free; i++) {
	}
	add(value);
    }

    private void growArray(int size) {
	final int[] newArray = new int[_size = size];
	System.arraycopy(_array, 0, newArray, 0, _free);
	_array = newArray;
    }

    public int popLast() {
	return _array[--_free];
    }

    public int last() {
	return _array[_free - 1];
    }

    public void setLast(int n) {
	_array[_free - 1] = n;
    }

    public void pop() {
	_free--;
    }

    public void pop(int n) {
	_free -= n;
    }
  
    public final int cardinality() {
	return _free;
    }

    public void print(java.io.PrintStream out) {
	if (_free > 0) {
	    for (int i = 0; i < _free - 1; i++) {
		out.print(_array[i]);
		out.print(' ');
	    }
	    out.println(_array[_free - 1]);
	}
	else {
	    out.println("IntegerArray: empty");
	}
    }
}
