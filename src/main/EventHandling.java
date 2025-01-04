package main;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// GamePanel class implementation
public class EventHandling extends JPanel {
    private static final int TILE_SIZE = 80; // Size of each tile on the chessboard
    private static final int BOARD_SIZE = 8; // 8x8 chessboard

    public EventHandling() {
        setPreferredSize(new Dimension(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });
    }

    public void launchGame() {
        System.out.println("Game Launched");
        // Additional setup logic for the game
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawChessBoard(g);
    }

    private void drawChessBoard(Graphics g) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 == 0) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.GRAY);
                }
                g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void handleMouseClick(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int row = y / TILE_SIZE;
        int col = x / TILE_SIZE;

        // Validate user input to avoid invalid actions
        if (isValidMove(row, col)) {
            System.out.println("Valid Move at: Row " + row + ", Col " + col);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid move. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidMove(int row, int col) {
        // Example logic for validation (this will evolve based on chess rules)
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }
}

