package learning.som;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;

public class SelfOrgMap<V> {
    private V[][] map;
    private ToDoubleBiFunction<V, V> distance;
    private WeightedAverager<V> averager;

    public SelfOrgMap(int side, Supplier<V> makeDefault, ToDoubleBiFunction<V, V> distance, WeightedAverager<V> averager) {
        this.distance = distance;
        this.averager = averager;
        map = (V[][])new Object[side][side];
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                map[i][j] = makeDefault.get();
            }
        }
    }

    // TODO: Return a SOMPoint corresponding to the map square which has the
    //  smallest distance compared to example.
    // NOTE: The unit tests assume that the map is traversed in row-major order,
    //  that is, the y-coordinate is updated in the outer loop, and the x-coordinate
    //  is updated in the inner loop.
    public SOMPoint bestFor(V example) {
        SOMPoint closestPoint = null;
        double smallestDist = Double.MAX_VALUE;
        for (int y = 0; y < getMapHeight(); y++) {
            for (int x = 0; x < getMapWidth(); x++) {
                V point = getNode(x, y);
                double currentDist = distance.applyAsDouble(point, example);
                if (currentDist < smallestDist) {
                    smallestDist = currentDist;
                    closestPoint = new SOMPoint(x, y);
                }
            }
        }
        return closestPoint;
    }

    // TODO: Train this SOM with example.
    //  1. Find the best matching node.
    //  2. Update the best matching node with the average of itself and example,
    //     using a learning rate of 0.9.
    //  3. Update each neighbor of the best matching node that is in the map,
    //     using a learning rate of 0.4.
    public void train(V example) {
        SOMPoint bestNode = bestFor(example);
        V bestMatching = getNode(bestNode.x(), bestNode.y());
        V averageNode = averager.weightedAverage(example, bestMatching, 0.9);
        map[bestNode.x()][bestNode.y()] = averageNode;
        for (SOMPoint neighbor : bestNode.neighbors()) {
            if (inMap(neighbor)) {
                V neighborNode = getNode(neighbor.x(), neighbor.y());
                V averageNeighbor = averager.weightedAverage(example, neighborNode, 0.4);
                map[neighbor.x()][neighbor.y()] = averageNeighbor;
            }
        }
    }

    public V getNode(int x, int y) {
        return map[x][y];
    }

    public int getMapWidth() {
        return map.length;
    }

    public int getMapHeight() {
        return map[0].length;
    }

    public int numMapNodes() {
        return getMapHeight() * getMapWidth();
    }

    public boolean inMap(SOMPoint point) {
        return point.x() >= 0 && point.x() < getMapWidth() && point.y() >= 0 && point.y() < getMapHeight();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof SelfOrgMap that) {
            if (this.getMapHeight() == that.getMapHeight() && this.getMapWidth() == that.getMapWidth()) {
                for (int x = 0; x < getMapWidth(); x++) {
                    for (int y = 0; y < getMapHeight(); y++) {
                        if (!map[x][y].equals(that.map[x][y])) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int x = 0; x < getMapWidth(); x++) {
            for (int y = 0; y < getMapHeight(); y++) {
                result.append(String.format("(%d, %d):\n", x, y));
                result.append(getNode(x, y));
            }
        }
        return result.toString();
    }
}