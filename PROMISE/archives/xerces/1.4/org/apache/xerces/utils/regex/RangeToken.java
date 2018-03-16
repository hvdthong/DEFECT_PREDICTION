package org.apache.xerces.utils.regex;


/**
 * This class represents a character class such as [a-z] or a period.
 */
final class RangeToken extends Token implements java.io.Serializable {

    int[] ranges;
    boolean sorted;
    boolean compacted;
    RangeToken icaseCache = null;
    int[] map = null;
    int nonMapIndex;

    RangeToken(int type) {
        super(type);
        this.setSorted(false);
    }

    protected void addRange(int start, int end) {
        this.icaseCache = null;
        int r1, r2;
        if (start <= end) {
            r1 = start;
            r2 = end;
        } else {
            r1 = end;
            r2 = start;
        }

        int pos = 0;
        if (this.ranges == null) {
            this.ranges = new int[2];
            this.ranges[0] = r1;
            this.ranges[1] = r2;
            this.setSorted(true);
        } else {
            pos = this.ranges.length;
            if (this.ranges[pos-1]+1 == r1) {
                this.ranges[pos-1] = r2;
                return;
            }
            int[] temp = new int[pos+2];
            System.arraycopy(this.ranges, 0, temp, 0, pos);
            this.ranges = temp;
            if (this.ranges[pos-1] >= r1)
                this.setSorted(false);
            this.ranges[pos++] = r1;
            this.ranges[pos] = r2;
            if (!this.sorted)
                this.sortRanges();
        }
    }

    private final boolean isSorted() {
        return this.sorted;
    }
    private final void setSorted(boolean sort) {
        this.sorted = sort;
        if (!sort)  this.compacted = false;
    }
    private final boolean isCompacted() {
        return this.compacted;
    }
    private final void setCompacted() {
        this.compacted = true;
    }

    protected void sortRanges() {
        if (this.isSorted())
            return;
        if (this.ranges == null)
            return;

        for (int i = this.ranges.length-4;  i >= 0;  i -= 2) {
            for (int j = 0;  j <= i;  j += 2) {
                if (this.ranges[j] > this.ranges[j+2]
                    || this.ranges[j] == this.ranges[j+2] && this.ranges[j+1] > this.ranges[j+3]) {
                    int tmp;
                    tmp = this.ranges[j+2];
                    this.ranges[j+2] = this.ranges[j];
                    this.ranges[j] = tmp;
                    tmp = this.ranges[j+3];
                    this.ranges[j+3] = this.ranges[j+1];
                    this.ranges[j+1] = tmp;
                }
            }
        }
        this.setSorted(true);
    }

