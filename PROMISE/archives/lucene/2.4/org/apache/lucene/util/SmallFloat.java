public class SmallFloat {

  /** Converts a 32 bit float to an 8 bit float.
   * <br>Values less than zero are all mapped to zero.
   * <br>Values are truncated (rounded down) to the nearest 8 bit value.
   * <br>Values between zero and the smallest representable value
   *  are rounded up.
   *
   * @param f the 32 bit float to be converted to an 8 bit float (byte)
   * @param numMantissaBits the number of mantissa bits to use in the byte, with the remainder to be used in the exponent
   * @param zeroExp the zero-point in the range of exponent values
   * @return the 8 bit float representation
   */
  public static byte floatToByte(float f, int numMantissaBits, int zeroExp) {
    int fzero = (63-zeroExp)<<numMantissaBits;
    int bits = Float.floatToRawIntBits(f);
    int smallfloat = bits >> (24-numMantissaBits);
    if (smallfloat < fzero) {
      return (bits<=0) ?
    } else if (smallfloat >= fzero + 0x100) {
    } else {
      return (byte)(smallfloat - fzero);
    }
  }

  /** Converts an 8 bit float to a 32 bit float. */
  public static float byteToFloat(byte b, int numMantissaBits, int zeroExp) {
    if (b == 0) return 0.0f;
    int bits = (b&0xff) << (24-numMantissaBits);
    bits += (63-zeroExp) << 24;
    return Float.intBitsToFloat(bits);
  }



  /** floatToByte(b, mantissaBits=3, zeroExponent=15)
   * <br>smallest non-zero value = 5.820766E-10
   * <br>largest value = 7.5161928E9
   * <br>epsilon = 0.125
   */
  public static byte floatToByte315(float f) {
    int bits = Float.floatToRawIntBits(f);
    int smallfloat = bits >> (24-3);
    if (smallfloat < (63-15)<<3) {
      return (bits<=0) ? (byte)0 : (byte)1;
    }
    if (smallfloat >= ((63-15)<<3) + 0x100) {
      return -1;
    }
    return (byte)(smallfloat - ((63-15)<<3));
 }

  /** byteToFloat(b, mantissaBits=3, zeroExponent=15) */
  public static float byte315ToFloat(byte b) {
    if (b == 0) return 0.0f;
    int bits = (b&0xff) << (24-3);
    bits += (63-15) << 24;
    return Float.intBitsToFloat(bits);
  }


  /** floatToByte(b, mantissaBits=5, zeroExponent=2)
   * <br>smallest nonzero value = 0.033203125
   * <br>largest value = 1984.0
   * <br>epsilon = 0.03125
   */
  public static byte floatToByte52(float f) {
    int bits = Float.floatToRawIntBits(f);
    int smallfloat = bits >> (24-5);
    if (smallfloat < (63-2)<<5) {
      return (bits<=0) ? (byte)0 : (byte)1;
    }
    if (smallfloat >= ((63-2)<<5) + 0x100) {
      return -1;
    }
    return (byte)(smallfloat - ((63-2)<<5));
  }

  /** byteToFloat(b, mantissaBits=5, zeroExponent=2) */
  public static float byte52ToFloat(byte b) {
    if (b == 0) return 0.0f;
    int bits = (b&0xff) << (24-5);
    bits += (63-2) << 24;
    return Float.intBitsToFloat(bits);
  }
}
