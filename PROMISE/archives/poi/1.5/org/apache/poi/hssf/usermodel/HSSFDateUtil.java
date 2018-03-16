package org.apache.poi.hssf.usermodel;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Contains methods for dealing with Excel dates.
 *
 * @author  Michael Harhen
 * @author  Glen Stampoultzis (glens at apache.org)
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
     *
     * @param  date the Excel Date
     *
     * @return Java representation of a date (null if error)
     */

    public static Date getJavaDate(double date)
    {
        if (isValidExcelDate(date))
        {
            int               wholeDaysSince1900 = ( int ) Math.floor(date);
            GregorianCalendar calendar           = new GregorianCalendar(1900,
                                                       0, wholeDaysSince1900
                                                       - 1);
            int               millisecondsInDay  =
                ( int ) ((date - Math.floor(date))
                         * ( double ) DAY_MILLISECONDS + 0.5);

            calendar.set(GregorianCalendar.MILLISECOND, millisecondsInDay);
            return calendar.getTime();
        }
        else
        {
            return null;
        }
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
