package server.models;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class User {
    private int userID;
    private String username;
    private String email;
    private String hashedPassword;
    private String salt;
    private String sessionToken;

    public User(int userID, String username, String email, String hashedPassword, String salt, String sessionToken) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.sessionToken = sessionToken;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public static ArrayList<User> users = new ArrayList<>();

    public static int nextId() {
        int id = 0;
        for (User u: users) {
            if (u.getUserID() > id) {
                id = u.getUserID();
            }
        }
        return id + 1;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("userID", getUserID());
        j.put("username", getUsername());
        j.put("email", getEmail());
        j.put("hashedPassword", getHashedPassword());
        j.put("salt", getSalt());
        j.put("sessionToken", getSessionToken());

        return j;
    }
}