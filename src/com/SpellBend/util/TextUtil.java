package com.SpellBend.util;

import com.SpellBend.data.FilterTexts;
import com.SpellBend.organize.AllowedWord;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class TextUtil {
    /**Replaces all aliases in The String, then checks for bad words
     * if it contains a bad word a random FilterTexts.funSentence is returned
     * otherwise the original String gets returned
     *
     * NO FILTER IS PERFECT!!
     * AKA do not rely on this
     *
     * @param message The String to filter if it contains bad words
     * @return The filtered String
     */
    public static @NotNull String filterMessage(@NotNull String message) { //TODO make this work
        String startMessage = message;

        if (hasBadWord(message)) return (String) MathUtil.randomEntry(FilterTexts.funSentence);
        message = replaceAliases(filterFormattingSymbols(message));
        Bukkit.getLogger().info("§b" + message);

        if (hasBadWord(message)) return (String) MathUtil.randomEntry(FilterTexts.funSentence);
        return startMessage;
    }

    /**Checks the String for bad Words
     *
     * @param string The String to check
     * @return if the String contains bad words or not
     */
    public static boolean hasBadWord(@NotNull String string) {
        for (Map.Entry<String, ArrayList<AllowedWord>> filteredWord : FilterTexts.filtered.entrySet()) {
            boolean hasMatch = false;
            Bukkit.getLogger().info("§bFiltering for " + filteredWord.getKey() + ": " + string);
            for(Integer i : getStringOccurrences(string, filteredWord.getKey())) {
                if (string.regionMatches(i, filteredWord.getKey(), 0, filteredWord.getKey().length())) {
                    Bukkit.getLogger().info("§bMatch found at " + i);
                    for (AllowedWord allowedWord : filteredWord.getValue()) {
                        Bukkit.getLogger().info("§bChecking if index " + i + " matches " + allowedWord.getWord() + ": " + string);
                        if (string.regionMatches(i - allowedWord.getCharsBefore(), allowedWord.getWord(), 0, allowedWord.getWord().length())) {
                            Bukkit.getLogger().info("§bMatch!");
                            hasMatch = true;
                            break;
                        } else Bukkit.getLogger().info("§bno match");
                    }
                }
                if (!hasMatch) return true;
            }
        }
        return false;
    }

    /**Replaces all " ", ".", ",", ":" and "?" with ""
     *
     * @param string The string to filter
     * @return The filtered String
     */
    public static @NotNull String filterFormattingSymbols(@NotNull String string) {
        String[] remove = {" ", ".", ",", ":", "?"};
        for (String stringToRemove : remove) string = string.replace(stringToRemove, "");
        return string;
    }

    /**Replaces all aliases for alphabetic characters inside the String
     * also makes the complete String lowerCase
     *
     * @param string The String to replace aliases
     * @return The deAliased String
     */
    public static @NotNull String replaceAliases(@NotNull String string) {
        string = string.toLowerCase();
        for (int i = FilterTexts.aliases.size()-1;i>=0;i--)
            for (Map.Entry<String, String> entry : FilterTexts.aliases.get(i).entrySet())
                string = string.replace(entry.getKey(), entry.getValue());
        return string;
    }

    /** Returns a List of all indexes where the subString starts
     *
     * @param mainString The String to check inside
     * @param subString The String to check for
     * @return The List with all starting indexes
     */
    public static @NotNull ArrayList<Integer> getStringOccurrences(@NotNull String mainString, @NotNull String subString) {
        ArrayList<Integer> occurrences = new ArrayList<>();
        for (int index = mainString.indexOf(subString); index >= 0; index = mainString.indexOf(subString, index + 1))
        {
            occurrences.add(index);
        }
        return occurrences;
    }
}
