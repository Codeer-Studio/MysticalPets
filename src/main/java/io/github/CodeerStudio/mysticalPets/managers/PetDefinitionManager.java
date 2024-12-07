package io.github.CodeerStudio.mysticalPets.managers;

import io.github.CodeerStudio.mysticalPets.MysticalPets;
import io.github.CodeerStudio.mysticalPets.data.PetDefinition;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the loading and retrieval of pet definitions from the pets.yml file.
 */
public class PetDefinitionManager {

    private final MysticalPets mysticalPets;
    private final Map<String, PetDefinition> petDefinitions = new HashMap<>();

    /**
     * Constructs a PetDefinitionManager and loads pet definitions.
     *
     * @param mysticalPets The main plugin instance.
     */
    public PetDefinitionManager(MysticalPets mysticalPets) {
        this.mysticalPets = mysticalPets;
        loadPetDefinitions();
    }

    /**
     * Retrieves a pet definition by its unique ID.
     *
     * @param id The unique ID of the pet.
     * @return The PetDefinition object, or null if not found.
     */
    public PetDefinition getPetDefinition(String id) {
        return petDefinitions.get(id);
    }

    /**
     * Retrieves all loaded pet definitions as an unmodifiable map.
     *
     * @return An unmodifiable map of all pet definitions.
     */
    public Map<String, PetDefinition> getAllPetDefinitions() {
        return Collections.unmodifiableMap(petDefinitions);
    }

    public void reloadDefinitions() {
        loadPetDefinitions();
    }

    /**
     * Loads all pet definitions from the pets.yml file into memory.
     * Logs warnings for any incomplete or invalid definitions.
     */
    private void loadPetDefinitions() {
        petDefinitions.clear();
        ConfigurationSection petsSection = mysticalPets.getPetsConfig().getConfigurationSection("pets");

        if (petsSection == null) {
            mysticalPets.getLogger().warning("No pets found in pets.yml! Ensure the file contains a 'pets' section.");
            return;
        }

        for (String id : petsSection.getKeys(false)) {
            String name = petsSection.getString(id + ".name");
            String headData = petsSection.getString(id + ".head");

            if (name == null || headData == null) {
                mysticalPets.getLogger().warning(String.format(
                        "Pet definition for ID '%s' is incomplete! Missing 'name' or 'head'.", id));
                continue;
            }

            petDefinitions.put(id, new PetDefinition(id, name, headData));
        }

        mysticalPets.getLogger().info(String.format("Loaded %d pet definitions from pets.yml.", petDefinitions.size()));
    }
}
