package com.geullo.opchat.Operator;

import com.geullo.opchat.ChatConfig;
import com.geullo.opchat.Config;
import com.geullo.opchat.Opchat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.util.UUID;

public class Invisible implements Listener {

    public static boolean getInvisible(Player player){//ChatConfig.config.addDefault(uid+".invisible", false);
        return ChatConfig.config.getBoolean(player.getUniqueId().toString()+".invisible");
    }
    public static void changeInvisible(Player player){
        changeInvisible(player.getUniqueId());
    }
    public static void changeInvisible(UUID uuid){
        if (!Operator.getInstance().isOperator(uuid)){
            return;
        }
        String uid = uuid.toString();
        Player player = Bukkit.getPlayer(uuid);
        if (ChatConfig.config.getBoolean(uid+".invisible")) {
            player.sendMessage(Opchat.Subject+"HIDE MODE "+ ChatColor.GRAY+ChatColor.BOLD+"OFF");
            ChatConfig.config.set(uid+".invisible",false);
            ChatConfig.save();
            ChatConfig.reload();
            Bukkit.getOnlinePlayers().forEach(p->{
                if (!Operator.getInstance().isOperator(p)){
                    p.showPlayer(Opchat.getPlugin(),player);
                }
            });
        }else {
            player.sendMessage(Opchat.Subject+"HIDE MODE "+ChatColor.GREEN+ChatColor.BOLD+"ON");
            ChatConfig.config.set(uid+".invisible",true);
            ChatConfig.save();
            ChatConfig.reload();
            Bukkit.getOnlinePlayers().forEach(p->{
                if (!Operator.getInstance().isOperator(p)){
                    p.hidePlayer(Opchat.getPlugin(),player);
                }
            });
        }
    }

    @EventHandler
    public void onJoinOperator(PlayerJoinEvent e){
        if (!Operator.getInstance().isOperator(e.getPlayer())){
            Config.config.getKeys(true).forEach(p->{
                UUID uid = UUID.fromString(p);
                Player oper = Bukkit.getPlayer(uid);
                if (oper!=null) {
                    if (getInvisible(oper)){
                        e.getPlayer().hidePlayer(Opchat.getPlugin(),oper);
                    }else{
                        e.getPlayer().showPlayer(Opchat.getPlugin(),oper);
                    }
                }
            });
        }else{
            Bukkit.getOnlinePlayers().forEach(p->{
                if ((!Operator.getInstance().isOperator(p))){
                    if (getInvisible(e.getPlayer())){
                        p.hidePlayer(Opchat.getPlugin(),e.getPlayer());
                    }else{
                        p.showPlayer(Opchat.getPlugin(),e.getPlayer());
                    }

                }
            });
        }
    }
}
