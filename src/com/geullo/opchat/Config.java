package com.geullo.opchat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    public static File file;
    public static FileConfiguration config;
    public static void setUp(){
        file = new File(Opchat.getPlugin().getDataFolder(),"oplist.yml");
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

}
