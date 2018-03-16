import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.lang.ref.WeakReference;

/** Java's builtin ThreadLocal has a serious flaw:
 *  it can take an arbitrarily long amount of time to
 *  dereference the things you had stored in it, even once the
 *  ThreadLocal instance itself is no longer referenced.
 *  This is because there is single, master map stored for
 *  each thread, which all ThreadLocals share, and that
 *  master map only periodically purges "stale" entries.
 *
 *  While not technically a memory leak, because eventually
 *  the memory will be reclaimed, it can take a long time
 *  and you can easily hit OutOfMemoryError because from the
 *  GC's standpoint the stale entries are not reclaimaible.
 * 
 *  This class works around that, by only enrolling
 *  WeakReference values into the ThreadLocal, and
 *  separately holding a hard reference to each stored
 *  value.  When you call {@link #close}, these hard
 *  references are cleared and then GC is freely able to
 *  reclaim space by objects stored in it. */

public class CloseableThreadLocal {

  private ThreadLocal t = new ThreadLocal();

  private Map hardRefs = new HashMap();
  
  protected Object initialValue() {
    return null;
  }
  
  public Object get() {
    WeakReference weakRef = (WeakReference) t.get();
    if (weakRef == null) {
      Object iv = initialValue();
      if (iv != null) {
        set(iv);
        return iv;
      } else
        return null;
    } else {
      Object v = weakRef.get();
      assert v != null;
      return v;
    }
  }

  public void set(Object object) {

    t.set(new WeakReference(object));

    synchronized(hardRefs) {
      hardRefs.put(Thread.currentThread(), object);

      Iterator it = hardRefs.keySet().iterator();
      while(it.hasNext()) {
        Thread t = (Thread) it.next();
        if (!t.isAlive())
          it.remove();
      }
    }
  }

  public void close() {
    hardRefs = null;
    t = null;
  }
}
