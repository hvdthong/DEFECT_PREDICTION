import java.io.IOException;
import java.io.Reader;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

/**
 * Holds state for inverting all occurrences of a single
 * field in the document.  This class doesn't do anything
 * itself; instead, it forwards the tokens produced by
 * analysis to its own consumer
 * (InvertedDocConsumerPerField).  It also interacts with an
 * endConsumer (InvertedDocEndConsumerPerField).
 */

final class DocInverterPerField extends DocFieldConsumerPerField {

  final private DocInverterPerThread perThread;
  final private FieldInfo fieldInfo;
  final InvertedDocConsumerPerField consumer;
  final InvertedDocEndConsumerPerField endConsumer;
  final DocumentsWriter.DocState docState;
  final DocInverter.FieldInvertState fieldState;

  public DocInverterPerField(DocInverterPerThread perThread, FieldInfo fieldInfo) {
    this.perThread = perThread;
    this.fieldInfo = fieldInfo;
    docState = perThread.docState;
    fieldState = perThread.fieldState;
    this.consumer = perThread.consumer.addField(this, fieldInfo);
    this.endConsumer = perThread.endConsumer.addField(this, fieldInfo);
  }

  void abort() {
    consumer.abort();
    endConsumer.abort();
  }

  public void processFields(final Fieldable[] fields,
                            final int count) throws IOException {

    fieldState.reset(docState.doc.getBoost());

    final int maxFieldLength = docState.maxFieldLength;

    final boolean doInvert = consumer.start(fields, count);

    for(int i=0;i<count;i++) {

      final Fieldable field = fields[i];

      if (field.isIndexed() && doInvert) {

        if (fieldState.length > 0)
          fieldState.position += docState.analyzer.getPositionIncrementGap(fieldInfo.name);

          String stringValue = field.stringValue();
          final int valueLength = stringValue.length();
          Token token = perThread.localToken.reinit(stringValue, fieldState.offset, fieldState.offset + valueLength);
          boolean success = false;
          try {
            consumer.add(token);
            success = true;
          } finally {
            if (!success)
              docState.docWriter.setAborting();
          }
          fieldState.offset += valueLength;
          fieldState.length++;
          fieldState.position++;
          final TokenStream stream;
          final TokenStream streamValue = field.tokenStreamValue();

          if (streamValue != null) 
            stream = streamValue;
          else {
            final Reader readerValue = field.readerValue();

            if (readerValue != null)
              reader = readerValue;
            else {
              String stringValue = field.stringValue();
              if (stringValue == null)
                throw new IllegalArgumentException("field must have either TokenStream, String or Reader value");
              perThread.stringReader.init(stringValue);
              reader = perThread.stringReader;
            }
          
            stream = docState.analyzer.reusableTokenStream(fieldInfo.name, reader);
          }

          stream.reset();

          try {
            int offsetEnd = fieldState.offset-1;
            final Token localToken = perThread.localToken;
            for(;;) {

              Token token = stream.next(localToken);

              if (token == null) break;
              fieldState.position += (token.getPositionIncrement() - 1);
              boolean success = false;
              try {
                consumer.add(token);
                success = true;
              } finally {
                if (!success)
                  docState.docWriter.setAborting();
              }
              fieldState.position++;
              offsetEnd = fieldState.offset + token.endOffset();
              if (++fieldState.length >= maxFieldLength) {
                if (docState.infoStream != null)
                  docState.infoStream.println("maxFieldLength " +maxFieldLength+ " reached for field " + fieldInfo.name + ", ignoring following tokens");
                break;
              }
            }
            fieldState.offset = offsetEnd+1;
          } finally {
            stream.close();
          }
        }

        fieldState.boost *= field.getBoost();
      }
    }

    consumer.finish();
    endConsumer.finish();
  }
}
