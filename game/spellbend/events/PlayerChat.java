package game.spellbend.events;

import game.spellbend.commands.HoldMsgsCommand;
import game.spellbend.commands.MuteCommand;
import game.spellbend.moderation.HoldMsgs;
import game.spellbend.moderation.Mute;
import game.spellbend.moderation.Punishment;
import game.spellbend.playerdata.Punishments;
import game.spellbend.util.DataUtil;
import game.spellbend.util.EventUtil;
import game.spellbend.playerdata.PlayerDataUtil;
import game.spellbend.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerChat implements Listener {
    public PlayerChat() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (Punishments.playerHasPunishmentType(player, Mute.class)) {
            event.setCancelled(true);
            player.sendMessage("§cYou are muted!");
            MuteCommand.listMutes(player, new String[]{"", player.getDisplayName()});
            return;
        }

        if (Punishments.playerHasPunishmentType(player, HoldMsgs.class)) {
            event.setCancelled(true);
            player.sendMessage("§cYour message has been put on control!");

            UUID checker = null;
            for (Punishment punishment : Punishments.getPunishments(player))
                if (punishment instanceof HoldMsgs holdMsgs) {
                    checker = holdMsgs.getMsgChecker();
                    break;
                }

            Player msgCheckerPlayer = null;
            if (checker != null)
                msgCheckerPlayer = Bukkit.getPlayer(checker);

            if (msgCheckerPlayer == null) {
                ArrayList<Player> onlineModeratingPersons = DataUtil.getOnlineModeratingPersons();
                if (onlineModeratingPersons.size() != 0) {
                    msgCheckerPlayer = onlineModeratingPersons.get(0);
                } else {
                    int ID = event.getMessage().hashCode();
                    Bukkit.getLogger().warning("§cCheck:\n" +
                            "Player: §f" + player.getDisplayName() + "\n" +
                            "§cID: §f" + ID + "\n" +
                            "§cMessage: §f" + event.getMessage());
                    player.sendMessage("§cBecause no staff is online to check your message it will be send to console!\n" +
                            "There probably will not be a check on it.");

                    if (!HoldMsgsCommand.getPlayerIDMessageMap().containsKey(player.getUniqueId()))
                        HoldMsgsCommand.getPlayerIDMessageMap().put(player.getUniqueId(), new HashMap<>());
                    HoldMsgsCommand.getPlayerIDMessageMap().get(player.getUniqueId()).put(ID, event.getMessage());
                    return;
                }
            }

            int ID = event.getMessage().hashCode();
            msgCheckerPlayer.sendMessage("§cCheck:\n" +
                    "Player: §f" + player.getDisplayName() + "\n" +
                    "§cID: §f" + ID + "\n" +
                    "§cMessage: §f" + event.getMessage());

            if (!HoldMsgsCommand.getPlayerIDMessageMap().containsKey(player.getUniqueId()))
                HoldMsgsCommand.getPlayerIDMessageMap().put(player.getUniqueId(), new HashMap<>());
            HoldMsgsCommand.getPlayerIDMessageMap().get(player.getUniqueId()).put(ID, event.getMessage());
            return;
        }

        event.setFormat(PlayerDataUtil.constructDisplayString(player) + " » §f" + TextUtil.filterMessage(event.getMessage()));
    }
}
