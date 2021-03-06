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

import java.util.TimerTask;

public class SimpleTimer extends TimerTask {
	private AutoHealth m_Plugin;
	private long time = 0;

	SimpleTimer(AutoHealth plugin) {
		m_Plugin = plugin;
	}

	@Override
	public void run() {
		time++;
		m_Plugin.handleHealth(time);
		if(time>100000000)
			time=0;
	}

}
