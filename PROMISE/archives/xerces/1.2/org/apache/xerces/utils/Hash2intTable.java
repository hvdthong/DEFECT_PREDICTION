package org.apache.xerces.utils;

/**
 * A light-weight hashtable class that takes 2 ints as key and 1 int as value
 * @version
 */

public final class Hash2intTable {
    
    
    private static final int INITIAL_BUCKET_SIZE = 4;
    private static final int HASHTABLE_SIZE = 256;
    private int[][] fHashTable = new int[HASHTABLE_SIZE][];


    public void put(int key1, int key2, int key3, int value) {
        int hash = (key1+key2+key3+2) % HASHTABLE_SIZE;
        int[] bucket = fHashTable[hash];
        
        if (bucket == null) {
            bucket = new int[1 + 4*INITIAL_BUCKET_SIZE];
            bucket[0] = 1;
            bucket[1] = key1;
            bucket[2] = key2;
            bucket[3] = key3;
            bucket[4] = value;
            fHashTable[hash] = bucket;
        } else {
            int count = bucket[0];
            int offset = 1 + 4*count;
            if (offset == bucket.length) {
                int newSize = count + INITIAL_BUCKET_SIZE;
                int[] newBucket = new int[1 + 4*newSize];
                System.arraycopy(bucket, 0, newBucket, 0, offset);
                bucket = newBucket;
                fHashTable[hash] = bucket;
            }
            boolean found = false;
            int j=1;
            for (int i=0; i<count; i++){
                if ( bucket[j] == key1 && bucket[j+1] == key2
                     && bucket[j+2] == key3 ) {
                    bucket[j+3] = value;
                    found = true;
                    break;
                }
                j += 4;
            }
            if (! found) {
                bucket[offset++] = key1;
                bucket[offset++] = key2;
                bucket[offset++] = key3;
                bucket[offset]= value;
                bucket[0] = ++count;
            }
            
        }
    }

    public int get(int key1, int key2, int key3) {
        int hash = (key1+key2+key3+2) % HASHTABLE_SIZE;
        int[] bucket = fHashTable[hash];

        if (bucket == null) {
            return -1;
        }
        int count = bucket[0];

        int j=1;
        for (int i=0; i<count; i++){
            if ( bucket[j] == key1 && bucket[j+1] == key2
                  && bucket[j+2] == key3) {
                return bucket[j+3];
            }
            j += 4;
        }
        return -1;
    }


  












