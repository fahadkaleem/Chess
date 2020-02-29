package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {
    protected final int piecePosition;
    // Alliance is each piece being associated with a white tile or a black tile
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;

    public Piece(final int piecePosition, final Alliance pieceAlliance) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        // TODO: Finish first move implementation
        this.isFirstMove = false;
    }

    public boolean isFirstMove(){
        return this.isFirstMove;
    }

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public int getPiecePosition(){ return this.piecePosition; }

    public abstract Collection<Move> calculateLegalMoves(final Board board);
}
