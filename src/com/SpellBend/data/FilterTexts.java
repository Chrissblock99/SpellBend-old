package com.SpellBend.data;

import com.SpellBend.organize.AllowedWord;
//import com.SpellBend.util.TextUtil;
//import com.SpellBend.util.math.NumArrayBase;
//import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Map;

public class FilterTexts {
    public static final ArrayList<HashMap<String, String>> aliases = createAliasesList()/*dealiasAliasesList(createAliasedAliasesList())*/;
    //public static final HashMap<String, ArrayList<String>> aliasesAliases = createAliasesAliasesList();
    public static final HashMap<String, ArrayList<AllowedWord>> filtered = createFilteredMap();
    @SuppressWarnings("SpellCheckingInspection")
    public static final String[] funSentence = {"Pump it up DJ!", "I love sitting by the lake on Tuesdays.", "You're good at this!", "Big, big, chungus...", "I'm a big fan!",
            "I've always despised pineapple Pizza...", "I should be in class...", "This milk tastes horrible.", "Sheesh!", "Me and your MOM!", "Is water wet?", "So toxic...",
            "That was not easy, in fact, you held up quite a good fight!", "Good game indeed!", "White chocolate; it's fake!", "Dark chocolate; you know it, you love it...", "Real...",
            "True...", "False...", "Thanks, my little pogchamp!", "UWU - snuzzles your OMEGALUL...", "You the homies?",
            "Actor Leonard Nimoy was born on 26th March 1931 in Boston, Massachusetts, United States.", "Offering free kills!", "You're the goat, cuz!", "Do you play Minecraft?",
            "It's like... we were meant to be... <3", "You're my best friends!", "I don't like you.", "Welcome to the server! How can I help?", "Helper, how buy gems?",
            "Are you selling operator?", "Hey! I would love to help you out!", "What's up guys, welcome to my Minecraft let's play!"};

