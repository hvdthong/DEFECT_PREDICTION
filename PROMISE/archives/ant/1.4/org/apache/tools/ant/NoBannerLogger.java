package org.apache.tools.ant;

/**
 * Extends DefaultLogger to strip out empty targets.
 *
 * @author <a href="mailto:donaldp@apache.org">Peter Donald</a>
 */
public class NoBannerLogger extends DefaultLogger {

    protected String targetName;

    public void targetStarted(BuildEvent event) {
        targetName = event.getTarget().getName();
    }

    public void targetFinished(BuildEvent event) {
        targetName = null;
    }

    public void messageLogged(BuildEvent event) {

        if( event.getPriority() > msgOutputLevel ||
                null == event.getMessage() || 
            "".equals( event.getMessage().trim() ) ) {
            return;
        }

        if( null != targetName ) {
            out.println(lSep + targetName + ":");
            targetName = null;
        }

        super.messageLogged( event );
    }
}
