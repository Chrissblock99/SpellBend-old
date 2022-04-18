package game.SpellBend.organize;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class AllowedWord {
    private final String word;
    private final int charsBefore;
    private final int charsMid;
    private final int charsAfter;

    public AllowedWord(@NotNull String word, int charsBefore, int charsMid, int charsAfter) {
        if (word.length() != (charsBefore+charsMid+charsAfter)) Bukkit.getLogger().warning("AllowedWord \"" + word + "\" has length specifications not matching it's length!");
        this.word = word;
        this.charsBefore = charsBefore;
        this.charsMid = charsMid;
        this.charsAfter = charsAfter;
    }

    public String getWord() {return word;}

    public int getCharsBefore() {return charsBefore;}

    public int getCharsMid() {return charsMid;}

    public int getCharsAfter() {return charsAfter;}
}
