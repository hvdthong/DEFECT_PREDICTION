package org.apache.xerces.utils;

import java.lang.*;


/**
 * This class provides ISO8601 date/time formatting conversion into
 * Java DateFormat, 
 * This is based on ISO Internation Standard 1988-06-15 
 *


timeDuration
============
PnYnMnDTnHnMnS

Years
Months
Days

Hours
Minutes
Seconds

x < y iff x-y is positive


recurringDuration
================
timeDuration that recurrs from a specific origin.

2 constraining facets:

  duration ( timeDuration)
           0  a single instant of time

  period   ( timeDuration)
           0 does not recurr 

both duration and period should be given

CCYY-MM-DDThh:mm:ss.sss Z

Z Coordinated Universal Time -
  difference between the local time and Coordinated Universal Time
  +/-


Derived Datatype timeInstant uses same lexical
Date, time, timePeriod, recurringDate use truncated version

Suppose we wanted to define a day of the week i.e. a 24 hour duration that recurs every 7 days. This could be done as follows:  
<simpleType name='dayOfWeek' base='recurringDuration'>
    <duration value='PT24H'/>
    <period value='P7D'/>
</simpleType>
 

timeInstant
===========

for Calendar Dates:
  year - month - day - time designator - hour - minute - second

for ordinal date:
  year - day - time designator - hour - minute - second

for dates identified by week and day numbers:
  year - week designator - week - day - time designator - hour - minute - second

T - time designator
- and :  are separators

T maybe omitted ub applications where there is no risk of confusing a combined date and time.

CCYYMMDDThhmmss
CCYYMMDDThhmm
CCYYMMDDThh


CCYY-MM-DDThh:mm:ss
CCYY-MM-DDThh:mm
CCYY-MM-DDThh

CCYYDDDThhmmss
CCYYDDDThhmm
CCYYDDDThh

CCYY-DDDThh:mm:ss
CCYY-DDDThh:mm
CCYY-DDDThh

CCYYWwwDThhmmss
CCYYWwwDThhmm
CCYYWwwDThh

CCYY-Www-DThh:mm:ss
CCYY-Www-DThh:mm
CCYY-Www-DThh


TimeInstant has as basetype recurrindDuration where
duration facet = P0Y
period   facet = P0Y

1999-05-31T13:20:00-05:00. 



time
====
base type is recurringDuration 
duration facet = P0Y
period   facet = PY24H ; since this instance of time occurs every 24 hours

Time are in 24 hour period.


Lexical is left truncated representation for timeInstant
with optional following time zone indicator.

13:20:00-05:00

Basic forms       
Extended forms

Fractions are indicated by [.] or [,].
Comma is preferred ( should support both).

Coordinated UTC 
===============

Time representation as above following immediately without spaces
by time-zone designator [Z].

Local Time
==========
Time difference should be expressed only in:
hours minutes
hours
+/-
Should not be used alone!

timePeriod
==========
baseType - recurringDuration with period face equal to "P0Y" (no recurrence
Domain is space of Periods of time.

So as per ISO spece is either:

a - duration of tome delimited by a specific start and specific end
b - quantity of time expressed in one or more specific components
but not associated with any specific start or end.
c - A quantity of time associated with a specific start.
d - A quantity of time associated with a specific end.

two components can be separated by a solidus [/] for a, b, and c

a) basic format:
   CCYYMMDDThhmmss/CCYYMMDDThhmmss

b) representation of duration of time 
   Basic format: PnYnMnDTnHnMnS

c) Representation of Period identified by its start and its duration.
   CCYYMMDDThhmmss/PnYnnMnDTnHnMnS

d) Representation of period identified by its duration and its end.
   PnYnMnDTnHnMnS/CCYYMMDDThhmmss

date
====

month
=====

year
====

century
=======

recurringDate
=============

recurringDay
============



TimeInstant
===========

Combination od date and time values that represent a single instant in time

Space of Gregorian dates and legal times
Lexical:


CCYY-MM-DDThh:mm:ss.ssss

CC Century
YY Year
MM Month
DD day
Optional -  by default a + is assumed or omitted
T date/time separator
hh hour
mm minute
ss.sss second  , additional digits may be used
Additional digits to left can be added to allow for years greater than 9999

CCYY-MM-DDThh:mm:ss.ssss can be followed by a Z to indicate Coordinated Universal
Time.

To indicate time zone the difference between the local time and Coordinated Universal
Time, follows the time and consist of a sign, + or - followed by hh:mm

e.g.   1999-05-31T13:20:00-05:00

TimeDuration
============
P1Y2M3DT10H30M

PnYnMnDTnH nMnS
nY number of  Years
nM number of  Hours
nD number of  days
T   date/time separator
nH number of  hours
nM number of  minutes
nS number of  seconds


recurringInstant
===============

Lexical
=======
Left truncated representation for timeInstant

-CC omitted from the timeInstant representation it means a timeinstant
that recurs every hundred years.
-CCYY omitted is designated a time instant that recurrs every year.
-Every two character "unit" of the representation that is omitted is indicated
by a single hyphen "-".
1:20 pm on May the 31st every year is
--05-31T13:20:00-05:00


*/




/**
 *  
 * @author Jeffrey Rodriguez
 * @version
 */


public final class ISO8601Format {



    public ISO8601Format( String dateString ){

    }
   


    
    /**
     * Encodes hex octects into Base64
     * 
     * @param binaryData Array containing binaryData
     * @return Encoded Base64 array
     */
  
}                                                        
