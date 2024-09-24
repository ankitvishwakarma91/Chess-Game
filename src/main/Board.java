package main;

import java.awt.*;

public class Board {

    final int MAX_COL = 8;
    final int MAX_ROW = 8;
    public static final int SQUARE_SIZE = 100;
    public static final int HALF_SIZE = SQUARE_SIZE / 2;

    // This function is used for draw the chess board
    public void draw(Graphics2D g2) {
        int clr = 0;
        for (int row = 0; row < MAX_ROW; row++) {

            for (int col = 0; col < MAX_COL; col++) {

                // This is used for color changed
                if (clr == 0) {
                    g2.setColor(Color.BLACK);
//                    g2.setColor(new Color(210, 165, 125));
                    clr = 1;
                } else if (clr == 1) {
                    g2.setColor(Color.white);
//                    g2.setColor(new Color(175, 115, 70));
                    clr = 0;
                }
                g2.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
            if (clr == 0) {
                clr = 1;
            } else {
                clr = 0;
            }
        }
    }

}
