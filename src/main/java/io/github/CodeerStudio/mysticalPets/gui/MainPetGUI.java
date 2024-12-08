package io.github.CodeerStudio.mysticalPets.gui;

import io.github.CodeerStudio.mysticalPets.managers.DatabaseManager;
import io.github.CodeerStudio.mysticalPets.managers.PetDefinitionManager;
import io.github.CodeerStudio.mysticalPets.managers.PetManager;
import io.github.CodeerStudio.mysticalPets.utils.CustomHeadUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
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
     */
    public MainPetGUI(DatabaseManager databaseManager, PetManager petManager, PetDefinitionManager petDefinitionManager) {
        this.databaseManager = databaseManager;
        this.petManager = petManager;
        this.petDefinitionManager = petDefinitionManager;
    }

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

    public void handleInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(ChatColor.AQUA + "Your Pets")) return;

        event.setCancelled(true);
    }

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
