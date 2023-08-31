package learning.markov;

import learning.core.Histogram;

import java.util.*;

public class MarkovChain<L,S> {
    private LinkedHashMap<L, HashMap<Optional<S>, Histogram<S>>> label2symbol2symbol = new LinkedHashMap<>();

    public Set<L> allLabels() {return label2symbol2symbol.keySet();}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (L language: label2symbol2symbol.keySet()) {
            sb.append(language);
            sb.append('\n');
            for (Map.Entry<Optional<S>, Histogram<S>> entry: label2symbol2symbol.get(language).entrySet()) {
                sb.append("    ");
                sb.append(entry.getKey());
                sb.append(":");
                sb.append(entry.getValue().toString());
                sb.append('\n');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    // Increase the count for the transition from prev to next.
    // Should pass SimpleMarkovTest.testCreateChains().
    public void count(Optional<S> prev, L label, S next) {
        if (!label2symbol2symbol.containsKey(label)) {
            label2symbol2symbol.put(label, new HashMap<>());
        }
        if (!label2symbol2symbol.get(label).containsKey(prev)) {
            label2symbol2symbol.get(label).put(prev, new Histogram<>());
        }
        label2symbol2symbol.get(label).get(prev).bump(next);
    }

    // Returns P(sequence | label)
    // Should pass SimpleMarkovTest.testSourceProbabilities() and MajorMarkovTest.phraseTest()
    //
    // HINT: Be sure to add 1 to both the numerator and denominator when finding the probability of a
    // transition. This helps avoid sending the probability to zero.
    public double probability(ArrayList<S> sequence, L label) {
        double totalProbability = 1;
        Optional<S> prevChar = Optional.empty();
        for (int i = 0; i < sequence.size(); i++) {
            if (label2symbol2symbol.get(label).containsKey(prevChar)) {
                Histogram<S> h = label2symbol2symbol.get(label).get(prevChar);
                double num = h.getCountFor(sequence.get(i)) + 1;
                double prob = num / (h.getTotalCounts() + 1);
                totalProbability *= prob;
            }
            prevChar = Optional.of(sequence.get(i));
        }
        return totalProbability;
    }

    // Return a map from each label to P(label | sequence).
    // Call probability on every possible label in a sequence and return a LinkedHashMap where the language (L) is the key,
    // and the resulting probability is the value (double).
    // Should pass MajorMarkovTest.testSentenceDistributions()
    public LinkedHashMap<L, Double> labelDistribution(ArrayList<S> sequence) {
        LinkedHashMap<L, Double> distMap = new LinkedHashMap<>();
        double totalProbability = 0.0;
        for (L label: label2symbol2symbol.keySet()) {
            distMap.put(label, probability(sequence, label));
            totalProbability += probability(sequence, label);
        }

        for (L label : distMap.keySet()) {
            double normalProb = distMap.get(label) / totalProbability;
            distMap.put(label, normalProb);
        }

        return distMap;
    }

    // Calls labelDistribution(). Returns the label with the highest probability.
    // Should pass MajorMarkovTest.bestChainTest()
    public L bestMatchingChain(ArrayList<S> sequence) {
        LinkedHashMap<L, Double> probabilities = labelDistribution(sequence);
        return probabilities.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

}
