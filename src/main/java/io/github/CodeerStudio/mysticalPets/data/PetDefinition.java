package io.github.CodeerStudio.mysticalPets.data;

public class PetDefinition {

    private final String id;
    private final String name;
    private final String headData;

    public PetDefinition(String id, String name, String headData) {
        this.id = id;
        this.name = name;
        this.headData = headData;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHeadData() {
        return headData;
    }

}
