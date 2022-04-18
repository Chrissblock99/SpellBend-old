package game.SpellBend.commands;

import game.SpellBend.PluginMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class spawn {
    PluginMain plugin = PluginMain.getInstance();

    public spawn() {                                                                         //this doesnt work lol
        FileConfiguration config = plugin.getConfig();

        HashMap<String, Location> spawns = new HashMap<String, Location>();
        spawns.put("world", new Location(Bukkit.getWorld("world"), config.getDouble("spawns.world.x"), config.getInt("spawns.world.y"), config.getInt("spawns.world.z"), (float) config.getDouble("spawns.world.Yaw"), (float) config.getDouble("spawns.world.Pitch")));

        new CommandBase("setspawn",0, 6, true) {
            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                Player player = (Player) sender;
                Location location = player.getLocation();

                switch(arguments.length) {
                    case(3): location = new Location(location.getWorld(), Double.parseDouble(arguments[0]), Double.parseDouble(arguments[1]), Double.parseDouble(arguments[2]), 0, 0);break;
                    case(4): location = new Location(location.getWorld(), Double.parseDouble(arguments[0]), Double.parseDouble(arguments[1]), Double.parseDouble(arguments[2]), Float.parseFloat(arguments[3]), 0); break;
                    case(5): location = new Location(location.getWorld(), Double.parseDouble(arguments[0]), Double.parseDouble(arguments[1]), Double.parseDouble(arguments[2]), Float.parseFloat(arguments[3]), Float.parseFloat(arguments[4])); break;
                    case(6): location = new Location(Bukkit.getWorld(arguments[5]), Double.parseDouble(arguments[0]), Double.parseDouble(arguments[1]), Double.parseDouble(arguments[2]), Float.parseFloat(arguments[3]), Float.parseFloat(arguments[4])); break;
                }
                config.set("spawn." + location.getWorld().getName() + ".x", location.getBlockX());
                config.set("spawn." + location.getWorld().getName() + ".y", location.getBlockY());
                config.set("spawn." + location.getWorld().getName() + ".z", location.getBlockZ());
                config.set("spawn." + location.getWorld().getName() + ".yaw", location.getYaw());
                config.set("spawn." + location.getWorld().getName() + ".pitch", location.getPitch());
                plugin.saveConfig();
                spawns.put(location.getWorld().getName(), location);

                player.sendMessage("§aSet new spawn!");
                return true;
            }

            @Override
            public @NotNull String getUsage() {
                return "/setspawn [x] [y] [z] [yaw] [pitch] [world]";
            }
        }.setPermission("SpellBend.spawn.set");

        new CommandBase("spawn", true) {
            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                Player player = (Player) sender;
                player.teleport(spawns.get(player.getWorld().getName()));
                sender.sendMessage("&b&lWhoosh!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1, 1);    //volume, pitch
                return true;
            }

            @Override
            public @NotNull String getUsage() {
                return "Please use \"/spawn\" without any extra arguments.";
            }
        }.enableDelay(30);
    }
}
