/*
 * Copyright (C) 2011 <mag0ca@yahoo.ca>
 *
 * This file is part of the Bukkit plugin AutoHealth based off of SlowHealth by <adam@truncale.net>.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307, USA.
 */

package me.mag0ca.AutoHealth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.io.FileInputStream;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
//import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

//import com.nijiko.permissions.PermissionHandler;
//import com.nijikokun.bukkit.Permissions.Permissions;

public class AutoHealth extends JavaPlugin {
	private File m_Folder = new File("plugins" + File.separator + "AutoHealth");
	private Timer m_Timer = new Timer(true);
	private HashMap<String, AHWorld> worldProp = new HashMap<String, AHWorld>();
	private HashMap<String, AHWorld> worldList = new HashMap<String, AHWorld>();
	private final AHPlayerListener playerListener = new AHPlayerListener(this); // Listen to players
//	public static PermissionHandler Permissions;
//	public boolean installedPermissions = false;
	Logger log = Logger.getLogger("Minecraft");
	
	public void onEnable() {

		PluginDescriptionFile pdfFile = this.getDescription();
		log.info("[" + pdfFile.getName() + "] version [" + pdfFile.getVersion() + "] is loaded.");

		// create files if not found: Folder, default.properties
		createFiles();
		loadDefaultProps();
		loadWorldList();
//		setupPermissions();

		for (Entry<String, AHWorld> entry : worldList.entrySet()) {
			loadWorldProps(entry.getKey());
			// System.out.println("Found server " + entry.getKey());
		}

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_BED_LEAVE, playerListener, Priority.Normal, this);