    private static @NotNull ArrayList<HashMap<String, String>> createAliasesList() {
        ArrayList<HashMap<String, String>> aliasesList = new ArrayList<>();
        HashMap<String, String> aliasesMap = new HashMap<>();

        aliasesMap.put("4", "a");
        aliasesMap.put("8", "b");
        aliasesMap.put("(", "c");
        aliasesMap.put("[", "c");
        aliasesMap.put("{", "c");
        aliasesMap.put("<", "c");
        aliasesMap.put("3", "e");
        aliasesMap.put("6", "g");
        aliasesMap.put("9", "g");
        aliasesMap.put("!", "i");
        aliasesMap.put("|", "i");
        aliasesMap.put("/", "i");
        aliasesMap.put("\\", "i");
        aliasesMap.put("1", "i");
        aliasesMap.put("0", "o");
        aliasesMap.put("5", "s");
        aliasesMap.put("$", "s");
        aliasesMap.put("2", "z");

        //noinspection unchecked
        aliasesList.add((HashMap<String, String>) aliasesMap.clone());
        aliasesMap.clear();

        aliasesMap.put("/\\", "a");
        aliasesMap.put("|)", "d");
        aliasesMap.put("|]", "d");
        aliasesMap.put("|}", "d");
        aliasesMap.put("|>", "d");
        aliasesMap.put("i)", "d");
        aliasesMap.put("i]", "d");
        aliasesMap.put("i}", "d");
        aliasesMap.put("i>", "d");
        aliasesMap.put("1)", "d");
        aliasesMap.put("1]", "d");
        aliasesMap.put("1}", "d");
        aliasesMap.put("1>", "d");
        aliasesMap.put("!)", "d");
        aliasesMap.put("!]", "d");
        aliasesMap.put("!}", "d");
        aliasesMap.put("!>", "d");
        aliasesMap.put("l)", "d");
        aliasesMap.put("l]", "d");
        aliasesMap.put("l}", "d");
        aliasesMap.put("l>", "d");
        aliasesMap.put("|<", "k");
        aliasesMap.put("()", "o");
        aliasesMap.put("(]", "o");
        aliasesMap.put("(}", "o");
        aliasesMap.put("(>", "o");
        aliasesMap.put("[)", "o");
        aliasesMap.put("[]", "o");
        aliasesMap.put("[}", "o");
        aliasesMap.put("[>", "o");
        aliasesMap.put("{)", "o");
        aliasesMap.put("{]", "o");
        aliasesMap.put("{}", "o");
        aliasesMap.put("{>", "o");
        aliasesMap.put("<)", "o");
        aliasesMap.put("<]", "o");
        aliasesMap.put("<}", "o");
        aliasesMap.put("<>", "o");
        aliasesMap.put("L|", "u");
        aliasesMap.put("Li", "u");
        aliasesMap.put("L1", "u");
        aliasesMap.put("L!", "u");
        aliasesMap.put("Ll", "u");
        aliasesMap.put("\\/", "v");

        //noinspection unchecked
        aliasesList.add((HashMap<String, String>) aliasesMap.clone());
        aliasesMap.clear();

        aliasesMap.put("/-/", "h");
        aliasesMap.put("\\-\\", "h");
        aliasesMap.put("|-|", "h");
        aliasesMap.put("|-i", "h");
        aliasesMap.put("|-1", "h");
        aliasesMap.put("|-!", "h");
        aliasesMap.put("|-l", "h");
        aliasesMap.put("i-|", "h");
        aliasesMap.put("i-i", "h");
        aliasesMap.put("i-1", "h");
        aliasesMap.put("i-!", "h");
        aliasesMap.put("i-l", "h");
        aliasesMap.put("1-|", "h");
        aliasesMap.put("1-i", "h");
        aliasesMap.put("1-1", "h");
        aliasesMap.put("1-!", "h");
        aliasesMap.put("1-l", "h");
        aliasesMap.put("!-|", "h");
        aliasesMap.put("!-i", "h");
        aliasesMap.put("!-1", "h");
        aliasesMap.put("!-!", "h");
        aliasesMap.put("!-l", "h");
        aliasesMap.put("l-|", "h");
        aliasesMap.put("l-i", "h");
        aliasesMap.put("l-1", "h");
        aliasesMap.put("l-!", "h");
        aliasesMap.put("l-l", "h");
        aliasesMap.put("|\\|", "n");
        aliasesMap.put("|\\i", "n");
        aliasesMap.put("|\\1", "n");
        aliasesMap.put("|\\!", "n");
        aliasesMap.put("|\\l", "n");
        aliasesMap.put("i\\|", "n");
        aliasesMap.put("i\\i", "n");
        aliasesMap.put("i\\1", "n");
        aliasesMap.put("i\\!", "n");
        aliasesMap.put("i\\l", "n");
        aliasesMap.put("1\\|", "n");
        aliasesMap.put("1\\i", "n");
        aliasesMap.put("1\\1", "n");
        aliasesMap.put("1\\!", "n");
        aliasesMap.put("1\\l", "n");
        aliasesMap.put("!\\|", "n");
        aliasesMap.put("!\\i", "n");
        aliasesMap.put("!\\1", "n");
        aliasesMap.put("!\\!", "n");
        aliasesMap.put("!\\l", "n");
        aliasesMap.put("l\\|", "n");
        aliasesMap.put("l\\i", "n");
        aliasesMap.put("l\\1", "n");
        aliasesMap.put("l\\!", "n");
        aliasesMap.put("l\\l", "n");
        aliasesMap.put("|/|", "n");
        aliasesMap.put("|/i", "n");
        aliasesMap.put("|/1", "n");
        aliasesMap.put("|/!", "n");
        aliasesMap.put("|/l", "n");
        aliasesMap.put("i/|", "n");
        aliasesMap.put("i/i", "n");
        aliasesMap.put("i/1", "n");
        aliasesMap.put("i/!", "n");
        aliasesMap.put("i/l", "n");
        aliasesMap.put("1/|", "n");
        aliasesMap.put("1/i", "n");
        aliasesMap.put("1/1", "n");
        aliasesMap.put("1/!", "n");
        aliasesMap.put("1/l", "n");
        aliasesMap.put("!/|", "n");
        aliasesMap.put("!/i", "n");
        aliasesMap.put("!/1", "n");
        aliasesMap.put("!/!", "n");
        aliasesMap.put("!/l", "n");
        aliasesMap.put("l/|", "n");
        aliasesMap.put("l/i", "n");
        aliasesMap.put("l/1", "n");
        aliasesMap.put("l/!", "n");
        aliasesMap.put("l/l", "n");

        aliasesList.add(aliasesMap);

        return aliasesList;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static @NotNull HashMap<String, ArrayList<AllowedWord>> createFilteredMap() {
        HashMap<String, ArrayList<AllowedWord>> filteredMap = new HashMap<>();
        ArrayList<AllowedWord> allowedList = new ArrayList<>();

        allowedList.add(new AllowedWord("freeze", 3, 2, 1));
        //noinspection unchecked
        filteredMap.put("ez", (ArrayList<AllowedWord>) allowedList.clone());
        allowedList.clear();

        allowedList.add(new AllowedWord("documentary", 2, 3, 6));
        //noinspection unchecked
        filteredMap.put("cum", (ArrayList<AllowedWord>) allowedList.clone());
        allowedList.clear();

        filteredMap.put("hentai", allowedList);

        filteredMap.put("sex", allowedList);

        filteredMap.put("bitch", allowedList);

        filteredMap.put("nigger", allowedList);

        filteredMap.put("whore", allowedList);

        filteredMap.put("slag", allowedList);

        filteredMap.put("twat", allowedList);

        filteredMap.put("prick", allowedList);

        filteredMap.put("faggot", allowedList);

        filteredMap.put("chink", allowedList);

        filteredMap.put("nonce", allowedList);

        filteredMap.put("cunt", allowedList);

        filteredMap.put("coon", allowedList);

        filteredMap.put("retard", allowedList);

        filteredMap.put("paki", allowedList);

        filteredMap.put("nigga", allowedList);

        filteredMap.put("nigg", allowedList);

        allowedList.add(new AllowedWord("night", 0, 3, 2));
        allowedList.add(new AllowedWord("enigma", 1, 3, 2));
        //noinspection unchecked
        filteredMap.put("nig", (ArrayList<AllowedWord>) allowedList.clone());
        allowedList.clear();

        filteredMap.put("dick", allowedList);

        filteredMap.put("cock", allowedList);

        filteredMap.put("dildo", allowedList);

        filteredMap.put("condom", allowedList);

        filteredMap.put("penis", allowedList);

        filteredMap.put("pussy", allowedList);

        filteredMap.put("vagina", allowedList);

        filteredMap.put("wank", allowedList);

        filteredMap.put("porn", allowedList);

        allowedList.add(new AllowedWord("grape", 1, 4, 0));
        //noinspection unchecked
        filteredMap.put("rape", (ArrayList<AllowedWord>) allowedList.clone());

        filteredMap.put("raping", allowedList);

        filteredMap.put("rapeing", allowedList);

        filteredMap.put("boner", allowedList);

        return filteredMap;
    }
}
