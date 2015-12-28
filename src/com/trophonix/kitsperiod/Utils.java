package com.trophonix.kitsperiod;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Utils {

	public static String secondsToTimestamp(int seconds) {
		int remainder, div, d = 0, h = 0, m = 0;
		div = 60 * 60 * 24;
		if (seconds >= div) {
			remainder = Integer.remainderUnsigned(seconds, div);
			d = seconds / div;
			seconds = remainder;
		}

		div = 60 * 60;
		if (seconds >= div) {
			remainder = Integer.remainderUnsigned(seconds, div);
			h = seconds / div;
			seconds = remainder;
		}

		div = 60;
		if (seconds >= div) {
			remainder = Integer.remainderUnsigned(seconds, div);
			m = seconds / div;
			seconds = remainder;
		}

		String output = "";
		if (d > 0)
			output = d + "d";
		if (h > 0)
			output += h + "h";
		if (m > 0)
			output += m + "m";
		if (seconds > 0)
			output += seconds + "s";

		return output;
	}

	public static int timestampToSeconds(String timestamp) {
		String[] split = timestamp.split("");
		int d = 0, h = 0, m = 0, s = 0;

		for (int i = 0; i < split.length; i++) {
			if (split[i].equalsIgnoreCase("d")) {
				int length = 0;
				for (int j = 1; j < 100; j++) {
					if ((i - j < 0) || Character.isLetter(split[i-j].charAt(0)))
						break;
					length++;
				}
				if (length == 0) continue;
				String string = "";
				for (int j = i - length; j < i; j++) {
					string = string + split[j];
				}
				
				d = Integer.parseInt(string);
			} else if (split[i].equalsIgnoreCase("h")) {
				int length = 0;
				for (int j = 1; j < 100; j++) {
					if ((i - j < 0) || Character.isLetter(split[i-j].charAt(0)))
						break;
					length++;
				}
				if (length == 0) continue;
				String string = "";
				for (int j = i - length; j < i; j++) {
					string = string + split[j];
				}
				
				h = Integer.parseInt(string);
			} else if (split[i].equalsIgnoreCase("m")) {
				int length = 0;
				for (int j = 1; j < 100; j++) {
					if ((i - j < 0) || Character.isLetter(split[i-j].charAt(0)))
						break;
					length++;
				}
				if (length == 0) continue;
				String string = "";
				for (int j = i - length; j < i; j++) {
					string = string + split[j];
				}

				m = Integer.parseInt(string);
			} else if (split[i].equalsIgnoreCase("s")) {
				int length = 0;
				for (int j = 1; j < 100; j++) {
					if ((i - j < 0) || Character.isLetter(split[i-j].charAt(0)))
						break;
					length++;
				}
				if (length == 0) continue;
				String string = "";
				for (int j = i - length; j < i; j++) {
					string = string + split[j];
				}

				s = Integer.parseInt(string);
			}

		}

		return (d * 24 * 60 * 60) + (h * 60 * 60) + (m * 60) + s;
	}

	public static FileConfiguration getPlayerConfig(Player player) {
		File file = new File("plugins" + File.separator + "kitsperiod" + File.separator + "playerdata" + File.separator
				+ player.getUniqueId() + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		if (!file.exists()) {
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return config;
	}

	public static void savePlayerConfig(FileConfiguration config, Player player) {
		File file = new File("plugins" + File.separator + "kitsperiod" + File.separator + "playerdata" + File.separator
				+ player.getUniqueId() + ".yml");
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
