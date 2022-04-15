package com.geullo.opchat;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SendData {




    public void SendData(String sendID, Plugin plugin){
        for (Player player : Bukkit.getOnlinePlayers()) {
            SendData(sendID,player,plugin);
        }
    }
    public void SendData(String sendID, Player player, Plugin plugin){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(new StringBuilder(sendID).toString());
        player.sendPluginMessage(plugin, "operatorchat", out.toByteArray());
    }

}
