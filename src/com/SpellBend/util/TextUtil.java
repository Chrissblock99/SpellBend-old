package com.SpellBend.util;

import com.SpellBend.data.FilterTexts;
import com.SpellBend.organize.AllowedWord;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class TextUtil {
    public static String filterMessage(String message) { //TODO make this work
        message = message.toLowerCase().replace(" ", "").replace(".", "").replace(",", "").replace("?", "");
        for (int i = FilterTexts.aliases.size()-1;i>=0;i--) {
            for (Map.Entry<String, String> entry : FilterTexts.aliases.get(i).entrySet()) message = message.replace(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, ArrayList<AllowedWord>> entry : FilterTexts.filtered.entrySet()) {
            boolean hasMatch = false;
            for (Integer i : getStringOccurrences(message, entry.getKey())) for (AllowedWord word : entry.getValue())
                if (message.regionMatches(i - word.getCharsBefore(), word.getWord(), 0, word.getWord().length())) {
                    hasMatch = true;
                    break;
                }
                if (!hasMatch) return (String) MathUtil.randomEntry(FilterTexts.funSentence);
        }

        return message;
    }

    public static @NotNull ArrayList<Integer> getStringOccurrences(@NotNull String mainString, @NotNull String subString) {
        ArrayList<Integer> occurrences = new ArrayList<>();
        for (int index = mainString.indexOf(subString); index >= 0; index = mainString.indexOf(subString, index + 1))
        {
            occurrences.add(index);
        }
        return occurrences;
    }
}
