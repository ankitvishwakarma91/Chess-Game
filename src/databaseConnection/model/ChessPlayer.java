package databaseConnection.model;

public class ChessPlayer {


    private int id;
    private String name;
    private int ranking;
    private String country;

    public ChessPlayer(int id, String name, int ranking, String country) {
        this.id = id;
        this.name = name;
        this.ranking = ranking;
        this.country = country;
    }

    public ChessPlayer(String name, int ranking, String country) {
        this.name = name;
        this.ranking = ranking;
        this.country = country;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRanking() {
        return ranking;
    }

    public String getCountry() {
        return country;
    }

}
