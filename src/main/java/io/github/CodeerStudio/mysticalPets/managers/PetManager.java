package io.github.CodeerStudio.mysticalPets.managers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

public class PetManager {

    public boolean summonPet(Player player, String petName) {

        Location playerLocation = player.getLocation();

        // Calculate the location to the left of the player
        Vector direction = playerLocation.getDirection();
        Vector leftDirection = direction.clone().rotateAroundY(Math.toRadians(90)); // Rotate 90 degrees to the left
        Location spawnLocation = playerLocation.add(leftDirection.multiply(1.5)); // Offset 1.5 blocks to the left

        // Spawn an armor stand at the calculated location
        ArmorStand pet = (ArmorStand) player.getWorld().spawnEntity(spawnLocation, EntityType.ARMOR_STAND);

        // Configure the armor stand
        pet.setCustomName(petName + " pet");
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

        return true;
    }
}
