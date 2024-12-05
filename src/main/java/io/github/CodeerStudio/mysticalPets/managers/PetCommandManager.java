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

/**
 * Manages the /pet command and its subcommands.
 * This class is responsible for registering subcommands and executing the correct subcommand when issued.
 */
public class PetCommandManager implements CommandExecutor {

    // A map of subcommand names to their corresponding PetSubCommand implementations
    private final Map<String, PetSubCommand> subCommands = new HashMap<>();

    /**
     * Registers a new subcommand for the /pet command.
     *
     * @param name the name of the subcommand (e.g., "summon", "dismiss")
     * @param subCommand the PetSubCommand implementation to handle the subcommand's logic
     */
    public void registerSubCommand(String name, PetSubCommand subCommand) {
        subCommands.put(name.toLowerCase(), subCommand);
    }

    /**
     * Executes the appropriate subcommand based on the input provided by the player.
     *
     * @param commandSender the entity who issued the command (usually a player)
     * @param command the original command being executed
     * @param label the alias or name of the command
     * @param args the arguments passed with the command
     * @return true if the command was executed successfully, false otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // If no subcommand is provided, send usage message
        if (args.length < 1) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /pet <subcommand> [args]");
            return true;
        }

        // Get the subcommand name and retrieve the corresponding subcommand
        String subCommandName = args[0].toLowerCase();
        PetSubCommand subCommand = subCommands.get(subCommandName);

        // If the subcommand doesn't exist, inform the player
        if (subCommand == null) {
            commandSender.sendMessage(ChatColor.RED + "Unknown subcommand: " + subCommandName);
            return true;
        }

        // Execute the subcommand with the remaining arguments
        return subCommand.execute(commandSender, Arrays.copyOfRange(args, 1, args.length));
    }
}

