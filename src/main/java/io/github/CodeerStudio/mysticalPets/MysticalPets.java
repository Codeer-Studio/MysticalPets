package io.github.CodeerStudio.mysticalPets;

import io.github.CodeerStudio.mysticalPets.commands.PetDismissCommand;
import io.github.CodeerStudio.mysticalPets.commands.PetSummonCommand;
import io.github.CodeerStudio.mysticalPets.listeners.PetInteractionListener;
import io.github.CodeerStudio.mysticalPets.listeners.PlayerLeaveListener;
import io.github.CodeerStudio.mysticalPets.managers.PetCommandManager;
import io.github.CodeerStudio.mysticalPets.managers.PetManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MysticalPets extends JavaPlugin {

    PetManager petManager;

    @Override
    public void onEnable() {

        PetCommandManager petCommandManager = new PetCommandManager();
        petManager = new PetManager(this);

        petCommandManager.registerSubCommand("summon", new PetSummonCommand(petManager));
        petCommandManager.registerSubCommand("dismiss", new PetDismissCommand(petManager));

        getCommand("pet").setExecutor(petCommandManager);

        getServer().getPluginManager().registerEvents(new PetInteractionListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(petManager), this);

        getLogger().info("MysticalPets Enabled");

    }

    @Override
    public void onDisable() {
        petManager.removeAllPets();
        getLogger().info("MysticalPets Disabled");
    }
}
