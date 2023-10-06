package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.Set;
import java.util.function.ToIntFunction;

// Monotonic
// If there is treasure, then return the sum total of all the distances between each unclaimed treasure and the goal.
// If there is no treasure, then use ManhattanGoal.

public class ManhattanTreasure implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer node) {
        Pos goal = node.getGoal().getLocation();
        Pos location = node.getLocation();
        int total = 0;
        Set<Pos> treasure = node.getAllTreasureFromMaze();
        if (!treasure.isEmpty()) {
            for (Pos position : treasure) {
                if (!node.getAllTreasureFound().contains(position)) {
                    total += position.getManhattanDist(goal);
                    return total;
                }
            }
        }
        return location.getManhattanDist(goal);
    }

}