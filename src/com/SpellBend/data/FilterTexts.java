package com.SpellBend.data;

import com.SpellBend.organize.AllowedWord;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterTexts {
    public static final ArrayList<HashMap<String, String>> aliases = createAliasesList();
    public static final HashMap<String, ArrayList<AllowedWord>> filtered = createFilteredMap();
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
        aliasesMap.put("{", "c");
        aliasesMap.put("[", "c");
        aliasesMap.put("(", "c");
        aliasesMap.put("3", "e");
        aliasesMap.put("!", "i");
        aliasesMap.put("|", "i");
        aliasesMap.put("1", "i");
        aliasesMap.put("0", "o");
        aliasesMap.put("5", "s");

        //noinspection unchecked
        aliasesList.add((HashMap<String, String>) aliasesMap.clone());
        aliasesMap.clear();

        aliasesMap.put("/\\", "a");
        aliasesMap.put("|)", "d");
        aliasesMap.put("|]", "d");
        aliasesMap.put("|}", "d");
        aliasesMap.put("1)", "d");
        aliasesMap.put("1]", "d");
        aliasesMap.put("1}", "d");
        aliasesMap.put("!)", "d");
        aliasesMap.put("!]", "d");
        aliasesMap.put("!}", "d");
        aliasesMap.put("i)", "d");
        aliasesMap.put("i]", "d");
        aliasesMap.put("i}", "d");
        aliasesMap.put("l)", "d");
        aliasesMap.put("l]", "d");
        aliasesMap.put("l}", "d");
        aliasesMap.put("|<", "k");
        aliasesMap.put("()", "o");
        aliasesMap.put("[]", "o");
        aliasesMap.put("{}", "o");
        aliasesMap.put("\\/", "v");

        aliasesList.add(aliasesMap);

        return aliasesList;
    }

    private static @NotNull HashMap<String, ArrayList<AllowedWord>> createFilteredMap() {
        HashMap<String, ArrayList<AllowedWord>> filteredMap = new HashMap<>();
        ArrayList<AllowedWord> allowedList = new ArrayList<>();

        filteredMap.put("ez", allowedList);

        allowedList.add(new AllowedWord("documentary", 2, 3, 6));
        //noinspection unchecked
        filteredMap.put("cum", (ArrayList<AllowedWord>) allowedList.clone());
        allowedList.clear();

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

        filteredMap.put("dick", allowedList);

        filteredMap.put("cock", allowedList);

        filteredMap.put("dildo", allowedList);

        filteredMap.put("condom", allowedList);

        filteredMap.put("penis", allowedList);

        filteredMap.put("pussy", allowedList);

        filteredMap.put("vagina", allowedList);

        filteredMap.put("wank", allowedList);

        filteredMap.put("porn", allowedList);

        filteredMap.put("rape", allowedList);

        filteredMap.put("raping", allowedList);

        filteredMap.put("rapeing", allowedList);

        filteredMap.put("boner", allowedList);

        return filteredMap;
    }
}
