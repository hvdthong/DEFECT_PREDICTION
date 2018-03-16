public interface ExtendedParseException
{
    /**
     * returns the Template name where this exception occured.
     * @return The Template name where this exception occured.
     */
    String getTemplateName();

    /**
     * returns the line number where this exception occured.
     * @return The line number where this exception occured.
     */
    int getLineNumber();

    /**
     * returns the column number where this exception occured.
     * @return The column number where this exception occured.
     */
    int getColumnNumber();
}

