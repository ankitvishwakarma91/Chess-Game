package databaseConnection;


import databaseConnection.dao.ChessDAO;
import databaseConnection.model.ChessPlayer;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ChessDAO chessDAO = new ChessDAO();

        try {
            // Add new players
            chessDAO.addPlayer(new ChessPlayer("Magnus Carlsen", 1, "Norway"));
            chessDAO.addPlayer(new ChessPlayer("Ian Nepomniachtchi", 2, "Russia"));

            // Fetch and display all players
            List<ChessPlayer> players = chessDAO.getAllPlayers();
            System.out.println("List of Chess Players:");
            for (ChessPlayer player : players) {
                System.out.println(player.getId() + ": " + player.getName() +
                        " (Rank: " + player.getRanking() + ", Country: " + player.getCountry() + ")");
            }

            // Update player ranking
            chessDAO.updatePlayerRanking(1, 2);
            System.out.println("Updated ranking for player with ID 1.");

            // Delete a player
            chessDAO.deletePlayer(2);
            System.out.println("Deleted player with ID 2.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}