package io.github.CodeerStudio.mysticalPets.commands;

import io.github.CodeerStudio.mysticalPets.gui.MainPetGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PetCommand implements PetSubCommand {

    private final MainPetGUI mainPetGUI;

    public PetCommand(MainPetGUI mainPetGUI) {
        this.mainPetGUI = mainPetGUI;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        mainPetGUI.openGUI(player);
        return true;
    }

}
