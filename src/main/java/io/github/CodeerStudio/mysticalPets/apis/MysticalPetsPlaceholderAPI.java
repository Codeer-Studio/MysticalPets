package io.github.CodeerStudio.mysticalPets.apis;

import io.github.CodeerStudio.mysticalPets.managers.DatabaseManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MysticalPetsPlaceholderAPI extends PlaceholderExpansion {

    private final DatabaseManager databaseManager;

    public MysticalPetsPlaceholderAPI(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mysticalpets";
    }

    @Override
    public @NotNull String getAuthor() {
        return "CodeerStudio";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return ""; // Return empty string if no player context is provided.
        }

        // Check for pet ownership placeholder: %mysticalpets_owns_<petID>%
        if (params.startsWith("owns_")) {
            String petID = params.substring(5); // Extract the pet ID.
            boolean ownsPet = databaseManager.ownsPet(player.getUniqueId().toString(), petID);
            return ownsPet ? "true" : "false";
        }

        return null;
    }
}
