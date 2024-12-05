package io.github.CodeerStudio.mysticalPets;

import io.github.CodeerStudio.mysticalPets.commands.PetSummonCommand;
import io.github.CodeerStudio.mysticalPets.managers.PetCommandManager;
import io.github.CodeerStudio.mysticalPets.managers.PetManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MysticalPets extends JavaPlugin {

    @Override
    public void onEnable() {

        PetCommandManager petCommandManager = new PetCommandManager();
        PetManager petManager = new PetManager();

        petCommandManager.registerSubCommand("summon", new PetSummonCommand(petManager));

        getCommand("pet").setExecutor(petCommandManager);

        getLogger().info("MysticalPets Enabled");

    }

    @Override
    public void onDisable() {
        getLogger().info("MysticalPets Disabled");
    }
}
