package piece;

import main.GamePannel;
import main.Type;

public class King extends Piece {

    public King(int color, int col, int row) {
        super(color, col, row);

        type = Type.KING;

        if (color == GamePannel.WHITE) {
            image = getImage("/piece/w-king");
        } else {
            image = getImage("/piece/b-king");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {

        if (isWithinBoard(targetCol, targetRow)) {
            //Movement
            if (Math.abs(targetCol - preCol) + Math.abs(targetRow - preRow) == 1
                    || Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow) == 1) {

                if (isValidSquare(targetCol, targetRow)) {
                    return true;
                }
            }

            // Castling
            if(moved == false){

                // Right castling
                if (targetCol == preCol+2 && targetRow == preRow && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                    for(Piece piece : GamePannel.simPieces){
                        if(piece.col == preCol+3 && piece.row == preRow && piece.moved == false){
                            GamePannel.castlingPiece = piece;
                            return true;
                        }
                    }
                }

                // Left Castling
                if (targetCol == preCol-2 && targetRow == preRow && pieceIsOnStraightLine(targetCol, targetRow) == false){
                    Piece p[] = new Piece[2];
                    for(Piece piece : GamePannel.simPieces){
                        if (piece.col == preCol-3 && piece.row == targetRow) {
                            p[0] = piece;
                        }
                        if (piece.col == preCol-4 && piece.row == targetRow) {
                            p[1] = piece;
                        }
                        if (p[0] == null && p[1] != null  && p[1].moved == false) {
                            GamePannel.castlingPiece = piece;
                            return true;
                        }
                    }
                }

            }
        }
        return false;
    }
}
