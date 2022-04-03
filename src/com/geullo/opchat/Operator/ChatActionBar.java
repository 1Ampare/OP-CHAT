package com.geullo.opchat.Operator;

import com.geullo.opchat.ChatConfig;
import com.geullo.opchat.Config;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ChatActionBar extends BukkitRunnable{
    @Override
    public void run() {
        Config.config.getKeys(true).stream().forEach(uuid->{
            UUID uid = UUID.fromString(uuid);
            Player p = Bukkit.getPlayer(uid);
            if (p!=null){
                if (ChatConfig.getChatMode(p)) {
                    TextComponent textComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&f&l[ &6&lOP-CHAT &f&l] &a활성화 되었습니다.&f"));
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
                }
            }
        });
    }
}
