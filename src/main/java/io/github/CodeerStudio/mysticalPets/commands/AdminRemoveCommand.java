package io.github.CodeerStudio.mysticalPets.commands;

import io.github.CodeerStudio.mysticalPets.managers.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the admin command to remove a pet from a player.
 * This command allows administrators to revoke a pet from a specified player.
 */
public class AdminRemoveCommand implements PetSubCommand{

    private final DatabaseManager databaseManager;

    /**
     * Constructs the AdminRemoveCommand with the specified DatabaseManager.
     *
     * @param databaseManager the manager responsible for database operations.
     */
    public AdminRemoveCommand(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Executes the command to remove a pet from a player.
     *
     * @param sender the entity executing the command (e.g., player or console).
     * @param args the command arguments. Requires two arguments: player name and pet ID.
     * @return true if the command executed successfully, false otherwise.
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage /pet remove <player_name> <pet_id>");
            return true;
        }

        String playerName = args[0];
        String petId = args[1];

        Player targetPlayer = Bukkit.getPlayerExact(playerName);

        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Player not found: " + playerName);
            return true;
        }

        String playerUUID = targetPlayer.getUniqueId().toString();

        if (!databaseManager.ownsPet(playerUUID, petId)) {
            sender.sendMessage(ChatColor.RED + "Player doesn't own this pet");
            return true;
        }

        // Remove the pet from the database
        try {
            databaseManager.removePet(playerUUID, petId);
            sender.sendMessage(ChatColor.GREEN + "Successfully removed pet " + ChatColor.GOLD + petId +
                    ChatColor.GREEN + " from player " + ChatColor.AQUA + playerName + ChatColor.GREEN + ".");
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "An error occurred while adding the pet to the database.");
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Gets the permission required to execute this command.
     *
     * @return the permission string required for this command.
     */
    @Override
    public String getPermission() {
        return "mysticalpets.admin.remove";
    }
}
