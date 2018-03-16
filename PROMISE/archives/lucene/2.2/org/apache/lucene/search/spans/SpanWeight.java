import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Expert-only.  Public for use by other weight implementations
 */
public class SpanWeight implements Weight {
  protected Similarity similarity;
  protected float value;
  protected float idf;
  protected float queryNorm;
  protected float queryWeight;

  protected Set terms;
  protected SpanQuery query;

  public SpanWeight(SpanQuery query, Searcher searcher)
    throws IOException {
    this.similarity = query.getSimilarity(searcher);
    this.query = query;
    terms=new HashSet();
    query.extractTerms(terms);

    idf = this.query.getSimilarity(searcher).idf(terms, searcher);
  }

  public Query getQuery() { return query; }
  public float getValue() { return value; }

  public float sumOfSquaredWeights() throws IOException {
  }

  public void normalize(float queryNorm) {
    this.queryNorm = queryNorm;
  }

  public Scorer scorer(IndexReader reader) throws IOException {
    return new SpanScorer(query.getSpans(reader), this,
                          similarity,
                          reader.norms(query.getField()));
  }

  public Explanation explain(IndexReader reader, int doc)
    throws IOException {

    ComplexExplanation result = new ComplexExplanation();
    result.setDescription("weight("+getQuery()+" in "+doc+"), product of:");
    String field = ((SpanQuery)getQuery()).getField();

    StringBuffer docFreqs = new StringBuffer();
    Iterator i = terms.iterator();
    while (i.hasNext()) {
      Term term = (Term)i.next();
      docFreqs.append(term.text());
      docFreqs.append("=");
      docFreqs.append(reader.docFreq(term));

      if (i.hasNext()) {
        docFreqs.append(" ");
      }
    }

    Explanation idfExpl =
      new Explanation(idf, "idf(" + field + ": " + docFreqs + ")");

    Explanation queryExpl = new Explanation();
    queryExpl.setDescription("queryWeight(" + getQuery() + "), product of:");

    Explanation boostExpl = new Explanation(getQuery().getBoost(), "boost");
    if (getQuery().getBoost() != 1.0f)
      queryExpl.addDetail(boostExpl);
    queryExpl.addDetail(idfExpl);

    Explanation queryNormExpl = new Explanation(queryNorm,"queryNorm");
    queryExpl.addDetail(queryNormExpl);

    queryExpl.setValue(boostExpl.getValue() *
                       idfExpl.getValue() *
                       queryNormExpl.getValue());

    result.addDetail(queryExpl);

    ComplexExplanation fieldExpl = new ComplexExplanation();
    fieldExpl.setDescription("fieldWeight("+field+":"+query.toString(field)+
                             " in "+doc+"), product of:");

    Explanation tfExpl = scorer(reader).explain(doc);
    fieldExpl.addDetail(tfExpl);
    fieldExpl.addDetail(idfExpl);

    Explanation fieldNormExpl = new Explanation();
    byte[] fieldNorms = reader.norms(field);
    float fieldNorm =
      fieldNorms!=null ? Similarity.decodeNorm(fieldNorms[doc]) : 0.0f;
    fieldNormExpl.setValue(fieldNorm);
    fieldNormExpl.setDescription("fieldNorm(field="+field+", doc="+doc+")");
    fieldExpl.addDetail(fieldNormExpl);

    fieldExpl.setMatch(Boolean.valueOf(tfExpl.isMatch()));
    fieldExpl.setValue(tfExpl.getValue() *
                       idfExpl.getValue() *
                       fieldNormExpl.getValue());

    result.addDetail(fieldExpl);
    result.setMatch(fieldExpl.getMatch());

    result.setValue(queryExpl.getValue() * fieldExpl.getValue());

    if (queryExpl.getValue() == 1.0f)
      return fieldExpl;

    return result;
  }
}
