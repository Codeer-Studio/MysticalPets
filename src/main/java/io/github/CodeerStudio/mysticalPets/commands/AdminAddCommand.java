package io.github.CodeerStudio.mysticalPets.commands;

import io.github.CodeerStudio.mysticalPets.managers.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the admin command to add a pet to a player.
 */
public class AdminAddCommand implements PetSubCommand{

    private final DatabaseManager databaseManager;

    public AdminAddCommand(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

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

    @Override
    public String getPermission() {
        return "mysticalpets.admin.add";
    }
}
