package io.github.CodeerStudio.mysticalPets;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MysticalPets extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("MysticalPets Enabled");

    }

    @Override
    public void onDisable() {
        getLogger().info("MysticalPets Disabled");
    }
}
