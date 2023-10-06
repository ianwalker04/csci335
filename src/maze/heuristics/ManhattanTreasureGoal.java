package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.Set;
import java.util.function.ToIntFunction;

// Non-monotonic
// Sum total of ManhattanGoal + ManhattanTreasureTotal.

public class ManhattanTreasureGoal implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer node) {
        Pos goal = node.getGoal().getLocation();
        Pos location = node.getLocation();
        int total = 0;
        Set<Pos> treasure = node.getAllTreasureFromMaze();
        if (!treasure.isEmpty()) {
            for (Pos position : treasure) {
                if (!node.getAllTreasureFound().contains(position)) {
                    total += position.getManhattanDist(location);
                }
            }
        }
        return total + location.getManhattanDist(goal);
    }

}