    /**
     * this.ranges is sorted.
     */
    protected void compactRanges() {
        boolean DEBUG = false;
        if (this.ranges == null || this.ranges.length <= 2)
            return;
        if (this.isCompacted())
            return;

        while (target < this.ranges.length) {
            if (base != target) {
                this.ranges[base] = this.ranges[target++];
                this.ranges[base+1] = this.ranges[target++];
            } else
                target += 2;
            int baseend = this.ranges[base+1];
            while (target < this.ranges.length) {
                if (baseend+1 < this.ranges[target])
                    break;
                if (baseend+1 == this.ranges[target]) {
                    if (DEBUG)
                        System.err.println("Token#compactRanges(): Compaction: ["+this.ranges[base]
                                           +", "+this.ranges[base+1]
                                           +"], ["+this.ranges[target]
                                           +", "+this.ranges[target+1]
                                           +"] -> ["+this.ranges[base]
                                           +", "+this.ranges[target+1]
                                           +"]");
                    this.ranges[base+1] = this.ranges[target+1];
                    baseend = this.ranges[base+1];
                    target += 2;
                } else if (baseend >= this.ranges[target+1]) {
                    if (DEBUG)
                        System.err.println("Token#compactRanges(): Compaction: ["+this.ranges[base]
                                           +", "+this.ranges[base+1]
                                           +"], ["+this.ranges[target]
                                           +", "+this.ranges[target+1]
                                           +"] -> ["+this.ranges[base]
                                           +", "+this.ranges[base+1]
                                           +"]");
                    target += 2;
                } else if (baseend < this.ranges[target+1]) {
                    if (DEBUG)
                        System.err.println("Token#compactRanges(): Compaction: ["+this.ranges[base]
                                           +", "+this.ranges[base+1]
                                           +"], ["+this.ranges[target]
                                           +", "+this.ranges[target+1]
                                           +"] -> ["+this.ranges[base]
                                           +", "+this.ranges[target+1]
                                           +"]");
                    this.ranges[base+1] = this.ranges[target+1];
                    baseend = this.ranges[base+1];
                    target += 2;
                } else {
                    throw new RuntimeException("Token#compactRanges(): Internel Error: ["
                                               +this.ranges[base]
                                               +","+this.ranges[base+1]
                                               +"] ["+this.ranges[target]
                                               +","+this.ranges[target+1]+"]");
                }
            base += 2;
        }

        if (base != this.ranges.length) {
            int[] result = new int[base];
            System.arraycopy(this.ranges, 0, result, 0, base);
            this.ranges = result;
        }
        this.setCompacted();
    }

    protected void mergeRanges(Token token) {
        if (token.type != this.type)
            throw new IllegalArgumentException("Token#mergeRanges(): Mismatched Type: "+token.type);
        RangeToken tok = (RangeToken)token;
        this.sortRanges();
        tok.sortRanges();
        if (tok.ranges == null)
            return;
        this.icaseCache = null;
        this.setSorted(true);
        if (this.ranges == null) {
            this.ranges = new int[tok.ranges.length];
            System.arraycopy(tok.ranges, 0, this.ranges, 0, tok.ranges.length);
            return;
        }
        int[] result = new int[this.ranges.length+tok.ranges.length];
        for (int i = 0, j = 0, k = 0;  i < this.ranges.length || j < tok.ranges.length;) {
            if (i >= this.ranges.length) {
                result[k++] = tok.ranges[j++];
                result[k++] = tok.ranges[j++];
            } else if (j >= tok.ranges.length) {
                result[k++] = this.ranges[i++];
                result[k++] = this.ranges[i++];
            } else if (tok.ranges[j] < this.ranges[i]
                       || tok.ranges[j] == this.ranges[i] && tok.ranges[j+1] < this.ranges[i+1]) {
                result[k++] = tok.ranges[j++];
                result[k++] = tok.ranges[j++];
            } else {
                result[k++] = this.ranges[i++];
                result[k++] = this.ranges[i++];
            }
        }
        this.ranges = result;
    }

    protected void subtractRanges(Token token) {
        if (token.type == NRANGE) {
            this.intersectRanges(token);
            return;
        }
        RangeToken tok = (RangeToken)token;
        if (tok.ranges == null || this.ranges == null)
            return;
        this.icaseCache = null;
        this.sortRanges();
        this.compactRanges();
        tok.sortRanges();
        tok.compactRanges();


        int[] result = new int[this.ranges.length+tok.ranges.length];
        int wp = 0, src = 0, sub = 0;
        while (src < this.ranges.length && sub < tok.ranges.length) {
            int srcbegin = this.ranges[src];
            int srcend = this.ranges[src+1];
            int subbegin = tok.ranges[sub];
            int subend = tok.ranges[sub+1];
                result[wp++] = this.ranges[src++];
                result[wp++] = this.ranges[src++];
            } else if (srcend >= subbegin
                if (subbegin <= srcbegin && srcend <= subend) {
                    src += 2;
                } else if (subbegin <= srcbegin) {
                    this.ranges[src] = subend+1;
                    sub += 2;
                } else if (srcend <= subend) {
                    result[wp++] = srcbegin;
                    result[wp++] = subbegin-1;
                    src += 2;
                } else {
                    result[wp++] = srcbegin;
                    result[wp++] = subbegin-1;
                    this.ranges[src] = subend+1;
                    sub += 2;
                }
            } else if (subend < srcbegin) {
                sub += 2;
            } else {
                throw new RuntimeException("Token#subtractRanges(): Internal Error: ["+this.ranges[src]
                                           +","+this.ranges[src+1]
                                           +"] - ["+tok.ranges[sub]
                                           +","+tok.ranges[sub+1]
                                           +"]");
            }
        }
        while (src < this.ranges.length) {
            result[wp++] = this.ranges[src++];
            result[wp++] = this.ranges[src++];
        }
        this.ranges = new int[wp];
        System.arraycopy(result, 0, this.ranges, 0, wp);
    }

