package org.apache.poi.poifs.dev;

import java.io.*;

import java.util.*;

/**
 * This class contains methods used to inspect POIFSViewable objects
 *
 * @author Marc Johnson (mjohnson at apache dot org)
 */

public class POIFSViewEngine
{
    private static final String _EOL = System.getProperty("line.separator");

    /**
     * Inspect an object that may be viewable, and drill down if told
     * to
     *
     * @param viewable the object to be viewed
     * @param drilldown if true, and the object implements
     *                  POIFSViewable, inspect the objects' contents
     *                  (recursively)
     * @param indentLevel how far in to indent each string
     * @param indentString string to use for indenting
     *
     * @return a List of Strings holding the content
     */

    public static List inspectViewable(final Object viewable,
                                       final boolean drilldown,
                                       final int indentLevel,
                                       final String indentString)
    {
        List objects = new ArrayList();

        if (viewable instanceof POIFSViewable)
        {
            POIFSViewable inspected = ( POIFSViewable ) viewable;

            objects.add(indent(indentLevel, indentString,
                               inspected.getShortDescription()));
            if (drilldown)
            {
                if (inspected.preferArray())
                {
                    Object[] data = inspected.getViewableArray();

                    for (int j = 0; j < data.length; j++)
                    {
                        objects.addAll(inspectViewable(data[ j ], drilldown,
                                                       indentLevel + 1,
                                                       indentString));
                    }
                }
                else
                {
                    Iterator iter = inspected.getViewableIterator();

                    while (iter.hasNext())
                    {
                        objects.addAll(inspectViewable(iter.next(),
                                                       drilldown,
                                                       indentLevel + 1,
                                                       indentString));
                    }
                }
            }
        }
        else
        {
            objects.add(indent(indentLevel, indentString,
                               viewable.toString()));
        }
        return objects;
    }

    private static String indent(final int indentLevel,
                                 final String indentString, final String data)
    {
        StringBuffer finalBuffer  = new StringBuffer();
        StringBuffer indentPrefix = new StringBuffer();

        for (int j = 0; j < indentLevel; j++)
        {
            indentPrefix.append(indentString);
        }
        LineNumberReader reader =
            new LineNumberReader(new StringReader(data));

        try
        {
            String line = reader.readLine();

            while (line != null)
            {
                finalBuffer.append(indentPrefix).append(line).append(_EOL);
                line = reader.readLine();
            }
        }
        catch (IOException e)
        {
            finalBuffer.append(indentPrefix).append(e.getMessage())
                .append(_EOL);
        }
        return finalBuffer.toString();
    }

