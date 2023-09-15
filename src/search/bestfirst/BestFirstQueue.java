package search.bestfirst;

import search.SearchNode;
import search.SearchQueue;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.ToIntFunction;
import java.util.PriorityQueue;

public class BestFirstQueue<T> implements SearchQueue<T> {
    private PriorityQueue<SearchNode<T>> queue = new PriorityQueue<>(new Comparator<SearchNode<T>>() {
        @Override
        public int compare(SearchNode<T> o1, SearchNode<T> o2) {
            int estimate1 = heuristic.applyAsInt(o1.getValue()) + o1.getDepth();
            int estimate2 = heuristic.applyAsInt(o2.getValue()) + o2.getDepth();
            return Integer.compare(estimate1, estimate2);
        }
    });
    private HashMap<T, Integer> visited = new HashMap<>();
    private ToIntFunction<T> heuristic;

    public BestFirstQueue(ToIntFunction<T> heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public void enqueue(SearchNode<T> node) {
        int estimate = heuristic.applyAsInt(node.getValue()) + node.getDepth();
        if (!visited.containsKey(node.getValue()) || estimate < visited.get(node.getValue())) {
            queue.add(node);
            visited.put(node.getValue(), estimate);
        }
    }

    @Override
    public Optional<SearchNode<T>> dequeue() {
        if (queue.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(queue.remove());
        }
    }
}
