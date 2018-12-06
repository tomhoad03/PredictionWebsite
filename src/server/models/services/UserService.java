package server.models.services;

import server.Logger;
import server.DatabaseConnection;
import server.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserService {

    public static String selectAllInto(List<User> targetList) {
        targetList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT UserID, Username, Email, HashedPassword, Salt, SessionToken FROM Users"
            );
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    while (results.next()) {
                        targetList.add(new User(results.getInt("UserID"), results.getString("Username"), results.getString("Email"), results.getString("HashedPassword"), results.getString("Salt"), results.getString("SessionToken")));
                    }
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select all from 'Users' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
        return "OK";
    }

    public static User selectById(int id) {
        User result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT UserID, Username, Email, HashedPassword, Salt, SessionToken FROM Users WHERE UserID = ?"
            );
            if (statement != null) {
                statement.setInt(6, id);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new User(results.getInt("UserID"), results.getString("Username"), results.getString("Email"), results.getString("HashedPassword"), results.getString("Salt"), results.getString("SessionToken"));
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by id from 'Users' table: " + resultsException.getMessage();

            Logger.log(error);
        }
        return result;
    }

    public static String insert(User itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "INSERT INTO Users (UserID, Username, Email, HashedPassword, Salt, SessionToken) VALUES (?, ?, ?, ?, ?, ?)"
            );
            statement.setInt(1, itemToSave.getUserID());
            statement.setString(2, itemToSave.getUsername());
            statement.setString(3, itemToSave.getEmail());
            statement.setString(4, itemToSave.getHashedPassword());
            statement.setString(5, itemToSave.getSalt());
            statement.setString(6, itemToSave.getSessionToken());

            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't insert into 'Users' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String update(User itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "UPDATE Users SET Username = ?, Email = ?, HashedPassword = ?, Salt = ?, SessionToken = ? WHERE UserID = ?"
            );
            statement.setString(1, itemToSave.getUsername());
            statement.setString(2, itemToSave.getEmail());
            statement.setString(3, itemToSave.getHashedPassword());
            statement.setString(4, itemToSave.getSalt());
            statement.setString(5, itemToSave.getSessionToken());
            statement.setInt(6, itemToSave.getUserID());

            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't update 'Users' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String deleteById(int id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "DELETE FROM Users WHERE UserID = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't delete by id from 'Users' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

}