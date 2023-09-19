package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import core.Duple;

import java.util.Optional;
import java.util.Set;
import java.util.function.ToIntFunction;

public class NegaMax extends CheckersSearcher {
    private int numNodes = 0;

    public NegaMax(ToIntFunction<Checkerboard> e) {
        super(e);
    }

    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    public Duple<Integer, Move> NegaMaxFunction(Checkerboard board, int depthLimit) {
        if (board.gameOver()) {
            if (board.playerWins(board.getCurrentPlayer())) {
                return new Duple<>(Integer.MAX_VALUE, board.getLastMove());
            } else if (board.playerWins(board.getCurrentPlayer().opponent())) {
                return new Duple<>(-Integer.MAX_VALUE, board.getLastMove());
            } else {
                return new Duple<>(0, board.getLastMove());
            }
        } else if (depthLimit <= 0) {
            return new Duple<>(getEvaluator().applyAsInt(board), board.getLastMove());
        }
        Duple<Integer, Move> bestMove = new Duple<>(-Integer.MAX_VALUE, null);
        Set<Move> moves = board.getCurrentPlayerMoves();
        for (Move move : moves) {
            numNodes += 1;
            Checkerboard newBoard = board.duplicate();
            newBoard.move(move);
            Duple<Integer, Move> newValues = NegaMaxFunction(newBoard, depthLimit - 1); // Negative?
            if (board.getCurrentPlayer() != newBoard.getCurrentPlayer()) {
                newValues = new Duple<Integer, Move>(-newValues.getFirst(), newValues.getSecond());
            }
            if (newValues.getFirst() >= bestMove.getFirst()) {
                bestMove = new Duple<Integer, Move>(newValues.getFirst(), newBoard.getLastMove());
            }
        }
        return bestMove;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        return Optional.of(NegaMaxFunction(board, getDepthLimit()));
    }

}
