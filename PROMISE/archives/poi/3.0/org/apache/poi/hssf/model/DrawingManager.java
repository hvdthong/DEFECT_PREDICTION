   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at


   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.hssf.model;

import org.apache.poi.ddf.EscherDggRecord;
import org.apache.poi.ddf.EscherDgRecord;

import java.util.Map;
import java.util.HashMap;

/**
 * Provides utilities to manage drawing groups.
 *
 * @author Glen Stampoultzis (glens at apache.org)
 */
public class DrawingManager
{
    EscherDggRecord dgg;

    public DrawingManager( EscherDggRecord dgg )
    {
        this.dgg = dgg;
    }

    public EscherDgRecord createDgRecord()
    {
        EscherDgRecord dg = new EscherDgRecord();
        dg.setRecordId( EscherDgRecord.RECORD_ID );
        short dgId = findNewDrawingGroupId();
        dg.setOptions( (short) ( dgId << 4 ) );
        dg.setNumShapes( 0 );
        dg.setLastMSOSPID( -1 );
        dgg.addCluster( dgId, 0 );
        dgg.setDrawingsSaved( dgg.getDrawingsSaved() + 1 );
        dgMap.put( new Short( dgId ), dg );
        return dg;
    }

    /**
     * Allocates new shape id for the new drawing group id.
     *
     * @return a new shape id.
     */
    public int allocateShapeId(short drawingGroupId)
    {
        EscherDgRecord dg = (EscherDgRecord) dgMap.get(new Short(drawingGroupId));
        int lastShapeId = dg.getLastMSOSPID();


        int newShapeId = 0;
        if (lastShapeId % 1024 == 1023)
        {
            newShapeId = findFreeSPIDBlock();
            dgg.addCluster(drawingGroupId, 1);
        }
        else
        {
            for (int i = 0; i < dgg.getFileIdClusters().length; i++)
            {
                EscherDggRecord.FileIdCluster c = dgg.getFileIdClusters()[i];
                if (c.getDrawingGroupId() == drawingGroupId)
                {
                    if (c.getNumShapeIdsUsed() != 1024)
                    {
                        c.incrementShapeId();
                    }
                }
                if (dg.getLastMSOSPID() == -1)
                {
                    newShapeId = findFreeSPIDBlock();
                }
                else
                {
                    newShapeId = dg.getLastMSOSPID() + 1;
                }
            }
        }
        dgg.setNumShapesSaved(dgg.getNumShapesSaved() + 1);
        if (newShapeId >= dgg.getShapeIdMax())
        {
            dgg.setShapeIdMax(newShapeId + 1);
        }
        dg.setLastMSOSPID(newShapeId);
        dg.incrementShapeCount();


        return newShapeId;
    }

    short findNewDrawingGroupId()
    {
        short dgId = 1;
        while ( drawingGroupExists( dgId ) )
            dgId++;
        return dgId;
    }

    boolean drawingGroupExists( short dgId )
    {
        for ( int i = 0; i < dgg.getFileIdClusters().length; i++ )
        {
            if ( dgg.getFileIdClusters()[i].getDrawingGroupId() == dgId )
                return true;
        }
        return false;
    }

    int findFreeSPIDBlock()
    {
        int max = dgg.getShapeIdMax();
        int next = ( ( max / 1024 ) + 1 ) * 1024;
        return next;
    }

    public EscherDggRecord getDgg()
    {
        return dgg;
    }

}
