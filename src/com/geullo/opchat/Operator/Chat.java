package com.geullo.opchat.Operator;

import com.geullo.opchat.ChatConfig;
import com.geullo.opchat.Config;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Chat implements Listener {
    public boolean tt = false;
    public String consoleMessage = "";
    @EventHandler
    public void onRc(PlayerInteractAtEntityEvent e){
        e.getRightClicked().getType().equals(EntityType.ARMOR_STAND);
    }

    @EventHandler
    public void onOperatorChat(PlayerChatEvent e){
        if (Operator.getInstance().isOperator(e.getPlayer())){
            Player player = e.getPlayer();
            if (ChatConfig.getChatMode(player)){
                e.setCancelled(true);
                String message = e.getMessage();
                boolean[] mentioned = {false};
                TextComponent textComponent = new TextComponent(message);
                SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분 ss초");
                Calendar t = Calendar.getInstance();
                String nowDay = format.format(t.getTime());
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + nowDay).create()));
                Config.config.getKeys(true).stream().forEach(uuid-> {

                    UUID uid = UUID.fromString(uuid);
                    Player catcher = Bukkit.getPlayer(uid);
                    if (catcher != null) {
                        String fakeName = changeFakeName(player.getName());
                        String message3 = "", id;
                        AtomicReference<Boolean> s = new AtomicReference<>(false);
                        if (!fakeName.isEmpty()) {
                            id = ChatColor.AQUA + "[ " + ChatColor.GOLD + fakeName + ChatColor.AQUA + " ] ";
                            consoleMessage = "\u001B[36m[ \u001B[33m" + fakeName + "\u001B[36m ] ";
                        } else {
                            id = ChatColor.AQUA + "[ " + ChatColor.GOLD + player.getName() + ChatColor.AQUA + " ] ";
                            consoleMessage = "\u001B[36m[ \u001B[33m" + player.getName() + "\u001B[36m ] ";
                        }
                        message3 = message;
                        if (message.contains('@' + changeFakeName(catcher.getName()))) {
                            message3 = message3.replaceAll("@" + changeFakeName(catcher.getName()), "&6&l@" + changeFakeName(catcher.getName()) + "&b");
                            tagPlayer(player, catcher.getName(), changeFakeName(catcher.getName()), textComponent, message3, nowDay);
                            mentioned[0] = true;
                            message3 = ChatColor.translateAlternateColorCodes('&', message3);
                            textComponent.setText(id + message3);
                            message3 = message3.replaceAll("§6","\u001B[33m");
                            message3 = message3.replaceAll("§l","");
                            message3 = message3.replaceAll("§b","\u001B[36m");
                            consoleMessage = consoleMessage +message3;
                        } else {
                            message3 = ChatColor.translateAlternateColorCodes('&', message3);
                            if (!mentioned[0]) {
                                consoleMessage = consoleMessage +message3;
                                textComponent.setText(id + message3);
                            }
                        }
                    }
                });

                Config.config.getKeys(true).stream().forEach(uuid-> {
                    UUID uid = UUID.fromString(uuid);
                    Player catcher = Bukkit.getPlayer(uid);
                    if (catcher != null) {
                        catcher.spigot().sendMessage(textComponent);
                    }
                });
                System.out.println("\u001B[31m[OP CHAT LOG]\u001B[37m " + consoleMessage + "\u001B[37m");
                tt=false;
            }
        }
    }

    public static String changeFakeName(String playerName){
        Player player = Bukkit.getPlayer(playerName);
        String uuid = player.getUniqueId().toString()+".FAKE-NAME";
        ChatConfig.reload();
        if (ChatConfig.config.getKeys(true).stream().anyMatch(uuid::equals)) {
                return ChatConfig.config.getString(uuid);
        }

        return "";
    }


    private void tagPlayer(Player executor,String suffer, String fakename,TextComponent  textComponent,String message,String nowDay){
        String me2 = message;
        me2 = ChatColor.translateAlternateColorCodes('&',ChatColor.translateAlternateColorCodes('&',message));
        Player tagplayer = Bukkit.getPlayer(suffer);
        OfflinePlayer tagplayer2 = Bukkit.getOfflinePlayer(suffer);
        if (tagplayer != null) {
            String tagFakeNm = changeFakeName(tagplayer.getName());
            tagplayer.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + changeFakeName(executor.getName()) + ChatColor.WHITE + " 님이 태그 하셨습니다.", me2, 5, 14, 5);
            tagplayer.playSound(tagplayer.getLocation(), Sound.BLOCK_NOTE_FLUTE,SoundCategory.PLAYERS,1,2);
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(ChatColor.WHITE+changeFakeName(executor.getName())+" 님이 "+ChatColor.AQUA+tagFakeNm+ChatColor.AQUA+"님"+ChatColor.WHITE+"을 태그 했습니다."+ChatColor.GRAY+"\n("+nowDay+")").create()));
        }else{
            String tagFakeNm = changeFakeName(tagplayer2.getName());
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(ChatColor.WHITE+changeFakeName(executor.getName())+" 님이 "+ChatColor.AQUA+tagFakeNm+ChatColor.AQUA+"님"+ChatColor.WHITE+"을 태그 했습니다."+ChatColor.GRAY+"\n("+nowDay+")").create()));
        }
    }
}
