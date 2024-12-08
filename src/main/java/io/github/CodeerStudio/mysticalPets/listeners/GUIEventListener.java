package io.github.CodeerStudio.mysticalPets.listeners;

import io.github.CodeerStudio.mysticalPets.gui.MainPetGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIEventListener implements Listener {

    private final MainPetGUI mainPetGUI;

    public GUIEventListener(MainPetGUI mainPetGUI) {
        this.mainPetGUI = mainPetGUI;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        mainPetGUI.handleInventoryClick(event);
    }
}
