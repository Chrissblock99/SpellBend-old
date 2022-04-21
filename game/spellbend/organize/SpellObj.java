package game.spellbend.organize;

import game.spellbend.playerdata.Gold;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpellObj {
    private final String name;
    private final ItemStack item;
    private final int price;

    public SpellObj(String name, ItemStack item, int price) {
        this.name = name;
        this.item = item;
        this.price = price;
    }

    public boolean playerCanBuy(@NotNull Player player) {
        return Gold.getGold(player)>=price;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getPrice() {
        return price;
    }
}
