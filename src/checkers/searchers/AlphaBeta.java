package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import core.Duple;

import java.util.Optional;
import java.util.Set;
import java.util.function.ToIntFunction;

public class AlphaBeta extends CheckersSearcher {
    private int numNodes = 0;

    public AlphaBeta(ToIntFunction<Checkerboard> e) {
        super(e);
    }

    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    public Duple<Integer, Move> NegaMaxFunction(Checkerboard board, int depthLimit, int alpha, int beta) {
        Set<Move> moves = board.getCurrentPlayerMoves();
        if (board.gameOver() || moves.isEmpty()) {
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
        for (Move move : moves) {
            numNodes += 1;
            Checkerboard newBoard = board.duplicate();
            newBoard.move(move);
            Duple<Integer, Move> newValues;
            if (board.getCurrentPlayer() == newBoard.getCurrentPlayer()) {
                newValues = NegaMaxFunction(newBoard, depthLimit - 1, alpha, beta);
            } else {
                newValues = NegaMaxFunction(newBoard, depthLimit - 1, -beta, -alpha);
                newValues = new Duple<Integer, Move>(-newValues.getFirst(), newValues.getSecond());
            }
            if (newValues.getFirst() >= bestMove.getFirst()) {
                bestMove = new Duple<Integer, Move>(newValues.getFirst(), newBoard.getLastMove());
                if (newValues.getFirst() >= alpha) {
                    alpha = newValues.getFirst();
                }
                if (alpha > beta) {
                    break;
                }
            }
        }
        return bestMove;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        return Optional.of(NegaMaxFunction(board, getDepthLimit(), -Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

}
