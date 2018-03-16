package org.apache.tools.ant.taskdefs.optional;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.text.*;

/**
 *PropertyFile task uses java.util.Properties to modify integer, String and
 *Date settings in a property file.<p>
 *
 *
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
 *        &lt;entry key="product.build.date"    type="date" operation="now" /&gt;<br>
 *        &lt;entry key="intSet" type="int" operation="=" value="681"/&gt;<br>
 *        &lt;entry key="intDec" type="int" operation="-"/&gt;<br>
 *        &lt;entry key="NeverDate" type="date" operation="never"/&gt;<br>
 *        &lt;entry key="StringEquals" type="string" value="testValue"/&gt;<br>
 *        &lt;entry key="NowDate" type="date" operation="now"/&gt;<br></ul>
 *     &lt;/propertyfile&gt;<br></ul>
 *   &lt;/target&gt;</ul><p>
 *
 *The &lt;propertyfile&gt; task must have:<br>
 *    <ul><li>file</li></ul>
 *Other parameters are:<br>
 *    <ul><li>comment, key, operation, type and value (the final four being eliminated shortly)</li></ul>
 *
 *The &lt;entry&gt; task must have:<br>
 *    <ul><li>key</li></ul>
 *Other parameters are:<br>
 *    <ul><li>operation</li>
 *        <li>type</li>
 *        <li>value</li>
 *        <li>offset</li></ul>
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
 *    <li>offset:<br>valid for "-" or "+", the offset (default
 *    set to 1) will be added or subtracted from "int" or
 *    "date" type value.</li>
 *    </ul>
 *
 *String property types can only use the "=" operation.
 *Date property types can only use the "never" or "now" operations.
 *Int property types can only use the "=", "-" or "+" operations.<p>
 *
 *The message property is used for the property file header, with "\\" being
 *a newline delimiter charater.
 *
 * @author Thomas Christen <a href="mailto:chr@active.ch">chr@active.ch</a>
 * @author Jeremy Mawson <a href="mailto:jem@loftinspace.com.au>jem@loftinspace.com.au</a>
*/
public class PropertyFile extends Task
{

    /* ========================================================================
    *
    * Static variables.
    */

    private static final String NEWLINE = System.getProperty("line.separator");


    /* ========================================================================
    *
    * Instance variables.
    */

    private String              m_comment;

    private Properties          m_properties;
    private File                m_propertyfile;

    private Vector entries = new Vector();

    /* ========================================================================
    *
    * Constructors
    */

    /* ========================================================================
    *
    * Methods
    */

    public void execute() throws BuildException
    {
        checkParameters();
        readFile();
        executeOperation();
        writeFile();
    }

    public Entry createEntry()
    {
        Entry e = new Entry();
        entries.addElement(e);
        return e;
    }

    private void executeOperation() throws BuildException
    {
        for (Enumeration e = entries.elements(); e.hasMoreElements(); )
        {
            Entry entry = (Entry)e.nextElement();
            entry.executeOn(m_properties);
        }
    }

    private void readFile() throws BuildException
    {
        m_properties = new Properties();
        try
        {
            if (m_propertyfile.exists())
            {
                log("Updating property file: "+m_propertyfile.getAbsolutePath());
                m_properties.load(new BufferedInputStream(
                                    new FileInputStream(m_propertyfile)));
            }
            else
            {
                log("Creating new property file: "+
                    m_propertyfile.getAbsolutePath());
                FileOutputStream out = new FileOutputStream(m_propertyfile.getAbsolutePath());
                out.flush();
                out.close();
            }
        }
        catch(IOException ioe)
        {
            throw new BuildException(ioe.toString());
        }
    }

    private void checkParameters() throws BuildException
    {
        if (!checkParam(m_propertyfile))
        {
            throw new BuildException("file token must not be null.", location);
        }
    }

    public void setFile(File file)
    {
        m_propertyfile = file;
    }

    public void setComment(String hdr)
    {
        m_comment = hdr;
    }

