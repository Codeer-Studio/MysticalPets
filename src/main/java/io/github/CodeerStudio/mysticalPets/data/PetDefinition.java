package io.github.CodeerStudio.mysticalPets.data;

/**
 * Represents the definition of a pet in the system, including its unique ID, name, and head texture data.
 */
public class PetDefinition {

    private final String id;
    private final String name;
    private final String headData;

    /**
     * Constructs a new PetDefinition with the provided ID, name, and head texture data.
     *
     * @param id The unique identifier for the pet.
     * @param name The name of the pet.
     * @param headData The head texture data for the pet's head.
     */
    public PetDefinition(String id, String name, String headData) {
        this.id = id;
        this.name = name;
        this.headData = headData;
    }

    /**
     * Gets the unique ID of the pet.
     *
     * @return The unique ID of the pet.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name of the pet.
     *
     * @return The name of the pet.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the head texture data for the pet.
     *
     * @return The head texture data for the pet.
     */
    public String getHeadData() {
        return headData;
    }

    @Override
    public String toString() {
        return String.format("PetDefinition{id='%s', name='%s', headData='%s'}", id, name, headData);
    }
}
