package io.github.CodeerStudio.mysticalPets.managers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages database operations for player pets in the MysticalPets plugin.
 * Responsible for creating and interacting with the PlayerPets table, which stores the association
 * between players and their owned pets.
 */
public class DatabaseManager {

    private Connection connection;

    /**
     * Sets up the SQLite database and creates the PlayerPets table if it does not already exist.
     */
    public void setupDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/MysticalPets/player_pets.db");
            try (Statement statement = connection.createStatement()) {
                String createTableQuery = "CREATE TABLE IF NOT EXISTS PlayerPets (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "player_uuid VARCHAR(36) NOT NULL," +
                        "pet_id VARCHAR(255) NOT NULL" +
                        ");";
                statement.execute(createTableQuery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the active database connection.
     *
     * @return The active {@link Connection} object.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Closes the database connection if it is open. This should be called during plugin shutdown.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a pet to the PlayerPets database for a specific player.
     *
     * @param playerUUID The UUID of the player.
     * @param petID The ID of the pet.
     */
    public void addPet(String playerUUID, String petID) {
        String query = "INSERT INTO PlayerPets (player_uuid, pet_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, playerUUID);
            stmt.setString(2, petID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a pet from the PlayerPets database for a specific player.
     *
     * @param playerUUID The UUID of the player.
     * @param petID The ID of the pet to be removed.
     */
    public void removePet(String playerUUID, String petID) {
        String query = "DELETE FROM PlayerPets WHERE player_uuid = ? AND pet_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, playerUUID);
            stmt.setString(2, petID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a player owns a specific pet.
     *
     * @param playerUUID The UUID of the player.
     * @param petID The ID of the pet.
     * @return True if the player owns the pet, false otherwise.
     */
    public boolean ownsPet(String playerUUID, String petID) {
        String query = "SELECT COUNT(*) FROM PlayerPets WHERE player_uuid = ? AND pet_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, playerUUID);
            stmt.setString(2, petID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a list of pet IDs owned by a specific player.
     *
     * @param playerUUID The UUID of the player.
     * @return A list of pet IDs owned by the player.
     */
    public List<String> getOwnedPets(String playerUUID) {
        List<String> pets = new ArrayList<>();
        String query = "SELECT pet_id FROM PlayerPets WHERE player_uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, playerUUID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pets.add(rs.getString("pet_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }
}
