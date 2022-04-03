package com.geullo.opchat;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ChatConfig {
    private static File file;
    public static FileConfiguration config;
    public static void setUp(){
        file = new File(Opchat.getPlugin().getDataFolder(),"opchat.yml");
        if (!file.exists()){
            try {
                file.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);

    }
    public static FileConfiguration get(){
        return config;
    }
    public static void save(){
        try {
            config.save(file);
            reload();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void clear(){
        try {
            file.delete();
            file.createNewFile();
            reload();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void reload(){
        config = YamlConfiguration.loadConfiguration(file);
    }
    public static boolean getChatMode(Player player){
        UUID uuid = player.getUniqueId();
        return getChatMode(uuid);
    }
    @Deprecated
    public static boolean getChatMode(UUID uuid){
        String uid = uuid.toString()+".opchat";
        if (config.getKeys(true).stream().anyMatch(uid::equals)) {
            if (config.getBoolean(uid)) {
                return true;
            }
        }
        return false;
    }
    public static void changeChatMode(Player executor, UUID uuid){
        String uid = uuid.toString()+".opchat";
        if (config.getKeys(true).stream().anyMatch(uid::equals)) {
            if (config.getBoolean(uid)) {
                changeChatMode(executor,uuid,false);
            } else {
                changeChatMode(executor,uuid,true);
            }
        }
    }
    @Deprecated
    public static boolean changeChatMode(Player executor,UUID uuid,boolean mode){
        String uid = uuid.toString()+".opchat";
        if (config.getKeys(true).stream().anyMatch(uid::equals)) {
            config.set(uid,mode);
            save();
            reload();
            if (!mode){
                TextComponent textComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&f&l[ &6&lOP-CHAT &f&l] &c비활성화 되었습니다.&f"));
                executor.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
            }
            return true;
        }else {
            return false;
        }
    }
    public static boolean changeCustomName(String changeName,UUID uuid){
        String uid = uuid.toString()+".FAKE-NAME";
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        if (changeName == null||changeName.equals(""))  changeName = offlinePlayer.getName();
        if (config.getKeys(true).stream().anyMatch(uid::equals)) {
            config.set(uid,changeName);
            save();
            reload();
            return true;
        }
        return false;
    }
    public static String getCustomName(UUID uuid){
        String uid = uuid.toString()+".FAKE-NAME";
        if (config.getKeys(true).stream().anyMatch(uid::equals)) {
            return config.getString(uid);
        }
        return "[ERROR] NOT SETTING TO NAME - opchat.yml UUID IS NOT CORRECT SETTING";
    }
}
