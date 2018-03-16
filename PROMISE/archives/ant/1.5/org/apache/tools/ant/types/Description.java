package org.apache.tools.ant.types;



/**
 * Description is used to provide a project-wide description element
 * (that is, a description that applies to a buildfile as a whole).
 * If present, the &lt;description&gt; element is printed out before the
 * target descriptions.
 * 
 * Description has no attributes, only text.  There can only be one
 * project description per project.  A second description element will
 * overwrite the first.
 *
 * @author <a href="mailto:cstrong@arielpartners.com">Craeg Strong</a>
 * @version $Revision: 274041 $ $Date: 2003-02-10 22:25:38 +0800 (周一, 2003-02-10) $
 *
 * @ant.datatype ignore="true"
 */
public class Description extends DataType {

    /**
     * Adds descriptive text to the project.
     */
    public void addText(String text) {
        String currentDescription = project.getDescription();
        if (currentDescription == null) {
            project.setDescription(text);
        } else {
            project.setDescription(currentDescription + text);
        }
    }
}
