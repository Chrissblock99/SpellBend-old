package game.spellbend.data;

import game.spellbend.organize.BadgeObj;
import game.spellbend.organize.CoolDownType;
import game.spellbend.organize.DmgModType;
import game.spellbend.organize.RankObj;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Lists {
    public static final ArrayList<DmgModType> dmgModTypeList = createDmgModTypeList();
    public static final ArrayList<CoolDownType> coolDownTypeList = createCoolDownTypeList();
    public static final ArrayList<RankObj> rankList = createRankList();
    public static final ArrayList<BadgeObj> badgeList = createBadgeList();

    private static @NotNull ArrayList<DmgModType> createDmgModTypeList() {
        ArrayList<DmgModType> dmgModTypeList = new ArrayList<>();

        dmgModTypeList.add(new DmgModType("spell", 0));
        dmgModTypeList.add(new DmgModType("handicap", 1));
        dmgModTypeList.add(new DmgModType("force", 2));

        return dmgModTypeList;
    }

    private static @NotNull ArrayList<CoolDownType> createCoolDownTypeList() {
        ArrayList<CoolDownType> coolDownTypeList = new ArrayList<>();

        coolDownTypeList.add(new CoolDownType("windup", -1));
        coolDownTypeList.add(new CoolDownType("active", 0));
        coolDownTypeList.add(new CoolDownType("cooldown", 1));

        return coolDownTypeList;
    }

    private static @NotNull ArrayList<RankObj> createRankList() {
        ArrayList<RankObj> rankList = new ArrayList<>();

        rankList.add(new RankObj("norank", 0 ,"§7NoRank", "§7", "§8"));
        rankList.add(new RankObj("player", 1, "§7Player", "§f", "§8"));
        rankList.add(new RankObj("patron", 10, "§bPatron", "§3", "§9"));
        rankList.add(new RankObj("legend", 12, "§e§lLegend", "§6", "§8"));
        rankList.add(new RankObj("retired", 19,"§dRetired", "§7", "§8"));
        rankList.add(new RankObj("helper", 20, "§dHelper", "§5"));
        rankList.add(new RankObj("mod", 22, "§aMod", "§2"));
        rankList.add(new RankObj("admin", 30, "§cAdmin", "§4", "§8"));
        rankList.add(new RankObj("owner", 50, "§9Owner", "§6", "§c"));

        return rankList;
    }

    private static @NotNull ArrayList<BadgeObj> createBadgeList() {
        ArrayList<BadgeObj> badgeList = new ArrayList<>();

        badgeList.add(new BadgeObj("winner", 2, "§e❈"));
        badgeList.add(new BadgeObj("builder", 21, "§2✎"));
        badgeList.add(new BadgeObj("dev", 25, "§9✩"));

        return badgeList;
    }

    public static DmgModType getDmgModTypeByName(String name) {
        return dmgModTypeList.stream()
                .filter(object -> name.equals(object.name))
                .findAny()
                .orElse(null);
    }

    public static CoolDownType getCoolDownTypeByName(String name) {
        return coolDownTypeList.stream()
                .filter(object -> name.equals(object.name))
                .findAny()
                .orElse(null);
    }

    public static CoolDownType getCoolDownTypeByTypeInt(int TypeInt) {
        return coolDownTypeList.stream()
                .filter(object -> TypeInt == object.typeInt)
                .findAny()
                .orElse(null);
    }

    public static RankObj getRankByName(String name) {
        return rankList.stream()
                .filter(object -> name.equals(object.rankName))
                .findAny()
                .orElse(null);
    }

    public static BadgeObj getBadgeByName(String name) {
        return badgeList.stream()
                .filter(object -> name.equals(object.badgeName))
                .findAny()
                .orElse(null);
    }
}
