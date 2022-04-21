package game.spellbend;

import game.spellbend.commands.*;
import game.spellbend.moderation.*;
import game.spellbend.spell.SpellHandler;
import game.spellbend.util.EventUtil;
import game.spellbend.util.PlayerDataBoard;
import game.spellbend.playerdata.PlayerDataUtil;
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
            PlayerDataUtil.loadAll(player);
            SpellHandler.registerPlayer(player);
        }

        new Shop();
        new Spawn();
        new Random();
        PlayerDataBoard.start();
        new Rank();
        new Badge();
        new Nick();
        new Test();
        new Testing();

        EventUtil.registerAllEvents();

        getLogger().info("SpellBend enabled");
    }

    @Override
    public void onDisable() {
        //save data of all players online
        for (Player player : Bukkit.getOnlinePlayers()) {
            SpellHandler.deRegisterPlayer(player);
            PlayerDataUtil.saveAll(player);
        }

        getLogger().warning("SpellBend does not support SpellState saving across reloads and restarts!");
        getLogger().info("SpellBend disabled");
    }

    public static PluginMain getInstance() {
        return instance;
    }
}
