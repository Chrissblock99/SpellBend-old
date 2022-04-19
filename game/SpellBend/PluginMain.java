package game.SpellBend;

import game.SpellBend.commands.*;
import game.SpellBend.moderation.*;
import game.SpellBend.spell.SpellHandler;
import game.SpellBend.util.EventUtil;
import game.SpellBend.util.playerDataBoard;
import game.SpellBend.playerData.playerDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginMain extends JavaPlugin {
    private static PluginMain instance;

    @Override
    public void onEnable() {
        instance = this;
        ConfigurationSerialization.registerClass(Punishment.class);
        ConfigurationSerialization.registerClass(Warn.class);
        ConfigurationSerialization.registerClass(Mute.class);
        ConfigurationSerialization.registerClass(Ban.class);
        ConfigurationSerialization.registerClass(HoldMsgs.class);
        saveDefaultConfig();

        //load data of all players online
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerDataUtil.loadAll(player);
            SpellHandler.registerPlayer(player);
        }

        new shop();
        new spawn();
        new random();
        playerDataBoard.start();
        new rank();
        new badge();
        new nick();
        new test();
        new testing();

        EventUtil.registerAllEvents();

        getLogger().info("SpellBend enabled");
    }

    @Override
    public void onDisable() {
        //save data of all players online
        for (Player player : Bukkit.getOnlinePlayers()) {
            SpellHandler.deRegisterPlayer(player);
            playerDataUtil.saveAll(player);
        }

        getLogger().warning("SpellBend does not support SpellState saving across reloads and restarts!");
        getLogger().info("SpellBend disabled");
    }

    public static PluginMain getInstance() {
        return instance;
    }
}
