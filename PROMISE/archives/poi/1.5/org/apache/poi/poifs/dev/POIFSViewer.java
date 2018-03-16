package org.apache.poi.poifs.dev;

import java.io.*;

import java.util.*;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * A simple viewer for POIFS files
 *
 * @author Marc Johnson (mjohnson at apache dot org)
 */

public class POIFSViewer
{

    /**
     * Display the contents of multiple POIFS files
     *
     * @param args the names of the files to be displayed
     */

    public static void main(final String args[])
    {
        if (args.length < 0)
        {
            System.err.println("Must specify at least one file to view");
            System.exit(1);
        }
        boolean printNames = (args.length > 1);

        for (int j = 0; j < args.length; j++)
        {
            viewFile(args[ j ], printNames);
        }
    }

    private static void viewFile(final String filename,
                                 final boolean printName)
    {
        if (printName)
        {
            StringBuffer flowerbox = new StringBuffer();

            flowerbox.append(".");
            for (int j = 0; j < filename.length(); j++)
            {
                flowerbox.append("-");
            }
            flowerbox.append(".");
            System.out.println(flowerbox);
            System.out.println("|" + filename + "|");
            System.out.println(flowerbox);
        }
        try
        {
            POIFSViewable fs      =
                new POIFSFileSystem(new FileInputStream(filename));
            List          strings = POIFSViewEngine.inspectViewable(fs, true,
                                        0, "  ");
            Iterator      iter    = strings.iterator();

            while (iter.hasNext())
            {
                System.out.print(iter.next());
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

