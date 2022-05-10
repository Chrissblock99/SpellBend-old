package game.spellbend.playerdata;

import game.spellbend.moderation.Punishment;
import game.spellbend.organize.CoolDownEntry;
import game.spellbend.data.Enums;
import game.spellbend.organize.DamageEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PersistentPlayerSessionStorage {
    public static HashMap<UUID, Integer> gems = new HashMap<>();
    public static HashMap<UUID, Integer> gold = new HashMap<>();
    public static HashMap<UUID, int[]> spellsOwned = new HashMap<>();
    public static HashMap<UUID, ArrayList<DamageEntry>> health = new HashMap<>();
    public static HashMap<UUID, HashMap<Enums.SpellType, CoolDownEntry>> coolDowns = new HashMap<>();
    public static HashMap<UUID, Float[]> dmgMods = new HashMap<>();
    public static HashMap<UUID, ArrayList<Punishment>> punishments = new HashMap<>();
    public static HashMap<UUID, ArrayList<String>> ranks = new HashMap<>();
    public static HashMap<UUID, ArrayList<String>> badges = new HashMap<>();
    public static HashMap<UUID, String> nick = new HashMap<>();
    public static HashMap<UUID, String> suffix = new HashMap<>();
    public static HashMap<UUID, Integer> crystals = new HashMap<>();
    public static HashMap<UUID, Integer> crystalShards = new HashMap<>();
}
