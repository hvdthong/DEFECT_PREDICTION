package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import java.io.*;
import java.util.*;
import java.text.*;

/**
 * Sets TSTAMP, DSTAMP and TODAY
 *
 * @author costin@dnt.ro
 * @author stefano@apache.org
 * @author roxspring@yahoo.com
 * @author conor@cognet.com.au
 */
public class Tstamp extends Task {
    
    private Vector customFormats = new Vector();

    public void execute() throws BuildException {
        try {
            Date d = new Date();

            SimpleDateFormat dstamp = new SimpleDateFormat ("yyyyMMdd");
            project.setProperty("DSTAMP", dstamp.format(d));

            SimpleDateFormat tstamp = new SimpleDateFormat ("HHmm");
            project.setProperty("TSTAMP", tstamp.format(d));

            SimpleDateFormat today  = new SimpleDateFormat ("MMMM d yyyy", Locale.US);
            project.setProperty("TODAY", today.format(d));
            
            Enumeration i = customFormats.elements();
            while(i.hasMoreElements())
            {
                CustomFormat cts = (CustomFormat)i.nextElement();
                cts.execute(project,d, location);
            }
            
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }
    
    public CustomFormat createFormat()
    {
        CustomFormat cts = new CustomFormat();
        customFormats.addElement(cts);
        return cts;
    }
    
    public class CustomFormat
    {
        private String propertyName;
        private String pattern;
        private int offset = 0;
        private int field = Calendar.DATE;
        
        public CustomFormat()
        {
        }
        
        public void setProperty(String propertyName)
        {
            this.propertyName = propertyName;
        }
        
        public void setPattern(String pattern)
        {
            this.pattern = pattern;
        }
        
        public void setOffset(int offset) {
            this.offset = offset;
        }
        
        public void setUnit(String unit) {
            if (unit.equalsIgnoreCase("millisecond")) {
                field = Calendar.MILLISECOND;
            }
            else if (unit.equalsIgnoreCase("second")) {
                field = Calendar.SECOND;
            }
            else if (unit.equalsIgnoreCase("minute")) {
                field = Calendar.MINUTE;
            }
            else if (unit.equalsIgnoreCase("hour")) {
                field = Calendar.HOUR_OF_DAY;
            }
            else if (unit.equalsIgnoreCase("day")) {
                field = Calendar.DATE;
            }
            else if (unit.equalsIgnoreCase("week")) {
                field = Calendar.WEEK_OF_YEAR;
            }
            else if (unit.equalsIgnoreCase("month")) {
                field = Calendar.MONTH;
            }
            else if (unit.equalsIgnoreCase("year")) {
                field = Calendar.YEAR;
            }
            else {
                throw new BuildException(unit + " is not a unit supported by the tstamp task");
            }
        }            
        
        public void execute(Project project, Date date, Location location)
        {
            if (propertyName == null) {
                throw new BuildException("property attribute must be provided", location);
            }
            
            if (pattern == null) {
                throw new BuildException("pattern attribute must be provided", location);
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat (pattern);
            if (offset != 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(field, offset);
                date = calendar.getTime();
            }

            project.setProperty(propertyName, sdf.format(date));
        }
    }
}
