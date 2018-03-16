import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;

/**
 * Class responsible for access to stored document fields.
 *
 * It uses &lt;segment&gt;.fdt and &lt;segment&gt;.fdx; files.
 *
 * @version $Id: FieldsReader.java 405870 2006-05-12 21:04:00Z dnaber $
 */
final class FieldsReader {
  private FieldInfos fieldInfos;
  private IndexInput fieldsStream;
  private IndexInput indexStream;
  private int size;

  FieldsReader(Directory d, String segment, FieldInfos fn) throws IOException {
    fieldInfos = fn;

    fieldsStream = d.openInput(segment + ".fdt");
    indexStream = d.openInput(segment + ".fdx");

    size = (int)(indexStream.length() / 8);
  }

  final void close() throws IOException {
    fieldsStream.close();
    indexStream.close();
  }

  final int size() {
    return size;
  }

  final Document doc(int n) throws IOException {
    indexStream.seek(n * 8L);
    long position = indexStream.readLong();
    fieldsStream.seek(position);

    Document doc = new Document();
    int numFields = fieldsStream.readVInt();
    for (int i = 0; i < numFields; i++) {
      int fieldNumber = fieldsStream.readVInt();
      FieldInfo fi = fieldInfos.fieldInfo(fieldNumber);

      byte bits = fieldsStream.readByte();
      
      boolean compressed = (bits & FieldsWriter.FIELD_IS_COMPRESSED) != 0;
      boolean tokenize = (bits & FieldsWriter.FIELD_IS_TOKENIZED) != 0;
      
      if ((bits & FieldsWriter.FIELD_IS_BINARY) != 0) {
        final byte[] b = new byte[fieldsStream.readVInt()];
        fieldsStream.readBytes(b, 0, b.length);
        if (compressed)
          doc.add(new Field(fi.name, uncompress(b), Field.Store.COMPRESS));
        else
          doc.add(new Field(fi.name, b, Field.Store.YES));
      }
      else {
        Field.Index index;
        Field.Store store = Field.Store.YES;
        
        if (fi.isIndexed && tokenize)
          index = Field.Index.TOKENIZED;
        else if (fi.isIndexed && !tokenize)
          index = Field.Index.UN_TOKENIZED;
        else
          index = Field.Index.NO;
        
        Field.TermVector termVector = null;
        if (fi.storeTermVector) {
          if (fi.storeOffsetWithTermVector) {
            if (fi.storePositionWithTermVector) {
              termVector = Field.TermVector.WITH_POSITIONS_OFFSETS;
            }
            else {
              termVector = Field.TermVector.WITH_OFFSETS;
            }
          }
          else if (fi.storePositionWithTermVector) {
            termVector = Field.TermVector.WITH_POSITIONS;
          }
          else {
            termVector = Field.TermVector.YES;
          }
        }
        else {
          termVector = Field.TermVector.NO;
        }
        
        if (compressed) {
          store = Field.Store.COMPRESS;
          final byte[] b = new byte[fieldsStream.readVInt()];
          fieldsStream.readBytes(b, 0, b.length);
              store,
              index,
              termVector);
          f.setOmitNorms(fi.omitNorms);
          doc.add(f);
        }
        else {
                store,
                index,
                termVector);
          f.setOmitNorms(fi.omitNorms);
          doc.add(f);
        }
      }
    }

    return doc;
  }
  
  private final byte[] uncompress(final byte[] input)
    throws IOException
  {
  
    Inflater decompressor = new Inflater();
    decompressor.setInput(input);
  
    ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
  
    byte[] buf = new byte[1024];
    while (!decompressor.finished()) {
      try {
        int count = decompressor.inflate(buf);
        bos.write(buf, 0, count);
      }
      catch (DataFormatException e) {
        IOException newException = new IOException("field data are in wrong format: " + e.toString());
        newException.initCause(e);
        throw newException;
      }
    }
  
    decompressor.end();
    
    return bos.toByteArray();
  }
}
