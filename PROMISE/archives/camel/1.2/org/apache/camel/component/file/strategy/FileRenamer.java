package org.apache.camel.component.file.strategy;

import java.io.File;

/**
 * @version $Revision: 1.1 $
 */
public interface FileRenamer {

    File renameFile(File file);
}
