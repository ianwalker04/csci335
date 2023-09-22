package checkers.evaluators;

import checkers.core.Checkerboard;

import java.util.function.ToIntFunction;

public class ImprovedEvaluator implements ToIntFunction<Checkerboard> {
    public int applyAsInt(Checkerboard c) {
        int eval = c.numPiecesOf(c.getCurrentPlayer()) - c.numPiecesOf(c.getCurrentPlayer().opponent());
        eval += (c.numKingsOf(c.getCurrentPlayer()) - c.numKingsOf(c.getCurrentPlayer().opponent())) * 2;
        return eval;
    }
}
