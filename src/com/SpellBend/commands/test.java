package com.SpellBend.commands;

import com.SpellBend.organize.CoolDownEntry;
import com.SpellBend.organize.Enums;
import com.SpellBend.data.Lists;
import com.SpellBend.spell.Spell;
import com.SpellBend.spell.SpellHandler;
import com.SpellBend.util.Item;
import com.SpellBend.util.MathUtil;
import com.SpellBend.util.playerDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

import java.util.*;

public class test {
    private final HashMap<String, advancedSubCommand> subCommands = new HashMap<>();

    public test() {
        subCommands.put("item", new advancedSubCommand(new Class[0]) {
            @Override
            public boolean onCommand(CommandSender sender, ArrayList<Object> arguments) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§4Only players can use this subCommand!");
                    return true;
                }

                Inventory inv = ((Player) sender).getInventory();
                inv.addItem(Item.create(Material.CAMPFIRE, "§c§lFiery Rage", 1, new String[]{"spellName", "spellType"}, new String[]{"Fiery_Rage", "AURA"}));
                inv.addItem(Item.create(Material.GOLDEN_HORSE_ARMOR, "§c§lEmber Blast", 1, new String[]{"spellName", "spellType"}, new String[]{"Ember_Blast", "BLAST"}));
                return true;
            }
        });

        subCommands.put("memory spell", new advancedSubCommand(new Class[]{Player.class}, new String[]{"player"}) {
            @Override
            public boolean onCommand(CommandSender sender, ArrayList<Object> arguments) {
                sender.sendMessage("Spells:");
                ArrayList<Spell> playerSpells = SpellHandler.activeSpells.get(((Player) arguments.get(0)).getUniqueId());
                if (playerSpells.size() == 0) {
                    sender.sendMessage("none");
                    return true;
                }
                for (Spell spell : playerSpells) {
                    sender.sendMessage(spell.getClass().getName());
                }
                return true;
            }
        });

        subCommands.put("memory tasks", new advancedSubCommand(new Class[]{String.class}, new String[]{"filter"}) {
            @Override
            public boolean onCommand(CommandSender sender, ArrayList<Object> arguments) {
                boolean filtering = !arguments.get(0).equals("all");
                String filter = null;
                if (filtering) filter = (String) arguments.get(0);
                int printed = 0;

                sender.sendMessage("Workers:");
                List<BukkitWorker> workers = Bukkit.getScheduler().getActiveWorkers();
                for (BukkitWorker worker : workers) {
                    if (filtering) if (!worker.getOwner().getName().equals(filter)) continue;
                    sender.sendMessage((worker.getOwner().getName() + " " + worker.getTaskId() + ": " + worker));
                    printed++;
                }
                if (printed == 0) {
                    sender.sendMessage("none");
                }
                printed = 0;

                sender.sendMessage("Tasks:");
                List<BukkitTask> tasks = Bukkit.getScheduler().getPendingTasks();
                for (BukkitTask task : tasks) {
                    if (filtering) if (!task.getOwner().getName().equals(filter)) continue;
                    sender.sendMessage((task.getOwner().getName() + " " + task.getTaskId() + ": " + task));
                    printed ++;
                }
                if (printed == 0) {
                    sender.sendMessage("none");
                }
                return true;
            }
        });

        subCommands.put("value dmgMod get", new advancedSubCommand(new Class[]{String.class, Player.class}, new String[]{"dmgMod", "player"}) {
            @Override
            public boolean onCommand(CommandSender sender, ArrayList<Object> arguments) {
                String dmgMod = (String) arguments.get(0);
                Player player = (Player) arguments.get(1);

                if (Lists.getDmgModTypeByName(dmgMod) == null && !dmgMod.equals("all")) {
                    sender.sendMessage("§4" + dmgMod + " is not a valid DmgModifier!");
                    return true;
                }

                sender.sendMessage("DmgModifier " + dmgMod + " of " + player.getDisplayName() + ": " + playerDataUtil.getDmgMod(player, dmgMod));
                return true;
            }
        });

        subCommands.put("value dmgMod set", new advancedSubCommand(new Class[]{String.class, Player.class, Float.class}, new String[]{"dmgMod", "player", "number"}) {
            @Override
            public boolean onCommand(CommandSender sender, ArrayList<Object> arguments) {
                String dmgMod = (String) arguments.get(0);
                Player player = (Player) arguments.get(1);
                Float num = (Float) arguments.get(2);

                if (Lists.getDmgModTypeByName(dmgMod) == null) {
                    sender.sendMessage("§4" + arguments.get(1) + " is not a settable DmgModifier!");
                    return true;
                }

                playerDataUtil.setDmgMod(player, dmgMod, num);
                sender.sendMessage("Chriss go implement some more info here and check if it actually has been set (or if setting was rejected)");
                return true;
            }
        });

        subCommands.put("value cooldown get", new advancedSubCommand(new Class[]{String.class, Player.class}, new String[]{"coolDownType", "player"}) {
            @Override
            public boolean onCommand(CommandSender sender, ArrayList<Object> arguments) {
                String type = ((String) arguments.get(0)).toUpperCase();
                Player player = (Player) arguments.get(1);

                if (type.equals("ALL")) {
                    sender.sendMessage("active Cooldowns of " + player.getDisplayName() + ":");
                    Set<Map.Entry<Enums.SpellType, CoolDownEntry>> entrySet = playerDataUtil.getCoolDowns(player).entrySet();
                    if (entrySet.size() == 0) {
                        sender.sendMessage("none");
                        return true;
                    }
                    for (Map.Entry<Enums.SpellType, CoolDownEntry> entry : entrySet) {
                        CoolDownEntry coolDownEntry = entry.getValue();
                        sender.sendMessage(entry.getKey() + ": " + coolDownEntry.getRemainingCoolDownTime() + ", " + coolDownEntry.timeInS + ", " + coolDownEntry.coolDownType);
                    }
                    return true;
                }
                try {
                    CoolDownEntry coolDownEntry = playerDataUtil.getCoolDownEntry(player, Enums.SpellType.valueOf(type));
                    sender.sendMessage("Cooldown " + arguments.get(0) + " of " + player.getDisplayName() + ": "
                            + coolDownEntry.getRemainingCoolDownTime() + ", " + coolDownEntry.timeInS + ", " + coolDownEntry.coolDownType);
                } catch (IllegalArgumentException exception) {
                    sender.sendMessage("§4" + arguments.get(0) + " is not a valid spellType!");
                } catch (NullPointerException exception) {
                    sender.sendMessage("§4A NullPointerException was thrown when parsing \"" + type + "\" to a SpellType!");
                }
                return true;
            }
        });

        subCommands.put("value cooldown set", new advancedSubCommand(new Class[]{Enums.SpellType.class, Player.class, Integer.class, String.class},
                new String[]{"spellType", "player", "timeInTicks", "coolDownType"}) {
            @Override
            public boolean onCommand(CommandSender sender, ArrayList<Object> arguments) {
                Enums.SpellType spellType = (Enums.SpellType) arguments.get(0);
                Player player = (Player) arguments.get(1);
                Integer timeInTicks = (Integer) arguments.get(2);
                String CDType = (String) arguments.get(3);

                playerDataUtil.setCoolDown(player, spellType, timeInTicks, CDType);
                if (!playerDataUtil.getCoolDownEntry(player, spellType).equals(new CoolDownEntry(timeInTicks, new Date(), CDType))) {
                    sender.sendMessage("§4Something went wrong when setting " + player.getDisplayName() + "'s cooldown!");
                    return true;
                }
                sender.sendMessage("Successfully set " + player.getDisplayName() + "'s cooldown.");
                return true;
            }
        });

        subCommands.put("value cooldown add", new advancedSubCommand(new Class[]{Enums.SpellType.class, Player.class, Integer.class, String.class},
                new String[]{"spellType", "player", "timeInTicks", "coolDownType"}) {
            @Override
            public boolean onCommand(CommandSender sender, ArrayList<Object> arguments) {
                Enums.SpellType spellType = (Enums.SpellType) arguments.get(0);
                Player player = (Player) arguments.get(1);
                Integer timeInSeconds = (Integer) arguments.get(2);
                String CDType = (String) arguments.get(3);

                if (playerDataUtil.getCoolDownEntry(player, spellType).timeInS != 0) {
                    sender.sendMessage("§eWarning: This coolDown is already set, assigning the larger one!");
                    CoolDownEntry oldValues = playerDataUtil.getCoolDownEntry(player, spellType);
                    if (MathUtil.ASmallerB(
                            new long[]{Lists.getCoolDownTypeByName(oldValues.coolDownType).typeInt*(-1), (long) oldValues.timeInS*1000-(new Date().getTime()-oldValues.startDate.getTime())},
                            new long[]{Lists.getCoolDownTypeByName(CDType).typeInt *(-1), (long) timeInSeconds*1000}))
                        playerDataUtil.setCoolDown(player, spellType, timeInSeconds, CDType);
                    if (playerDataUtil.getCoolDownEntry(player, spellType).equals(new CoolDownEntry(timeInSeconds, new Date(), CDType))) {
                        sender.sendMessage("§4Something went wrong when setting " + player.getDisplayName() + "'s cooldown!");
                        return true;
                    }
                    sender.sendMessage("The coolDown already set was larger so the new coolDown wasn't assigned.");
                    return true;
                }

                playerDataUtil.setCoolDown(player, spellType, timeInSeconds, CDType);
                if (playerDataUtil.getCoolDownEntry(player, spellType).equals(new CoolDownEntry(timeInSeconds, new Date(), CDType))) {
                    sender.sendMessage("§4Something went wrong when setting " + player.getDisplayName() + "'s cooldown!");
                    return true;
                }
                sender.sendMessage("Successfully added the coolDown to " + player.getDisplayName() + ".");
                return true;
            }
        });

        new advancedCommandBase("test", "/test item or /test memory <spell|tasks [filter]> or /test value <dmgMod|cooldown> <valueName> <player>", subCommands){}
                .setRankingNeeded(Lists.getBadgeByName("dev").ranking).setPermission("SpellBend.test");
    }
}