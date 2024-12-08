package io.github.CodeerStudio.mysticalPets.gui;

import io.github.CodeerStudio.mysticalPets.managers.DatabaseManager;
import io.github.CodeerStudio.mysticalPets.managers.PetDefinitionManager;
import io.github.CodeerStudio.mysticalPets.managers.PetManager;
import io.github.CodeerStudio.mysticalPets.utils.CustomHeadUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Represents the main GUI menu for a player's pets.
 * Allows players to view and spawn their owned pets.
 */
public class MainPetGUI {

    private final DatabaseManager databaseManager;
    private final PetManager petManager;
    private final PetDefinitionManager petDefinitionManager;


    /**
     * Constructs the MainPetGUI with the required managers.
     *
     * @param databaseManager the database manager for querying owned pets.
     * @param petManager the pet manager for handling pet spawning.
     * @param petDefinitionManager the manager for retrieving pet definitions.
     */
    public MainPetGUI(DatabaseManager databaseManager, PetManager petManager, PetDefinitionManager petDefinitionManager) {
        this.databaseManager = databaseManager;
        this.petManager = petManager;
        this.petDefinitionManager = petDefinitionManager;
    }

    /**
     * Opens the pet GUI for the specified player.
     * The GUI dynamically adjusts its size based on the number of owned pets.
     *
     * @param player the player for whom the GUI is being opened.
     */
    public void openGUI(Player player) {
        List<String> ownedPets = databaseManager.getOwnedPets(String.valueOf(player.getUniqueId()));

        int size = ((ownedPets.size() - 1) / 9 + 1) * 9; // Dynamic size based on the number of pets
        Inventory inventory = Bukkit.createInventory(null, size, ChatColor.AQUA + "Your Pets");

        for (String petId : ownedPets) {
            ItemStack petItem = createPetItem(petId);
            inventory.addItem(petItem);
        }

        player.openInventory(inventory);
    }

    /**
     * Handles inventory click events within the pet GUI.
     * Prevents players from taking items out of the inventory.
     *
     * @param event the inventory click event.
     */
    public void handleInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(ChatColor.AQUA + "Your Pets")) return;

        event.setCancelled(true);
    }

    /**
     * Creates an item stack representing a pet, including its name and usage instructions.
     *
     * @param petId the ID of the pet for which to create the item.
     * @return an ItemStack representing the pet.
     */
    private ItemStack createPetItem(String petId) {
        ItemStack item = CustomHeadUtils.createCustomHeadFromURL(petDefinitionManager.getPetDefinition(petId).getHeadData());
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Pet: " + ChatColor.YELLOW + petId);
            meta.setLore(List.of(
                    ChatColor.GRAY + "Left-click to summon this pet."
            ));
            item.setItemMeta(meta);
        }

        return item;
    }
}
