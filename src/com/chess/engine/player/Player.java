package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;


    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves){
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = legalMoves;
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    private static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> opponentMoves) {
        final List<Move> attackMoves = new ArrayList<>();
        for(final Move move : opponentMoves){
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }

    protected King establishKing(){
        for(final Piece piece : getActivePieces()){
            if(piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw  new RuntimeException("King not found, not a valid board");
    }

    public boolean isInCheck(){
        return this.isInCheck;
    }

    public boolean isInCheckMate(){
        return this.isInCheck() && !hasEscapeMoves();
    }

    // TODO
    public boolean isInStaleMate(){
        return false;
    }

    public boolean isCastle(){
        return false;
    }

    protected boolean hasEscapeMoves() {
        for(final Move move: this.legalMoves){
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }

    public King getPlayerKing() {
        return this.playerKing;
    }

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }

    public MoveTransition makeMove(final Move move){
        /**
         * 1. If the move is illegal (not part of the legal moves) then movetransition you return is the same board and you dont move and the move status is Illegal
         * 2. We polymorphic execute the move which returns a new board that we transition to
         * 3. Are there any attacks on the king of the current player then that player could have not made that move, he has to protect the king or move the king to safety
         * 3. In this case, you return the same board with the status Leaves player in check
         * 4. If not then we make the move and return the new board
         *
         * **/
        if(!isMoveLegal(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();
        // After we make the move above we are no longer the current player so we get opponent player which is us because the current player is the opponent now
        // In short, opponent player is us now
        final int opponentsKingPosition = transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition();
        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(opponentsKingPosition, transitionBoard.currentPlayer().getLegalMoves());
        if(!kingAttacks.isEmpty()) {
            new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    private boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
}
