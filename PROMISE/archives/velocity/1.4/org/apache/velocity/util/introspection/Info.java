public class Info
{
    private int line;
    private int column;
    private String templateName;

    public Info(String tn, int l, int c)
    {
        templateName = tn;
        line = l;
        column = c;
    }

    private Info()
    {
    }

    public String getTemplateName()
    {
        return templateName;
    }

    public int getLine()
    {
        return line;
    }

    public int getColumn()
    {
        return column;
    }
}
