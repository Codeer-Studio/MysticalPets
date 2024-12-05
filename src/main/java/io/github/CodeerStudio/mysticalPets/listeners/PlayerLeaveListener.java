package io.github.CodeerStudio.mysticalPets.listeners;

import io.github.CodeerStudio.mysticalPets.managers.PetManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    private final PetManager petManager;

    public PlayerLeaveListener(PetManager petManager) {
        this.petManager = petManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        petManager.playerLeavePet(player);
    }
}
