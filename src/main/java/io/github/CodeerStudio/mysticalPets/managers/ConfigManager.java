package io.github.CodeerStudio.mysticalPets.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *  Manages multiple configuration files for the plugin
 */
public class ConfigManager {

    private final JavaPlugin plugin;
    private final Logger logger;
    private final Map<String, FileConfiguration> configs = new HashMap<>();
    private final Map<String, File> files = new HashMap<>();

    /**
     * Creates a ConfigManager instance.
     *
     * @param plugin The plugin instance
     */
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    /**
     * Loads or creates a configuration file.
     *
     * @param fileName The name of the file (e.g., "pets.yml")
     */
    public void loadConfig(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);

        // Create the file if it doesn't exist
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }

        // Load the configuration
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Store the file and configuration
        files.put(fileName, file);
        configs.put(fileName, config);

        logger.info("Loaded configuration file: " + fileName);
    }

    /**
     * Retrieves the FileConfiguration for the specified file.
     *
     * @param fileName The name of the file
     * @return The FileConfiguration object
     */
    public FileConfiguration getConfig(String fileName) {
        return configs.get(fileName);
    }

    /**
     * Saves the specified configuration file to disk.
     *
     * @param fileName The name of the file
     */
    public void saveConfig(String fileName) {
        File file = files.get(fileName);
        FileConfiguration config = configs.get(fileName);

        if (file == null || config == null) {
            logger.warning("Cannot save config file: " + fileName + " (file or config not found)");
            return;
        }

        try {
            config.save(file);
            logger.info("Saved configuration file: " + fileName);
        } catch (IOException e) {
            logger.severe("Could not save config file: " + fileName);
            e.printStackTrace();
        }
    }

    /**
     * Reloads the specified configuration file from disk.
     *
     * @param fileName The name of the file
     */
    public void reloadConfig(String fileName) {
        File file = files.get(fileName);

        if (file == null) {
            logger.warning("Cannot reload config file: " + fileName + " (file not found)");
            return;
        }

        configs.put(fileName, YamlConfiguration.loadConfiguration(file));
        logger.info("Reloaded configuration file: " + fileName);
    }

    /**
     * Saves all loaded configuration files to disk.
     */
    public void saveAllConfigs() {
        for (String fileName : configs.keySet()) {
            saveConfig(fileName);
        }
    }

    /**
     * Reloads all loaded configuration files from disk.
     */
    public void reloadAllConfigs() {
        for (String fileName : files.keySet()) {
            reloadConfig(fileName);
        }
    }


}
