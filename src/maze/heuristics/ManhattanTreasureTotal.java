package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.Set;
import java.util.function.ToIntFunction;

// Non-monotonic
// If there is treasure, return a sum total of the distances between the start and all the individual treasures.
// If not, then use ManhattanGoal.

public class ManhattanTreasureTotal implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer node) {
        Pos goal = node.getGoal().getLocation();
        Pos location = node.getLocation();
        int total = 0;
        Set<Pos> treasure = node.getAllTreasureFromMaze();
        if (!treasure.isEmpty()) {
            for (Pos position : treasure) {
                total += position.getManhattanDist(node.getLocation());
                return total;
            }
        }
        return location.getManhattanDist(goal);
    }

}