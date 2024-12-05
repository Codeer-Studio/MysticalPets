package io.github.CodeerStudio.mysticalPets.listeners;

import io.github.CodeerStudio.mysticalPets.managers.PetManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * A listener that handles player disconnection events.
 * When a player leaves the server, their associated pet is removed from the world.
 */
public class PlayerLeaveListener implements Listener {

    private final PetManager petManager;

    /**
     * Constructs a PlayerLeaveListener with the specified PetManager.
     *
     * @param petManager the PetManager instance used to manage pets
     */
    public PlayerLeaveListener(PetManager petManager) {
        this.petManager = petManager;
    }

    /**
     * Handles the PlayerQuitEvent.
     * Invoked when a player leaves the server. This ensures any pets
     * associated with the player are removed to prevent lingering entities.
     *
     * @param event the PlayerQuitEvent containing the leaving player's details
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Use PetManager to handle pet removal logic for the player
        petManager.dismissPet(player);
    }
}