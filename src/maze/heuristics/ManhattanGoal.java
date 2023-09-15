package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.function.ToIntFunction;

// Monotonic
// Manhattan distance from the start to the end.

public class ManhattanGoal implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer node) {
        Pos goal = node.getGoal().getLocation();
        Pos location = node.getLocation();
        return location.getManhattanDist(goal);
    }

}