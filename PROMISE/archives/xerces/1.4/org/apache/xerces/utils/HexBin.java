package org.apache.xerces.utils;

import java.lang.*;


/**
 * format validation
 *
 * This class encodes/decodes hexadecimal data
 * @author Jeffrey Rodriguez
 * @version $Id: HexBin.java 317193 2001-05-29 22:19:16Z neilg $
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
        return( hexNumberTable[octect] != -1 );
    }


    /**
     *       Array of bytes to check against Hex Table
     *
     * @param arrayOctect
     * @return
     */
    static boolean isArrayByteHex( byte[] arrayOctect ) {
        if (arrayOctect == null)
            return false;
        int length = arrayOctect.length;
        if (length % 2 != 0)
            return false;
        for( int i=0; i < length; i++ ){
            if( HexBin.isHex( arrayOctect[i] ) == false)
                return false;
       }
       return true;
   }

    public static boolean isHex( String isValidString ){
      if (isValidString == null)
        return false;
      return( isArrayByteHex( isValidString.getBytes()));
  }

    /**
     * array of byte to encode
     *
     * @param binaryData
     * @return return encode binary array
     */
    static public byte[] encode( byte[] binaryData ) {
        if (binaryData == null)
            return null;
        int lengthData   = binaryData.length;
        int lengthEncode = lengthData * 2;
        byte[] encodedData = new byte[lengthEncode];
        for( int i = 0; i<lengthData; i++ ){
            encodedData[i*2] = lookUpHexAlphabet[ binaryData[i] >> 4];
            encodedData[i*2+1] = lookUpHexAlphabet[ binaryData[i] & 0xf];
        }
        return encodedData;
    }

    static public  byte[] decode ( byte[]  binaryData ) {
        if (binaryData == null)
            return null;
        int lengthData   = binaryData.length;
        if (lengthData % 2 != 0)
            return null;

        int lengthDecode = lengthData / 2;
        byte[] decodedData = new byte[lengthDecode];
        for( int i = 0; i<lengthDecode; i++ ){
            decodedData[i] = (byte)((hexNumberTable[binaryData[i*2]] << 4) | hexNumberTable[binaryData[i*2+1]]);
        }
        return decodedData;
    }

    static public int getDecodedDataLength (byte[] hexData) {
        if (!isArrayByteHex(hexData))
            return -1;

        return hexData.length / 2;
    }
}