		m_Timer.schedule(new SimpleTimer(this), 0, (long) (1000));
	}

	private void loadWorldList() {

		try {
			BufferedReader reader = new BufferedReader(new FileReader((m_Folder.getAbsolutePath() + File.separator + "WORLDLIST.txt")));
			String line = reader.readLine();
			while (line != null) {
				String[] values = line.split(";");
				for (int x = 0; x < values.length; x++) {
					worldList.put(values[x], new AHWorld(values[x], 0, 0, 0, 0, 0, 0));
				}

				line = reader.readLine();
			}

		} catch (Exception ex) {}

	}

	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info("[" + pdfFile.getName() + "] version [" + pdfFile.getVersion() + "] is Disabled.");

		m_Timer.cancel();
	}

	public void handleHealth(long time) {	
		for (Player player : getServer().getOnlinePlayers()) {
				if (player.hasPermission("autohealth")) {
					healPlayer(player, time);
				}
		}
	}

	public void healPlayer(Player player, long time) {

		// System.out.println("[AutoHealth] heals " + player.getDisplayName());
		String PWorld = player.getLocation().getWorld().getName();
		int rate;
		int healAmount;
		int maxHeal;
		int minHeal;
		int altitude;
		int health;
		
		if (worldProp.containsKey(PWorld)) {
			rate = worldProp.get(PWorld).rate;
			healAmount = worldProp.get(PWorld).amount;
			maxHeal = worldProp.get(PWorld).maxHeal;
			minHeal = worldProp.get(PWorld).minHeal;
			altitude = worldProp.get(PWorld).altitude;
		} else {
			rate = worldProp.get("default").rate;
			healAmount = worldProp.get("default").amount;
			maxHeal = worldProp.get("default").maxHeal;
			minHeal = worldProp.get("default").minHeal;
			altitude = worldProp.get("default").altitude;
		}

		if (rate != 0) {
			if (time % rate == 0) {
				int HP = player.getHealth();
				if (player.getLocation().getBlockY() >= altitude) {
					if (HP < maxHeal && HP > minHeal && healAmount > 0) {
						health = HP + healAmount;
						if (health > 20)
							player.setHealth(20);
						else
							player.setHealth(health);
					} 
				}
			}
		}
	}

	public void healPlayerInBed(Player player) {
		
		String PWorld = player.getLocation().getWorld().getName();
		int healAmount;
		int maxHeal;
		int health;
		
		if (worldProp.containsKey(PWorld)) {
			healAmount = worldProp.get(PWorld).bedHeal;
			maxHeal = worldProp.get(PWorld).maxHeal;
		} else {
			healAmount = worldProp.get("default").bedHeal;
			maxHeal = worldProp.get("default").maxHeal;
		}

		int HP = player.getHealth();

		if (HP < maxHeal && HP > 0 && healAmount > 0){
			health = HP + healAmount;
			if (health > 20)
				player.setHealth(20);
			else
				player.setHealth(health);
		}
		
		player.sendMessage(ChatColor.GOLD + "You awake feeling refreshed!");
	}

	public void createFiles() {
		if (!m_Folder.exists()) {
			log.info("[AutoHealth] Config folder missing, creating...");
			m_Folder.mkdir();
		}

		File worldList = new File(m_Folder.getAbsolutePath() + File.separator + "WORLDLIST.txt");
		if (!worldList.exists()) {
			// System.out.print("AutoHealth: default.properties is missing, creating...");
			try {
				worldList.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter((m_Folder.getAbsolutePath() + File.separator + "WORLDLIST.txt")));
				writer.flush();
				writer.write(getServer().getWorlds().get(0).getName());
				writer.newLine();
				writer.close();

			} catch (IOException ex) {
				log.info("[AutoHealth] failed to load WORLDLIST.txt");
			}
		}

		File defaultConfig = new File(m_Folder.getAbsolutePath() + File.separator + "default.properties");
		if (!defaultConfig.exists()) {
			log.info("[AutoHealth] default.properties is missing, creating...");
			try {
				defaultConfig.createNewFile();

				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter((m_Folder.getAbsolutePath() + File.separator + "default.properties")));
					writer.flush();

					writer.write("regen-rate" + ":" + "1");
					writer.newLine();
					writer.write("regen-amount" + ":" + "1");
					writer.newLine();
					writer.write("regen-max" + ":" + "20");
					writer.newLine();
					writer.write("regen-min" + ":" + "0");
					writer.newLine();
					writer.write("regen-altitude" + ":" + "0");
					writer.newLine();
					writer.write("sleep-heal" + ":" + "20");
					writer.newLine();

					writer.close();

				} catch (Exception ex) {
					System.out.println("failed to write");
				}

			} catch (IOException ex) {
				System.out.println("failed to load default.properties");
			}
		}

	}

	public void loadDefaultProps() {
		Properties props = new Properties();
		int rate = 1;
		int healAmount = 1;
		int maxHeal = 20;
		int minHeal = 0;
		int altitude = 0;
		int bedHeal = 20;
		try {
			props.load(new FileInputStream(m_Folder.getAbsolutePath() + File.separator + "default.properties"));
			if (props.containsKey("regen-rate"))
				rate = Integer.parseInt(props.getProperty("regen-rate"));
			if (props.containsKey("regen-amount"))
				healAmount = Integer.parseInt(props.getProperty("regen-amount"));
			if (props.containsKey("regen-max"))
				maxHeal = Integer.parseInt(props.getProperty("regen-max"));
			if (props.containsKey("regen-min"))
				minHeal = Integer.parseInt(props.getProperty("regen-min"));
			if (props.containsKey("regen-min"))
				altitude = Integer.parseInt(props.getProperty("regen-altitude"));
			if (props.containsKey("sleep-heal"))
				bedHeal = Integer.parseInt(props.getProperty("sleep-heal"));

		} catch (IOException ioe) {
			log.info("[AutoHealth] Default has no property file. Please create one.");
		}

		worldProp.put("default", new AHWorld("default", rate, healAmount, maxHeal, minHeal, altitude, bedHeal));
	}

	public void loadWorldProps(String worldName) {

		Properties props = new Properties();

		int rate = worldProp.get("default").rate;
		int healAmount = worldProp.get("default").amount;
		int maxHeal = worldProp.get("default").maxHeal;
		int minHeal = worldProp.get("default").minHeal;
		int altitude = worldProp.get("default").altitude;
		int bedHeal = worldProp.get("default").bedHeal;

		try {
			props.load(new FileInputStream(m_Folder.getAbsolutePath() + File.separator + worldName + ".properties"));
			if (props.containsKey("regen-rate"))
				rate = Integer.parseInt(props.getProperty("regen-rate"));
			if (props.containsKey("regen-amount"))
				healAmount = Integer.parseInt(props.getProperty("regen-amount"));
			if (props.containsKey("regen-max"))
				maxHeal = Integer.parseInt(props.getProperty("regen-max"));
			if (props.containsKey("regen-min"))
				minHeal = Integer.parseInt(props.getProperty("regen-min"));
			if (props.containsKey("regen-min"))
				altitude = Integer.parseInt(props.getProperty("regen-altitude"));
			if (props.containsKey("sleep-heal"))
				bedHeal = Integer.parseInt(props.getProperty("sleep-heal"));

		} catch (IOException ioe) {
			log.info("[AutoHealth] " + worldName + " has no property file, using default values.");
			File config = new File(m_Folder.getAbsolutePath() + File.separator + worldName + ".properties");

			try {
				config.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter((m_Folder.getAbsolutePath() + File.separator + worldName + ".properties")));
				writer.flush();

				writer.write("regen-rate" + ":" + "1");
				writer.newLine();
				writer.write("regen-amount" + ":" + "1");
				writer.newLine();
				writer.write("regen-max" + ":" + "20");
				writer.newLine();
				writer.write("regen-min" + ":" + "0");
				writer.newLine();
				writer.write("regen-altitude" + ":" + "0");
				writer.newLine();
				writer.write("sleep-heal" + ":" + "20");
				writer.newLine();

				writer.close();

			} catch (Exception ex) {
				System.out.println("failed to write");
			}
		}

		worldProp.put(worldName, new AHWorld(worldName, rate, healAmount, maxHeal, minHeal, altitude, bedHeal));

	}

}