    /**
     * @param tok Ignore whether it is NRANGE or not.
     */
    protected void intersectRanges(Token token) {
        RangeToken tok = (RangeToken)token;
        if (tok.ranges == null || this.ranges == null)
            return;
        this.icaseCache = null;
        this.sortRanges();
        this.compactRanges();
        tok.sortRanges();
        tok.compactRanges();

        int[] result = new int[this.ranges.length+tok.ranges.length];
        int wp = 0, src1 = 0, src2 = 0;
        while (src1 < this.ranges.length && src2 < tok.ranges.length) {
            int src1begin = this.ranges[src1];
            int src1end = this.ranges[src1+1];
            int src2begin = tok.ranges[src2];
            int src2end = tok.ranges[src2+1];
                src1 += 2;
            } else if (src1end >= src2begin
                if (src2begin <= src2begin && src1end <= src2end) {
                    result[wp++] = src1begin;
                    result[wp++] = src1end;
                    src1 += 2;
                } else if (src2begin <= src1begin) {
                    result[wp++] = src1begin;
                    result[wp++] = src2end;
                    this.ranges[src1] = src2end+1;
                    src2 += 2;
                } else if (src1end <= src2end) {
                    result[wp++] = src2begin;
                    result[wp++] = src1end;
                    src1 += 2;
                } else {
                    result[wp++] = src2begin;
                    result[wp++] = src2end;
                    this.ranges[src1] = src2end+1;
                }
            } else if (src2end < src1begin) {
                src2 += 2;
            } else {
                throw new RuntimeException("Token#intersectRanges(): Internal Error: ["
                                           +this.ranges[src1]
                                           +","+this.ranges[src1+1]
                                           +"] & ["+tok.ranges[src2]
                                           +","+tok.ranges[src2+1]
                                           +"]");
            }
        }
        while (src1 < this.ranges.length) {
            result[wp++] = this.ranges[src1++];
            result[wp++] = this.ranges[src1++];
        }
        this.ranges = new int[wp];
        System.arraycopy(result, 0, this.ranges, 0, wp);
    }

    /**
     * for RANGE: Creates complement.
     * for NRANGE: Creates the same meaning RANGE.
     */
    static Token complementRanges(Token token) {
        if (token.type != RANGE && token.type != NRANGE)
            throw new IllegalArgumentException("Token#complementRanges(): must be RANGE: "+token.type);
        RangeToken tok = (RangeToken)token;
        tok.sortRanges();
        tok.compactRanges();
        int len = tok.ranges.length+2;
        if (tok.ranges[0] == 0)
            len -= 2;
        int last = tok.ranges[tok.ranges.length-1];
        if (last == UTF16_MAX)
            len -= 2;
        RangeToken ret = Token.createRange();
        ret.ranges = new int[len];
        int wp = 0;
        if (tok.ranges[0] > 0) {
            ret.ranges[wp++] = 0;
            ret.ranges[wp++] = tok.ranges[0]-1;
        }
        for (int i = 1;  i < tok.ranges.length-2;  i += 2) {
            ret.ranges[wp++] = tok.ranges[i]+1;
            ret.ranges[wp++] = tok.ranges[i+1]-1;
        }
        if (last != UTF16_MAX) {
            ret.ranges[wp++] = last+1;
            ret.ranges[wp] = UTF16_MAX;
        }
        ret.setCompacted();
        return ret;
    }

    synchronized RangeToken getCaseInsensitiveToken() {
        if (this.icaseCache != null)
            return this.icaseCache;
            
        RangeToken uppers = this.type == Token.RANGE ? Token.createRange() : Token.createNRange();
        for (int i = 0;  i < this.ranges.length;  i += 2) {
            for (int ch = this.ranges[i];  ch <= this.ranges[i+1];  ch ++) {
                if (ch > 0xffff)
                    uppers.addRange(ch, ch);
                else {
                    char uch = Character.toUpperCase((char)ch);
                    uppers.addRange(uch, uch);
                }
            }
        }
        RangeToken lowers = this.type == Token.RANGE ? Token.createRange() : Token.createNRange();
        for (int i = 0;  i < uppers.ranges.length;  i += 2) {
            for (int ch = uppers.ranges[i];  ch <= uppers.ranges[i+1];  ch ++) {
                if (ch > 0xffff)
                    lowers.addRange(ch, ch);
                else {
                    char uch = Character.toUpperCase((char)ch);
                    lowers.addRange(uch, uch);
                }
            }
        }
        lowers.mergeRanges(uppers);
        lowers.mergeRanges(this);
        lowers.compactRanges();

        this.icaseCache = lowers;
        return lowers;
    }

    void dumpRanges() {
        System.err.print("RANGE: ");
        if (this.ranges == null)
            System.err.println(" NULL");
        for (int i = 0;  i < this.ranges.length;  i += 2) {
            System.err.print("["+this.ranges[i]+","+this.ranges[i+1]+"] ");
        }
        System.err.println("");
    }

    boolean match(int ch) {
        if (this.map == null)  this.createMap();
        boolean ret;
        if (this.type == RANGE) {
            if (ch < MAPSIZE)
                return (this.map[ch/32] & (1<<(ch&0x1f))) != 0;
            ret = false;
            for (int i = this.nonMapIndex;  i < this.ranges.length;  i += 2) {
                if (this.ranges[i] <= ch && ch <= this.ranges[i+1])
                    return true;
            }
        } else {
            if (ch < MAPSIZE)
                return (this.map[ch/32] & (1<<(ch&0x1f))) == 0;
            ret = true;
            for (int i = this.nonMapIndex;  i < this.ranges.length;  i += 2) {
                if (this.ranges[i] <= ch && ch <= this.ranges[i+1])
                    return false;
            }
        }
        return ret;
    }

    private static final int MAPSIZE = 256;
    private void createMap() {
        this.map = new int[asize];
        this.nonMapIndex = this.ranges.length;
        for (int i = 0;  i < asize;  i ++)  this.map[i] = 0;
        for (int i = 0;  i < this.ranges.length;  i += 2) {
            int s = this.ranges[i];
            int e = this.ranges[i+1];
            if (s < MAPSIZE) {
                for (int j = s;  j <= e && j < MAPSIZE;  j ++)
            } else {
                this.nonMapIndex = i;
                break;
            }
            if (e >= MAPSIZE) {
                this.nonMapIndex = i;
                break;
            }
        }
    }

    public String toString(int options) {
        String ret;
        if (this.type == RANGE) {
            if (this == Token.token_dot)
                ret = ".";
            else if (this == Token.token_0to9)
                ret = "\\d";
            else if (this == Token.token_wordchars)
                ret = "\\w";
            else if (this == Token.token_spaces)
                ret = "\\s";
            else {
                StringBuffer sb = new StringBuffer();
                sb.append("[");
                for (int i = 0;  i < this.ranges.length;  i += 2) {
                    if ((options & RegularExpression.SPECIAL_COMMA) != 0 && i > 0)  sb.append(",");
                    if (this.ranges[i] == this.ranges[i+1]) {
                        sb.append(escapeCharInCharClass(this.ranges[i]));
                    } else {
                        sb.append(escapeCharInCharClass(this.ranges[i]));
                        sb.append((char)'-');
                        sb.append(escapeCharInCharClass(this.ranges[i+1]));
                    }
                }
                sb.append("]");
                ret = sb.toString();
            }
        } else {
            if (this == Token.token_not_0to9)
                ret = "\\D";
            else if (this == Token.token_not_wordchars)
                ret = "\\W";
            else if (this == Token.token_not_spaces)
                ret = "\\S";
            else {
                StringBuffer sb = new StringBuffer();
                sb.append("[^");
                for (int i = 0;  i < this.ranges.length;  i += 2) {
                    if ((options & RegularExpression.SPECIAL_COMMA) != 0 && i > 0)  sb.append(",");
                    if (this.ranges[i] == this.ranges[i+1]) {
                        sb.append(escapeCharInCharClass(this.ranges[i]));
                    } else {
                        sb.append(escapeCharInCharClass(this.ranges[i]));
                        sb.append('-');
                        sb.append(escapeCharInCharClass(this.ranges[i+1]));
                    }
                }
                sb.append("]");
                ret = sb.toString();
            }
        }
        return ret;
    }

    private static String escapeCharInCharClass(int ch) {
        String ret;
        switch (ch) {
          case '[':  case ']':  case '-':  case '^':
          case ',':  case '\\':
            ret = "\\"+(char)ch;
            break;
          case '\f':  ret = "\\f";  break;
          case '\n':  ret = "\\n";  break;
          case '\r':  ret = "\\r";  break;
          case '\t':  ret = "\\t";  break;
          case 0x1b:  ret = "\\e";  break;
          default:
            if (ch < 0x20) {
                String pre = "0"+Integer.toHexString(ch);
                ret = "\\x"+pre.substring(pre.length()-2, pre.length());
            } else if (ch >= 0x10000) {
                String pre = "0"+Integer.toHexString(ch);
                ret = "\\v"+pre.substring(pre.length()-6, pre.length());
            } else
                ret = ""+(char)ch;
        }
        return ret;
    }

}
