package io.github.CodeerStudio.mysticalPets.commands;

import org.bukkit.command.CommandSender;

/**
 * Interface for implementing subcommands under the /pet command.
 * Each subcommand will implement this interface and provide specific functionality when executed.
 */
public interface PetSubCommand {

    /**
     * Executes the specific subcommand logic.
     * This method is called when the player runs a specific subcommand under /pet.
     *
     * @param sender the CommandSender who issued the command (must be a player in most cases)
     * @param args the arguments passed with the command (e.g., pet name, other options)
     * @return true if the command was executed successfully, false otherwise
     */
    boolean execute(CommandSender sender, String[] args);

    /**
     * Gets the permission required to use this subcommand.
     *
     * @return The permission string, or null if no permission is required
     */
    default String getPermission() {
        return null; // No permission required by default
    }
}

