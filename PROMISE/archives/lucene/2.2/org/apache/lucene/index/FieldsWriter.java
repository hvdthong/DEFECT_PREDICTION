import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.Deflater;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexOutput;

final class FieldsWriter
{
  static final byte FIELD_IS_TOKENIZED = 0x1;
  static final byte FIELD_IS_BINARY = 0x2;
  static final byte FIELD_IS_COMPRESSED = 0x4;
  
    private FieldInfos fieldInfos;

    private IndexOutput fieldsStream;

    private IndexOutput indexStream;

    FieldsWriter(Directory d, String segment, FieldInfos fn) throws IOException {
        fieldInfos = fn;
        fieldsStream = d.createOutput(segment + ".fdt");
        indexStream = d.createOutput(segment + ".fdx");
    }

    final void close() throws IOException {
        fieldsStream.close();
        indexStream.close();
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
            boolean disableCompression = (field instanceof FieldsReader.FieldForMerge);
            if (field.isStored()) {
                fieldsStream.writeVInt(fieldInfos.fieldNumber(field.name()));

                byte bits = 0;
                if (field.isTokenized())
                    bits |= FieldsWriter.FIELD_IS_TOKENIZED;
                if (field.isBinary())
                    bits |= FieldsWriter.FIELD_IS_BINARY;
                if (field.isCompressed())
                    bits |= FieldsWriter.FIELD_IS_COMPRESSED;
                
                fieldsStream.writeByte(bits);
                
                if (field.isCompressed()) {
                  byte[] data = null;
                  
                  if (disableCompression) {
                      data = field.binaryValue();
                  } else {
                      if (field.isBinary()) {
                        data = compress(field.binaryValue());
                      }
                      else {
                        data = compress(field.stringValue().getBytes("UTF-8"));
                      }
                  }
                  final int len = data.length;
                  fieldsStream.writeVInt(len);
                  fieldsStream.writeBytes(data, len);
                }
                else {
                  if (field.isBinary()) {
                    byte[] data = field.binaryValue();
                    final int len = data.length;
                    fieldsStream.writeVInt(len);
                    fieldsStream.writeBytes(data, len);
                  }
                  else {
                    fieldsStream.writeString(field.stringValue());
                  }
                }
            }
        }
    }

    private final byte[] compress (byte[] input) {

      Deflater compressor = new Deflater();
      compressor.setLevel(Deflater.BEST_COMPRESSION);

      compressor.setInput(input);
      compressor.finish();

      /*
       * Create an expandable byte array to hold the compressed data.
       * You cannot use an array that's the same size as the orginal because
       * there is no guarantee that the compressed data will be smaller than
       * the uncompressed data.
       */
      ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);

      byte[] buf = new byte[1024];
      while (!compressor.finished()) {
        int count = compressor.deflate(buf);
        bos.write(buf, 0, count);
      }
      
      compressor.end();

      return bos.toByteArray();
    }
}
