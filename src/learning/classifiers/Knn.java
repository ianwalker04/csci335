package learning.classifiers;

import core.Duple;
import learning.core.Classifier;
import learning.core.Histogram;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.function.ToDoubleBiFunction;

// KnnTest.test() should pass once this is finished.
public class Knn<V, L> implements Classifier<V, L> {
    private ArrayList<Duple<V, L>> data = new ArrayList<>();
    private ToDoubleBiFunction<V, V> distance;
    private int k;

    public Knn(int k, ToDoubleBiFunction<V, V> distance) {
        this.k = k;
        this.distance = distance;
    }

    @Override
    public L classify(V value) {
        PriorityQueue<Duple<V, L>> queue = new PriorityQueue<>((d1, d2) ->
                Double.compare(distance.applyAsDouble(value, d2.getFirst()),  distance.applyAsDouble(value, d1.getFirst())));
        Histogram<L> hist = new Histogram<>();
        for (Duple<V, L> datum : data) {
            queue.add(datum);
            if (distance.applyAsDouble(datum.getFirst(), value) <= distance.applyAsDouble(queue.peek().getFirst(), value)) {
                queue.remove();
                queue.add(datum);
            }
        }
        for (Duple<V, L> pair : queue) {
            hist.bump(pair.getSecond());
        }
        return hist.getPluralityWinner();
    }

    @Override
    public void train(ArrayList<Duple<V, L>> training) {
        data.addAll(training);
    }
}
