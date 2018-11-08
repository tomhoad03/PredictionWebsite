package server.models;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Leaderboard {
    private int userID;
    private int totalPoints;
    private int position;

    public Leaderboard(int userID, int totalPoints, int position) {
        this.userID = userID;
        this.totalPoints = totalPoints;
        this.position = position;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static ArrayList<Leaderboard> leaderboards = new ArrayList<>();

    public static int nextId() {
        int id = 0;
        for (Leaderboard l: leaderboards) {
            if (l.getUserID() > id) {
                id = l.getUserID();
            }
        }
        return id + 1;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("userID", getUserID());
        j.put("totalPoints", getTotalPoints());
        j.put("position", getPosition());

        return j;
    }
}