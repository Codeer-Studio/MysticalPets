package io.github.CodeerStudio.mysticalPets.managers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private Connection connection;

    public void setupDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/MysticalPets/player_pets.db");
            Statement statement = connection.createStatement();

            String createTableQuery = "CREATE TABLE IF NOT EXISTS PlayerPets (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "player_uuid VARCHAR(36) NOT NULL," +
                    "pet_id VARCHAR(255) NOT NULL" +
                    ");";
            statement.execute(createTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPet(String playerUUID, String petID) {
        String query = "INSERT INTO PlayerPets (player_uuid, pet_id) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerUUID);
            stmt.setString(2, petID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removePet(String playerUUID, String petID) {
        String query = "INSERT INTO PlayerPets (player_uuid, pet_id) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerUUID);
            stmt.setString(2, petID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean ownsPet(String playerUUID, String petID) {
        String query = "SELECT COUNT(*) FROM PlayerPets WHERE player_uuid = ? AND pet_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerUUID);
            stmt.setString(2, petID);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getOwnedPets(String playerUUID) {
        List<String> pets = new ArrayList<>();
        String query = "SELECT pet_id FROM PlayerPets WHERE player_uuid = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerUUID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pets.add(rs.getString("pet_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }
}
