/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.watabou.pixeldungeon.items.weapon.melee;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Random;

import net.whitegem.pixeldungeon.LanguageFactory;

public class MeleeWeapon extends Weapon {
	
	private int tier;
	
	public MeleeWeapon( int tier, float acu, float dly ) {
		super();
		
		this.tier = tier;
		
		ACU = acu;
		DLY = dly;
		
		STR = typicalSTR();
	}
	
	protected int min0() {
		return tier;
	}
	
	protected int max0() {
		return (int)((tier * tier - tier + 10) / ACU * DLY);
	}
	
	@Override
	public int min() {
		return isBroken() ? min0() : min0() + level(); 
	}
	
	@Override
	public int max() {
		return isBroken() ? max0() : max0() + level() * tier;
	}
	
	@Override
	final public Item upgrade() {
		return upgrade( false );
	}
	
	public Item upgrade( boolean enchant ) {
		STR--;		
		return super.upgrade( enchant );
	}
	
	public Item safeUpgrade() {
		return upgrade( enchantment != null );
	}
	
	@Override
	public Item degrade() {		
		STR++;
		return super.degrade();
	}
	
	public int typicalSTR() {
		return 8 + tier * 2;
	}
	
	@Override
	public String info() {
		
		final String p = "\n\n";

		StringBuilder info = new StringBuilder( LanguageFactory.getTranslation(desc()) );
		
		int lvl = visiblyUpgraded();
		String quality = lvl != 0 ? 
			(lvl > 0 ? 
				(isBroken() ? "broken" : "upgraded") : 
				"degraded") : 
			"";
		info.append( p );
		info.append( LanguageFactory.getTranslation(Utils.format("This %s is %s tier-%d melee weapon. ", name, Utils.indefinite( quality ), tier)) );
		
		if (levelKnown) {
			int min = min();
			int max = max();
			info.append( LanguageFactory.getTranslation(Utils.format("Its average damage is %d points per hit. ", (min + (max - min) / 2))));
		} else {
			int min = min0();
			int max = max0();
			info.append(
					LanguageFactory.getTranslation(Utils.format("Its typical average damage is %d points per hit " +
							"and usually it requires %d points of strength. ", (min + (max - min) / 2), typicalSTR())));
			if (typicalSTR() > Dungeon.hero.STR()) {
				info.append( LanguageFactory.getTranslation("Probably this weapon is too heavy for you. ") );
			}
		}


		if (DLY != 1f) {
			if (ACU != 1f) {
				if (DLY < 1f)
				{
					if (ACU > 1f)
					{
						info.append(LanguageFactory.getTranslation("This is a rather fast and accurate weapon. "));
					}
					else
					{
						info.append(LanguageFactory.getTranslation("This is a rather fast but inaccurate weapon. "));
					}
				}
				else
				{
					if (ACU > 1f)
					{
						info.append(LanguageFactory.getTranslation("This is a rather slow but accurate weapon. "));
					}
					else
					{
						info.append(LanguageFactory.getTranslation("This is a rather slow and inaccurate weapon. "));
					}
				}
			}
			else
			{
				if (DLY < 1f)
				{
					info.append(LanguageFactory.getTranslation("This is a rather fast weapon. "));
				}
				else
				{
					info.append(LanguageFactory.getTranslation("This is a rather slow weapon. "));
				}
			}
		} else if (ACU != 1f) {
			info.append( LanguageFactory.getTranslation("This is a rather " + (ACU > 1f ? "accurate" : "inaccurate") + " weapon. ") );
		}
		switch (imbue) {
			case SPEED:
				info.append( LanguageFactory.getTranslation("It was balanced to make it faster. ") );
				break;
			case ACCURACY:
				info.append( LanguageFactory.getTranslation("It was balanced to make it more accurate. ") );
				break;
			case NONE:
		}

		if (enchantment != null) {
			info.append( LanguageFactory.getTranslation("It is enchanted.") );
		}

		if (levelKnown && Dungeon.hero.belongings.backpack.items.contains( this )) {
			if (STR > Dungeon.hero.STR()) {
				info.append( p );
				info.append(
						LanguageFactory.getTranslation(Utils.format("Because of your inadequate strength the accuracy and speed " +
								"of your attack with this %s is decreased.", name)));
			}
			if (STR < Dungeon.hero.STR()) {
				info.append( p );
				info.append(
						LanguageFactory.getTranslation(Utils.format("Because of your excess strength the damage " +
								"of your attack with this %s is increased.", name)));
			}
		}

		if (isEquipped( Dungeon.hero )) {
			info.append( p );
			if (cursed)
			{
				info.append( LanguageFactory.getTranslation(Utils.format("You hold the %s at the ready, and because it is cursed, you are powerless to let go.", name)));
			}
			else
			{
				info.append( LanguageFactory.getTranslation(Utils.format("You hold the %s at the ready.", name)));
			}
		} else {
			if (cursedKnown && cursed) {
				info.append( p );
				info.append( LanguageFactory.getTranslation(Utils.format("You can feel a malevolent magic lurking within %s.", name)));
			}
		}

		return info.toString();
	}
	
	@Override
	public int price() {
		int price = 20 * (1 << (tier - 1));
		if (enchantment != null) {
			price *= 1.5;
		}
		return considerState( price );
	}
	
	@Override
	public Item random() {
		super.random();
		
		if (Random.Int( 10 + level() ) == 0) {
			enchant();
		}
		
		return this;
	}
}
