package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.*;

final class SlotAllocator {

    private int   _firstAvailableSlot;
    private int   _size = 8;
    private int   _free = 0;
    private int[] _slotsTaken = new int[_size];
    
    public void initialize(LocalVariableGen[] vars) {
	final int length = vars.length;
	int slot = 0, size, index;

	for (int i = 0; i < length; i++) {
	    size  = vars[i].getType().getSize();
	    index = vars[i].getIndex();
	    slot  = Math.max(slot, index + size);
	}
	_firstAvailableSlot = slot;
    }

    public int allocateSlot(Type type) {
	final int size = type.getSize();
	final int limit = _free;
	int slot = _firstAvailableSlot, where = 0;

	if (_free + size > _size) {
	    final int[] array = new int[_size *= 2];
	    for (int j = 0; j < limit; j++)
		array[j] = _slotsTaken[j];
	    _slotsTaken = array;
	}

	while (where < limit) {
	    if (slot + size <= _slotsTaken[where]) {
		for (int j = limit - 1; j >= where; j--)
		    _slotsTaken[j + size] = _slotsTaken[j];
		break;
	    }
	    else {
		slot = _slotsTaken[where++] + 1;
	    }
	}
	
	for (int j = 0; j < size; j++)
	    _slotsTaken[where + j] = slot + j;
	
	_free += size;
	return slot;
    }

    public void releaseSlot(LocalVariableGen lvg) {
	final int size = lvg.getType().getSize();
	final int slot = lvg.getIndex();
	final int limit = _free;
	
	for (int i = 0; i < limit; i++) {
	    if (_slotsTaken[i] == slot) {
		int j = i + size;
		while (j < limit) {
		    _slotsTaken[i++] = _slotsTaken[j++];
		}
		_free -= size;
		return;
	    }
	}
	String state = "Variable slot allocation error"+
	               "(size="+size+", slot="+slot+", limit="+limit+")";
	ErrorMsg err = new ErrorMsg(ErrorMsg.INTERNAL_ERR, state);
	throw new Error(err.toString());
    }
}
