package io.github.CodeerStudio.mysticalPets.commands;

import io.github.CodeerStudio.mysticalPets.managers.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the admin command to add a pet to a player.
 * This command allows admins to assign a pet to a specified player.
 */
public class AdminAddCommand implements PetSubCommand{

    private final DatabaseManager databaseManager;

    /**
     * Constructs the AdminAddCommand with the specified DatabaseManager.
     *
     * @param databaseManager the database manager for managing player-pet relationships.
     */
    public AdminAddCommand(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Executes the command to add a pet to a player.
     * Validates input arguments, ensures the player exists, and adds the pet to the database.
     *
     * @param sender the entity executing the command (e.g., player or console).
     * @param args the command arguments, where args[0] is the player name and args[1] is the pet ID.
     * @return true if the command executed successfully, false otherwise.
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "UsageL /pet add <player_name> <pet_id>");
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

        // Add the pet to the database
        try {
            databaseManager.addPet(playerUUID, petId);
            sender.sendMessage(ChatColor.GREEN + "Successfully added pet " + ChatColor.GOLD + petId +
                    ChatColor.GREEN + " to player " + ChatColor.AQUA + playerName + ChatColor.GREEN + ".");
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
        return "mysticalpets.admin.add";
    }
}
