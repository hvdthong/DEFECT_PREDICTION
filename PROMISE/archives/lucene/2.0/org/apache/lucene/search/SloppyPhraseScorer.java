import org.apache.lucene.index.TermPositions;

import java.io.IOException;

final class SloppyPhraseScorer extends PhraseScorer {
    private int slop;

    SloppyPhraseScorer(Weight weight, TermPositions[] tps, int[] positions, Similarity similarity,
                       int slop, byte[] norms) {
        super(weight, tps, positions, similarity, norms);
        this.slop = slop;
    }

    protected final float phraseFreq() throws IOException {
        pq.clear();
        int end = 0;
        for (PhrasePositions pp = first; pp != null; pp = pp.next) {
            pp.firstPosition();
            if (pp.position > end)
                end = pp.position;
        }

        float freq = 0.0f;
        boolean done = false;
        do {
            PhrasePositions pp = (PhrasePositions) pq.pop();
            int start = pp.position;
            int next = ((PhrasePositions) pq.top()).position;
            for (int pos = start; pos <= next; pos = pp.position) {
                if (!pp.nextPosition()) {
                    break;
                }
            }

            int matchLength = end - start;
            if (matchLength <= slop)

            if (pp.position > end)
                end = pp.position;
        } while (!done);

        return freq;
    }
}
