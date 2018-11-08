package server.models.services;

import server.DatabaseConnection;
import server.Logger;
import server.models.Prediction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PredictionService {

    public static String selectAllInto(List<Prediction> targetList) {
        targetList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT PredictionID, UserID, QuestionNum, ChoiceID FROM Predictions"
            );
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    while (results.next()) {
                        targetList.add(new Prediction(results.getInt("PredictionID"), results.getInt("UserID"), results.getInt("QuestionNum"), results.getInt("ChoiceID")));


                    }
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select all from 'Predictions' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
        return "OK";
    }

    public static Prediction selectById(int id) {
        Prediction result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT PredictionID, UserID, QuestionNum, ChoiceID FROM Predictions WHERE PredictionID = ?"
            );
            if (statement != null) {
                statement.setInt(1, id);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new Prediction(results.getInt("PredictionID"), results.getInt("UserID"), results.getInt("QuestionNum"), results.getInt("ChoiceID"));


                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by id from 'Predictions' table: " + resultsException.getMessage();

            Logger.log(error);
        }
        return result;
    }

    public static String insert(Prediction itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "INSERT INTO Predictions (PredictionID, UserID, QuestionNum, ChoiceID) VALUES (?, ?, ?, ?)"
            );
            statement.setInt(1, itemToSave.getPredictionID());
            statement.setInt(2, itemToSave.getUserID());
            statement.setInt(3, itemToSave.getQuestionNum());
            statement.setInt(4, itemToSave.getChoiceID());






            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't insert into 'Predictions' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String update(Prediction itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "UPDATE Predictions SET UserID = ?, QuestionNum = ?, ChoiceID = ? WHERE PredictionID = ?"
            );
            statement.setInt(1, itemToSave.getUserID());
            statement.setInt(2, itemToSave.getQuestionNum());
            statement.setInt(3, itemToSave.getChoiceID());






            statement.setInt(4, itemToSave.getPredictionID());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't update 'Predictions' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String deleteById(int id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "DELETE FROM Predictions WHERE PredictionID = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't delete by id from 'Predictions' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

}