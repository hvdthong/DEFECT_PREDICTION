final class MergeDocIDRemapper {

  public MergeDocIDRemapper(SegmentInfos infos, int[][] docMaps, int[] delCounts, MergePolicy.OneMerge merge, int mergedDocCount) {
    this.docMaps = docMaps;
    SegmentInfo firstSegment = merge.segments.info(0);
    int i = 0;
    while(true) {
      SegmentInfo info = infos.info(i);
      if (info.equals(firstSegment))
        break;
      minDocID += info.docCount;
      i++;
    }

    int numDocs = 0;
    for(int j=0;j<docMaps.length;i++,j++) {
      numDocs += infos.info(i).docCount;
      assert infos.info(i).equals(merge.segments.info(j));
    }
    maxDocID = minDocID + numDocs;

    starts = new int[docMaps.length];
    newStarts = new int[docMaps.length];

    starts[0] = minDocID;
    newStarts[0] = minDocID;
    for(i=1;i<docMaps.length;i++) {
      final int lastDocCount = merge.segments.info(i-1).docCount;
      starts[i] = starts[i-1] + lastDocCount;
      newStarts[i] = newStarts[i-1] + lastDocCount - delCounts[i-1];
    }
    docShift = numDocs - mergedDocCount;


    assert docShift == maxDocID - (newStarts[docMaps.length-1] + merge.segments.info(docMaps.length-1).docCount - delCounts[docMaps.length-1]);
  }

  public int remap(int oldDocID) {
    if (oldDocID < minDocID)
      return oldDocID;
    else if (oldDocID >= maxDocID)
      return oldDocID - docShift;
    else {

      while (hi >= lo) {
        int mid = (lo + hi) >> 1;
        int midValue = starts[mid];
        if (oldDocID < midValue)
          hi = mid - 1;
        else if (oldDocID > midValue)
          lo = mid + 1;
          while (mid+1 < docMaps.length && starts[mid+1] == midValue) {
          }
          if (docMaps[mid] != null)
            return newStarts[mid] + docMaps[mid][oldDocID-starts[mid]];
          else
            return newStarts[mid] + oldDocID-starts[mid];
        }
      }
      if (docMaps[hi] != null)
        return newStarts[hi] + docMaps[hi][oldDocID-starts[hi]];
      else
        return newStarts[hi] + oldDocID-starts[hi];
    }
  }
}
