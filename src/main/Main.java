package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // This line create a window
        JFrame window = new JFrame();
        // when we shut down app it keeps stop program running
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.setTitle("Chess");

        // Adding game panel to window
        GamePannel gp = new GamePannel();
        window.add(gp);
        window.pack();

        // it means you cannot resize the window
        window.setResizable(false);

        // it means it show in center if you want to show in left or somewhere else then you should remove this line
        window.setLocationRelativeTo(null);

        // it shows a window
        window.setVisible(true);


        gp.launchGame();
    }
}