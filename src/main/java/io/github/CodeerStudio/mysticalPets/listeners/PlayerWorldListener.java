package io.github.CodeerStudio.mysticalPets.listeners;

import io.github.CodeerStudio.mysticalPets.managers.PetManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

/**
 * Listens for the event when a player changes worlds.
 * Dismisses the player's pet if one exists.
 */
public class PlayerWorldListener implements Listener {

    private final PetManager petManager;

    /**
     * Creates a new PlayerWorldListener instance.
     *
     * @param petManager The instance of PetManager for managing pets
     */
    public PlayerWorldListener(PetManager petManager) {
        this.petManager = petManager;
    }

    /**
     * Handles the PlayerChangedWorldEvent.
     * Dismisses the player's pet when they change worlds.
     *
     * @param event The PlayerChangedWorldEvent triggered when the player changes worlds
     */
    @EventHandler
    public void onPlayerWorldHop(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        petManager.dismissPet(player);
    }
}
