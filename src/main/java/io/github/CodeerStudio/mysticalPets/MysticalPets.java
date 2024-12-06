package io.github.CodeerStudio.mysticalPets;

import io.github.CodeerStudio.mysticalPets.commands.AdminReloadCommand;
import io.github.CodeerStudio.mysticalPets.commands.PetDismissCommand;
import io.github.CodeerStudio.mysticalPets.commands.PetSummonCommand;
import io.github.CodeerStudio.mysticalPets.listeners.PetInteractionListener;
import io.github.CodeerStudio.mysticalPets.listeners.PlayerLeaveListener;
import io.github.CodeerStudio.mysticalPets.listeners.PlayerWorldListener;
import io.github.CodeerStudio.mysticalPets.managers.ConfigManager;
import io.github.CodeerStudio.mysticalPets.managers.PetCommandManager;
import io.github.CodeerStudio.mysticalPets.managers.PetDefinitionManager;
import io.github.CodeerStudio.mysticalPets.managers.PetManager;

import org.bukkit.plugin.java.JavaPlugin;


public final class MysticalPets extends JavaPlugin {

    // Managers
    private PetManager petManager;
    private PetDefinitionManager petDefinitionManager;
    private PetCommandManager petCommandManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);

        initializeManagers();
        loadResourceFiles();
        registerCommands();
        registerListeners();

        getLogger().info("MysticalPets Enabled");
    }

    @Override
    public void onDisable() {
        if (petManager != null) {
            petManager.removeAllPets();
        }

        if (configManager != null) {
            configManager.saveAllConfigs();
        }

        getLogger().info("MysticalPets Disabled");
    }

    /**
     * Initializes the managers used in the plugin.
     */
    private void initializeManagers() {
        petManager = new PetManager(this);
        petDefinitionManager = new PetDefinitionManager(this, configManager);
        petCommandManager = new PetCommandManager();
    }

    /**
     * Registers the commands for the plugin.
     */
    private void registerCommands() {
        petCommandManager.registerSubCommand("summon", new PetSummonCommand(petManager, petDefinitionManager));
        petCommandManager.registerSubCommand("dismiss", new PetDismissCommand(petManager));
        petCommandManager.registerSubCommand("reload", new AdminReloadCommand(petManager, petDefinitionManager));

        getCommand("pet").setExecutor(petCommandManager);
    }

    /**
     * Registers the event listeners for the plugin.
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PetInteractionListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(petManager), this);
        getServer().getPluginManager().registerEvents(new PlayerWorldListener(petManager), this);
    }

    /**
     * Registers the resources files (configs)
     */
    private void loadResourceFiles() {
        configManager.loadConfig("pets.yml");
        petDefinitionManager.reloadDefinitions();
    }
}
