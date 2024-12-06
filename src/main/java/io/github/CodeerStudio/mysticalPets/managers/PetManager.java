package io.github.CodeerStudio.mysticalPets.managers;

import io.github.CodeerStudio.mysticalPets.MysticalPets;
import io.github.CodeerStudio.mysticalPets.data.PetDefinition;
import io.github.CodeerStudio.mysticalPets.utils.CustomHeadUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the creation, movement, and removal of pets for players.
 * Pets are represented by ArmorStand entities with player heads.
 */
public class PetManager {

    private final MysticalPets mysticalPets;
    private final Map<UUID, ArmorStand> pets = new HashMap<>();

    /**
     * Creates an instance of PetManager.
     *
     * @param mysticalPets The instance of MysticalPets plugin
     */
    public PetManager(MysticalPets mysticalPets) {
        this.mysticalPets = mysticalPets;
    }

    /**
     * Summons a pet for the specified player.
     * The pet is represented by an ArmorStand entity with the player's head on it.
     * The pet follows the player to the left of them.
     *
     * @param player The player who wants to summon the pet
     * @param petDefinition The name of the pet
     * @return true if the pet was summoned successfully, false otherwise
     */
    public String summonPet(Player player, PetDefinition petDefinition) {

        if (pets.get(player.getUniqueId()) != null) {
            return ChatColor.RED + "A pet is already active, remove it to spawn a new one";
        }

        if (petDefinition == null) {
            return ChatColor.RED + "That pet doesn't exist";
        }

        // Calculate the location to the left of the player
        Location playerLocation = player.getLocation();
        Vector direction = playerLocation.getDirection();
        Vector leftDirection = direction.clone().rotateAroundY(Math.toRadians(90)); // Rotate 90 degrees to the left
        Location spawnLocation = playerLocation.add(leftDirection.multiply(1.5)); // Offset 1.5 blocks to the left

        // Spawn an armor stand at the calculated location
        ArmorStand pet = (ArmorStand) player.getWorld().spawnEntity(spawnLocation, EntityType.ARMOR_STAND);

        // Configure the armor stand
        pet.setCustomName(petDefinition.getName());
        pet.setCustomNameVisible(true);
        pet.setGravity(false);
        pet.setInvisible(true);
        pet.setSmall(true);


        // Set the pet's helmet to the custom head
        ItemStack customHead = CustomHeadUtils.createCustomHeadFromURL(petDefinition.getHeadData());

        pet.getEquipment().setHelmet(customHead);

        pet.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        pet.addEquipmentLock(EquipmentSlot.BODY, ArmorStand.LockType.ADDING);
        pet.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING);
        pet.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.ADDING);
        pet.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING);
        pet.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING);
        pet.addEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.ADDING);

        pets.put(player.getUniqueId(), pet);

        petMovement(player, pet);

        return ChatColor.GREEN + "Your " + pet.getCustomName() + " has spawned!";
    }

    /**
     * Dismisses (removes) the pet of the specified player.
     *
     * @param player   The player whose pet is to be dismissed
     * @return true if the pet was dismissed successfully, false otherwise
     */
    public boolean dismissPet(Player player) {
        ArmorStand pet = pets.get(player.getUniqueId());
        if (pet != null) {
            pet.remove();
            pets.remove(player.getUniqueId(), pet);
            return true;
        }
        return false;
    }

    /**
     * Removes all pets from all players.
     * Typically used for cleanup.
     */
    public void removeAllPets() {
        for (ArmorStand pet : pets.values()) {
            pet.remove();  // Remove the pet (ArmorStand)
        }
        pets.clear();
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

            // Calculate the new position for the pet to the player's side
            Location playerLocation = player.getLocation();
            Vector direction = playerLocation.getDirection();
            Vector leftDirection = direction.clone().rotateAroundY(Math.toRadians(90)); // Rotate 90 degrees to the left
            Location targetLocation = playerLocation.clone().add(leftDirection.multiply(1.5)); // Offset 1.5 blocks to the left
            targetLocation.setY(playerLocation.getY() + 1); // Match the player's height + 1

            // Smoothly move the pet by interpolating its position
            Location currentLocation = pet.getLocation();
            double speed = 0.2; // Adjust speed for smoother movement
            double newX = currentLocation.getX() + (targetLocation.getX() - currentLocation.getX()) * speed;
            double newY = currentLocation.getY() + (targetLocation.getY() - currentLocation.getY()) * speed;
            double newZ = currentLocation.getZ() + (targetLocation.getZ() - currentLocation.getZ()) * speed;

            // Set the new interpolated location
            currentLocation.setX(newX);
            currentLocation.setY(newY);
            currentLocation.setZ(newZ);
            pet.teleport(currentLocation);

            pet.setRotation(playerLocation.getYaw(), playerLocation.getPitch());

        }, 0L, 1L);
    }
}
