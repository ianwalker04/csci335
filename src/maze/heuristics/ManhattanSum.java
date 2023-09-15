package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.Set;
import java.util.function.ToIntFunction;

// Non-monotonic
// If no treasure, use ManhattanGoal, if there is treasure, use ManhattanTreasureTotal.

public class ManhattanSum implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer node) {
        Pos goal = node.getGoal().getLocation();
        Pos location = node.getLocation();
        Set<Pos> treasure = node.getAllTreasureFromMaze();
        int total = 0;
        if (treasure.isEmpty()) {
            return location.getManhattanDist(goal);
        } else {
            for (Pos position : treasure) {
                total += position.getManhattanDist(location);
            }
            return total;
        }
    }

}