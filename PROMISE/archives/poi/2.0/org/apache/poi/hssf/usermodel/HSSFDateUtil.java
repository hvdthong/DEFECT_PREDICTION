package org.apache.poi.hssf.usermodel;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Contains methods for dealing with Excel dates.
 *
 * @author  Michael Harhen
 * @author  Glen Stampoultzis (glens at apache.org)
 * @author  Dan Sherman (dsherman at isisph.com)
 */

public class HSSFDateUtil
{
    private HSSFDateUtil()
    {
    }

    private static final int    BAD_DATE          =
    private static final long   DAY_MILLISECONDS  = 24 * 60 * 60 * 1000;
    private static final double CAL_1900_ABSOLUTE =
        ( double ) absoluteDay(new GregorianCalendar(1900, Calendar
        .JANUARY, 1)) - 2.0;

    /**
     * Given a Date, converts it into a double representing its internal Excel representation,
     *   which is the number of days since 1/1/1900. Fractional days represent hours, minutes, and seconds.
     *
     * @return Excel representation of Date (-1 if error - test for error by checking for less than 0.1)
     * @param  date the Date
     */

    public static double getExcelDate(Date date)
    {
        Calendar calStart = new GregorianCalendar();

        calStart.setTime(
        if (calStart.get(Calendar.YEAR) < 1900)
        {
            return BAD_DATE;
        }
        else
        {
            calStart = dayStart(calStart);
            double fraction = (date.getTime() - calStart.getTime().getTime())
                              / ( double ) DAY_MILLISECONDS;

            return fraction + ( double ) absoluteDay(calStart)
                   - CAL_1900_ABSOLUTE;
        }
    }

    /**
     * Given a excel date, converts it into a Date.
     * Assumes 1900 date windowing.
     *
     * @param  date the Excel Date
     *
     * @return Java representation of a date (null if error)
     * @see #getJavaDate(double,boolean)
     */

    public static Date getJavaDate(double date)
    {
        return getJavaDate(date,false);
    }
    
    /**
     *  Given an Excel date with either 1900 or 1904 date windowing,
     *  converts it to a java.util.Date.
     *
     *  @param date  The Excel date.
     *  @param use1904windowing  true if date uses 1904 windowing,
     *   or false if using 1900 date windowing.
     *  @return Java representation of the date, or null if date is not a valid Excel date
     */
    public static Date getJavaDate(double date, boolean use1904windowing) {
        if (isValidExcelDate(date)) {
            int startYear = 1900;
            int wholeDays = (int)Math.floor(date);
            if (use1904windowing) {
                startYear = 1904;
            }
            else if (wholeDays < 61) {
                dayAdjust = 0;
            }
            GregorianCalendar calendar = new GregorianCalendar(startYear,0,
                                                     wholeDays + dayAdjust);
            int millisecondsInDay = (int)((date - Math.floor(date)) * 
                                          (double) DAY_MILLISECONDS + 0.5);
            calendar.set(GregorianCalendar.MILLISECOND, millisecondsInDay);
            return calendar.getTime();
        }
        else {
            return null;
        }
    }

    /**
     * given a format ID this will check whether the format represents
     * an internal date format or not. 
     */
    public static boolean isInternalDateFormat(int format) {
      boolean retval =false;

            switch(format) {
                case 0x0e:
                case 0x0f:
                case 0x10:
                case 0x11:
                case 0x12:
                case 0x13:
                case 0x14:
                case 0x15:
                case 0x16:
                case 0x2d:
                case 0x2e:
                case 0x2f:
                case 0xa4:
                case 0xa5:
                case 0xa6:
                case 0xa7:
                case 0xa8:
                case 0xa9:
                case 0xaa:
                case 0xab:
                case 0xac:
                case 0xad:
                    retval = true;
                    break;
                    
                default:
                    retval = false;
                    break;
            }
       return retval;
    }

    /**
     *  Check if a cell contains a date
     *  Since dates are stored internally in Excel as double values 
     *  we infer it is a date if it is formatted as such. 
     *  @see #isInternalDateFormat(int)
     */
    public static boolean isCellDateFormatted(HSSFCell cell) {
        if (cell == null) return false;
        boolean bDate = false;
        
        double d = cell.getNumericCellValue();
        if ( HSSFDateUtil.isValidExcelDate(d) ) {
            HSSFCellStyle style = cell.getCellStyle();
            int i = style.getDataFormat();
            bDate = isInternalDateFormat(i);
        }
        return bDate;
    }


    /**
     * Given a double, checks if it is a valid Excel date.
     *
     * @return true if valid
     * @param  value the double value
     */

    public static boolean isValidExcelDate(double value)
    {
        return (value > -Double.MIN_VALUE);
    }

    /**
     * Given a Calendar, return the number of days since 1600/12/31.
     *
     * @return days number of days since 1600/12/31
     * @param  cal the Calendar
     * @exception IllegalArgumentException if date is invalid
     */

    private static int absoluteDay(Calendar cal)
    {
        return cal.get(Calendar.DAY_OF_YEAR)
               + daysInPriorYears(cal.get(Calendar.YEAR));
    }

    /**
     * Return the number of days in prior years since 1601
     *
     * @return    days  number of days in years prior to yr.
     * @param     yr    a year (1600 < yr < 4000)
     * @exception IllegalArgumentException if year is outside of range.
     */

    private static int daysInPriorYears(int yr)
    {
        if (yr < 1601)
        {
            throw new IllegalArgumentException(
                "'year' must be 1601 or greater");
        }
        int y    = yr - 1601;

        return days;
    }

    private static Calendar dayStart(final Calendar cal)
    {
        cal.get(Calendar
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.get(Calendar
        return cal;
    }

}
