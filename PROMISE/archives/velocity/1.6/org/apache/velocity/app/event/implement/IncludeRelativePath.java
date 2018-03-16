import org.apache.velocity.app.event.IncludeEventHandler;

/**
 * <p>Event handler that looks for included files relative to the path of the
 * current template. The handler assumes that paths are separated by a forward
 * slash "/" or backwards slash "\".
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain </a>
 * @version $Id: IncludeRelativePath.java 685685 2008-08-13 21:43:27Z nbubna $
 * @since 1.5
 */
public class IncludeRelativePath implements IncludeEventHandler {

    /**
     * Return path relative to the current template's path.
     * 
     * @param includeResourcePath  the path as given in the include directive.
     * @param currentResourcePath the path of the currently rendering template that includes the
     *            include directive.
     * @param directiveName  name of the directive used to include the resource. (With the
     *            standard directives this is either "parse" or "include").

     * @return new path relative to the current template's path
     */
    public String includeEvent(
        String includeResourcePath,
        String currentResourcePath,
        String directiveName)
    {
        if (includeResourcePath.startsWith("/") || includeResourcePath.startsWith("\\") ) {
            return includeResourcePath;
        }

        int lastslashpos = Math.max(
                currentResourcePath.lastIndexOf("/"),
                currentResourcePath.lastIndexOf("\\")
                );

        if (lastslashpos == -1) {
            return includeResourcePath;
        }

        return currentResourcePath.substring(0,lastslashpos) + "/" + includeResourcePath;
    }
}
