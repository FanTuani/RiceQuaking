package top.ricequakes.ricequaking;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.*;

public class StaticValues {
    public static HashMap<String, Location> ghostModeBackLocations = new HashMap<>();
    public static HashMap<Player, Villager> magicControllingPair = new HashMap<>();
    public static Vector<Player> isDirecting = new Vector<>();

    public static List<String> getPerfectList() {
        FileConfiguration config = Ricequaking.instance.getConfig();
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();
        List<String> mergedList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            for (String name : config.getStringList("savedLocationNames")) {
                if (i == 2 && (name.contains("地狱：") || name.contains("地狱:"))) {
                    list2.add(name);
                } else if (i == 3 && (name.contains("末地：") || name.contains("末地:"))) {
                    list3.add(name);
                } else if (i == 1 && !(name.contains("地狱：") || name.contains("地狱:") || name.contains("末地：") || name.contains("末地:"))) {
                    list1.add(name);
                }
            }
        }
        Collections.sort(list1);
        Collections.sort(list2);
        Collections.sort(list3);
        for (String name : list1) mergedList.add(name);
        for (String name : list2) mergedList.add(name);
        for (String name : list3) mergedList.add(name);
        return mergedList;
    }
}
