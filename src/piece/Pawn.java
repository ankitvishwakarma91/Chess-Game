package piece;

import main.GamePannel;
import main.Type;


public class Pawn extends Piece {
    public Pawn(int color, int col, int row) {
        super(color, col, row);

        type = Type.PAWN;

        if (color == GamePannel.WHITE) {
            image = getImage("/piece/w-pawn");
        } else {
            image = getImage("/piece/b-pawn");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false) {

            // Define the move value based on its color
            int moveValue;
            if (color == GamePannel.WHITE) {
                moveValue = -1;
            } else {
                moveValue = 1;
            }

            // Check the hitting pieces
            hittingPiece = getHitting(targetCol, targetRow);

            // 1 square movement
            if (targetCol == preCol && targetRow == preRow + moveValue && hittingPiece == null) {
                return true;
            }

            // 2 squares movement
            if (targetCol == preCol && targetRow == preRow + moveValue * 2 && hittingPiece == null && moved == false
                    && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                return true;
            }

            // Diagonal movement & capture (if a piece is on a square diagonally in front of it)
            if (Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue && hittingPiece != null && hittingPiece.color != color) {
                return true;
            }

            // En Passant
            if (Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue){
                for (Piece piece : GamePannel.simPieces){
                    if(piece.col == targetCol && piece.row == preRow && piece.twoStepped  == true){
                        hittingPiece = piece;
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
