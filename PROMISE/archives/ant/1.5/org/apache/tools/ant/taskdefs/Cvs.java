package org.apache.tools.ant.taskdefs;

/**
 * Performs operations on a CVS repository.
 *
 * original 1.20
 *
 *  NOTE: This implementation has been moved to AbstractCvsTask with
 *  the addition of some accessors for extensibility.
 *
 *
 * @author costin@dnt.ro
 * @author stefano@apache.org
 * @author Wolfgang Werner 
 *         <a href="mailto:wwerner@picturesafe.de">wwerner@picturesafe.de</a>
 * @author Kevin Ross 
 *         <a href="mailto:kevin.ross@bredex.com">kevin.ross@bredex.com</a>
 *
 * @since Ant 1.1
 * 
 * @ant.task category="scm"
 */
public class Cvs extends AbstractCvsTask {

    /**
     * CVS Task - now implemented by the Abstract CVS Task base class
     */
    public Cvs() {
    }
}
