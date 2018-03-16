import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.Deflater;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMOutputStream;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.IndexInput;

final class FieldsWriter
{
  static final byte FIELD_IS_TOKENIZED = 0x1;
  static final byte FIELD_IS_BINARY = 0x2;
  static final byte FIELD_IS_COMPRESSED = 0x4;

  static final int FORMAT = 0;

  static final int FORMAT_VERSION_UTF8_LENGTH_IN_BYTES = 1;

  static final int FORMAT_CURRENT = FORMAT_VERSION_UTF8_LENGTH_IN_BYTES;
  
    private FieldInfos fieldInfos;

    private IndexOutput fieldsStream;

    private IndexOutput indexStream;

    private boolean doClose;

    FieldsWriter(Directory d, String segment, FieldInfos fn) throws IOException {
        fieldInfos = fn;

        boolean success = false;
        final String fieldsName = segment + "." + IndexFileNames.FIELDS_EXTENSION;
        try {
          fieldsStream = d.createOutput(fieldsName);
          fieldsStream.writeInt(FORMAT_CURRENT);
          success = true;
        } finally {
          if (!success) {
            try {
              close();
            } catch (Throwable t) {
            }
            try {
              d.deleteFile(fieldsName);
            } catch (Throwable t) {
            }
          }
        }

        success = false;
        final String indexName = segment + "." + IndexFileNames.FIELDS_INDEX_EXTENSION;
        try {
          indexStream = d.createOutput(indexName);
          indexStream.writeInt(FORMAT_CURRENT);
          success = true;
        } finally {
          if (!success) {
            try {
              close();
            } catch (IOException ioe) {
            }
            try {
              d.deleteFile(fieldsName);
            } catch (Throwable t) {
            }
            try {
              d.deleteFile(indexName);
            } catch (Throwable t) {
            }
          }
        }

        doClose = true;
    }

    FieldsWriter(IndexOutput fdx, IndexOutput fdt, FieldInfos fn) {
        fieldInfos = fn;
        fieldsStream = fdt;
        indexStream = fdx;
        doClose = false;
    }

    void setFieldsStream(IndexOutput stream) {
      this.fieldsStream = stream;
    }

    void flushDocument(int numStoredFields, RAMOutputStream buffer) throws IOException {
      indexStream.writeLong(fieldsStream.getFilePointer());
      fieldsStream.writeVInt(numStoredFields);
      buffer.writeTo(fieldsStream);
    }

    void skipDocument() throws IOException {
      indexStream.writeLong(fieldsStream.getFilePointer());
      fieldsStream.writeVInt(0);
    }

    void flush() throws IOException {
      indexStream.flush();
      fieldsStream.flush();
    }

    final void close() throws IOException {
      if (doClose) {

        try {
          if (fieldsStream != null) {
            try {
              fieldsStream.close();
            } finally {
              fieldsStream = null;
            }
          }
        } catch (IOException ioe) {
          try {
            if (indexStream != null) {
              try {
                indexStream.close();
              } finally {
                indexStream = null;
              }
            }
          } catch (IOException ioe2) {
          }
          throw ioe;
        } finally {
          if (indexStream != null) {
            try {
              indexStream.close();
            } finally {
              indexStream = null;
            }
          }
        }
      }
    }

    final void writeField(FieldInfo fi, Fieldable field) throws IOException {
      boolean disableCompression = (field instanceof FieldsReader.FieldForMerge);
      fieldsStream.writeVInt(fi.number);
      byte bits = 0;
      if (field.isTokenized())
        bits |= FieldsWriter.FIELD_IS_TOKENIZED;
      if (field.isBinary())
        bits |= FieldsWriter.FIELD_IS_BINARY;
      if (field.isCompressed())
        bits |= FieldsWriter.FIELD_IS_COMPRESSED;
                
      fieldsStream.writeByte(bits);
                
      if (field.isCompressed()) {
        final byte[] data;
        final int len;
        final int offset;
        if (disableCompression) {
          data = field.getBinaryValue();
          assert data != null;
          len = field.getBinaryLength();
          offset = field.getBinaryOffset();  
        } else {
          if (field.isBinary()) {
            data = compress(field.getBinaryValue(), field.getBinaryOffset(), field.getBinaryLength());
          } else {
            byte x[] = field.stringValue().getBytes("UTF-8");
            data = compress(x, 0, x.length);
          }
          len = data.length;
          offset = 0;
        }
        
        fieldsStream.writeVInt(len);
        fieldsStream.writeBytes(data, offset, len);
      }
      else {
        if (field.isBinary()) {
          final byte[] data;
          final int len;
          final int offset;
          data = field.getBinaryValue();
          len = field.getBinaryLength();
          offset =  field.getBinaryOffset();

          fieldsStream.writeVInt(len);
          fieldsStream.writeBytes(data, offset, len);
        }
        else {
          fieldsStream.writeString(field.stringValue());
        }
      }
    }

    /** Bulk write a contiguous series of documents.  The
     *  lengths array is the length (in bytes) of each raw
     *  document.  The stream IndexInput is the
     *  fieldsStream from which we should bulk-copy all
     *  bytes. */
    final void addRawDocuments(IndexInput stream, int[] lengths, int numDocs) throws IOException {
      long position = fieldsStream.getFilePointer();
      long start = position;
      for(int i=0;i<numDocs;i++) {
        indexStream.writeLong(position);
        position += lengths[i];
      }
      fieldsStream.copyBytes(stream, position-start);
      assert fieldsStream.getFilePointer() == position;
    }

    final void addDocument(Document doc) throws IOException {
        indexStream.writeLong(fieldsStream.getFilePointer());

        int storedCount = 0;
        Iterator fieldIterator = doc.getFields().iterator();
        while (fieldIterator.hasNext()) {
            Fieldable field = (Fieldable) fieldIterator.next();
            if (field.isStored())
                storedCount++;
        }
        fieldsStream.writeVInt(storedCount);

        fieldIterator = doc.getFields().iterator();
        while (fieldIterator.hasNext()) {
            Fieldable field = (Fieldable) fieldIterator.next();
            if (field.isStored())
              writeField(fieldInfos.fieldInfo(field.name()), field);
        }
    }

    private final byte[] compress (byte[] input, int offset, int length) {
      Deflater compressor = new Deflater();
      compressor.setLevel(Deflater.BEST_COMPRESSION);

      compressor.setInput(input, offset, length);
      compressor.finish();

      /*
       * Create an expandable byte array to hold the compressed data.
       * You cannot use an array that's the same size as the orginal because
       * there is no guarantee that the compressed data will be smaller than
       * the uncompressed data.
       */
      ByteArrayOutputStream bos = new ByteArrayOutputStream(length);

      try {
        compressor.setLevel(Deflater.BEST_COMPRESSION);

        compressor.setInput(input);
        compressor.finish();

        byte[] buf = new byte[1024];
        while (!compressor.finished()) {
          int count = compressor.deflate(buf);
          bos.write(buf, 0, count);
        }

      } finally {      
        compressor.end();
      }

      return bos.toByteArray();
    }
}
