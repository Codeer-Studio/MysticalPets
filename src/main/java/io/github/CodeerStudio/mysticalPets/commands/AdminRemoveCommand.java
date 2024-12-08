package io.github.CodeerStudio.mysticalPets.commands;

import io.github.CodeerStudio.mysticalPets.managers.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the admin command to remove a pet from the player.
 */
public class AdminRemoveCommand implements PetSubCommand{

    private final DatabaseManager databaseManager;

    public AdminRemoveCommand(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

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

    @Override
    public String getPermission() {
        return "mysticalpets.admin.remove";
    }
}
