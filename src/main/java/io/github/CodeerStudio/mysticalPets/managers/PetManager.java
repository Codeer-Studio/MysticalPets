package io.github.CodeerStudio.mysticalPets.managers;

import io.github.CodeerStudio.mysticalPets.MysticalPets;
import io.github.CodeerStudio.mysticalPets.data.PetDefinition;
import io.github.CodeerStudio.mysticalPets.utils.CustomHeadUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the creation, movement, and removal of pets for players.
 * Pets are represented by ArmorStand entities with player heads.
 */
public class PetManager {

    private final MysticalPets mysticalPets;
    private final DatabaseManager databaseManager;
    private final Map<UUID, ArmorStand> pets = new HashMap<>();
    private final Map<UUID, String> activePets = new HashMap<>();

    /**
     * Creates an instance of PetManager.
     *
     * @param mysticalPets The instance of MysticalPets plugin
     */
    public PetManager(MysticalPets mysticalPets, DatabaseManager databaseManager) {
        this.mysticalPets = mysticalPets;
        this.databaseManager = databaseManager;
    }

    /**
     * Summons a pet for the specified player.
     * The pet is represented by an ArmorStand entity with the player's head on it.
     * The pet follows the player to the left of them.
     *
     * @param player       The player who wants to summon the pet
     * @param petDefinition The pet definition containing the name and head data
     */
    public void summonPet(Player player, PetDefinition petDefinition) {

        if (!databaseManager.ownsPet(String.valueOf(player.getUniqueId()), petDefinition.getId())) {
            player.sendMessage(ChatColor.RED + "You don't own this pet");
            return;
        }

        if (pets.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "A pet is already active, remove it to spawn a new one");
            return;
        }

        if (petDefinition == null) {
            player.sendMessage(ChatColor.RED + "That pet doesn't exist");
            return;
        }

        // Calculate the location to the left of the player
        Location spawnLocation = calculatePetLocation(player);

        // Spawn an armor stand at the calculated location
        ArmorStand pet = spawnArmorStand(spawnLocation, petDefinition);

        pets.put(player.getUniqueId(), pet);
        activePets.put(player.getUniqueId(), petDefinition.getId());

        // Start moving the pet smoothly
        petMovement(player, pet);

        player.sendMessage(ChatColor.GREEN + "Your " + pet.getCustomName() + " has spawned!");
    }

    /**
     * Dismisses (removes) the pet of the specified player.
     *
     * @param player The player whose pet is to be dismissed
     */
    public void dismissPet(Player player) {
        ArmorStand pet = pets.get(player.getUniqueId());
        if (pet != null) {
            pet.remove();
            pets.remove(player.getUniqueId(), pet);
            activePets.remove(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "Your pet " + pet.getCustomName() + ChatColor.GREEN + " has been dismissed!");
            return;
        }
        player.sendMessage(ChatColor.RED + "Failed to dismiss pet. Make sure you own it and it exists!");
    }

    /**
     * Removes all pets from all players. Typically used for cleanup.
     */
    public void removeAllPets() {
        for (ArmorStand pet : pets.values()) {
            pet.remove(); // Remove the pet (ArmorStand)
        }
        pets.clear();
        activePets.clear();
    }

    public String  getUserActivePet(Player player) {
        return activePets.get(player.getUniqueId());
    }

    /**
     * Moves the pet to follow the player at a smooth pace.
     * The pet will always stick to the left side of the player.
     *
     * @param player The player whose pet is following
     * @param pet    The ArmorStand representing the pet
     */
    private void petMovement(Player player, ArmorStand pet) {

        pet.setMetadata("isPet", new FixedMetadataValue(mysticalPets, true));

        Bukkit.getScheduler().runTaskTimer(mysticalPets, () -> {

            // Smoothly move the pet by interpolating its position
            Location targetLocation = calculatePetLocation(player);
            Location currentLocation = pet.getLocation();

            double speed = 0.2; // Adjust speed for smoother movement
            currentLocation.setX(lerp(currentLocation.getX(), targetLocation.getX(), speed));
            currentLocation.setY(lerp(currentLocation.getY(), targetLocation.getY(), speed));
            currentLocation.setZ(lerp(currentLocation.getZ(), targetLocation.getZ(), speed));

            pet.teleport(currentLocation);
            pet.setRotation(player.getLocation().getYaw(), player.getLocation().getPitch());

        }, 0L, 1L);
    }

    /**
     * Calculates the location for the pet to the left of the player.
     *
     * @param player The player whose pet is being summoned
     * @return The calculated location for the pet
     */
    private Location calculatePetLocation(Player player) {
        Location playerLocation = player.getLocation();
        Vector direction = playerLocation.getDirection();

        // Remove the vertical component (Y axis) of the direction vector
        direction.setY(0); // This ensures the pet moves horizontally only

        // Normalize the direction vector to ensure the pet moves at a consistent speed
        direction = direction.normalize();

        // Rotate the direction 90 degrees to the left
        Vector leftDirection = direction.clone().rotateAroundY(Math.toRadians(90));

        // Calculate the target location for the pet to the left of the player (1.5 blocks away)
        Location targetLocation = playerLocation.add(leftDirection.multiply(1.5));

        // Ensure the pet's Y position matches the player's Y position + offset (e.g., above the player)
        targetLocation.setY(playerLocation.getY() + 1);

        return targetLocation;
    }

    /**
     * Spawns an armor stand at the specified location with the given pet definition.
     *
     * @param spawnLocation The location where the pet will spawn
     * @param petDefinition The pet definition containing name and head data
     * @return The spawned ArmorStand representing the pet
     */
    private ArmorStand spawnArmorStand(Location spawnLocation, PetDefinition petDefinition) {
        ArmorStand pet = (ArmorStand) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ARMOR_STAND);

        // Configure the armor stand
        pet.setCustomName(petDefinition.getName());
        pet.setCustomNameVisible(true);
        pet.setGravity(false);
        pet.setInvisible(true);
        pet.setSmall(true);

        // Set the pet's helmet to the custom head
        ItemStack customHead = CustomHeadUtils.createCustomHeadFromURL(petDefinition.getHeadData());
        pet.getEquipment().setHelmet(customHead);

        // Lock armor stand equipment to prevent modification
        lockArmorStandEquipment(pet);

        return pet;
    }

    /**
     * Locks the armor stand equipment to prevent changes.
     *
     * @param pet The armor stand representing the pet
     */
    private void lockArmorStandEquipment(ArmorStand pet) {
        pet.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        pet.addEquipmentLock(EquipmentSlot.BODY, ArmorStand.LockType.ADDING);
        pet.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING);
        pet.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.ADDING);
        pet.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING);
        pet.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING);
        pet.addEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.ADDING);
    }

    /**
     * Performs linear interpolation between two values.
     *
     * @param start The starting value
     * @param end The ending value
     * @param t The interpolation factor (between 0 and 1)
     * @return The interpolated value
     */
    private double lerp(double start, double end, double t) {
        return start + (end - start) * t;
    }
}
