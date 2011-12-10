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

public class AHWorld {
	
	public String name;
	public int rate;
	public int amount;
	public int maxHeal;
	public int minHeal;
	public int altitude;
	public int bedHeal;
	
	public AHWorld(String name, int Xrate, int Xamount, int XmaxHeal, int XminHeal, int Xaltitude, int XbedHeal ) {
		rate = Xrate;
		amount = Xamount;
		maxHeal = XmaxHeal;
		minHeal = XminHeal;
		altitude = Xaltitude;
		bedHeal = XbedHeal;
	}
	
	public void set(String name, int Xrate, int Xamount, int XmaxHeal, int XminHeal, int Xaltitude, int XbedHeal ) {
		rate = Xrate;
		amount = Xamount;
		maxHeal = XmaxHeal;
		minHeal = XminHeal;
		altitude = Xaltitude;
		bedHeal = XbedHeal;
	}
	
	

}
