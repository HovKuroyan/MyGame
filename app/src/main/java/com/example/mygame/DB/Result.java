package com.example.mygame.DB;

public class Result {
    private int id;
    private String name;
    private String surname;

    private String gameMode;

    private int highScore;
    public Result(int id, String name, String surname, int highScore, String gameMode) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.gameMode = gameMode;
        this.highScore = highScore;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getGameMode() {
        return gameMode;
    }

    public int getHighScore() {
        return highScore;
    }
}

