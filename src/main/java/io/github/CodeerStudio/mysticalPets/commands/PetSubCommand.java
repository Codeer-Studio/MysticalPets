package io.github.CodeerStudio.mysticalPets.commands;

import org.bukkit.command.CommandSender;

public interface PetSubCommand {
    boolean execute(CommandSender sender, String[] strings);
}
