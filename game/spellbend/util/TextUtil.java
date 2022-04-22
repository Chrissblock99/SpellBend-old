package game.spellbend.util;

import game.spellbend.data.FilterTexts;
import game.spellbend.organize.AllowedWord;
import game.spellbend.util.math.MathUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TextUtil {
    public static final String[] lowerCaseABC = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    public static final String[] upperCaseABC = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public static final ArrayList<String> lowerCaseABCList = new ArrayList<>(Arrays.asList(lowerCaseABC));
    public static final ArrayList<String> upperCaseABCList = new ArrayList<>(Arrays.asList(upperCaseABC));
    public static final String[] formattingSymbols = {" ", ".", ",", "?", "!", ":", "\""};
    public static final String[] notAliasedFormattingSymbols = {" ", ".", ",", "?", ":", "\""};

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
    public static @NotNull String filterMessage(@NotNull String message) {
        String startMessage = message;

        //Bukkit.getLogger().info("§ball");
        if (hasBadWord(message)) return (String) MathUtil.randomEntry(FilterTexts.funSentence);
        //Bukkit.getLogger().info("§ballABC");
        if (hasBadWord(removeOtherStrings(message.toLowerCase(), lowerCaseABC))) return (String) MathUtil.randomEntry(FilterTexts.funSentence);
        //Bukkit.getLogger().info("§bupperCaseABC");
        if (hasBadWord(removeOtherStrings(message, upperCaseABC))) return (String) MathUtil.randomEntry(FilterTexts.funSentence);
        //Bukkit.getLogger().info("§blowerCaseABC");
        if (hasBadWord(removeOtherStrings(message, lowerCaseABC))) return (String) MathUtil.randomEntry(FilterTexts.funSentence);
        //Bukkit.getLogger().info("§b1");
        if (hasBadWord(removeOtherStrings(replaceAliases(removeStrings(switchCase(message), upperCaseABC)), lowerCaseABC))) return (String) MathUtil.randomEntry(FilterTexts.funSentence);
        //Bukkit.getLogger().info("§b2");
        if (hasBadWord(removeOtherStrings(replaceAliases(removeStrings(message, upperCaseABC)), lowerCaseABC))) return (String) MathUtil.randomEntry(FilterTexts.funSentence);

        //Bukkit.getLogger().info("§breplacing Aliases: " + message);
        message = removeOtherStrings(replaceAliases(removeStrings(message, notAliasedFormattingSymbols)).toLowerCase(), lowerCaseABC);

        //Bukkit.getLogger().info("§bdeAliased");
        if (hasBadWord(message)) return (String) MathUtil.randomEntry(FilterTexts.funSentence);

        return startMessage;
    }

    /**Checks the String for bad Words
     *
     * @param string The String to check
     * @return if the String contains bad words or not
     */
    public static boolean hasBadWord(@NotNull String string) {
        if (string.isEmpty()) return false;
        string = string.toLowerCase();
        for (Map.Entry<String, ArrayList<AllowedWord>> filteredWord : FilterTexts.filtered.entrySet()) {
            boolean hasMatch = false;
            //Bukkit.getLogger().info("§bFiltering for " + filteredWord.getKey() + ": " + string);
            for(Integer i : getStringOccurrences(string, filteredWord.getKey())) {
                if (string.regionMatches(i, filteredWord.getKey(), 0, filteredWord.getKey().length())) {
                    //Bukkit.getLogger().info("§bMatch found at " + i);
                    for (AllowedWord allowedWord : filteredWord.getValue()) {
                        //Bukkit.getLogger().info("§bChecking if index " + i + " matches " + allowedWord.getWord() + ": " + string);
                        if (string.regionMatches(i - allowedWord.getCharsBefore(), allowedWord.getWord(), 0, allowedWord.getWord().length())) {
                            //Bukkit.getLogger().info("§bMatch!");
                            hasMatch = true;
                            break;
                        }// else Bukkit.getLogger().info("§bno match");
                    }
                }
                if (!hasMatch) return true;
            }
        }
        return false;
    }

    /**Replaces all aliases for alphabetic characters inside the String
     *
     * @param string The String to replace aliases
     * @return The deAliased String
     */
    public static @NotNull String replaceAliases(@NotNull String string) {
        for (int i = FilterTexts.aliases.size()-1;i>=0;i--)
            for (Map.Entry<String, String> entry : FilterTexts.aliases.get(i).entrySet()) {
                string = string.replace(entry.getKey(), entry.getValue());
                //Bukkit.getLogger().info("§b" + string);
            }
        return string;
    }

    public static @NotNull String switchCase(@NotNull String string) {
        String[] charList = string.split("");
        for (int i = 0;i< charList.length;i++)
            if (lowerCaseABCList.contains(charList[i])) charList[i] = charList[i].toUpperCase();
            else charList[i] = charList[i].toLowerCase();
        return String.join("", charList);
    }

    /**Removes all strings not contained in the list from the string
     *
     * @param string The string to remove from
     * @param list The strings not to remove
     * @return The filtered String
     */
    public static @NotNull String removeOtherStrings(@NotNull String string, @NotNull String[] list) {
        ArrayList<String> chars = new ArrayList<>(List.of(string.split("")));
        ArrayList<String> notRemove = new ArrayList<>(List.of(list));
        for (int i = chars.size()-1;i>=0;i--)
            if (!notRemove.contains(chars.get(i))) chars.remove(i);
        return String.join("", chars);
    }

    /**Removes all Strings contained in List from String
     *
     * @param string The string to filter
     * @return The filtered String
     */
    public static @NotNull String removeStrings(@NotNull String string, @NotNull String[] list) {
        for (String stringToRemove : list) string = string.replace(stringToRemove, "");
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
            occurrences.add(index);
        return occurrences;
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    static <T> @NotNull T addArrays(@NotNull T array1, @NotNull T array2) {
        if (!array1.getClass().isArray() || !array2.getClass().isArray()) {
            throw new IllegalArgumentException("Only arrays are accepted.");
        }

        Class<?> compType1 = array1.getClass().getComponentType();
        Class<?> compType2 = array2.getClass().getComponentType();

        if (!compType1.equals(compType2)) {
            throw new IllegalArgumentException("Two arrays have different types.");
        }

        int len1 = Array.getLength(array1);
        int len2 = Array.getLength(array2);

        @SuppressWarnings("unchecked")
        //the cast is safe due to the previous checks
        T result = (T) Array.newInstance(compType1, len1 + len2);

        System.arraycopy(array1, 0, result, 0, len1);
        System.arraycopy(array2, 0, result, len1, len2);

        return result;
    }
}
