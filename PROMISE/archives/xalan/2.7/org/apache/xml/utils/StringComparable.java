package org.apache.xml.utils;

import java.util.Vector;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.text.CollationElementIterator;
import java.util.Locale;
import java.text.CollationKey;


/**
* International friendly string comparison with case-order
 * @author Igor Hersht, igorh@ca.ibm.com
*/
public class StringComparable implements Comparable  {
    
     public final static int UNKNOWN_CASE = -1;
     public final static int UPPER_CASE = 1;
     public final static int LOWER_CASE = 2;
     
     private  String m_text;
     private  Locale m_locale;
     private RuleBasedCollator m_collator;
     private String m_caseOrder;
     private int m_mask = 0xFFFFFFFF; 
     
    public StringComparable(final String text, final Locale locale, final Collator collator, final String caseOrder){
         m_text =  text;
         m_locale = locale;
         m_collator = (RuleBasedCollator)collator;
         m_caseOrder = caseOrder;
         m_mask = getMask(m_collator.getStrength());
    }
  
   public final static Comparable getComparator( final String text, final Locale locale, final Collator collator, final String caseOrder){
            return  ((RuleBasedCollator)collator).getCollationKey(text);
       }else{
            return new StringComparable(text, locale, collator, caseOrder);
       }       
   }
   
   public final String toString(){return m_text;}
   
   public int compareTo(Object o) {
   final String pattern = ((StringComparable)o).toString();
      return 0;
   }
   final int savedStrength = m_collator.getStrength(); 
   int comp = 0;
     if(((savedStrength == Collator.PRIMARY) || (savedStrength == Collator.SECONDARY))){  
         comp = m_collator.compare(m_text, pattern );     
         m_collator.setStrength(Collator.SECONDARY);
         comp = m_collator.compare(m_text, pattern );
         m_collator.setStrength(savedStrength);
     }
        return comp ; 
     }      
        
       comp = getCaseDiff(m_text, pattern);
       if(comp != 0){  
           return comp;
            return m_collator.compare(m_text, pattern );
       }      
  }
  
 
  private final int getCaseDiff (final String text, final String pattern){
     final int savedStrength = m_collator.getStrength();
     final int savedDecomposition = m_collator.getDecomposition();
     
    final int diff[] =getFirstCaseDiff (text, pattern, m_locale);
    if(diff != null){  
       if((m_caseOrder).equals("upper-first")){
            if(diff[0] == UPPER_CASE){
                return -1;
            }else{
                return 1;
            }
            if(diff[0] == LOWER_CASE){
                return -1;
            }else{
                return 1;
            }
       }
        return 0;
   }
    
  }
  
   
    
  private final int[] getFirstCaseDiff(final String text, final String pattern, final Locale locale){
        
        final CollationElementIterator targIter = m_collator.getCollationElementIterator(text);
        final CollationElementIterator patIter = m_collator.getCollationElementIterator(pattern);  
        int startTarg = -1;
        int endTarg = -1;
        int startPatt = -1;
        int endPatt = -1;
        final int done = getElement(CollationElementIterator.NULLORDER);
        int patternElement = 0, targetElement = 0;
        boolean getPattern = true, getTarget = true;
        
        while (true) { 
            if (getPattern){
                 startPatt = patIter.getOffset();
                 patternElement = getElement(patIter.next());
                 endPatt = patIter.getOffset();
            }
            if ((getTarget)){               
                 startTarg  = targIter.getOffset(); 
                 targetElement   = getElement(targIter.next()); 
                 endTarg  = targIter.getOffset();
            }
            getTarget = getPattern = true;                          
            if ((patternElement == done) ||( targetElement == done)) {
                return null;                      
            } else if (targetElement == 0) {
              getPattern = false;           
            } else if (patternElement == 0) {
              getTarget = false;           
                if((startPatt < endPatt) && (startTarg < endTarg)){
                    final String  subText = text.substring(startTarg, endTarg);
                    final String  subPatt = pattern.substring(startPatt, endPatt);
                    final String  subTextUp = subText.toUpperCase(locale);
                    final String  subPattUp = subPatt.toUpperCase(locale);
                        continue;
                    }
                    
                    int diff[] = {UNKNOWN_CASE, UNKNOWN_CASE};
                    if(m_collator.compare(subText, subTextUp) == 0){
                        diff[0] = UPPER_CASE;
                    }else if(m_collator.compare(subText, subText.toLowerCase(locale)) == 0){
                       diff[0] = LOWER_CASE; 
                    }
                    if(m_collator.compare(subPatt, subPattUp) == 0){
                        diff[1] = UPPER_CASE;
                    }else if(m_collator.compare(subPatt, subPatt.toLowerCase(locale)) == 0){
                       diff[1] = LOWER_CASE; 
                    }
                    
                    if(((diff[0] == UPPER_CASE) && ( diff[1] == LOWER_CASE)) ||
                       ((diff[0] == LOWER_CASE) && ( diff[1] == UPPER_CASE))){        
                        return diff;
                      continue; 
                    }  
                }else{
                    continue; 
                }
                    
           }
        }
                           
  }
  
  
    private static final int getMask(final int strength) {
        switch (strength) {
            case Collator.PRIMARY:
                return 0xFFFF0000;
            case Collator.SECONDARY:
                return 0xFFFFFF00;
            default: 
                return 0xFFFFFFFF;
        }
    }
  private final int getElement(int maxStrengthElement){
    
    return (maxStrengthElement & m_mask);
  }  



