package org.apache.xalan.lib;


import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;
import org.apache.xpath.objects.XString;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;

/**
 * <meta name="usage" content="general"/>
 * This class contains EXSLT dates and times extension functions.
 * It is accessed by specifying a namespace URI as follows:
 * <pre>
 * </pre>
 * 
 * The documentation for each function has been copied from the relevant
 * EXSLT Implementer page.
 * 
 */

public class ExsltDatetime
{
    static final String dt = "yyyy-MM-dd'T'HH:mm:ss";
    static final String d = "yyyy-MM-dd";
    static final String gym = "yyyy-MM";
    static final String gy = "yyyy";
    static final String gmd = "MM-dd";
    static final String gm = "MM";
    static final String gd = "dd";
    static final String t = "HH:mm:ss";

    /**
     * The date:date-time function returns the current date and time as a date/time string. 
     * The date/time string that's returned must be a string in the format defined as the 
     * lexical representation of xs:dateTime in 
     * The date/time format is basically CCYY-MM-DDThh:mm:ss, although implementers should consult
     * The date/time string format must include a time zone, either a Z to indicate Coordinated 
     * Universal Time or a + or - followed by the difference between the difference from UTC 
     * represented as hh:mm. 
     */
    public static XString dateTime()
    {
      Calendar cal = Calendar.getInstance();
      Date datetime = cal.getTime();
      SimpleDateFormat dateFormat = new SimpleDateFormat(dt);
      
      StringBuffer buff = new StringBuffer(dateFormat.format(datetime));
      int offset = cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET);
      if (offset == 0)
        buff.append("Z");
      else
      {
        int hrs = offset/(60*60*1000);
        int min = offset%(60*60*1000);
        char posneg = hrs < 0? '-': '+';
        buff.append(posneg + formatDigits(hrs) + ':' + formatDigits(min));
      }
      return new XString(buff.toString());
    }
    
    /**
     * Represent the hours and minutes with two-digit strings.
     * @param q hrs or minutes.
     * @return two-digit String representation of hrs or minutes.
     */
    private static String formatDigits(int q)
    {
      String dd = String.valueOf(Math.abs(q));
      return dd.length() == 1 ? '0' + dd : dd;
    }

    /**
     * The date:date function returns the date specified in the date/time string given 
     * as the argument. If no argument is given, then the current local date/time, as 
     * returned by date:date-time is used as a default argument. 
     * The date/time string that's returned must be a string in the format defined as the 
     * lexical representation of xs:dateTime in 
     * If the argument is not in either of these formats, date:date returns an empty string (''). 
     * The date/time format is basically CCYY-MM-DDThh:mm:ss, although implementers should consult 
     * The date is returned as a string with a lexical representation as defined for xs:date in 
     * [3.2.9 date] of [XML Schema Part 2: Datatypes]. The date format is basically CCYY-MM-DD, 
     * although implementers should consult [XML Schema Part 2: Datatypes] and [ISO 8601] for details.
     * If no argument is given or the argument date/time specifies a time zone, then the date string 
     * format must include a time zone, either a Z to indicate Coordinated Universal Time or a + or - 
     * followed by the difference between the difference from UTC represented as hh:mm. If an argument 
     * is specified and it does not specify a time zone, then the date string format must not include 
     * a time zone. 
     */
    public static XString date(String datetimeIn)
      throws ParseException
    {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String leader = edz[0];
      String datetime = edz[1];
      String zone = edz[2];
      if (datetime == null || zone == null) 
        return new XString("");
                    
      String[] formatsIn = {dt, d};
      String formatOut = d;
      Date date = testFormats(datetime, formatsIn);
      if (date == null) return new XString("");
      
      SimpleDateFormat dateFormat = new SimpleDateFormat(formatOut);
      dateFormat.setLenient(false);
      String dateOut = dateFormat.format(date);      
      if (dateOut.length() == 0)
          return new XString("");
      else        
        return new XString(leader + dateOut + zone);
    }
    
    
    /**
     * See above.
     */
    public static XString date()
    {
      String datetime = dateTime().toString();
      String date = datetime.substring(0, datetime.indexOf("T"));
      String zone = datetime.substring(getZoneStart(datetime));
      return new XString(date + zone);
    }
    
    /**
     * The date:time function returns the time specified in the date/time string given 
     * as the argument. If no argument is given, then the current local date/time, as 
     * returned by date:date-time is used as a default argument. 
     * The date/time string that's returned must be a string in the format defined as the 
     * lexical representation of xs:dateTime in 
     * If the argument string is not in this format, date:time returns an empty string (''). 
     * The date/time format is basically CCYY-MM-DDThh:mm:ss, although implementers should consult 
     * The date is returned as a string with a lexical representation as defined for xs:time in 
     * The time format is basically hh:mm:ss, although implementers should consult [XML Schema Part 2: 
     * Datatypes] and [ISO 8601] for details. 
     * If no argument is given or the argument date/time specifies a time zone, then the time string 
     * format must include a time zone, either a Z to indicate Coordinated Universal Time or a + or - 
     * followed by the difference between the difference from UTC represented as hh:mm. If an argument 
     * is specified and it does not specify a time zone, then the time string format must not include 
     * a time zone. 
     */
    public static XString time(String timeIn)
      throws ParseException      
    {
      String[] edz = getEraDatetimeZone(timeIn);
      String time = edz[1];
      String zone = edz[2];
      if (time == null || zone == null) 
        return new XString("");
                    
      String[] formatsIn = {dt, d};
      String formatOut =  t;
      Date date = testFormats(time, formatsIn);
      if (date == null) return new XString("");
      SimpleDateFormat dateFormat = new SimpleDateFormat(formatOut);
      String out = dateFormat.format(date);
      return new XString(out + zone);
    }

    /**
     * See above.
     */
    public static XString time()
    {
      String datetime = dateTime().toString();
      String time = datetime.substring(datetime.indexOf("T")+1);
      String zone = datetime.substring(getZoneStart(datetime));      
      return new XString(time + zone);
    } 
       
    /**
     * The date:year function returns the year of a date as a number. If no 
     * argument is given, then the current local date/time, as returned by 
     * date:date-time is used as a default argument.
     * The date/time string specified as the first argument must be a right-truncated 
     * string in the format defined as the lexical representation of xs:dateTime in one 
     * of the formats defined in 
     * The permitted formats are as follows: 
     *   xs:dateTime (CCYY-MM-DDThh:mm:ss) 
     *   xs:date (CCYY-MM-DD) 
     *   xs:gYearMonth (CCYY-MM) 
     *   xs:gYear (CCYY) 
     * If the date/time string is not in one of these formats, then NaN is returned. 
     */
    public static XNumber year(String datetimeIn)
      throws ParseException
    {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) 
        return new XNumber(Double.NaN);
      
      String[] formats = {dt, d, gym, gy};
      double yr = getNumber(datetime, formats, Calendar.YEAR);
      if (ad || yr == Double.NaN)
        return new XNumber(yr);
      else
        return new XNumber(-yr);
    }
     
    /**
     * See above.
     */
    public static XNumber year()
    {
      Calendar cal = Calendar.getInstance();
      return new XNumber(cal.get(Calendar.YEAR));
    }
    
    /**
     * The date:year function returns the month of a date as a number. If no argument 
     * is given, then the current local date/time, as returned by date:date-time is used 
     * as a default argument. 
     * The date/time string specified as the first argument is a left or right-truncated 
     * string in the format defined as the lexical representation of xs:dateTime in one of 
     * the formats defined in 
     * The permitted formats are as follows: 
     *    xs:dateTime (CCYY-MM-DDThh:mm:ss) 
     *    xs:date (CCYY-MM-DD) 
     *    xs:gYearMonth (CCYY-MM) 
     * If the date/time string is not in one of these formats, then NaN is returned. 
     */
    public static XNumber monthInYear(String datetimeIn)
      throws ParseException
    {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null)
        return new XNumber(Double.NaN);      
      
      String[] formats = {dt, d, gym};
      return new XNumber(getNumber(datetime, formats, Calendar.MONTH));
    }
    
    /**
     * See above.
     */
    public static XNumber monthInYear()
    {      
      Calendar cal = Calendar.getInstance();
      return new XNumber(cal.get(Calendar.MONTH));
   }
    
    /**
     * The date:week-in-year function returns the week of the year as a number. If no argument 
     * is given, then the current local date/time, as returned by date:date-time is used as the 
     * default argument. For the purposes of numbering, counting follows ISO 8601: week 1 in a year 
     * is the week containing the first Thursday of the year, with new weeks beginning on a Monday. 
     * The date/time string specified as the argument is a right-truncated string in the format 
     * defined as the lexical representation of xs:dateTime in one of the formats defined in 
     * permitted formats are as follows: 
     *    xs:dateTime (CCYY-MM-DDThh:mm:ss) 
     *    xs:date (CCYY-MM-DD) 
     * If the date/time string is not in one of these formats, then NaN is returned. 
     */
    public static XNumber weekInYear(String datetimeIn)
      throws ParseException
    {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) 
        return new XNumber(Double.NaN);      
      
      String[] formats = {dt, d};
      return new XNumber(getNumber(datetime, formats, Calendar.WEEK_OF_YEAR));
    }
        
    /**
     * See above.
     */
    public static XNumber weekInYear()
    {
       Calendar cal = Calendar.getInstance();
      return new XNumber(cal.get(Calendar.WEEK_OF_YEAR));
   }

    /**
     * The date:day-in-year function returns the day of a date in a year 
     * as a number. If no argument is given, then the current local
     * date/time, as returned by date:date-time is used the default argument.
     * The date/time string specified as the argument is a right-truncated 
     * string in the format defined as the lexical representation of xs:dateTime
     * in one of the formats defined in 
     * The permitted formats are as follows:
     *     xs:dateTime (CCYY-MM-DDThh:mm:ss)
     *     xs:date (CCYY-MM-DD) 
     * If the date/time string is not in one of these formats, then NaN is returned. 
     */
    public static XNumber dayInYear(String datetimeIn)
      throws ParseException
    {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) 
        return new XNumber(Double.NaN);            
      
      String[] formats = {dt, d};
      return new XNumber(getNumber(datetime, formats, Calendar.DAY_OF_YEAR));
    }
    
    /**
     * See above.
     */
    public static XNumber dayInYear()
    {
       Calendar cal = Calendar.getInstance();
      return new XNumber(cal.get(Calendar.DAY_OF_YEAR));
   }
    

    /**
     * The date:day-in-month function returns the day of a date as a number. 
     * If no argument is given, then the current local date/time, as returned 
     * by date:date-time is used the default argument. 
     * The date/time string specified as the argument is a left or right-truncated 
     * string in the format defined as the lexical representation of xs:dateTime 
     * in one of the formats defined in 
     * The permitted formats are as follows: 
     *      xs:dateTime (CCYY-MM-DDThh:mm:ss) 
     *      xs:date (CCYY-MM-DD) 
     *      xs:gMonthDay (--MM-DD) 
     *      xs:gDay (---DD) 
     * If the date/time string is not in one of these formats, then NaN is returned. 
     */
    public static XNumber dayInMonth(String datetimeIn)
      throws ParseException
    {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      String[] formats = {dt, d, gmd, gd};
      double day = getNumber(datetime, formats, Calendar.DAY_OF_MONTH);
      return new XNumber(day);
    }
    
    /**
     * See above.
     */
    public static XNumber dayInMonth()
    {
      Calendar cal = Calendar.getInstance();
      return new XNumber(cal.get(Calendar.DAY_OF_MONTH));
   }
    
    /**
     * The date:day-of-week-in-month function returns the day-of-the-week 
     * in a month of a date as a number (e.g. 3 for the 3rd Tuesday in May). 
     * If no argument is given, then the current local date/time, as returned 
     * by date:date-time is used the default argument. 
     * The date/time string specified as the argument is a right-truncated string
     * in the format defined as the lexical representation of xs:dateTime in one 
     * of the formats defined in 
     * The permitted formats are as follows: 
     *      xs:dateTime (CCYY-MM-DDThh:mm:ss) 
     *      xs:date (CCYY-MM-DD) 
     * If the date/time string is not in one of these formats, then NaN is returned. 
     */
    public static XNumber dayOfWeekInMonth(String datetimeIn)
      throws ParseException
    {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) 
        return new XNumber(Double.NaN);            

      String[] formats =  {dt, d};
      return new XNumber(getNumber(datetime, formats, Calendar.DAY_OF_WEEK_IN_MONTH));
    }
    
    /**
     * See above.
     */
    public static XNumber dayOfWeekInMonth()
    {
       Calendar cal = Calendar.getInstance();
      return new XNumber(cal.get(Calendar.DAY_OF_WEEK_IN_MONTH));
   }
      
    
    /**
     * The date:day-in-week function returns the day of the week given in a 
     * date as a number. If no argument is given, then the current local date/time, 
     * as returned by date:date-time is used the default argument. 
     * The date/time string specified as the argument is a right-truncated string 
     * in the format defined as the lexical representation of xs:dateTime in one 
     * of the formats defined in 
     * The permitted formats are as follows: 
     *      xs:dateTime (CCYY-MM-DDThh:mm:ss) 
     *      xs:date (CCYY-MM-DD) 
     * If the date/time string is not in one of these formats, then NaN is returned. 
                            The numbering of days of the week starts at 1 for Sunday, 2 for Monday and so on up to 7 for Saturday.  
     */
    public static XNumber dayInWeek(String datetimeIn)
      throws ParseException
    {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) 
        return new XNumber(Double.NaN);            

      String[] formats = {dt, d};
      return new XNumber(getNumber(datetime, formats, Calendar.DAY_OF_WEEK));
    }
    
    /**
     * See above.
     */
    public static XNumber dayInWeek()
    {
       Calendar cal = Calendar.getInstance();
      return new XNumber(cal.get(Calendar.DAY_OF_WEEK));
   }        

    /**
     * The date:hour-in-day function returns the hour of the day as a number. 
     * If no argument is given, then the current local date/time, as returned 
     * by date:date-time is used the default argument. 
     * The date/time string specified as the argument is a right-truncated 
     * string  in the format defined as the lexical representation of xs:dateTime
     * in one of the formats defined in 
     * The permitted formats are as follows: 
     *     xs:dateTime (CCYY-MM-DDThh:mm:ss) 
     *     xs:time (hh:mm:ss) 
     * If the date/time string is not in one of these formats, then NaN is returned. 
     */
    public static XNumber hourInDay(String datetimeIn)
      throws ParseException
    {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) 
        return new XNumber(Double.NaN);            
      
      String[] formats = {d, t};
      return new XNumber(getNumber(datetime, formats, Calendar.HOUR_OF_DAY));
    }
    
    /**
     * See above.
     */
    public static XNumber hourInDay()
    {
       Calendar cal = Calendar.getInstance();
      return new XNumber(cal.get(Calendar.HOUR_OF_DAY));
   }
    
    /**
     * The date:minute-in-hour function returns the minute of the hour 
     * as a number. If no argument is given, then the current local
     * date/time, as returned by date:date-time is used the default argument. 
     * The date/time string specified as the argument is a right-truncated 
     * string in the format defined as the lexical representation of xs:dateTime
     * in one of the formats defined in 
     * The permitted formats are as follows: 
     *      xs:dateTime (CCYY-MM-DDThh:mm:ss) 
     *      xs:time (hh:mm:ss) 
     * If the date/time string is not in one of these formats, then NaN is returned. 
     */
    public static XNumber minuteInHour(String datetimeIn)
      throws ParseException
    {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) 
        return new XNumber(Double.NaN);            
      
      String[] formats = {dt,t};
      return new XNumber(getNumber(datetime, formats, Calendar.MINUTE));
    }    
    
    /**
     * See above.
     */
   public static XNumber minuteInHour()
    {
       Calendar cal = Calendar.getInstance();
      return new XNumber(cal.get(Calendar.MINUTE));
   }    

    /**
     * The date:second-in-minute function returns the second of the minute 
     * as a number. If no argument is given, then the current local 
     * date/time, as returned by date:date-time is used the default argument. 
     * The date/time string specified as the argument is a right-truncated 
     * string in the format defined as the lexical representation of xs:dateTime
     * in one of the formats defined in 
     * The permitted formats are as follows: 
     *      xs:dateTime (CCYY-MM-DDThh:mm:ss) 
     *      xs:time (hh:mm:ss) 
     * If the date/time string is not in one of these formats, then NaN is returned. 
     */
    public static XNumber secondInMinute(String datetimeIn)
      throws ParseException
    {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) 
        return new XNumber(Double.NaN);            
      
      String[] formats = {dt, t};
      return new XNumber(getNumber(datetime, formats, Calendar.SECOND));
    }

    /**
     * See above.
     */
    public static XNumber secondInMinute()
    {
       Calendar cal = Calendar.getInstance();
      return new XNumber(cal.get(Calendar.SECOND));
    }
       
    /**
     * The date:leap-year function returns true if the year given in a date 
     * is a leap year. If no argument is given, then the current local
     * date/time, as returned by date:date-time is used as a default argument. 
     * The date/time string specified as the first argument must be a 
     * right-truncated string in the format defined as the lexical representation
     * of xs:dateTime in one of the formats defined in 
     * The permitted formats are as follows: 
     *    xs:dateTime (CCYY-MM-DDThh:mm:ss) 
     *    xs:date (CCYY-MM-DD) 
     *    xs:gYearMonth (CCYY-MM) 
     *    xs:gYear (CCYY) 
     * If the date/time string is not in one of these formats, then NaN is returned. 
     */
    public static XObject leapYear(String datetimeIn)
      throws ParseException
    {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) 
        return new XNumber(Double.NaN);            
            
      String[] formats = {dt, d, gym, gy};
      double dbl = getNumber(datetime, formats, Calendar.YEAR);
      if (dbl == Double.NaN) 
        return new XNumber(Double.NaN);
      int yr = (int)dbl;
      return new XBoolean(yr % 400 == 0 || (yr % 100 != 0 && yr % 4 == 0));
    }
    
    /**
     * See above.
     */
    public static XBoolean leapYear()
    {
      Calendar cal = Calendar.getInstance();
      int yr = (int)cal.get(Calendar.YEAR);
      return new XBoolean(yr % 400 == 0 || (yr % 100 != 0 && yr % 4 == 0));      
    }    
       
    /**
     * The date:month-name function returns the full name of the month of a date. 
     * If no argument is given, then the current local date/time, as returned by 
     * date:date-time is used the default argument. 
     * The date/time string specified as the argument is a left or right-truncated 
     * string in the format defined as the lexical representation of xs:dateTime in
     *  one of the formats defined in 
     * The permitted formats are as follows: 
     *    xs:dateTime (CCYY-MM-DDThh:mm:ss) 
     *    xs:date (CCYY-MM-DD) 
     *    xs:gYearMonth (CCYY-MM) 
     *    xs:gMonth (--MM--) 
     * If the date/time string is not in one of these formats, then an empty string ('') 
     * is returned. 
     * The result is an English month name: one of 'January', 'February', 'March', 
     * 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November' 
     * or 'December'. 
     */
    public static XString monthName(String datetimeIn)
      throws ParseException
    {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) 
        return new XString("");
      
      String[] formatsIn = {dt, d, gym, gm};
      String formatOut = "MMMM";
      return new XString (getNameOrAbbrev(datetimeIn, formatsIn, formatOut));    
    }
    
    /**
     * See above.
     */
    public static XString monthName()
    {
      Calendar cal = Calendar.getInstance();
      String format = "MMMM";
      return new XString(getNameOrAbbrev(format));  
    }
        
    /**
     * The date:month-abbreviation function returns the abbreviation of the month of 
     * a date. If no argument is given, then the current local date/time, as returned 
     * by date:date-time is used the default argument. 
     * The date/time string specified as the argument is a left or right-truncated 
     * string in the format defined as the lexical representation of xs:dateTime in 
     * one of the formats defined in 
     * The permitted formats are as follows: 
     *    xs:dateTime (CCYY-MM-DDThh:mm:ss) 
     *    xs:date (CCYY-MM-DD) 
     *    xs:gYearMonth (CCYY-MM) 
     *    xs:gMonth (--MM--) 
     * If the date/time string is not in one of these formats, then an empty string ('') 
     * is returned. 
     * The result is a three-letter English month abbreviation: one of 'Jan', 'Feb', 'Mar', 
     * 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov' or 'Dec'. 
     * An implementation of this extension function in the EXSLT date namespace must conform 
     * to the behaviour described in this document. 
     */
    public static XString monthAbbreviation(String datetimeIn)
      throws ParseException
    {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) 
        return new XString("");
      
      String[] formatsIn = {dt, d, gym, gm};
      String formatOut = "MMM";
      return new XString (getNameOrAbbrev(datetimeIn, formatsIn, formatOut));
    }
    
    /**
     * See above.
     */
    public static XString monthAbbreviation()
    {
      String format = "MMM";
      return new XString(getNameOrAbbrev(format));  
    }
        
    /**
     * The date:day-name function returns the full name of the day of the week 
     * of a date.  If no argument is given, then the current local date/time, 
     * as returned by date:date-time is used the default argument. 
     * The date/time string specified as the argument is a left or right-truncated 
     * string in the format defined as the lexical representation of xs:dateTime 
     * in one of the formats defined in 
     * The permitted formats are as follows: 
     *     xs:dateTime (CCYY-MM-DDThh:mm:ss) 
     *     xs:date (CCYY-MM-DD) 
     * If the date/time string is not in one of these formats, then the empty string ('') 
     * is returned. 
     * The result is an English day name: one of 'Sunday', 'Monday', 'Tuesday', 'Wednesday', 
     * 'Thursday' or 'Friday'. 
     * An implementation of this extension function in the EXSLT date namespace must conform 
     * to the behaviour described in this document. 
     */
    public static XString dayName(String datetimeIn)
      throws ParseException
    {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) 
        return new XString("");
            
      String[] formatsIn = {dt, d};
      String formatOut = "EEEE";
      return new XString (getNameOrAbbrev(datetimeIn, formatsIn, formatOut));    
    }
    
    /**
     * See above.
     */
    public static XString dayName()
    {
      String format = "EEEE";
      return new XString(getNameOrAbbrev(format));        
    }    
    
    /**
     * The date:day-abbreviation function returns the abbreviation of the day 
     * of the week of a date. If no argument is given, then the current local 
     * date/time, as returned  by date:date-time is used the default argument. 
     * The date/time string specified as the argument is a left or right-truncated 
     * string in the format defined as the lexical representation of xs:dateTime 
     * in one of the formats defined in 
     * The permitted formats are as follows: 
     *     xs:dateTime (CCYY-MM-DDThh:mm:ss) 
     *     xs:date (CCYY-MM-DD) 
     * If the date/time string is not in one of these formats, then the empty string 
     * ('') is returned. 
     * The result is a three-letter English day abbreviation: one of 'Sun', 'Mon', 'Tue', 
     * 'Wed', 'Thu' or 'Fri'. 
     * An implementation of this extension function in the EXSLT date namespace must conform 
     * to the behaviour described in this document. 
     */
    public static XString dayAbbreviation(String datetimeIn)
      throws ParseException
    {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) 
        return new XString("");            
      
      String[] formatsIn = {dt, d};
      String formatOut = "EEE";
      return new XString (getNameOrAbbrev(datetimeIn, formatsIn, formatOut));
    }
    
    /**
     * See above.
     */
    public static XString dayAbbreviation()
    {
      String format = "EEE";
      return new XString(getNameOrAbbrev(format));              
    }
    
    /**
     * Returns an array with the 3 components that a datetime input string 
     * may contain: - (for BC era), datetime, and zone. If the zone is not
     * valid, return null for that component.
     */
    private static String[] getEraDatetimeZone(String in)
    {
      String leader = "";
      String datetime = in;
      String zone = "";
      if (in.charAt(0)=='-')
      {
        datetime = in.substring(1);
      }
      int z = getZoneStart(datetime);
      if (z > 0)
      {
        zone = datetime.substring(z);
        datetime = datetime.substring(0, z);
      }
      else if (z == -2)
        zone = null;
      return new String[]{leader, datetime, zone};  
    }    
    
    /**
     * Get the start of zone information if the input ends
     * with 'Z' or +/-hh:mm. If a zone string is not
     * found, return -1; if the zone string is invalid,
     * return -2.
     */
    private static int getZoneStart (String datetime)
    {
      if (datetime.indexOf("Z") == datetime.length()-1)
        return datetime.indexOf("Z");
      else if (
               (datetime.lastIndexOf("-") == datetime.length()-6 &&
                datetime.charAt(datetime.length()-3) == ':')               
                || 
                (datetime.indexOf("+") == datetime.length() -6)
              )
      {
        try
        {
          SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
          dateFormat.setLenient(false);
          Date d = dateFormat.parse(datetime.substring(datetime.length() -5));
          return datetime.length()-6;
        }
        catch (ParseException pe)
        {
          System.out.println("ParseException " + pe.getErrorOffset());
        }

      }
    }
    
    /**
     * Attempt to parse an input string with the allowed formats, returning
     * null if none of the formats work. Input formats are passed in longest to shortest,
     * so if any parse operation fails with a parse error in the string, can
     * immediately return null.
     */
    private static Date testFormats (String in, String[] formats)
      throws ParseException
    {
      for (int i = 0; i <formats.length; i++)
      {
        try
        {
          SimpleDateFormat dateFormat = new SimpleDateFormat(formats[i]);
          dateFormat.setLenient(false);          
          return dateFormat.parse(in);
        }
        catch (ParseException pe)
        {
          if (pe.getErrorOffset() < in.length())
            return null;
        }
      }
      return null;
    }
    
    
    /**
     * Parse the input string and return the corresponding calendar field
     * number.
     */
    private static double getNumber(String in, String[] formats, int calField)
      throws ParseException
    {
      Calendar cal = Calendar.getInstance();
      cal.setLenient(false);
      Date date = testFormats(in, formats);
      if (date == null) return Double.NaN;
      cal.setTime(date);
      return cal.get(calField);
    }    
     
    /**
     *  Get the full name or abbreviation of the month or day.
     */
    private static String getNameOrAbbrev(String in, 
                                         String[] formatsIn,
                                         String formatOut)
      throws ParseException
    {
      {
        try
        {
          SimpleDateFormat dateFormat = new SimpleDateFormat(formatsIn[i]);
          dateFormat.setLenient(false);
          Date dt = dateFormat.parse(in);          
          dateFormat.applyPattern(formatOut);
          return dateFormat.format(dt);
        }
        catch (ParseException pe)
        {
          if (pe.getErrorOffset() < in.length())
            return "";
        }
      }
      return "";
    }
    /**
     * Get the full name or abbreviation for the current month or day 
     * (no input string).
     */
    private static String getNameOrAbbrev(String format)
    {
      Calendar cal = Calendar.getInstance();
      SimpleDateFormat dateFormat = new SimpleDateFormat(format);
      return dateFormat.format(cal.getTime());
    }

}
