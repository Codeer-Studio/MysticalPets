package io.github.CodeerStudio.mysticalPets;

import io.github.CodeerStudio.mysticalPets.commands.*;
import io.github.CodeerStudio.mysticalPets.gui.MainPetGUI;
import io.github.CodeerStudio.mysticalPets.listeners.GUIEventListener;
import io.github.CodeerStudio.mysticalPets.listeners.PetInteractionListener;
import io.github.CodeerStudio.mysticalPets.listeners.PlayerLeaveListener;
import io.github.CodeerStudio.mysticalPets.listeners.PlayerWorldListener;
import io.github.CodeerStudio.mysticalPets.managers.*;

import org.bukkit.plugin.java.JavaPlugin;


public final class MysticalPets extends JavaPlugin {

    // Managers
    private PetManager petManager;
    private PetDefinitionManager petDefinitionManager;
    private PetCommandManager petCommandManager;
    private ConfigManager configManager;
    private DatabaseManager databaseManager;

    // GUI
    private MainPetGUI mainPetGUI;


    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);

        initializeManagers();
        loadResourceFiles();
        initializeGUI();
        registerCommands();
        registerListeners();

        databaseManager.setupDatabase();

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

        if (databaseManager != null) {
            databaseManager.closeConnection();
        }

        getLogger().info("MysticalPets Disabled");
    }

    /**
     * Initializes the managers used in the plugin.
     */
    private void initializeManagers() {
        databaseManager = new DatabaseManager();
        petManager = new PetManager(this, databaseManager);
        petDefinitionManager = new PetDefinitionManager(this, configManager);
        petCommandManager = new PetCommandManager();
    }

    /**
     * Initializes the gui menus for the plugin.
     */
    private void initializeGUI() {
        mainPetGUI = new MainPetGUI(databaseManager, petManager, petDefinitionManager);
    }

    /**
     * Registers the commands for the plugin.
     */
    private void registerCommands() {
        petCommandManager.registerSubCommand("summon", new PetSummonCommand(petManager, petDefinitionManager));
        petCommandManager.registerSubCommand("dismiss", new PetDismissCommand(petManager));
        petCommandManager.registerSubCommand("reload", new AdminReloadCommand(petManager, petDefinitionManager));
        petCommandManager.registerSubCommand("add", new AdminAddCommand(databaseManager));
        petCommandManager.registerSubCommand("remove", new AdminRemoveCommand(databaseManager));
        petCommandManager.registerSubCommand("pet", new PetCommand(mainPetGUI));

        getCommand("pet").setExecutor(petCommandManager);
    }

    /**
     * Registers the event listeners for the plugin.
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PetInteractionListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(petManager), this);
        getServer().getPluginManager().registerEvents(new PlayerWorldListener(petManager), this);
        getServer().getPluginManager().registerEvents(new GUIEventListener(mainPetGUI), this);
    }

    /**
     * Registers the resources files (configs)
     */
    private void loadResourceFiles() {
        configManager.loadConfig("pets.yml");
        petDefinitionManager.reloadDefinitions();
    }
}
