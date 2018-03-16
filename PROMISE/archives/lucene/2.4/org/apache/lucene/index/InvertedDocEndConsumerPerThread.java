abstract class InvertedDocEndConsumerPerThread {
  abstract void startDocument();
  abstract InvertedDocEndConsumerPerField addField(DocInverterPerField docInverterPerField, FieldInfo fieldInfo);
  abstract void finishDocument();
  abstract void abort();
}
