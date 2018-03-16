package org.apache.log4j.lf5.viewer;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * An AdjustmentListener which ensures that an Adjustable (e.g. a Scrollbar)
 * will "track" when the Adjustable expands.
 * For example, when a vertical scroll bar is at its bottom anchor,
 * the scrollbar will remain at the bottom.  When the vertical scroll bar
 * is at any other location, then no tracking will happen.
 * An instance of this class should only listen to one Adjustable as
 * it retains state information about the Adjustable it listens to.
 *
 * @author Richard Wan
 */


public class TrackingAdjustmentListener implements AdjustmentListener {


  protected int _lastMaximum = -1;




  public void adjustmentValueChanged(AdjustmentEvent e) {
    Adjustable bar = e.getAdjustable();
    int currentMaximum = bar.getMaximum();
    if (bar.getMaximum() == _lastMaximum) {
    }
    int bottom = bar.getValue() + bar.getVisibleAmount();

    if (bottom + bar.getUnitIncrement() >= _lastMaximum) {
    }
    _lastMaximum = currentMaximum;
  }



}

