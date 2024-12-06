package io.github.CodeerStudio.mysticalPets.managers;

import io.github.CodeerStudio.mysticalPets.MysticalPets;
import io.github.CodeerStudio.mysticalPets.data.PetDefinition;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class PetDefinitionManager {

    private final MysticalPets mysticalPets;
    private final Map<String, PetDefinition> petDefinitions = new HashMap<>();

    public PetDefinitionManager(MysticalPets mysticalPets) {
        this.mysticalPets = mysticalPets;
        loadPetDefinitions();
    }

    /**
     * Gets a pet definition by its unique ID.
     *
     * @param id The unique ID of the pet.
     * @return The PetDefinition object, or null if not found.
     */
    public PetDefinition getPetDefinition(String id) {
        return petDefinitions.get(id);
    }

    /**
     * Gets all loaded pet definitions.
     *
     * @return A map of all pet definitions.
     */
    public Map<String, PetDefinition> getAllPetDefinitions() {
        return petDefinitions;
    }

    /**
     * Loads all pet definitions from the pets.yml file.
     */
    private void loadPetDefinitions() {
        ConfigurationSection petsSection = mysticalPets.getPetsConfig().getConfigurationSection("pets");
        if (petsSection == null) {
            mysticalPets.getLogger().warning("No pets found in pets.yml!");
            return;
        }

        for (String id : petsSection.getKeys(false)) {
            String name = petsSection.getString(id + ".name");
            String headData = petsSection.getString(id + ".head");

            if (name == null || headData == null) {
                mysticalPets.getLogger().warning("Pet definition for ID '" + id + "' is incomplete!");
                continue;
            }

            petDefinitions.put(id, new PetDefinition(id, name, headData));
        }
    }
}
