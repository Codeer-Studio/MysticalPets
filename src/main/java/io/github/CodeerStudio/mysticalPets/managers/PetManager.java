package io.github.CodeerStudio.mysticalPets.managers;

import io.github.CodeerStudio.mysticalPets.MysticalPets;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Skull;
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

public class PetManager {

    private final MysticalPets mysticalPets;
    private Map<UUID, ArmorStand> pets = new HashMap<>();

    public PetManager(MysticalPets mysticalPets) {
        this.mysticalPets = mysticalPets;
    }

    public boolean summonPet(Player player, String petName) {

        // Calculate the location to the left of the player
        Location playerLocation = player.getLocation();
        Vector direction = playerLocation.getDirection();
        Vector leftDirection = direction.clone().rotateAroundY(Math.toRadians(90)); // Rotate 90 degrees to the left
        Location spawnLocation = playerLocation.add(leftDirection.multiply(1.5)); // Offset 1.5 blocks to the left

        // Spawn an armor stand at the calculated location
        ArmorStand pet = (ArmorStand) player.getWorld().spawnEntity(spawnLocation, EntityType.ARMOR_STAND);

        // Configure the armor stand
        pet.setCustomName(petName + " Pet");
        pet.setCustomNameVisible(true);
        pet.setGravity(false);
        pet.setInvisible(true);
        pet.setSmall(true);

        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD,1);
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(player);
            playerHead.setItemMeta(skullMeta);
        }

        pet.getEquipment().setHelmet(playerHead);

        pet.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        pet.addEquipmentLock(EquipmentSlot.BODY, ArmorStand.LockType.ADDING);
        pet.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING);
        pet.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.ADDING);
        pet.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING);
        pet.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING);
        pet.addEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.ADDING);

        pets.put(player.getUniqueId(), pet);

        return true;
    }

    public boolean dismissPet(Player player, String petName) {

        ArmorStand pet = pets.get(player.getUniqueId());

        pet.remove();

        pets.remove(player.getUniqueId(), pet);

        return true;
    }
}
