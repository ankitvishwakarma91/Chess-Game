package main;

import piece.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// it creates because we show them game screen
public class GamePannel extends JPanel implements Runnable {

    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    final int FPS = 60;
    Thread gameThread;

    Board board = new Board();

    Mouse mouse = new Mouse();

    // Pieces
    public static ArrayList<Piece> pieces = new ArrayList<Piece>();
    public static ArrayList<Piece> simPieces = new ArrayList<Piece>();
    ArrayList<Piece> promoPiece = new ArrayList<Piece>();
    Piece activePiece, checkingPieces;
    public static Piece castlingPiece;


    // Color
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentColor = WHITE;

    // Booleans
    boolean canMove;
    boolean validSquare;
    boolean promotion;
    boolean gameOver;

    public GamePannel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        setBackground(Color.BLACK);
        setPieces();
//        testPromotion();
//        testIllegal();
        copyPieces(pieces, simPieces);
    }


    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setPieces() {

        //White team
        pieces.add(new Pawn(WHITE, 0, 6));
        pieces.add(new Pawn(WHITE, 1, 6));
        pieces.add(new Pawn(WHITE, 2, 6));
        pieces.add(new Pawn(WHITE, 3, 6));
        pieces.add(new Pawn(WHITE, 4, 6));
        pieces.add(new Pawn(WHITE, 5, 6));
        pieces.add(new Pawn(WHITE, 6, 6));
        pieces.add(new Pawn(WHITE, 7, 6));
        pieces.add(new Rook(WHITE, 0, 7));
        pieces.add(new Rook(WHITE, 7, 7));
        pieces.add(new Knight(WHITE, 1, 7));
        pieces.add(new Knight(WHITE, 6, 7));
        pieces.add(new Bishop(WHITE, 2, 7));
        pieces.add(new Bishop(WHITE, 5, 7));
        pieces.add(new Queen(WHITE, 3, 7));
        pieces.add(new King(WHITE, 4, 7));


        //Black team
        pieces.add(new Pawn(BLACK, 0, 1));
        pieces.add(new Pawn(BLACK, 1, 1));
        pieces.add(new Pawn(BLACK, 2, 1));
        pieces.add(new Pawn(BLACK, 3, 1));
        pieces.add(new Pawn(BLACK, 4, 1));
        pieces.add(new Pawn(BLACK, 5, 1));
        pieces.add(new Pawn(BLACK, 6, 1));
        pieces.add(new Pawn(BLACK, 7, 1));
        pieces.add(new Rook(BLACK, 0, 0));
        pieces.add(new Rook(BLACK, 7, 0));
        pieces.add(new Knight(BLACK, 1, 0));
        pieces.add(new Knight(BLACK, 6, 0));
        pieces.add(new Bishop(BLACK, 2, 0));
        pieces.add(new Bishop(BLACK, 5, 0));
        pieces.add(new Queen(BLACK, 3, 0));
        pieces.add(new King(BLACK, 4, 0));
    }

    public void testPromotion() {
        pieces.add(new Pawn(WHITE, 0, 3));
        pieces.add(new Pawn(BLACK, 5, 4));
    }

    public void testIllegal(){
        pieces.add(new Pawn(WHITE, 7, 6));
        pieces.add(new King(WHITE, 3, 7));
        pieces.add(new King(BLACK, 0, 3));
        pieces.add(new Bishop(BLACK, 1, 4));
        pieces.add(new Queen(BLACK, 4, 5));
    }

    public void copyPieces(ArrayList<Piece> source, ArrayList<Piece> dest) {
        dest.clear();
        for (int i = 0; i < source.size(); i++) {
            dest.add(source.get(i));
        }
    }

    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / FPS;
        long lastTime = System.nanoTime();
        double delta = 0;
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }


    public void update() {

        if (promotion) {

            promoting();

        } else {
            // Mouse Button pressed
            if (mouse.pressed) {

                // If the activePiece is null, check if you can pick up a piece
                if (activePiece == null) {

                    // if the mouse is on an all piece, pick it up as the active
                    for (Piece piece : simPieces) {
                        if (piece.color == currentColor && piece.col == mouse.x / Board.SQUARE_SIZE
                                && piece.row == mouse.y / Board.SQUARE_SIZE) {
                            activePiece = piece;
                        }
                    }

                } else {
                    // if the player is holding a piece, simulate the move
                    simulate();
                }
            }

            // Mouse button released
            if (mouse.pressed == false) {
                if (activePiece != null) {

                    if (validSquare) {
                        // MOVE CONFIRMED

                        // Update the piece list in case a piece has been captured and removed during the simulation

                        copyPieces(simPieces, pieces);
                        activePiece.updatePosition();

                        if (castlingPiece != null) {
                            castlingPiece.updatePosition();
                        }

                        if (isKingInCheck()) {
                            // TODO: Probably game over

                        }
//                        else{
//                            if (canPromote()) {
//                                promotion = true;
//                            } else {
//                                //Change player
//                                changePlayer();
//                            }
//                        }
                        if (canPromote()) {
                            promotion = true;
                        } else {
                            //Change player
                            changePlayer();
                        }
                    } else {
                        // The move is not valid so reset everything
                        copyPieces(pieces, simPieces);
                        activePiece.resetPosition();
                        activePiece = null;
                    }
                }
            }
        }
    }


    private void simulate() {

        canMove = false;
        validSquare = false;

        //Reset the piece list in every loop
        //This is basically for restoring the removed piece during the simulation
        copyPieces(pieces, simPieces);

        // Reset the castling piece's position
        if (castlingPiece != null) {
            castlingPiece.col = castlingPiece.preCol;
            castlingPiece.x = castlingPiece.getX(castlingPiece.col);
            castlingPiece = null;
        }


        // If a piece is being held, update its potions
        activePiece.x = mouse.x - Board.HALF_SIZE;
        activePiece.y = mouse.y - Board.HALF_SIZE;
        activePiece.col = activePiece.getCol(activePiece.x);
        activePiece.row = activePiece.getRow(activePiece.y);


        //check if the piece is hovering over a reachable square
        if (activePiece.canMove(activePiece.col, activePiece.row)) {
            canMove = true;

            // If hitting a piece, remove it from the list
            if (activePiece.hittingPiece != null) {
                simPieces.remove(activePiece.hittingPiece.getIndex());
            }
            checkCastling();

            if (isIllegal(activePiece) == false && opponentCanCaptureKing() == false) {
                validSquare = true;
            }
        }
    }

    private boolean isIllegal(Piece king) {
        if (king.type == Type.KING) {
            for (Piece piece : simPieces) {
                if (piece != null && piece.color != king.color && piece.canMove(king.col, king.row)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean opponentCanCaptureKing(){
        Piece king = getKing(false);

        for (Piece piece : simPieces) {
            if (piece.color != king.color && piece.canMove(king.col, king.row)) {
                return true;
            }
        }
        return false;
    }

    public boolean isKingInCheck(){

        Piece king = getKing(true);

        if (activePiece.canMove(king.col , king.row)) {
            checkingPieces = activePiece;
            return true;
        }else{
            checkingPieces = null;
        }

        return false;
    }

    private Piece getKing(boolean opponent){
        Piece king = null;
        for (Piece piece : simPieces) {
            if(opponent){
                if (piece.type == Type.KING && piece.color != currentColor) {
                    king = piece;
                }
            }else{
                if (piece.type == Type.KING && piece.color == currentColor) {
                    king = piece;
                }
            }
        }
        return king;
    }

    public void checkCastling() {
        if (castlingPiece != null) {
            if (castlingPiece.col == 0) {
                castlingPiece.col += 3;
            } else if (castlingPiece.col == 7) {
                castlingPiece.col -= 2;
            }
            castlingPiece.x = castlingPiece.getX(castlingPiece.col);
        }

    }

    public void changePlayer() {
        if (currentColor == WHITE) {
            currentColor = BLACK;

            // Resets black's two stepped status
            for (Piece piece : pieces) {
                if (piece.color == BLACK) {
                    piece.twoStepped = false;
                }
            }
        } else {
            currentColor = WHITE;
            // Resets white's two stepped status
            for (Piece piece : pieces) {
                if (piece.color == WHITE) {
                    piece.twoStepped = false;
                }
            }
        }
        activePiece = null;
    }

    public boolean canPromote() {
        if (activePiece.type == Type.PAWN) {
            if (currentColor == WHITE && activePiece.row == 0 || currentColor == BLACK && activePiece.row == 7) {
                promoPiece.clear();
                promoPiece.add(new Rook(BLACK, 9, 2));
                promoPiece.add(new Knight(BLACK, 9, 3));
                promoPiece.add(new Bishop(BLACK, 9, 4));
                promoPiece.add(new Queen(BLACK, 9, 5));
                return true;
            }
        }
        return false;
    }

    public void promoting() {
        if (mouse.pressed) {
            for (Piece piece : promoPiece) {
                if (piece.col == mouse.x / Board.SQUARE_SIZE && piece.row == mouse.y / Board.SQUARE_SIZE) {
                    switch (piece.type) {
                        case ROOK:
                            simPieces.add(new Rook(currentColor, activePiece.col, activePiece.row));
                            break;
                        case KNIGHT:
                            simPieces.add(new Knight(currentColor, activePiece.col, activePiece.row));
                            break;
                        case BISHOP:
                            simPieces.add(new Bishop(currentColor, activePiece.col, activePiece.row));
                            break;
                        case QUEEN:
                            simPieces.add(new Queen(currentColor, activePiece.col, activePiece.row));
                            break;
                        default:
                            break;
                    }
                    simPieces.remove(activePiece.getIndex());
                    copyPieces(simPieces, pieces);
                    activePiece = null;
                    promotion = false;
                    changePlayer();
                }
            }
        }
    }

    // paintComponent is used to drawing objects on panel
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Board
        board.draw(g2);

        // Pieces
        for (Piece piece : simPieces) {
            piece.draw(g2);
        }

        if (activePiece != null) {

            if (canMove) {

                if (isIllegal(activePiece) || opponentCanCaptureKing()) {
                    g2.setColor(Color.RED);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                    g2.fillRect(activePiece.col * Board.SQUARE_SIZE, activePiece.row * Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                } else {

                    g2.setColor(Color.CYAN);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                    g2.fillRect(activePiece.col * Board.SQUARE_SIZE, activePiece.row * Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                }
            }

            // Draw the active piece in the end, so it won't be hidden by the board or the colored square
            activePiece.draw(g2);
        }

        // STATUS MESSAGE
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));


        if (promotion) {
            g2.drawString("Promote to ", 840, 150);
            for (Piece piece : promoPiece) {
                g2.drawImage(piece.image, piece.getX(piece.col), piece.getY(piece.row), Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
            }
        } else {
            if (currentColor == WHITE) {
                g2.drawString("White's turn", 840, 550);
                if (checkingPieces != null && checkingPieces.color == BLACK){
                    g2.setColor(Color.RED);
                    g2.drawString("The King ", 840, 650);
                    g2.drawString("Is in check !" , 840, 700);
                }
            } else {
                g2.drawString("Black's turn", 840, 250);
                if (checkingPieces != null && checkingPieces.color == WHITE){
                    g2.setColor(Color.RED);
                    g2.drawString("The King ", 840, 100);
                    g2.drawString("Is in check !" , 840, 150);
                }
            }
        }


    }

}