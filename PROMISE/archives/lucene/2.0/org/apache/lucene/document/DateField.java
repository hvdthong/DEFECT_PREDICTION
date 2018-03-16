import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.RangeQuery;


/**
 * Provides support for converting dates to strings and vice-versa.
 * The strings are structured so that lexicographic sorting orders by date,
 * which makes them suitable for use as field values and search terms.
 *
 * <P>Note that this class saves dates with millisecond granularity,
 * which is bad for {@link RangeQuery} and {@link PrefixQuery}, as those
 * queries are expanded to a BooleanQuery with a potentially large number
 * of terms when searching. Thus you might want to use
 * {@link DateTools} instead.
 *
 * <P>
 * Note: dates before 1970 cannot be used, and therefore cannot be
 * indexed when using this class. See {@link DateTools} for an
 * alternative without such a limitation.
 *
 * @deprecated If you build a new index, use {@link DateTools} instead. This class is included for use with existing
 * indices and will be removed in a future release.
 */
public class DateField {
  
  private DateField() {}

  private static int DATE_LEN = Long.toString(1000L*365*24*60*60*1000,
					       Character.MAX_RADIX).length();

  public static String MIN_DATE_STRING() {
    return timeToString(0);
  }

  public static String MAX_DATE_STRING() {
    char[] buffer = new char[DATE_LEN];
    char c = Character.forDigit(Character.MAX_RADIX-1, Character.MAX_RADIX);
    for (int i = 0 ; i < DATE_LEN; i++)
      buffer[i] = c;
    return new String(buffer);
  }

  /**
   * Converts a Date to a string suitable for indexing.
   * @throws RuntimeException if the date specified in the
   * method argument is before 1970
   */
  public static String dateToString(Date date) {
    return timeToString(date.getTime());
  }
  /**
   * Converts a millisecond time to a string suitable for indexing.
   * @throws RuntimeException if the time specified in the
   * method argument is negative, that is, before 1970
   */
  public static String timeToString(long time) {
    if (time < 0)
      throw new RuntimeException("time '" + time + "' is too early, must be >= 0");

    String s = Long.toString(time, Character.MAX_RADIX);

    if (s.length() > DATE_LEN)
      throw new RuntimeException("time '" + time + "' is too late, length of string " +
          "representation must be <= " + DATE_LEN);

    if (s.length() < DATE_LEN) {
      StringBuffer sb = new StringBuffer(s);
      while (sb.length() < DATE_LEN)
        sb.insert(0, 0);
      s = sb.toString();
    }

    return s;
  }

  /** Converts a string-encoded date into a millisecond time. */
  public static long stringToTime(String s) {
    return Long.parseLong(s, Character.MAX_RADIX);
  }
  /** Converts a string-encoded date into a Date object. */
  public static Date stringToDate(String s) {
    return new Date(stringToTime(s));
  }
}
