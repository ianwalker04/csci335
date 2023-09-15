package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.function.ToIntFunction;

// Monotonic
// Euclidean distance between the start and the end.

public class Euclidean implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer node) {
        Pos goal = node.getGoal().getLocation();
        Pos location = node.getLocation();
        int x = location.getX() - goal.getX();
        int y = location.getY() - goal.getY();
        return (int) Math.sqrt(x * x + y * y);
    }

}