    private void writeFile() throws BuildException
    {
        BufferedOutputStream bos = null;
        try
        {
            bos = new BufferedOutputStream(new FileOutputStream(m_propertyfile));

            Method m =
                Properties.class.getMethod("store",
                                           new Class[] {
                                               OutputStream.class,
                                               String.class}
                                           );
            m.invoke(m_properties, new Object[] {bos, m_comment});

        } catch (NoSuchMethodException nsme) {
            m_properties.save(bos, m_comment);
        } catch (InvocationTargetException ite) {
            Throwable t = ite.getTargetException();
            throw new BuildException(t, location);
        } catch (IllegalAccessException iae) {
            throw new BuildException(iae, location);
        }
        catch (IOException ioe)
        {
            throw new BuildException(ioe, location);
        }
        finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException ioex) {}
            }
        }
    }

    /*
    * Returns whether the given parameter has been defined.
    */
    private boolean checkParam(String param)
    {
        return !((param == null) || (param.equals("null")));
    }

    private boolean checkParam(File param)
    {
        return !(param == null);
    }

    /**
     * Instance of this class represents nested elements of
     * a task propertyfile.
     */
    public static class Entry
    {

        static final String NOW_VALUE_ =        "now";
        static final String NULL_VALUE_ =       "never";

        private static final int    DEFAULT_INT_VALUE =     1;
        private static final GregorianCalendar
            DEFAULT_DATE_VALUE = new GregorianCalendar();

        private String              m_key = null;
        private int                 m_type = Type.STRING_TYPE;
        private int                 m_operation = Operation.EQUALS_OPER;
        private String              m_value ="1";
        private String              m_default = null;
        private String              m_pattern = null;

        public void setKey(String value)
        {
            this.m_key = value;
        }
        public void setValue(String value)
        {
            this.m_value = value;
        }
        public void setOperation(Operation value)
        {
            int newOperation = Operation.toOperation(value.getValue());
            if (newOperation == Operation.NOW_VALUE) {
                this.m_operation = Operation.EQUALS_OPER;
                this.setValue(this.NOW_VALUE_);
            }
            else if (newOperation == Operation.NULL_VALUE) {
                this.m_operation = Operation.EQUALS_OPER;
                this.setValue(this.NULL_VALUE_);
            }
            else {
                this.m_operation = newOperation;
            }
        }
        public void setType(Type value)
        {
            this.m_type = Type.toType(value.getValue());
        }
        public void setDefault(String value)
        {
            this.m_default = value;
        }
        public void setPattern(String value)
        {
            this.m_pattern = value;
        }

        protected void executeOn(Properties props) throws BuildException
        {
            checkParameters();

            try {
                if (m_type == Type.INTEGER_TYPE)
                {
                    executeInteger((String)props.get(m_key));
                }
                else if (m_type == Type.DATE_TYPE)
                {
                    executeDate((String)props.get(m_key));
                }
                else if (m_type == Type.STRING_TYPE)
                {
                    executeString((String)props.get(m_key));
                }
                else
                {
                    throw new BuildException("Unknown operation type: "+m_type+"");
                }
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
            props.put(m_key, m_value);

        }

        /**
        * Handle operations for type <code>date</code>.
        *
        * @param oldValue the current value read from the property file or
        *                 <code>null</code> if the <code>key</code> was
        *                 not contained in the property file.
        */
        private void executeDate(String oldValue) throws BuildException
        {
            GregorianCalendar value = new GregorianCalendar();
            GregorianCalendar newValue = new GregorianCalendar();

            if (m_pattern == null) m_pattern = "yyyy/MM/dd HH:mm";
            DateFormat fmt = new SimpleDateFormat(m_pattern);

            if (m_value != null) {
                if (NOW_VALUE_.equals(m_value.toLowerCase())) {
                    value.setTime(new Date());
                }
                else if (NULL_VALUE_.equals(m_value.toLowerCase())) {
                    value = null;
                }
                else {
                    try {
                        value.setTime(fmt.parse(m_value));
                    }
                    catch (Exception ex) {
                        try {
                            int offset = Integer.parseInt(m_value);
                            value.clear();
                            value.set(Calendar.DAY_OF_YEAR, offset);
                        }
                        catch (Exception ex_) {
                            value.clear();
                            value.set(Calendar.DAY_OF_YEAR, 1);
                        }
                    }

                }
            }

            if (m_default != null &&
                NOW_VALUE_.equals(m_default.toLowerCase()) &&
                (m_operation == Operation.INCREMENT_OPER ||
                 m_operation == Operation.DECREMENT_OPER) ) {
                oldValue = null;
            }

            if (oldValue != null) {
                try {
                    newValue.setTime(fmt.parse(oldValue));
                }
                catch (ParseException pe)  { /* swollow */ }
            }
            else {
                if (m_default != null) {
                    if (NOW_VALUE_.equals(m_default.toLowerCase())) {
                        newValue.setTime(new Date());
                    }
                    else if (NULL_VALUE_.equals(m_default.toLowerCase())) {
                        newValue = null;
                    }
                    else {
                        try {
                            newValue.setTime(fmt.parse(m_default));
                        }
                        catch (ParseException pe)  { /* swollow */ }
                    }
                }
            }

            if (m_operation == Operation.EQUALS_OPER) {
                newValue = value;
            }
            else if (m_operation == Operation.INCREMENT_OPER) {
                newValue.add(Calendar.SECOND, value.get(Calendar.SECOND));
                newValue.add(Calendar.MINUTE, value.get(Calendar.MINUTE));
                newValue.add(Calendar.HOUR_OF_DAY, value.get(Calendar.HOUR_OF_DAY));
                newValue.add(Calendar.DAY_OF_YEAR, value.get(Calendar.DAY_OF_YEAR));
            }
            else if (m_operation == Operation.DECREMENT_OPER) {
                newValue.add(Calendar.SECOND, -1 * value.get(Calendar.SECOND));
                newValue.add(Calendar.MINUTE, -1 * value.get(Calendar.MINUTE));
                newValue.add(Calendar.HOUR_OF_DAY, -1 * value.get(Calendar.HOUR_OF_DAY));
                newValue.add(Calendar.DAY_OF_YEAR, -1 * value.get(Calendar.DAY_OF_YEAR));
            }
            if (newValue != null) {
                m_value = fmt.format(newValue.getTime());
            }
            else {
                m_value = "";
            }
        }


        /**
        * Handle operations for type <code>int</code>.
        *
        * @param oldValue the current value read from the property file or
        *                 <code>null</code> if the <code>key</code> was
        *                 not contained in the property file.
        */
        private void executeInteger(String oldValue) throws BuildException
        {
            int value = 0;
            int newValue  = 0;

            DecimalFormat fmt = (m_pattern != null) ? new DecimalFormat(m_pattern)
                                                    : new DecimalFormat();

            if (m_value != null) {
                try {
                    value = fmt.parse(m_value).intValue();
                }
                catch (NumberFormatException nfe) { /* swollow */ }
                catch (ParseException pe)  { /* swollow */ }
            }
            if (oldValue != null) {
                try {
                    newValue = fmt.parse(oldValue).intValue();
                }
                catch (NumberFormatException nfe) { /* swollow */ }
                catch (ParseException pe)  { /* swollow */ }
            }
            else if (m_default != null) {
                try {
                    newValue = fmt.parse(m_default).intValue();
                }
                catch (NumberFormatException nfe) { /* swollow */ }
                catch (ParseException pe)  { /* swollow */ }
            }

            if (m_operation == Operation.EQUALS_OPER) {
                newValue = value;
            }
            else if (m_operation == Operation.INCREMENT_OPER) {
                newValue += value;
            }
            else if (m_operation == Operation.DECREMENT_OPER) {
                newValue -= value;
            }
            m_value = fmt.format(newValue);
        }

        /**
        * Handle operations for type <code>string</code>.
        *
        * @param oldValue the current value read from the property file or
        *                 <code>null</code> if the <code>key</code> was
        *                 not contained in the property file.
        */
        private void executeString(String oldValue) throws BuildException
        {
            String value = "";
            String newValue  = "";

            if (m_value != null) {
                value = m_value;
            }
            if (oldValue != null) {
                newValue = oldValue;
            }
            else if (m_default != null) {
                newValue = m_default;
            }

            if (m_operation == Operation.EQUALS_OPER) {
                newValue = value;
            }
            else if (m_operation == Operation.INCREMENT_OPER) {
                newValue += value;
            }
            m_value = newValue;
        }

        /**
         * Check if parameter combinations can be supported
         */
        private void checkParameters() throws BuildException {
            if (m_type == Type.STRING_TYPE &&
                m_operation == Operation.DECREMENT_OPER) {
                throw new BuildException("- is not suported for string properties (key:" + m_key + ")");
            }
            if (m_value == null) {
                throw new BuildException("value is mandatory (key:" + m_key + ")");
            }
            if (m_key == null) {
                throw new BuildException("key is mandatory");
            }
            if (m_type == Type.STRING_TYPE &&
                m_pattern != null) {
                throw new BuildException("pattern is not suported for string properties (key:" + m_key + ")");
            }
        }

        /**
         * Enumerated attribute with the values "+", "-", "=", "now" and "never".
         */
        public static class Operation extends EnumeratedAttribute {

            public static final int INCREMENT_OPER =   0;
            public static final int DECREMENT_OPER =   1;
            public static final int EQUALS_OPER =      2;

            public static final int NOW_VALUE =        3;
            public static final int NULL_VALUE =       4;

            public String[] getValues() {
                return new String[] {"+", "-", "=", NOW_VALUE_, NULL_VALUE_};
            }

            public static int toOperation(String oper) {
                if ("+".equals(oper)) {
                    return INCREMENT_OPER;
                }
                else if ("-".equals(oper)) {
                    return DECREMENT_OPER;
                }
                else if (NOW_VALUE_.equals(oper)) {
                    return NOW_VALUE;
                }
                else if (NULL_VALUE_.equals(oper)) {
                    return NULL_VALUE;
                }
                return EQUALS_OPER;
            }
        }

        /**
         * Enumerated attribute with the values "int", "date" and "string".
         */
        public static class Type extends EnumeratedAttribute {

            public static final int INTEGER_TYPE =     0;
            public static final int DATE_TYPE =        1;
            public static final int STRING_TYPE =      2;

            public String[] getValues() {
                return new String[] {"int", "date", "string"};
            }

            public static int toType(String type) {
                if ("int".equals(type)) {
                    return INTEGER_TYPE;
                }
                else if ("date".equals(type)) {
                    return DATE_TYPE;
                }
                return STRING_TYPE;
            }
        }
    }
}
