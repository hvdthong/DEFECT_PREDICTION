   Copyright 2002-2004   Apache Software Foundation

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at


   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.hpsf;

/**
 * <p>This exception is thrown if an {@link java.io.InputStream} does
 * not support the {@link java.io.InputStream#mark} operation.</p>
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: MarkUnsupportedException.java 353513 2004-02-22 11:54:53Z glens $
 * @since 2002-02-09
 */
public class MarkUnsupportedException extends HPSFException
{

    public MarkUnsupportedException()
    {
        super();
    }


    public MarkUnsupportedException(final String msg)
    {
        super(msg);
    }


    public MarkUnsupportedException(final Throwable reason)
    {
        super(reason);
    }


    public MarkUnsupportedException(final String msg, final Throwable reason)
    {
        super(msg, reason);
    }

}
