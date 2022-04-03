package com.geullo.opchat.Operator;

import com.geullo.opchat.ChatConfig;
import com.geullo.opchat.Config;
import com.geullo.opchat.Opchat;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

public class Operator {
    private static Operator instance;
    public static Operator getInstance(){
        if (instance==null) instance=new Operator();
        return instance;
    }
    public boolean addOperator(String playername){
        OfflinePlayer of = Bukkit.getOfflinePlayer(playername);
        if (of==null) return false;
        UUID pu = of.getUniqueId();
        Config.config.getKeys(true).forEach(p->{
            UUID uid = UUID.fromString(p);
            Player oper = Bukkit.getPlayer(uid);
            Player target = Bukkit.getPlayer(pu);
            if (oper!=null&&target!=null) {
                target.showPlayer(Opchat.getPlugin(), oper);
            }
        });
        String uid = pu.toString();
        if (Config.config.contains(uid)){
                Config.config.set(uid, "");
                Config.save();
                if (!ChatConfig.config.getKeys(true).stream().anyMatch(uid::equals)){

                    ChatConfig.config.addDefault(uid+".opchat",false);
                    ChatConfig.config.addDefault(uid+".FAKE-NAME",playername);
                    ChatConfig.config.addDefault(uid+".invisible", false);
                    ChatConfig.config.options().copyDefaults(true);
                    ChatConfig.save();
                    ChatConfig.reload();
                }
                Player p = Bukkit.getPlayer(pu);
                if (p!=null){
                    p.sendMessage(Opchat.Subject+"관리자 목록에 추가되셨습니다.");
                }
                return true;
        }else {
            Config.config.addDefault(pu.toString(), "");
            Config.config.options().copyDefaults(true);
            Config.save();
            Config.reload();
            ChatConfig.config.addDefault(uid+".opchat", false);
            ChatConfig.config.addDefault(uid+".invisible", false);
            ChatConfig.config.addDefault(uid+".FAKE-NAME",playername);
            ChatConfig.config.options().copyDefaults(true);
            ChatConfig.save();
            ChatConfig.reload();
            Player p = Bukkit.getPlayer(pu);
            if (p!=null){
                p.sendMessage(Opchat.Subject+"관리자 목록에 추가되셨습니다.");
            }
            return true;
        }
    }
    public boolean removeOperator(String playername){
        OfflinePlayer p = Bukkit.getOfflinePlayer(playername);
        if (p==null) return false;
        String pu = p.getUniqueId().toString();
        if (Config.config.getKeys(true).stream().anyMatch(pu::equals)) {
            Player player = Bukkit.getPlayer(playername);
            boolean check = false;

            Config.config.set(pu, null);
            Config.save();
            Config.reload();
            if (ChatConfig.getChatMode(p.getUniqueId())) {
                check = true;
            }
            ChatConfig.config.set(pu, null);
            ChatConfig.save();
            ChatConfig.reload();
            if (player != null && check) {
                TextComponent textComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&f&l[ &6&lOP-CHAT &f&l] &c비활성화 되었습니다.&f"));
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
            }else if (player!=null){
                Bukkit.getOnlinePlayers().forEach(p2->{
                    if ((!Operator.getInstance().isOperator(p2))) {
                        p2.showPlayer(Opchat.getPlugin(), player);
                    }
                });
            }
            if (player!=null) {
                Config.config.getKeys(true).forEach(t -> {
                    UUID UID = UUID.fromString(t);
                    Player op = Bukkit.getPlayer(UID);
                    if (op != null) {
                        if (getInstance().isOperator(op)) {
                            player.hidePlayer(Opchat.getPlugin(), op);
                        }
                    }
                });
                player.sendMessage(Opchat.Subject+"관리자 목록에서 제거되셨습니다.");
            }
            return true;
        }else{
            return false;
        }
    }

    public boolean clearOperator(){
        Config.config.getKeys(true).forEach(t -> {
            UUID UID = UUID.fromString(t);
            Player op = Bukkit.getPlayer(UID);
            if (op != null) {
                op.sendMessage(Opchat.Subject+"관리자 목록에서 제거되셨습니다.");
            }
        });
        Config.clear();
        ChatConfig.clear();
        return true;
    }
    public boolean isOperator(Player player){
        UUID uuid = player.getUniqueId();
        return isOperator(uuid);
    }
    @Deprecated
    public boolean isOperator(UUID uuid){
        AtomicBoolean q = new AtomicBoolean(false);
        Config.config.getKeys(true).stream().forEach(pu->{
            if (pu.equals(uuid.toString())) {
                q.set(true);
            }
        });
        return q.get();
    }
    public boolean showOperators(boolean senderIsPlayer, Player player){
        AtomicBoolean q = new AtomicBoolean(false);
        if (Config.config.getKeys(true).size()<=0){
            if (senderIsPlayer) {
                player.sendMessage(Opchat.Subject+"관리자 목록이 비어있습니다.");
            } else {
                Opchat.log("관리자 목록이 비어있습니다.", Level.INFO);
            }
            return false;
        }
        Config.config.getKeys(true).stream().forEach(pu->{
                UUID uuid = UUID.fromString(pu);
                if (senderIsPlayer) {
                    player.sendMessage(Opchat.Subject+Bukkit.getOfflinePlayer(uuid).getName());
                } else {
                    Opchat.log(Bukkit.getOfflinePlayer(uuid).getName(), Level.INFO);
                }
                q.set(true);
        });
        return q.get();
    }
}
