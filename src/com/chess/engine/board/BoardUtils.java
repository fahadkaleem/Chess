package com.chess.engine.board;

public class BoardUtils {
    public static boolean isValidTileCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate < 64;
    }
}
