package fr.jayasoft.ivy.util;

public interface MessageImpl {
    public void log(String msg, int level);
    public void rawlog(String msg, int level);
    public void progress();
    public void endProgress(String msg);
}
