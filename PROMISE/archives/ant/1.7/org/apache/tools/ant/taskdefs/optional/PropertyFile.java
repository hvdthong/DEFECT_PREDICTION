package org.apache.tools.ant.taskdefs.optional;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 *Modifies settings in a property file.
 *
 * <p>
 *The following is an example of its usage:
 *    <ul>&lt;target name="setState"&gt;<br>
 *    <ul>&lt;property<br>
 *        <ul>name="header"<br>
 *        value="##Generated file - do not modify!"/&gt;<br>
 *      &lt;propertyfile file="apropfile.properties" comment="${header}"&gt;<br>
 *        &lt;entry key="product.version.major" type="int"  value="5"/&gt;<br>
 *        &lt;entry key="product.version.minor" type="int"  value="0"/&gt;<br>
 *        &lt;entry key="product.build.major"   type="int"  value="0" /&gt;<br>
 *        &lt;entry key="product.build.minor"   type="int"  operation="+" /&gt;<br>
 *        &lt;entry key="product.build.date"    type="date" value="now" /&gt;<br>
 *        &lt;entry key="intSet" type="int" operation="=" value="681"/&gt;<br>
 *        &lt;entry key="intDec" type="int" operation="-"/&gt;<br>
 *        &lt;entry key="StringEquals" type="string" value="testValue"/&gt;<br>
 *     &lt;/propertyfile&gt;<br></ul>
 *   &lt;/target&gt;</ul><p>
 *
 *The &lt;propertyfile&gt; task must have:<br>
 *    <ul><li>file</li></ul>
 *Other parameters are:<br>
 *    <ul><li>comment, key, operation, type and value (the final four being
 *            eliminated shortly)</li></ul>
 *
 *The &lt;entry&gt; task must have:<br>
 *    <ul><li>key</li></ul>
 *Other parameters are:<br>
 *    <ul><li>operation</li>
 *        <li>type</li>
 *        <li>value</li>
 *        <li>default</li>
 *        <li>unit</li>
 *    </ul>
 *
 *If type is unspecified, it defaults to string
 *
 *Parameter values:<br>
 *    <ul><li>operation:</li>
 *        <ul><li>"=" (set -- default)</li>
 *        <li>"-" (dec)</li>
 *        <li>"+" (inc)</li>
 *
 *    <li>type:</li>
 *        <ul><li>"int"</li>
 *        <li>"date"</li>
 *        <li>"string"</li></ul></ul>
 *
 *    <li>value:</li>
 *      <ul><li>holds the default value, if the property
 *              was not found in property file</li>
 *          <li>"now" In case of type "date", the
 *              value "now" will be replaced by the current
 *              date/time and used even if a valid date was
 *              found in the property file.</li></ul>
 *
 *
 *String property types can only use the "=" operation.
 *Int property types can only use the "=", "-" or "+" operations.<p>
 *
 *The message property is used for the property file header, with "\\" being
 *a newline delimiter character.
 *
 */
public class PropertyFile extends Task {

    /* ========================================================================
    *
    * Instance variables.
    */

    private String              comment;

    private Properties          properties;
    private File                propertyfile;

    private Vector entries = new Vector();

    /* ========================================================================
    *
    * Constructors
    */

    /* ========================================================================
    *
    * Methods
    */

    /**
     * Execute the task.
     * @throws BuildException on error.
     */
    public void execute() throws BuildException {
        checkParameters();
        readFile();
        executeOperation();
        writeFile();
    }

    /**
     * The entry nested element.
     * @return an entry nested element to be configured.
     */
    public Entry createEntry() {
        Entry e = new Entry();
        entries.addElement(e);
        return e;
    }

    private void executeOperation() throws BuildException {
        for (Enumeration e = entries.elements(); e.hasMoreElements();) {
            Entry entry = (Entry) e.nextElement();
            entry.executeOn(properties);
        }
    }

