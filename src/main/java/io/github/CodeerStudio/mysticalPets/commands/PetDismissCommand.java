package io.github.CodeerStudio.mysticalPets.commands;

import io.github.CodeerStudio.mysticalPets.managers.PetManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PetDismissCommand implements PetSubCommand{

    private final PetManager petManager;

    public PetDismissCommand(PetManager petManager) {
        this.petManager = petManager;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        if (strings.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /pet dismiss <petName>");
            return true;
        }

        String petName = strings[0];
        boolean success = petManager.dismissPet(player, petName);

        if (success) {
            player.sendMessage(ChatColor.GREEN + "Your pet " + petName + " has been dismissed!");
        } else {
            player.sendMessage(ChatColor.RED + "Failed to summon pet. Make sure you own it!");
        }

        return true;
    }
}
