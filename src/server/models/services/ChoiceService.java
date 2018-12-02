package server.models.services;

import server.Logger;
import server.DatabaseConnection;
import server.models.Choice;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ChoiceService {

    public static String selectAllInto(List<Choice> targetList) {
        targetList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT ChoiceID, ChoiceName FROM Choices"
            );
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    while (results.next()) {
                        targetList.add(new Choice(results.getInt("ChoiceID"), results.getString("ChoiceName")));
                    }
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select all from 'Choices' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
        return "OK";
    }

    public static Choice selectById(int id) {
        Choice result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT ChoiceID, ChoiceName FROM Choices WHERE ChoiceID = ?"
            );
            if (statement != null) {
                statement.setInt(1, id);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new Choice(results.getInt("ChoiceID"), results.getString("ChoiceName"));
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by id from 'Choices' table: " + resultsException.getMessage();

            Logger.log(error);
        }
        return result;
    }

    public static String insert(Choice itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "INSERT INTO Choices (ChoiceID, ChoiceName) VALUES (?, ?)"
            );
            statement.setInt(1, itemToSave.getChoiceID());
            statement.setString(2, itemToSave.getChoiceName());

            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't insert into 'Choices' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String update(Choice itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "UPDATE Choices SET ChoiceName = ? WHERE ChoiceID = ?"
            );
            statement.setString(1, itemToSave.getChoiceName());
            statement.setInt(2, itemToSave.getChoiceID());

            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't update 'Choices' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String deleteById(int id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "DELETE FROM Choices WHERE ChoiceID = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't delete by id from 'Choices' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

}