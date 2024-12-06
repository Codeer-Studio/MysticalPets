package io.github.CodeerStudio.mysticalPets;

import io.github.CodeerStudio.mysticalPets.commands.PetDismissCommand;
import io.github.CodeerStudio.mysticalPets.commands.PetSummonCommand;
import io.github.CodeerStudio.mysticalPets.listeners.PetInteractionListener;
import io.github.CodeerStudio.mysticalPets.listeners.PlayerLeaveListener;
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
    PetManager petManager;
    PetDefinitionManager petDefinitionManager;
    PetCommandManager petCommandManager;

    // Config Files
    private File petsFile;
    private FileConfiguration petsConfig;

    @Override
    public void onEnable() {

        createPetsFile();

        // Manager creations
        petCommandManager = new PetCommandManager();
        petManager = new PetManager(this);
        petDefinitionManager = new PetDefinitionManager(this);

        // Command creations
        petCommandManager.registerSubCommand("summon", new PetSummonCommand(petManager, petDefinitionManager));
        petCommandManager.registerSubCommand("dismiss", new PetDismissCommand(petManager));
        getCommand("pet").setExecutor(petCommandManager);

        // Listeners creations
        getServer().getPluginManager().registerEvents(new PetInteractionListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(petManager), this);

        getLogger().info("MysticalPets Enabled");

    }

    @Override
    public void onDisable() {
        petManager.removeAllPets();
        getLogger().info("MysticalPets Disabled");
    }

    /**
     * Gets the pets.yml configuration.
     *
     * @return FileConfiguration object for pets.yml.
     */
    public FileConfiguration getPetsConfig() {
        if (petsConfig == null) {
            petsConfig = YamlConfiguration.loadConfiguration(petsFile);
        }
        return petsConfig;
    }

    /**
     * Saves the pets.yml configuration.
     */
    public void savePetsConfig() {
        if (petsConfig == null || petsFile == null) return;
        try {
            petsConfig.save(petsFile);
        } catch (IOException e) {
            getLogger().warning("Could not save pets.yml!");
            e.printStackTrace();
        }
    }

    /**
     * Creates the pets.yml file if it doesn't exist.
     */
    private void createPetsFile() {
        petsFile = new File(getDataFolder(), "pets.yml");
        if (!petsFile.exists()) {
            try {
                petsFile.getParentFile().mkdirs();
                saveResource("pets.yml", false);
            } catch (Exception e) {
                getLogger().warning("Could not create pets.yml file!");
                e.printStackTrace();
            }
        }
        petsConfig = YamlConfiguration.loadConfiguration(petsFile);
    }
}
