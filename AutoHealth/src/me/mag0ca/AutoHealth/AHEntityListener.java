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

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class AHEntityListener extends EntityListener{

	private final AutoHealth plugin;
	
	public AHEntityListener(final AutoHealth plugin) {
		this.plugin = plugin;
	}

	
	public void onFoodLevelChange(FoodLevelChangeEvent Event)
	{
		
		if (Event.getEntity() instanceof Player)
		{
			Player player = (Player) Event.getEntity();
			//plugin.log.info("player = " + player.getName());
			if (plugin.cancelFood(player))
			{
				Event.setFoodLevel(20);
			}
			
		}
	}
	

}
