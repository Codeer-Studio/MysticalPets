package io.github.CodeerStudio.mysticalPets.managers;

import io.github.CodeerStudio.mysticalPets.commands.PetSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PetCommandManager implements CommandExecutor {

    private final Map<String, PetSubCommand> subCommands = new HashMap<>();

    public void registerSubCommand(String name, PetSubCommand subCommand) {
        subCommands.put(name.toLowerCase(), subCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings.length < 1) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /pet <subcommand> [args]");
            return true;
        }

        String subCommandName = strings[0].toLowerCase();
        PetSubCommand subCommand = subCommands.get(subCommandName);

        if (subCommand == null) {
            commandSender.sendMessage(ChatColor.RED + "Unknown subcommand: " + subCommandName);
            return true;
        }

        return subCommand.execute(commandSender, Arrays.copyOfRange(strings, 1, strings.length));
    }
}
