package org.apache.xerces.utils;

import java.lang.*;


/**
 * format validation
 *
 * This class encodes/decodes hexadecimal data
 * @author Jeffrey Rodriguez
 * @version $Id: HexBin.java 316643 2000-11-29 00:22:23Z jeffreyr $
 */

public final class  HexBin {
    static private final int  BASELENGTH   = 255;       
    static private final int  LOOKUPLENGTH = 16;
    static private byte [] hexNumberTable       = new byte[BASELENGTH]; 
    static private byte [] lookUpHexAlphabet = new byte[LOOKUPLENGTH];


    static {
        for (int i = 0; i<BASELENGTH; i++ ) {  
            hexNumberTable[i] = -1; 
        }
        for ( int i = '9'; i >= '0'; i--) {
            hexNumberTable[i] = (byte) (i-'0');
        }
        for ( int i = 'F'; i>= 'A'; i--) {
            hexNumberTable[i] = (byte) ( i-'A' + 10 );
        }
        for ( int i = 'f'; i>= 'a'; i--) {
           hexNumberTable[i] = (byte) ( i-'a' + 10 );
        }

        for(int i = 0; i<10; i++ )
            lookUpHexAlphabet[i] = (byte) ('0'+i );
        for(int i = 10; i<=15; i++ )
            lookUpHexAlphabet[i] = (byte) ('A'+i -10);
    }

    /**
     * byte to be tested if it is Base64 alphabet
     * 
     * @param octect
     * @return 
     */
    static boolean isHex( byte octect ) {
        return(hexNumberTable[octect] != -1 ); 
    }


    /**
     *       Array of bytes to check against Hex Table 
     * 
     * @param arrayOctect
     * @return 
     */
    static boolean isArrayByteHex( byte[] arrayOctect ) {
       int length = arrayOctect.length;
       if( length == 0 )
           return false;
       for( int i=0; i < length; i++ ){   
           
           if( HexBin.isHex( arrayOctect[i] ) == false)
               return false;

       }
       return true;
   }

    public static boolean isHex( String isValidString ){
      return( isArrayByteHex( isValidString.getBytes()));
  }



    /**
     * array of byte to encode
     * 
     * @param binaryData
     * @return return encode binary array
     */
    static public byte[] encode( byte[] binaryData ) {
      int lengthData   = binaryData.length;
      int lengthEncode = lengthData;
      byte[] encodedData = new byte[lengthData];
      for( int i = 0; i<lengthData; i++ ){
         encodedData[i] = lookUpHexAlphabet[ binaryData[i] ];
      }
      return encodedData;
    }


    static public  byte[] decode ( byte[]  binaryData ) {
      int lengthData   = binaryData.length;
      int lengthEncode = lengthData;
      byte[] decodedData = new byte[lengthData];
      for( int i = 0; i<lengthData; i++ ){
      decodedData[i] = hexNumberTable[binaryData[i]];
      }
     return decodedData;
    }
}