    private void readFile() throws BuildException {
        properties = new Properties();
        try {
            if (propertyfile.exists()) {
                log("Updating property file: "
                    + propertyfile.getAbsolutePath());
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(propertyfile);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    properties.load(bis);
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                }
            } else {
                log("Creating new property file: "
                    + propertyfile.getAbsolutePath());
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(propertyfile.getAbsolutePath());
                    out.flush();
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
            }
        } catch (IOException ioe) {
            throw new BuildException(ioe.toString());
        }
    }

    private void checkParameters() throws BuildException {
        if (!checkParam(propertyfile)) {
            throw new BuildException("file token must not be null.", getLocation());
        }
    }

    /**
     * Location of the property file to be edited; required.
     * @param file the property file.
     */
    public void setFile(File file) {
        propertyfile = file;
    }

    /**
     * optional header comment for the file
     * @param hdr the string to use for the comment.
     */
    public void setComment(String hdr) {
        comment = hdr;
    }

    private void writeFile() throws BuildException {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(propertyfile));
            properties.store(bos, comment);
        } catch (IOException ioe) {
            throw new BuildException(ioe, getLocation());
        } finally {
            FileUtils.close(bos);
        }
    }

    private boolean checkParam(File param) {
        return !(param == null);
    }

    /**
     * Instance of this class represents nested elements of
     * a task propertyfile.
     */
    public static class Entry {
        private static final int DEFAULT_INT_VALUE = 0;
        private static final String DEFAULT_DATE_VALUE = "now";
        private static final String DEFAULT_STRING_VALUE = "";

        private String              key = null;
        private int                 type = Type.STRING_TYPE;
        private int                 operation = Operation.EQUALS_OPER;
        private String              value = null;
        private String              defaultValue = null;
        private String              newValue = null;
        private String              pattern = null;
        private int                 field = Calendar.DATE;

        /**
         * Name of the property name/value pair
         * @param value the key.
         */
        public void setKey(String value) {
            this.key = value;
        }

        /**
         * Value to set (=), to add (+) or subtract (-)
         * @param value the value.
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * operation to apply.
         * &quot;+&quot; or &quot;=&quot;
         *(default) for all datatypes; &quot;-&quot; for date and int only)\.
         * @param value the operation enumerated value.
         */
        public void setOperation(Operation value) {
            this.operation = Operation.toOperation(value.getValue());
        }

        /**
         * Regard the value as : int, date or string (default)
         * @param value the type enumerated value.
         */
        public void setType(Type value) {
            this.type = Type.toType(value.getValue());
        }

        /**
         * Initial value to set for a property if it is not
         * already defined in the property file.
         * For type date, an additional keyword is allowed: &quot;now&quot;
         * @param value the default value.
         */
        public void setDefault(String value) {
            this.defaultValue = value;
        }

        /**
         * For int and date type only. If present, Values will
         * be parsed and formatted accordingly.
         * @param value the pattern to use.
         */
        public void setPattern(String value) {
            this.pattern = value;
        }

        /**
         * The unit of the value to be applied to date +/- operations.
         *            Valid Values are:
         *            <ul>
         *               <li>millisecond</li>
         *               <li>second</li>
         *               <li>minute</li>
         *               <li>hour</li>
         *               <li>day (default)</li>
         *               <li>week</li>
         *               <li>month</li>
         *               <li>year</li>
         *            </ul>
         *            This only applies to date types using a +/- operation.
         * @param unit the unit enumerated value.
         * @since Ant 1.5
         */
        public void setUnit(PropertyFile.Unit unit) {
            field = unit.getCalendarField();
        }

        /**
         * Apply the nested element to the properties.
         * @param props the properties to apply the entry on.
         * @throws BuildException if there is an error.
         */
        protected void executeOn(Properties props) throws BuildException {
            checkParameters();

            String oldValue = (String) props.get(key);
            try {
                if (type == Type.INTEGER_TYPE) {
                    executeInteger(oldValue);
                } else if (type == Type.DATE_TYPE) {
                    executeDate(oldValue);
                } else if (type == Type.STRING_TYPE) {
                    executeString(oldValue);
                } else {
                    throw new BuildException("Unknown operation type: "
                        + type);
                }
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }

            if (newValue == null) {
                newValue = "";
            }

            props.put(key, newValue);
        }

        /**
        * Handle operations for type <code>date</code>.
        *
        * @param oldValue the current value read from the property file or
        *                 <code>null</code> if the <code>key</code> was
        *                 not contained in the property file.
        */
        private void executeDate(String oldValue) throws BuildException {
            Calendar currentValue = Calendar.getInstance();

            if (pattern == null) {
              pattern = "yyyy/MM/dd HH:mm";
            }
            DateFormat fmt = new SimpleDateFormat(pattern);

            String currentStringValue = getCurrentValue(oldValue);
            if (currentStringValue == null) {
                currentStringValue = DEFAULT_DATE_VALUE;
            }

            if ("now".equals(currentStringValue)) {
                currentValue.setTime(new Date());
            } else {
                try {
                    currentValue.setTime(fmt.parse(currentStringValue));
                } catch (ParseException pe)  {
                }
            }

            if (operation != Operation.EQUALS_OPER) {
                int offset = 0;
                try {
                    offset = Integer.parseInt(value);
                    if (operation == Operation.DECREMENT_OPER) {
                        offset = -1 * offset;
                    }
                } catch (NumberFormatException e) {
                    throw new BuildException("Value not an integer on " + key);
                }
                currentValue.add(field, offset);
            }

            newValue = fmt.format(currentValue.getTime());
        }


        /**
        * Handle operations for type <code>int</code>.
        *
        * @param oldValue the current value read from the property file or
        *                 <code>null</code> if the <code>key</code> was
        *                 not contained in the property file.
        */
        private void executeInteger(String oldValue) throws BuildException {
            int currentValue = DEFAULT_INT_VALUE;
            int newV  = DEFAULT_INT_VALUE;


            DecimalFormat fmt = (pattern != null) ? new DecimalFormat(pattern)
                                                    : new DecimalFormat();
            try {
                String curval = getCurrentValue(oldValue);
                if (curval != null) {
                    currentValue = fmt.parse(curval).intValue();
                } else {
                    currentValue = 0;
                }
            } catch (NumberFormatException nfe) {
            } catch (ParseException pe)  {
            }

            if (operation == Operation.EQUALS_OPER) {
                newV = currentValue;
            } else {
                int operationValue = 1;
                if (value != null) {
                    try {
                        operationValue = fmt.parse(value).intValue();
                    } catch (NumberFormatException nfe) {
                    } catch (ParseException pe)  {
                    }
                }

                if (operation == Operation.INCREMENT_OPER) {
                    newV = currentValue + operationValue;
                } else if (operation == Operation.DECREMENT_OPER) {
                    newV = currentValue - operationValue;
                }
            }

            this.newValue = fmt.format(newV);
        }

        /**
        * Handle operations for type <code>string</code>.
        *
        * @param oldValue the current value read from the property file or
        *                 <code>null</code> if the <code>key</code> was
        *                 not contained in the property file.
        */
        private void executeString(String oldValue) throws BuildException {
            String newV  = DEFAULT_STRING_VALUE;

            String currentValue = getCurrentValue(oldValue);

            if (currentValue == null) {
                currentValue = DEFAULT_STRING_VALUE;
            }

            if (operation == Operation.EQUALS_OPER) {
                newV = currentValue;
            } else if (operation == Operation.INCREMENT_OPER) {
                newV = currentValue + value;
            }
            this.newValue = newV;
        }

        /**
         * Check if parameter combinations can be supported
         * @todo make sure the 'unit' attribute is only specified on date
         *      fields
         */
        private void checkParameters() throws BuildException {
            if (type == Type.STRING_TYPE
                && operation == Operation.DECREMENT_OPER) {
                throw new BuildException("- is not supported for string "
                    + "properties (key:" + key + ")");
            }
            if (value == null && defaultValue == null) {
                throw new BuildException("\"value\" and/or \"default\" "
                    + "attribute must be specified (key:" + key + ")");
            }
            if (key == null) {
                throw new BuildException("key is mandatory");
            }
            if (type == Type.STRING_TYPE && pattern != null) {
                throw new BuildException("pattern is not supported for string "
                    + "properties (key:" + key + ")");
            }
        }

        private String getCurrentValue(String oldValue) {
            String ret = null;
            if (operation == Operation.EQUALS_OPER) {
                if (value != null && defaultValue == null) {
                    ret = value;
                }

                if (value == null && defaultValue != null && oldValue != null) {
                    ret = oldValue;
                }

                if (value == null && defaultValue != null && oldValue == null) {
                    ret = defaultValue;
                }

                if (value != null && defaultValue != null && oldValue != null) {
                    ret = value;
                }

                if (value != null && defaultValue != null && oldValue == null) {
                    ret = defaultValue;
                }
            } else {
                ret = (oldValue == null) ? defaultValue : oldValue;
            }

            return ret;
        }

        /**
         * Enumerated attribute with the values "+", "-", "="
         */
        public static class Operation extends EnumeratedAttribute {

            /** + */
            public static final int INCREMENT_OPER =   0;
            /** - */
            public static final int DECREMENT_OPER =   1;
            /** = */
            public static final int EQUALS_OPER =      2;

            /** {@inheritDoc}. */
            public String[] getValues() {
                return new String[] {"+", "-", "="};
            }

            /**
             * Convert string to index.
             * @param oper the string to convert.
             * @return the index.
             */
            public static int toOperation(String oper) {
                if ("+".equals(oper)) {
                    return INCREMENT_OPER;
                } else if ("-".equals(oper)) {
                    return DECREMENT_OPER;
                }
                return EQUALS_OPER;
            }
        }

        /**
         * Enumerated attribute with the values "int", "date" and "string".
         */
        public static class Type extends EnumeratedAttribute {

            /** int */
            public static final int INTEGER_TYPE =     0;
            /** date */
            public static final int DATE_TYPE =        1;
            /** string */
            public static final int STRING_TYPE =      2;

            /** {@inheritDoc} */
            public String[] getValues() {
                return new String[] {"int", "date", "string"};
            }

            /**
             * Convert string to index.
             * @param type the string to convert.
             * @return the index.
             */
            public static int toType(String type) {
                if ("int".equals(type)) {
                    return INTEGER_TYPE;
                } else if ("date".equals(type)) {
                    return DATE_TYPE;
                }
                return STRING_TYPE;
            }
        }
    }

    /**
     * Borrowed from Tstamp
     * @todo share all this time stuff across many tasks as a datetime datatype
     * @since Ant 1.5
     */
    public static class Unit extends EnumeratedAttribute {

        private static final String MILLISECOND = "millisecond";
        private static final String SECOND = "second";
        private static final String MINUTE = "minute";
        private static final String HOUR = "hour";
        private static final String DAY = "day";
        private static final String WEEK = "week";
        private static final String MONTH = "month";
        private static final String YEAR = "year";

        private static final String[] UNITS
            = {MILLISECOND, SECOND, MINUTE, HOUR,
               DAY, WEEK, MONTH, YEAR };

        private Map calendarFields = new HashMap();

        /** no arg constructor */
        public Unit() {
            calendarFields.put(MILLISECOND,
                                    new Integer(Calendar.MILLISECOND));
            calendarFields.put(SECOND, new Integer(Calendar.SECOND));
            calendarFields.put(MINUTE, new Integer(Calendar.MINUTE));
            calendarFields.put(HOUR, new Integer(Calendar.HOUR_OF_DAY));
            calendarFields.put(DAY, new Integer(Calendar.DATE));
            calendarFields.put(WEEK, new Integer(Calendar.WEEK_OF_YEAR));
            calendarFields.put(MONTH, new Integer(Calendar.MONTH));
            calendarFields.put(YEAR, new Integer(Calendar.YEAR));
        }

        /**
         * Convert the value to a Calendar field index.
         * @return the calander value.
         */
        public int getCalendarField() {
            String key = getValue().toLowerCase();
            Integer i = (Integer) calendarFields.get(key);
            return i.intValue();
        }

        /** {@inheritDoc}. */
        public String[] getValues() {
            return UNITS;
        }
    }
}
