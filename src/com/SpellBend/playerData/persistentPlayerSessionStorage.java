package com.SpellBend.playerData;

import com.SpellBend.organize.CoolDownEntry;
import com.SpellBend.data.Enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class persistentPlayerSessionStorage {
    public static HashMap<UUID, Integer> gems = new HashMap<>();
    public static HashMap<UUID, Integer> gold = new HashMap<>();
    public static HashMap<UUID, int[]> spellsOwned = new HashMap<>();
    public static HashMap<UUID, Double> health = new HashMap<>();
    public static HashMap<UUID, HashMap<Enums.SpellType, CoolDownEntry>> coolDowns = new HashMap<>();
    public static HashMap<UUID, Float[]> dmgMods = new HashMap<>();
    public static HashMap<UUID, ArrayList<String>> ranks = new HashMap<>();
    public static HashMap<UUID, ArrayList<String>> badges = new HashMap<>();
    public static HashMap<UUID, String> nick = new HashMap<>();
    public static HashMap<UUID, String> suffix = new HashMap<>();
    public static HashMap<UUID, Integer> crystals = new HashMap<>();
    public static HashMap<UUID, Integer> crystalShards = new HashMap<>();
}
