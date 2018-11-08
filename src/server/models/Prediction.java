package server.models;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Prediction {
    private int predictionID;
    private int userID;
    private int questionNum;
    private int choiceID;

    public Prediction(int predictionID, int userID, int questionNum, int choiceID) {
        this.predictionID = predictionID;
        this.userID = userID;
        this.questionNum = questionNum;
        this.choiceID = choiceID;
    }

    public int getPredictionID() {
        return predictionID;
    }

    public void setPredictionID(int predictionID) {
        this.predictionID = predictionID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public int getChoiceID() {
        return choiceID;
    }

    public void setChoiceID(int choiceID) {
        this.choiceID = choiceID;
    }

    public static ArrayList<Prediction> predictions = new ArrayList<>();

    public static int nextId() {
        int id = 0;
        for (Prediction p: predictions) {
            if (p.getPredictionID() > id) {
                id = p.getPredictionID();
            }
        }
        return id + 1;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("predictionID", getPredictionID());
        j.put("userID", getUserID());
        j.put("questionNum", getQuestionNum());
        j.put("choiceID", getChoiceID());

        return j;
    }
}