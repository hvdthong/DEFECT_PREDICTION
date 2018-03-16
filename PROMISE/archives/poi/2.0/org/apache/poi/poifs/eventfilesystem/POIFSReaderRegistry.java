package org.apache.poi.poifs.eventfilesystem;

import java.util.*;

import org.apache.poi.poifs.filesystem.DocumentDescriptor;
import org.apache.poi.poifs.filesystem.POIFSDocumentPath;

/**
 * A registry for POIFSReaderListeners and the DocumentDescriptors of
 * the documents those listeners are interested in
 *
 * @author Marc Johnson (mjohnson at apache dot org)
 * @version %I%, %G%
 */

class POIFSReaderRegistry
{

    private Set omnivorousListeners;

    private Map selectiveListeners;

    private Map chosenDocumentDescriptors;

    /**
     * Construct the registry
     */

    POIFSReaderRegistry()
    {
        omnivorousListeners       = new HashSet();
        selectiveListeners        = new HashMap();
        chosenDocumentDescriptors = new HashMap();
    }

    /**
     * register a POIFSReaderListener for a particular document
     *
     * @param listener the listener
     * @param path the path of the document of interest
     * @param documentName the name of the document of interest
     */

    void registerListener(final POIFSReaderListener listener,
                          final POIFSDocumentPath path,
                          final String documentName)
    {
        if (!omnivorousListeners.contains(listener))
        {

            Set descriptors = ( Set ) selectiveListeners.get(listener);

            if (descriptors == null)
            {

                descriptors = new HashSet();
                selectiveListeners.put(listener, descriptors);
            }
            DocumentDescriptor descriptor = new DocumentDescriptor(path,
                                                documentName);

            if (descriptors.add(descriptor))
            {

                Set listeners =
                    ( Set ) chosenDocumentDescriptors.get(descriptor);

                if (listeners == null)
                {

                    listeners = new HashSet();
                    chosenDocumentDescriptors.put(descriptor, listeners);
                }
                listeners.add(listener);
            }
        }
    }

    /**
     * register for all documents
     *
     * @param listener the listener who wants to get all documents
     */

    void registerListener(final POIFSReaderListener listener)
    {
        if (!omnivorousListeners.contains(listener))
        {

            removeSelectiveListener(listener);
            omnivorousListeners.add(listener);
        }
    }

    /**
     * get am iterator of listeners for a particular document
     *
     * @param path the document path
     * @param name the name of the document
     *
     * @return an Iterator POIFSReaderListeners; may be empty
     */

    Iterator getListeners(final POIFSDocumentPath path, final String name)
    {
        Set rval               = new HashSet(omnivorousListeners);
        Set selectiveListeners =
            ( Set ) chosenDocumentDescriptors.get(new DocumentDescriptor(path,
                name));

        if (selectiveListeners != null)
        {
            rval.addAll(selectiveListeners);
        }
        return rval.iterator();
    }

    private void removeSelectiveListener(final POIFSReaderListener listener)
    {
        Set selectedDescriptors = ( Set ) selectiveListeners.remove(listener);

        if (selectedDescriptors != null)
        {
            Iterator iter = selectedDescriptors.iterator();

            while (iter.hasNext())
            {
                dropDocument(listener, ( DocumentDescriptor ) iter.next());
            }
        }
    }

    private void dropDocument(final POIFSReaderListener listener,
                              final DocumentDescriptor descriptor)
    {
        Set listeners = ( Set ) chosenDocumentDescriptors.get(descriptor);

        listeners.remove(listener);
        if (listeners.size() == 0)
        {
            chosenDocumentDescriptors.remove(descriptor);
        }
    }

