package databaseConnection.dao;



import databaseConnection.model.ChessPlayer;
import databaseConnection.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChessDAO {

    public void addPlayer(ChessPlayer player) throws SQLException {
        String query = "INSERT INTO chessplayer (name, ranking, country) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, player.getName());
            stmt.setInt(2, player.getRanking());
            stmt.setString(3, player.getCountry());
            stmt.executeUpdate();
        }
    }

    public List<ChessPlayer> getAllPlayers() throws SQLException {
        List<ChessPlayer> players = new ArrayList<>();
        String query = "SELECT * FROM chessplayer";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                players.add(new ChessPlayer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("ranking"),
                        rs.getString("country")
                ));
            }
        }
        return players;
    }

    public void updatePlayerRanking(int id, int newRanking) throws SQLException {
        String query = "UPDATE chessplayer SET ranking = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, newRanking);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void deletePlayer(int id) throws SQLException {
        String query = "DELETE FROM chessplayer WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

}
