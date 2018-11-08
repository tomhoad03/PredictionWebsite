package server.models.services;

import server.Logger;
import server.DatabaseConnection;
import server.models.Leaderboard;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LeaderboardService {

    public static String selectAllInto(List<Leaderboard> targetList) {
        targetList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT UserID, TotalPoints, Position FROM Leaderboards"
            );
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    while (results.next()) {
                        targetList.add(new Leaderboard(results.getInt("UserID"), results.getInt("TotalPoints"), results.getInt("Position")));


                    }
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select all from 'Leaderboards' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
        return "OK";
    }

    public static Leaderboard selectById(int id) {
        Leaderboard result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT UserID, TotalPoints, Position FROM Leaderboards WHERE UserID = ?"
            );
            if (statement != null) {
                statement.setInt(1, id);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new Leaderboard(results.getInt("UserID"), results.getInt("TotalPoints"), results.getInt("Position"));


                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by id from 'Leaderboards' table: " + resultsException.getMessage();

            Logger.log(error);
        }
        return result;
    }

    public static String insert(Leaderboard itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "INSERT INTO Leaderboards (UserID, TotalPoints, Position) VALUES (?, ?, ?)"
            );
            statement.setInt(1, itemToSave.getUserID());
            statement.setInt(2, itemToSave.getTotalPoints());
            statement.setInt(3, itemToSave.getPosition());







            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't insert into 'Leaderboards' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String update(Leaderboard itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "UPDATE Leaderboards SET TotalPoints = ?, Position = ? WHERE UserID = ?"
            );
            statement.setInt(1, itemToSave.getTotalPoints());
            statement.setInt(2, itemToSave.getPosition());







            statement.setInt(3, itemToSave.getUserID());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't update 'Leaderboards' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String deleteById(int id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "DELETE FROM Leaderboards WHERE UserID = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't delete by id from 'Leaderboards' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

}