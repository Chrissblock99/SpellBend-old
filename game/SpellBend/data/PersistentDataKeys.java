package game.SpellBend.data;

import game.SpellBend.PluginMain;
import org.bukkit.NamespacedKey;

public class PersistentDataKeys {
    private final static PluginMain plugin = PluginMain.getInstance();

    public static final NamespacedKey spellNameKey = new NamespacedKey(plugin, "spellName"); //<- DO NOT CHANGE
    public static final NamespacedKey spellTypeKey = new NamespacedKey(plugin, "spellType"); //<- DO NOT CHANGE

    public final static NamespacedKey gemsKey = new NamespacedKey(plugin, "gems");
    public final static NamespacedKey goldKey = new NamespacedKey(plugin, "gold");
    public final static NamespacedKey spellsOwnedKey = new NamespacedKey(plugin, "spellsOwned");
    public static final NamespacedKey coolDownsKey = new NamespacedKey(plugin, "coolDowns");
    public final static NamespacedKey dmgModsKey = new NamespacedKey(plugin, "dmgMods");
    public final static NamespacedKey ranksKey = new NamespacedKey(plugin, "ranks");
    public final static NamespacedKey badgesKey = new NamespacedKey(plugin, "badges");
    public final static NamespacedKey nickKey = new NamespacedKey(plugin, "nick");
    public static final NamespacedKey suffixKey = new NamespacedKey(plugin, "suffix");
    public final static NamespacedKey crystalsKey = new NamespacedKey(plugin, "crystals");
    public final static NamespacedKey crystalShardsKey = new NamespacedKey(plugin, "crystalShards");

    public static final NamespacedKey itemActionKey = new NamespacedKey(plugin, "itemAction");
}
