package io.github.CodeerStudio.mysticalPets.commands;

import io.github.CodeerStudio.mysticalPets.managers.PetDefinitionManager;
import io.github.CodeerStudio.mysticalPets.managers.PetManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AdminReloadCommand implements PetSubCommand{

    PetDefinitionManager petDefinitionManager;
    PetManager petManager;

    public AdminReloadCommand(PetManager petManager, PetDefinitionManager petDefinitionManager) {
        this.petDefinitionManager = petDefinitionManager;
        this.petManager = petManager;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        petManager.removeAllPets();
        petDefinitionManager.reloadDefinitions();
        sender.sendMessage(ChatColor.GREEN + "Pets.yml reloaded successfully");
        return true;
    }

    @Override
    public String getPermission() {
        return "mysticalpets.admin.reload";
    }
}
