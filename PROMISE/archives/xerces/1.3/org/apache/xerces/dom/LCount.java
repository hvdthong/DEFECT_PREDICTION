package org.apache.xerces.dom;

import java.io.*;
import java.util.Vector;

import org.w3c.dom.*;

import org.w3c.dom.events.*;


/** Internal class LCount is used to track the number of
    listeners registered for a given event name, as an entry
    in a global hashtable. This should allow us to avoid generating,
    or discard, events for which no listeners are registered. 
    
    ***** There should undoubtedly be methods here to manipulate
    this table. At the moment that code's residing in NodeImpl.
    Move it when we have a chance to do so. Sorry; we were
    rushed.
    
    ***** Also, I'm currently asking "are there any listeners"
    by testing captures+bubbles+defaults =? 0. It would probably
    make sense to have a separate "total" field, calculated at
    add/remove, to save a few cycles during dispatch. Fix.
    
    ???? CONCERN: Hashtables are known to be "overserialized" in
    current versions of Java. That may impact performance.
    
    ???? CONCERN: The hashtable should probably be a per-document object.
    Finer granularity would be even better, but would cost more cycles to
    resolve and might not save enough event traffic to be worth the investment.
*/
class LCount 
{ 
    static java.util.Hashtable lCounts=new java.util.Hashtable();
    public int captures=0,bubbles=0,defaults=0;

    static LCount lookup(String evtName)
    {
        LCount lc=(LCount)lCounts.get(evtName);
        if(lc==null)
            lCounts.put(evtName,(lc=new LCount()));
        return lc;	        
    }
