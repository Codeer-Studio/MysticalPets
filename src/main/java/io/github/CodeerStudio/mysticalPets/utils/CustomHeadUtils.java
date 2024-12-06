package io.github.CodeerStudio.mysticalPets.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Utility class for creating custom player heads using a texture URL.
 */
public class CustomHeadUtils {

    /**
     * Creates a custom player head item with the specified texture URL.
     *
     * @param textureUrl The URL of the skin texture.
     * @return An ItemStack representing the custom player head.
     */
    public static ItemStack createCustomHeadFromURL(String textureUrl) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        if (meta != null) {
            PlayerProfile profile = createPlayerProfileFromURL(textureUrl);
            meta.setOwnerProfile(profile);
            head.setItemMeta(meta);
        }

        return head;
    }

    /**
     * Creates a PlayerProfile with the specified texture URL.
     *
     * @param textureUrl The URL of the skin texture.
     * @return A PlayerProfile with the custom texture applied.
     * @throws RuntimeException If the provided URL is invalid.
     */
    private static PlayerProfile createPlayerProfileFromURL(String textureUrl) {
        PlayerProfile profile = (PlayerProfile) Bukkit.createPlayerProfile(UUID.randomUUID()); // Generate a random UUID
        PlayerTextures textures = profile.getTextures();

        try {
            URL url = new URL(textureUrl);
            textures.setSkin(url);
            profile.setTextures(textures);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid texture URL: " + textureUrl, e);
        }

        return profile;
    }
}
