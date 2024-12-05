package io.github.CodeerStudio.mysticalPets;

import io.github.CodeerStudio.mysticalPets.commands.PetSummonCommand;
import io.github.CodeerStudio.mysticalPets.listeners.PetInteractionListener;
import io.github.CodeerStudio.mysticalPets.managers.PetCommandManager;
import io.github.CodeerStudio.mysticalPets.managers.PetManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MysticalPets extends JavaPlugin {

    @Override
    public void onEnable() {

        PetCommandManager petCommandManager = new PetCommandManager();
        PetManager petManager = new PetManager(this);

        petCommandManager.registerSubCommand("summon", new PetSummonCommand(petManager));

        getCommand("pet").setExecutor(petCommandManager);

        getServer().getPluginManager().registerEvents(new PetInteractionListener(), this);

        getLogger().info("MysticalPets Enabled");

    }

    @Override
    public void onDisable() {
        getLogger().info("MysticalPets Disabled");
    }
}
