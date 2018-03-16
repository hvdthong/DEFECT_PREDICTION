package org.apache.tools.ant.util.optional;

import org.apache.tools.ant.util.WeakishReference;

import java.lang.ref.WeakReference;

/**
 * This is a reference that really is is Weak, as it uses the
 * appropriate java.lang.ref class.
 *
 */
public class WeakishReference12 extends WeakishReference  {

    private WeakReference weakref;

    /**
     * create a new soft reference, which is bound to a
     * Weak reference inside
     * @param reference
     * @see java.lang.ref.WeakReference
     */
    public WeakishReference12(Object reference) {
        this.weakref = new WeakReference(reference);
    }

    /**
     * Returns this reference object's referent.
     *
     * @return referent.
     */
    public Object get() {
        return weakref.get();
    }

}
