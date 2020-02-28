package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    // Offset of tile coordinate from any position of the knight
    private final static int[] CANDIDATE_MOVES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public List<Move> calculateLegalMoves(Board board) {
        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<>();
        for(final int currentCandidate : CANDIDATE_MOVES){
            candidateDestinationCoordinate = this.piecePosition + currentCandidate;
            // Check if the coordinate is valid or not
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                // If destination tile is empty then we have a non attacking legal move
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new Move());
                }else{
                    // If the destination tile is not empty then we have an attacking move
                    // only if the destination tile is being occupied by an enemy
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    // Check if this knight has an alliance with the piece at destination or not
                    // If it has an alliance then it cant be a legal move because the destination is occupied by an ally
                    // If it doesnt have an alliance then it is a legal move
                    if(this.pieceAlliance != pieceAlliance){
                        legalMoves.add(new Move());
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

}
