package top.ricequakes.ricequaking;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.HashMap;
import java.util.Vector;

public class StaticValues {
    public static HashMap<String, Location> ghostModeBackLocations = new HashMap<>();
    public static HashMap<Player, Villager> magicControllingPair = new HashMap<>();
    public static Vector<Player> isDirecting = new Vector<>();
}
