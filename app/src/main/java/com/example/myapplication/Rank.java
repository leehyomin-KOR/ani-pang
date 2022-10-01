package com.example.myapplication;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public class Rank {
    private String player_name;
    private int score;
    private String clear_date;

    public Rank(String player_name, int score, String clear_date) {
        this.player_name = player_name;
        this.score = score;
        this.clear_date = clear_date;
    }
    public void setPlayer_name(String str) { this.player_name = str; }
    public String getPlayer_name() {
        return player_name;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) { this.score = score; }
    public String getClear_date() {
        return clear_date;
    }
    public void setClear_date(String str) { this.clear_date =  str; }
}



