package org.apache.lucene.search;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator over {@link Hits} that provides lazy fetching of each document.
 * {@link Hits#iterator()} returns an instance of this class.  Calls to {@link #next()}
 * return a {@link Hit} instance.
 *
 * @deprecated Hits will be removed in Lucene 3.0. Use {@link TopDocCollector} and {@link TopDocs} instead.
 */
public class HitIterator implements Iterator {
  private Hits hits;
  private int hitNumber = 0;

  /**
   * Constructed from {@link Hits#iterator()}.
   */
  HitIterator(Hits hits) {
    this.hits = hits;
  }

  /**
   * @return true if current hit is less than the total number of {@link Hits}.
   */
  public boolean hasNext() {
    return hitNumber < hits.length();
  }

  /**
   * Returns a {@link Hit} instance representing the next hit in {@link Hits}.
   *
   * @return Next {@link Hit}.
   */
  public Object next() {
    if (hitNumber == hits.length())
      throw new NoSuchElementException();

    Object next = new Hit(hits, hitNumber);
    hitNumber++;
    return next;
  }

  /**
   * Unsupported operation.
   *
   * @throws UnsupportedOperationException
   */
  public void remove() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the total number of hits.
   */
  public int length() {
    return hits.length();
  }
}


