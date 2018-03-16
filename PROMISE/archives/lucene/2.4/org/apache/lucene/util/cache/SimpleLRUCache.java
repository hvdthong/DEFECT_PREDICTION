import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple LRU cache implementation that uses a LinkedHashMap.
 * This cache is not synchronized, use {@link Cache#synchronizedCache(Cache)}
 * if needed.
 * 
 */
public class SimpleLRUCache extends SimpleMapCache {
  private final static float LOADFACTOR = 0.75f;

  private int cacheSize;

  /**
   * Creates a last-recently-used cache with the specified size. 
   */
  public SimpleLRUCache(int cacheSize) {
    super(null);
    this.cacheSize = cacheSize;
    int capacity = (int) Math.ceil(cacheSize / LOADFACTOR) + 1;

    super.map = new LinkedHashMap(capacity, LOADFACTOR, true) {
      protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > SimpleLRUCache.this.cacheSize;
      }
    };
  }

}
