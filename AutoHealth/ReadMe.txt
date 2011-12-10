Auto Health Healing plugin for Bukkit Minecraft server MOD

Based on Slow Health 3.2 by ACTruncale
http://forums.bukkit.org/threads/mech-slowhealth-v3-2-auto-heal-hurt-multi-world-permissions-1060.1256/
 
Description:
- Adds health (configurable) every second (configurable) to players on the server.
- Can hurt player if you make regen-amount negative
- Can set maximum amount AutoHealth will heal the player by
- Can set minimum amount AutoHealth will stop hurting the player by (if you set regen-amount to less than 0
- Can set the starting altitude that auto health with start working
- Multi-world support
- Permissions support

Instructions:
- Save AutoHealth.jar to plug-in folder
- Start and stop server to allow Auto Health to create the needed files.
- Inside your plug-in folder there should be a AutoHealth Folder with three files
- default.properties
- world.properties (or whatever your default world is in your server.properties file)
- WORLDLIST.txt
- If you only have a single world just change the settings in your worlds property file.

Adding Worlds to Auto Health:
- For every world you have add the world name in the WORLDLIST.txt file. One world per line.
- Run the server and let it create the properties file, then edit it to your liking.
- Any world not on the WORLDLIST.txt will use the setting in the default.properties file.

Customize Auto Health:
All values must be in integer form (whole numbers)
- regen-rate:1
- This value is how fast AutoHealth will heal you in second.
- regen-amount:1
- This is how many 1/2 hearts AutoHealth will heal you for.
- Set this to a negative number to hurt the player 
- regen-max:20
- Controls at what value AutoHealth will stop healing.
- regen-min:0
- This is used if you are hurting the player instead of healing them.
- Controls when your stop hurting them. 0 will lead to death.
- regen-altitude:0
- If a player goes below this depth, the will no longer be healed.
- Used if you want to make underground harder.
- sleep-heal:20
- When players sleep through the night they get healed this amount.

Permissions:
If using permissions use the node 'autohealth' for everyone you want to be affected by this mod. Permissions is not needed to use this mod, Auto Health will default to everyone getting healed if not installed.

ToDo:
- Update Permissions to use BukkitPerms
- Add Food regen/no decrease

Credits:
- Code modified from Slow Health by ACTruncale  