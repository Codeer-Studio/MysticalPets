# MysticalPets

**MysticalPets** is a Minecraft plugin that allows players to spawn and have pets follow them around
https://www.spigotmc.org/resources/coinfilp.121138/

---

## 🚀 Features
- **Player Owned Pets**: Players can own a number of pets
- **Easy Pet Creation**: Allows server owners to easily create pets

---

## Commands

- Use `/pet list <amount> <player>` opens the pet gui menu
- Use `/pet summon <player> <petId>` Spawns the pet
- Use `/pet dismiss <player> <petId>` Despawns the pet
- Use `/pet add <player> <petId>` To add a pet to a player
- Use `/pet remove <player> <petId>` To remove a pet from the player
- Use `/pet reload` To reload the pets.yml

---

## Permissions
- `mysticalpets.admin.add` to use the `/pet add` command
- `mysticalpets.admin.remove` to use the `/pet remove` command
- `mysticalpets.admin.reload` to use the `/pet reload` command

## 🛠️ Installation

To install **MysticalPets** on your Minecraft server, follow these simple steps:

1. **Download the Plugin**:
   - Go to the [releases section](https://github.com/Codeer-Studio/MysticalPets/releases) of the repository.
   - Download the latest `.jar` file.
  
2. **Place in plugins folder**:
   - Place the `MysticalPets.jar` file into your server's plugins folder

3. **Restart Your Server**:
   - Restart your Minecraft server to load the plugin.
   - Upon startup, the plugin will automatically generate a configuration file in the `plugins/MysticalPets/` directory (if needed).

4. **Verify Installation** (optional):
   - Run `/pet list` in-game to see if the plugin responds.
---

## Pet Creation
To create pets is very simple follow the format given in pets.yml

Test is the id for the pet, every pet needs a unique id.
name: is just the name of the pet in game
head: is the link to the head texture that will be displayed.

To get and find new head textures use this website: https://minecraft-heads.com
Once you have found a head texture you'd like go to the bottom till you find the developers area. Then just copy the Minecraft URL
![image](https://github.com/user-attachments/assets/3771acb2-c496-4c32-9a14-2af731e1635a)


---

 
