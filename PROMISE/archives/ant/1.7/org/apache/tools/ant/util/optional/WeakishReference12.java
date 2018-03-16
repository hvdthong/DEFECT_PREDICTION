package org.apache.tools.ant.util.optional;

import org.apache.tools.ant.util.WeakishReference;

/**
 * This is a reference that really is is Weak, as it uses the
 * appropriate java.lang.ref class.
 * @deprecated since 1.7.
 *             Just use {@link java.lang.ref.WeakReference} directly.
 * Note that in ant1.7 is parent was changed to extend HardReference.
 * This is because the latter has access to the (package scoped)
 * WeakishReference(Object) constructor, and both that and this are thin
 * facades on the underlying no-longer-abstract base class.
 */
public class WeakishReference12 extends WeakishReference.HardReference  {


    /**
     * create a new soft reference, which is bound to a
     * Weak reference inside
     * @param reference the object to reference.
     * @see java.lang.ref.WeakReference
     */
    public WeakishReference12(Object reference) {
        super(reference);
    }
}
