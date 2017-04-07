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
package com.watabou.pixeldungeon.utils;

import java.util.Locale;

import net.whitegem.pixeldungeon.LanguageFactory;


public class Utils {

	public static String capitalize( String str ) {
		return Character.toUpperCase( str.charAt( 0 ) ) + str.substring( 1 );
	}

	public static String format( String format, Object...args ) {
		String result = String.format( Locale.ENGLISH, format, args );
		LanguageFactory.INSTANCE.addFormatTranslation(result, format, args);
		return result;
	}

	public static String VOWELS	= "aoeiu";

	// 中文版不需要處理單複數
	public static String indefinite( String noun ) {
//		if (noun.length() == 0) {
//			return format("a%s", "");
//		} else {
//			String prefix = VOWELS.indexOf( Character.toLowerCase( noun.charAt( 0 ) ) ) != -1 ? "an " : "a ";
//			return format(prefix + "%s", noun);
//		}
		return noun;
	}
}
