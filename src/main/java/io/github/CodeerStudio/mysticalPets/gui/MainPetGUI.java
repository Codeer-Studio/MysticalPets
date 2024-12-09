package io.github.CodeerStudio.mysticalPets.gui;

import io.github.CodeerStudio.mysticalPets.managers.DatabaseManager;
import io.github.CodeerStudio.mysticalPets.managers.PetDefinitionManager;
import io.github.CodeerStudio.mysticalPets.managers.PetManager;
import io.github.CodeerStudio.mysticalPets.utils.CustomHeadUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
    public void openGUI(Player player, int page) {
        List<String> ownedPets = databaseManager.getOwnedPets(String.valueOf(player.getUniqueId()));

        int size = 54;
        Inventory inventory = Bukkit.createInventory(null, size, ChatColor.AQUA + "Your Pets (Page " + (page + 1) + ")");

        ItemStack borderItem = createBorderItem();
        for (int i = 0; i < size; i++) {
            if (isBorderSlot(i, size)) {
                inventory.setItem(i, borderItem);
            }
        }

        // Pagination logic: Display pets for the current page.
        int petsPerPage = 21; // Slots available for pets
        int startIndex = page * petsPerPage;
        int endIndex = Math.min(startIndex + petsPerPage, ownedPets.size());

        for (int i = startIndex; i < endIndex; i++) {
            String petId = ownedPets.get(i);
            ItemStack petItem = createPetItem(petId);
            inventory.addItem(petItem);
        }

        // Add navigation arrows.
        if (page > 0) {
            inventory.setItem(39, createNavigationItem(ChatColor.YELLOW + "Back", Material.ARROW));
        }
        if (endIndex < ownedPets.size()) {
            inventory.setItem(41, createNavigationItem(ChatColor.YELLOW + "Next", Material.ARROW));
        }

        String activePet = petManager.getUserActivePet(player);
        if (activePet != null) {
            inventory.setItem(37, activePetItem(activePet));
        }

        player.openInventory(inventory);
    }

    /**
     * Handles inventory click events within the pet GUI.
     * Prevents players from taking items out of the inventory.
     *
     * @param event the inventory click event.
     */
    public void handleInventoryClick(InventoryClickEvent event, Player player, int currentPage) {
        if (!event.getView().getTitle().startsWith(ChatColor.AQUA + "Your Pets")) return;

        event.setCancelled(true); // Prevent default interaction.

        int clickedSlot = event.getSlot();
        ItemStack clickedItem = event.getCurrentItem();

        // Handle navigation clicks.
        if (clickedSlot == 39) { // Back arrow.
            openGUI(player, currentPage - 1);
            return;
        } else if (clickedSlot == 41) { // Next arrow.
            openGUI(player, currentPage + 1);
            return;
        }

        if (clickedItem == null || !clickedItem.hasItemMeta() || clickedItem.getItemMeta().getDisplayName() == null) {
            return;
        }

        if (clickedSlot == 37) {
            petManager.dismissPet(player);
            player.closeInventory();
            return;
        }

        String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
        if (displayName.startsWith("Pet: ")) {
            String petId = displayName.replace("Pet: ", "").trim();

            try {
                petManager.summonPet(player, petDefinitionManager.getPetDefinition(petId));
                player.closeInventory();
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Failed to spawn pet: " + ChatColor.GOLD + petId);
                e.printStackTrace();
            }
        }

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

    /**
     * Creates a decorative item for the border of the inventory.
     *
     * @return an ItemStack representing a black stained-glass pane.
     */
    private ItemStack createBorderItem() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(" ");
            item.setItemMeta(meta);
        }

        return item;
    }

    /**
     * Creates an item for navigation (e.g., Back or Next).
     *
     * @param name the display name of the navigation item.
     * @param material the material to use for the navigation item.
     * @return an ItemStack representing the navigation item.
     */
    private ItemStack createNavigationItem(String name, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }

        return item;
    }

    /**
     * Creates an item stack representing a pet, including its name and usage instructions.
     *
     * @param petId the ID of the pet for which to create the item.
     * @return an ItemStack representing the pet.
     */
    private ItemStack activePetItem(String petId) {
        ItemStack item = CustomHeadUtils.createCustomHeadFromURL(petDefinitionManager.getPetDefinition(petId).getHeadData());
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Pet: " + ChatColor.YELLOW + petId);
            meta.setLore(List.of(
                    ChatColor.GRAY + "Left-click to dismiss this pet."
            ));
            item.setItemMeta(meta);
        }

        return item;
    }

    /**
     * Determines if a given slot index is part of the border.
     *
     * @param slot the slot index to check.
     * @param size the total size of the inventory.
     * @return true if the slot is a border slot, false otherwise.
     */
    private boolean isBorderSlot(int slot, int size) {
        int rows = size / 9;
        int row = slot / 9;
        int col = slot % 9;

        return row == 0 || row == rows - 1 || col == 0 || col == 8;
    }
}
