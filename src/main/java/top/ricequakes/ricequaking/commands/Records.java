package top.ricequakes.ricequaking.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import top.ricequakes.ricequaking.Gui.RecordsGui;
import top.ricequakes.ricequaking.Ricequaking;
import top.ricequakes.ricequaking.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Records implements CommandExecutor, TabExecutor {
    public final Ricequaking plugin;
    private FileConfiguration config;

    public Records(Ricequaking plugin) {
        this.plugin = plugin;
    }

    private void recordSave(String name, Player player) {
        Location playerLocation = player.getLocation();
        config = plugin.getConfig();
        List savedLocationNames = config.getList("savedLocationNames");
        if (savedLocationNames.contains(name)) {
            player.sendMessage(ChatColor.RED + "已存在同名记录点！");
            return;
        }
        savedLocationNames.add(name);
        Collections.sort(savedLocationNames);
        config.set("savedLocationNames", savedLocationNames);
        List list = new ArrayList();
        list.add((int) playerLocation.getX());
        list.add((int) playerLocation.getY());
        list.add((int) playerLocation.getZ());
        config.set(name, list);
        plugin.saveConfig();
        Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + "保存了新的记录点: " + name);
        new BukkitRunnable() {
            @Override
            public void run() {
                recordDisplayList(player);
            }
        }.runTaskLater(plugin, 10);
    }

    private void recordDelete(String name, Player player) {
        List savedLocationNames = config.getList("savedLocationNames");
        savedLocationNames.remove(savedLocationNames.indexOf(name));
        config.set("savedLocationNames", savedLocationNames);
        config.set(name, null);
        plugin.saveConfig();
        Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + "删除了记录点: " + name);
        new BukkitRunnable() {
            @Override
            public void run() {
                recordDisplayList(player);
            }
        }.runTaskLater(plugin, 10);
    }

    private void recordRename(String oldName, String newName, Player player) {
        List savedLocationNames = config.getList("savedLocationNames");
        savedLocationNames.remove(savedLocationNames.indexOf(oldName));
        savedLocationNames.add(newName);
        Collections.sort(savedLocationNames);
        config.set("savedLocationNames", savedLocationNames);
        config.set(newName, config.getList(oldName));
        config.set(oldName, null);
        plugin.saveConfig();
        Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + "将记录点: " + oldName + " 重命名为: " + newName);
    }

    private void recordDisplayList(Player player) {
        config = plugin.getConfig();
        config.set("savedLocationNames", getPerfectList());
        player.sendMessage(ChatColor.BLUE + "----------" + ChatColor.GOLD + "Records" + ChatColor.BLUE + "----------");
        List savedLocationNames = config.getList("savedLocationNames");
        for (Object name :
                savedLocationNames) {
            List list = config.getList((String) name);
            player.sendMessage(ChatColor.GOLD + (String) name + ": " + list.get(0) + " " +
                    list.get(1) + " " + list.get(2));
        }
        player.sendMessage(ChatColor.BLUE + "---------------------------");
        plugin.saveConfig();
    }

    private void recordTeleport(String name, Player player) {
        config = plugin.getConfig();
        List list = config.getList(name);
        double x = new Double(list.get(0).toString());
        double y = new Double(list.get(1).toString());
        double z = new Double(list.get(2).toString());
        if (name.contains("地狱：") || name.contains("地狱:")) {
            player.teleport(new Location(plugin.getServer().getWorld("world_nether"), x, y, z));
        } else if (name.contains("末地：") || name.contains("末地:")) {
            player.teleport(new Location(plugin.getServer().getWorld("world_the_end"), x, y, z));
        } else {
            player.teleport(new Location(plugin.getServer().getWorld("world"), x, y, z));
        }
        player.sendMessage(ChatColor.GOLD + "Teleported to: " + name);
    }

    private void recordAnnounce() {
        config = plugin.getConfig();
        Bukkit.broadcastMessage(ChatColor.BLUE + "----------" + ChatColor.GOLD + "Records" + ChatColor.BLUE + "----------");
        List savedLocationNames = config.getList("savedLocationNames");
        for (Object name :
                savedLocationNames) {
            List list = config.getList((String) name);
            Bukkit.broadcastMessage(ChatColor.GOLD + (String) name + ": " + list.get(0) + " " +
                    list.get(1) + " " + list.get(2));
        }
        Bukkit.broadcastMessage(ChatColor.BLUE + "---------------------------");
    }

    private void recordDirect(String name, Player player) {
        config = plugin.getConfig();
        List destList = config.getList(name);
        double x = new Double(destList.get(0).toString());
        double y = new Double(destList.get(1).toString());
        double z = new Double(destList.get(2).toString());
        Location destination = new Location(player.getWorld(), x, y, z);
        if (!Game.isDirecting.contains(player)) {
            Game.isDirecting.add(player);
            player.sendMessage(ChatColor.YELLOW + "directing..");
        } else {
            Game.isDirecting.remove(player);
            player.sendTitle("", "", 0, 1, 0);
            player.sendMessage(ChatColor.YELLOW + "stopped");
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Game.isDirecting.contains(player)) {
                    destination.setY(player.getLocation().getY());
                    int distance = (int) player.getLocation().distance(destination);
                    player.sendTitle(" ", ChatColor.GREEN + String.valueOf(distance), 0, 60, 0);
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 4);
    }

    private void recordTest(Player player) {
        config = plugin.getConfig();
        Bukkit.broadcastMessage(ChatColor.BLUE + "testing...");
        Bukkit.broadcastMessage(player.getWorld().getName());
    }

    private List<String> getPerfectList() {
        config = plugin.getConfig();
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("sl") && sender instanceof Player) {
            int x, y, z;
            Player player = (Player) sender;
            x = (int) player.getLocation().getX();
            y = (int) player.getLocation().getY();
            z = (int) player.getLocation().getZ();
            Bukkit.broadcastMessage(ChatColor.GOLD + sender.getName() + "的当前坐标为： " + x + " " + y + " " + z);
        } else if ((command.getName().equalsIgnoreCase("record" )
                || command.getName().equalsIgnoreCase("r")) && sender instanceof Player) {
            if (strings.length == 0) {
                recordDisplayList((Player) sender);
            } else if (strings.length == 2 && strings[0].equalsIgnoreCase("save")) {
                recordSave(strings[1], (Player) sender);
            } else if (strings.length == 2 && strings[0].equalsIgnoreCase("delete")) {
                recordDelete(strings[1], (Player) sender);
            } else if (strings.length == 3 && strings[0].equalsIgnoreCase("rename")) {
                recordRename(strings[1], strings[2], (Player) sender);
            } else if (strings.length == 2 && strings[0].equalsIgnoreCase("tp")) {
                recordTeleport(strings[1], (Player) sender);
            } else if (strings.length == 1 && strings[0].equalsIgnoreCase("announce")) {
                recordAnnounce();
            } else if (strings.length == 2 && strings[0].equalsIgnoreCase("direct")) {
                recordDirect(strings[1], (Player) sender);
            } else if (strings.length == 1 && strings[0].equalsIgnoreCase("test")) {
                recordTest((Player) sender);
            } else {
                sender.sendMessage(ChatColor.RED + "?");
            }
        }
        if (command.getName().equalsIgnoreCase("rm")) {
            RecordsGui.open((Player) sender);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> list1 = new ArrayList<>();
        if (strings.length == 1) {
            list1.add("save");
            list1.add("delete");
            list1.add("rename");
            list1.add("announce");
            list1.add("tp");
            list1.add("direct");
            Collections.sort(list1);
            return list1;
        } else if (strings.length == 2 && !strings[0].equalsIgnoreCase("save")) {
            config = plugin.getConfig();
            return config.getStringList("savedLocationNames");
        }
        return list1;
    }
}
