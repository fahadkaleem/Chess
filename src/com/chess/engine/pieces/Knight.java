package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;


public class Knight extends Piece {
    // Offset of tile coordinate from any position of the knight
    private final static int[] CANDIDATE_MOVES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVES) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;;

            // If knight is in first column, then some offsets dont work
            if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isSeconColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isEigthColumnExclusion(this.piecePosition, currentCandidateOffset)){
                continue;
            }
            // Check if the coordinate is valid or not
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                // If destination tile is empty then we have a non attacking legal move
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    // If the destination tile is not empty then we have an attacking move
                    // only if the destination tile is being occupied by an enemy
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    // Check if this knight has an alliance with the piece at destination or not
                    // If it has an alliance then it cant be a legal move because the destination is occupied by an ally
                    // If it doesnt have an alliance then it is a legal move
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new AttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition]
                && (candidateOffset == -17 || candidateOffset == -10
                || candidateOffset == 6 || candidateOffset == 15);
    }

    private static boolean isSeconColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);
    }

    private static boolean isEigthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHT_COLUMN[currentPosition]
                && (candidateOffset == -15 || candidateOffset == -6
                || candidateOffset == 10 || candidateOffset == 17);
    }

}
