package io.github.CodeerStudio.mysticalPets;

import io.github.CodeerStudio.mysticalPets.commands.AdminReloadCommand;
import io.github.CodeerStudio.mysticalPets.commands.PetDismissCommand;
import io.github.CodeerStudio.mysticalPets.commands.PetSummonCommand;
import io.github.CodeerStudio.mysticalPets.listeners.PetInteractionListener;
import io.github.CodeerStudio.mysticalPets.listeners.PlayerLeaveListener;
import io.github.CodeerStudio.mysticalPets.listeners.PlayerWorldListener;
import io.github.CodeerStudio.mysticalPets.managers.PetCommandManager;
import io.github.CodeerStudio.mysticalPets.managers.PetDefinitionManager;
import io.github.CodeerStudio.mysticalPets.managers.PetManager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class MysticalPets extends JavaPlugin {

    // Managers
    private PetManager petManager;
    private PetDefinitionManager petDefinitionManager;
    private PetCommandManager petCommandManager;

    // Config Files
    private File petsFile;
    private FileConfiguration petsConfig;

    @Override
    public void onEnable() {
        setupConfigFiles();
        initializeManagers();
        registerCommands();
        registerListeners();

        getLogger().info("MysticalPets Enabled");
    }

    @Override
    public void onDisable() {
        if (petManager != null) {
            petManager.removeAllPets();
        }

        savePetsConfig();

        getLogger().info("MysticalPets Disabled");
    }

    /**
     * Retrieves the pets.yml configuration.
     *
     * @return The FileConfiguration object for pets.yml.
     */
    public FileConfiguration getPetsConfig() {
        if (petsConfig == null) {
            petsConfig = YamlConfiguration.loadConfiguration(petsFile);
        }
        return petsConfig;
    }

    /**
     * Saves the pets.yml configuration to disk.
     */
    public void savePetsConfig() {
        if (petsConfig == null || petsFile == null) {
            getLogger().warning("petsConfig or petsFile is null!");
            return;
        }
        try {
            petsConfig.save(petsFile);
        } catch (IOException e) {
            getLogger().warning("Could not save pets.yml!");
            e.printStackTrace();
        }
    }

    /**
     * Initializes the configuration files.
     */
    public void setupConfigFiles() {
        petsFile = new File(getDataFolder(), "pets.yml");
        if (!petsFile.exists()) {
            saveResource("pets.yml", false);
        }
        petsConfig = YamlConfiguration.loadConfiguration(petsFile);
    }

    /**
     * Initializes the managers used in the plugin.
     */
    private void initializeManagers() {
        petManager = new PetManager(this);
        petDefinitionManager = new PetDefinitionManager(this);
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
}
