package io.github.CodeerStudio.mysticalPets.commands;

import io.github.CodeerStudio.mysticalPets.managers.PetDefinitionManager;
import io.github.CodeerStudio.mysticalPets.managers.PetManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Handles the admin command to reload the pet definitions and clear all active pets.
 * This command is used to refresh the plugin's data from the `pets.yml` file without restarting the server.
 */
public class AdminReloadCommand implements PetSubCommand{

    PetDefinitionManager petDefinitionManager;
    PetManager petManager;

    /**
     * Constructs the AdminReloadCommand with the specified PetManager and PetDefinitionManager.
     *
     * @param petManager the manager responsible for active pet instances.
     * @param petDefinitionManager the manager responsible for loading and managing pet definitions.
     */
    public AdminReloadCommand(PetManager petManager, PetDefinitionManager petDefinitionManager) {
        this.petDefinitionManager = petDefinitionManager;
        this.petManager = petManager;
    }

    /**
     * Executes the command to reload pet definitions and clear all active pets.
     *
     * @param sender the entity executing the command (e.g., player or console).
     * @param args the command arguments (not used in this implementation).
     * @return true if the command executed successfully, false otherwise.
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        petManager.removeAllPets();
        petDefinitionManager.reloadDefinitions();
        return true;
    }

    /**
     * Gets the permission required to execute this command.
     *
     * @return the permission string required for this command.
     */
    @Override
    public String getPermission() {
        return "mysticalpets.admin.reload";
    }
}
