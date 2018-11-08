package server.models;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Choice {
    private int choiceID;
    private String choiceName;

    public Choice(int choiceID, String choiceName) {
        this.choiceID = choiceID;
        this.choiceName = choiceName;
    }

    public int getChoiceID() {
        return choiceID;
    }

    public void setChoiceID(int choiceID) {
        this.choiceID = choiceID;
    }

    public String getChoiceName() {
        return choiceName;
    }

    public void setChoiceName(String choiceName) {
        this.choiceName = choiceName;
    }

    public static ArrayList<Choice> choices = new ArrayList<>();

    public static int nextId() {
        int id = 0;
        for (Choice c: choices) {
            if (c.getChoiceID() > id) {
                id = c.getChoiceID();
            }
        }
        return id + 1;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("choiceID", getChoiceID());
        j.put("choiceName", getChoiceName());

        return j;
    }
}