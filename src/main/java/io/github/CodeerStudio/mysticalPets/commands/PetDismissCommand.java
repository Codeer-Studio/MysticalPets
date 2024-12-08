package io.github.CodeerStudio.mysticalPets.commands;

import io.github.CodeerStudio.mysticalPets.managers.PetManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the "dismiss" subcommand for pets.
 * Allows players to dismiss a pet by name, removing it from the game world.
 */
public class PetDismissCommand implements PetSubCommand {

    private final PetManager petManager;

    /**
     * Constructs a PetDismissCommand with the specified PetManager.
     *
     * @param petManager the PetManager instance used to manage pets
     */
    public PetDismissCommand(PetManager petManager) {
        this.petManager = petManager;
    }

    /**
     * Executes the "dismiss" subcommand.
     * Dismisses a pet for the player by name if the pet exists and belongs to the player.
     *
     * @param sender the sender of the command, must be a player
     * @param args the command arguments; expects the pet name as the first argument
     * @return true to indicate the command has been handled
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        // Check if the command sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Ensure a pet name is provided
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /pet dismiss <petName>");
            return true;
        }

        // Checks if the pet exists in the pets.yml file
        String petName = args[0];
        petManager.dismissPet(player);

        return true;
    }
}
