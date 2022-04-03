package com.geullo.opchat;


import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.geullo.opchat.Operator.Chat;
import com.geullo.opchat.Operator.ChatActionBar;
import com.geullo.opchat.Operator.Invisible;
import com.geullo.opchat.Operator.Operator;
import com.geullo.opchat.SkAddon.PlayerIsOperatorChecked;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.*;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.IOException;
import java.util.logging.*;

public class Opchat extends JavaPlugin {
    public final static Logger log = Logger.getLogger("Minecraft");
    public static final String Subject = ChatColor.RED + "Operator :: "+ChatColor.WHITE;
    public static final String OperatorSubject = ChatColor.RED + "Operator LOG :: "+ChatColor.WHITE;
    private static Opchat plugin;
    private static Chat chatEvent = new Chat();
    private SkriptAddon addon;
    public static Opchat getPlugin(){
        return plugin;
    }
    public void onEnable() {
        plugin =this;
        addon = Skript.registerAddon(this);
        try {
            addon.loadClasses("com.geullo.opchat","SkAddon");
        }catch (IOException e){
            e.printStackTrace();
        }
        BukkitScheduler scheduler = getServer().getScheduler();
        Bukkit.getServer().getPluginManager().registerEvents(chatEvent, plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new Invisible(), plugin);
        scheduler.scheduleSyncRepeatingTask(this,new ChatActionBar(),0,1);

        initPlugin();
        log("OP CHAT Plugin Enabled.", Level.INFO);
    }
    public void onDisable() {
        log("OP CHAT Plugin Disabled.", Level.INFO);
    }
    public static void log(String string, Level level) {
        log.log(level,  "\u001B[31m[OP CHAT LOG]\u001B[37m " + string+"\u001B[37m");
    }
    public void initPlugin(){
        Config.setUp();
        Config.get().options().copyDefaults(true);
        Config.save();
        ChatConfig.setUp();
        ChatConfig.get().options().copyDefaults(true);
        ChatConfig.save();
        Opchat.log("COMPLETE LOAD",Level.INFO);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        switch (cmd.getName()) {
            case "관리자":
                if (player == null)
                {
                    if (args.length < 1) {helpMessage(0);return true;}
                    if ("추가".equals(args[0]) || "add".equals(args[0])) {
                        if (args.length < 2) {helpMessage(0);return true;}
                        OfflinePlayer player1 = Bukkit.getOfflinePlayer(args[1]);
                        if (player1 ==null){
                            log("존재하지 않는 플레이어 입니다.", Level.INFO);
                            return true;
                        }
                        if (Operator.getInstance().isOperator(player1.getUniqueId())){
                            log("해당 플레이어는 이미 관리자 입니다.", Level.INFO);
                            return true;
                        }
                        if (Operator.getInstance().addOperator(player1.getName())) {
                            log("성공적으로 " + args[1] + "님을 관리자 목록에 추가했습니다.", Level.INFO);
                        } else {
                            log("해당 플레이어를 추가하던중 오류가 발생했습니다.", Level.INFO);
                        }
                        return true;
                    } else if ("제거".equals(args[0]) || "remove".equals(args[0])) {
                        if (args.length < 2) {helpMessage(0);return true;}
                        if (Operator.getInstance().removeOperator(args[1])) {
                            log("성공적으로 " + args[1] + "님을 관리자 목록에서 제거했습니다.", Level.INFO);
                        } else {
                            log("해당 플레이어는 관리자가 아닙니다.", Level.INFO);
                        }
                        return true;
                    } else if ("초기화".equals(args[0]) || "clear".equals(args[0])) {
                        Operator.getInstance().clearOperator();
                        return true;
                    }else if ("닉네임".equals(args[0])||"nick".equals(args[0])){// /관리자 닉네임 [설정닉] [플레이어]
                        if (args.length==3) {
                            OfflinePlayer changer = Bukkit.getOfflinePlayer(args[2]);
                            if (changer==null) {log("플레이어 닉네임이 잘못되었습니다.",Level.INFO);return true;}
                            Player changer2 = Bukkit.getPlayer(args[2]);
                            if (changer2!=null){
                                changer2.sendMessage(Subject+"본인의"+ChatColor.AQUA+" OPERATOR CHATTING"+ChatColor.WHITE+" 닉네임이 "+ChatColor.GOLD+ChatColor.BOLD+"CONSOLE"+ChatColor.WHITE+"에 의해 "+ChatColor.GOLD+ChatColor.BOLD+ChatConfig.getCustomName(changer.getUniqueId())+ChatColor.WHITE+" 으로 설정되셨습니다.");

                            }
                            ChatConfig.changeCustomName(args[1], changer.getUniqueId());
                        }else {
                            helpMessage(0);
                            return true;
                        }
                        return true;

                    }else if ("목록".equals(args[0]) || "list".equals(args[0])) {
                        Operator.getInstance().showOperators(false, null);
                        return true;
                    }else if("도움말".equals(args[0])||"help".equals(args[0])){
                        if (args.length<2||"1".equals(args[1])) {
                            helpMessage(1);
                            return true;
                        }
                        if ("2".equals(args[1])){
                            helpMessage(2);
                            return true;
                        }else{
                            helpMessage(0);
                        }
                    }else {
                        helpMessage(0);
                    }
                    return true;
                }
                else if (player != null)
                {
                    if (!Operator.getInstance().isOperator(player)) {player.sendMessage("Unknown command. Type \"/help\" for help.");return true;}
                    if (args.length < 1) {helpMessage(player,0);return true;}
                    if ("추가".equals(args[0]) || "add".equals(args[0])) {
                        if (args.length < 2) {helpMessage(player,0);return true;}
                        OfflinePlayer player1 = Bukkit.getOfflinePlayer(args[1]);
                        if (Operator.getInstance().isOperator(player1.getUniqueId())){
                            sender.sendMessage(Subject+"해당 플레이어는 이미 관리자 입니다.");
                            return true;
                        }
                        if (Operator.getInstance().addOperator(args[1])) {
                            sender.sendMessage(Subject+"성공적으로 " +ChatColor.GREEN+ChatColor.BOLD+ args[1] +ChatColor.WHITE+ "님을 관리자 목록에 추가했습니다.");
                        } else {
                            sender.sendMessage(Subject+"해당 플레이어는 이미 추가된 플레이어 입니다.");
                        }
                        return true;
                    } else if ("제거".equals(args[0]) || "remove".equals(args[0])) {
                        if (args.length < 2) {helpMessage(player,0);return true;}
                        if (Operator.getInstance().removeOperator(args[1])) {
                            sender.sendMessage(Subject+"성공적으로 " +ChatColor.DARK_RED+ChatColor.BOLD+  args[1]+ChatColor.WHITE + "님을 관리자 목록에서 제거했습니다.");
                        } else {
                            sender.sendMessage(Subject+"해당 플레이어는 관리자 목록에 없습니다.");
                        }
                        return true;
                    } else if ("초기화".equals(args[0]) || "clear".equals(args[0])) {
                        Operator.getInstance().clearOperator();return true;
                    } else if ("닉네임".equals(args[0])||"nick".equals(args[0])){// /관리자 닉네임 [설정닉] [플레이어]
                        if (args.length==1){
                            if (ChatConfig.changeCustomName("", player.getUniqueId())){
                                player.sendMessage(Subject+"본인의 OPERATOR CHATTING 이름을 "+ChatColor.GOLD+ChatColor.BOLD+ChatConfig.getCustomName(player.getUniqueId())+ChatColor.WHITE+" 으로 설정했습니다.");
                            }else{
                                player.sendMessage(Subject+"설정하던 도중 오류가 발생했습니다. 관리자에게 문의해주세요");
                            }
                            return true;
                        }
                        if (args.length==2){
                            if (ChatConfig.changeCustomName(args[1], player.getUniqueId())){
                                player.sendMessage(Subject+"본인의 OPERATOR CHATTING 이름을 "+ChatColor.GOLD+ChatColor.BOLD+ChatConfig.getCustomName(player.getUniqueId())+ChatColor.WHITE+" 으로 설정했습니다.");
                            }else{
                                player.sendMessage(Subject+"설정하던 도중 오류가 발생했습니다. 관리자에게 문의해주세요");
                            }
                            return true;
                        }
                        else if (args.length==3) {
                            Player changer = Bukkit.getPlayer(args[2]);
                            if (changer==null) {player.sendMessage(Subject+"해당 플레이어는 접속중이지 않습니다.");return true;}
                            if (!Operator.getInstance().isOperator(changer.getUniqueId())){player.sendMessage(Subject+"해당 플레이어는 관리자가 아닙니다.");return true;}
                            if (ChatConfig.changeCustomName(args[1], changer.getUniqueId())){
                                changer.sendMessage(Subject+"본인의"+ChatColor.AQUA+" OPERATOR CHATTING"+ChatColor.WHITE+" 닉네임이 "+ChatColor.GOLD+ChatColor.BOLD+player.getName()+ChatColor.WHITE+"에 의해 "+ChatColor.GOLD+ChatColor.BOLD+ChatConfig.getCustomName(changer.getUniqueId())+ChatColor.WHITE+" 으로 설정되셨습니다.");

                                player.sendMessage(Subject+ChatColor.GOLD+""+ChatColor.BOLD+changer.getName()+ChatColor.WHITE+"님의"+ChatColor.AQUA+" OPERATOR CHATTING"+ChatColor.WHITE+" 닉네임을 "+ChatColor.GOLD+ChatColor.BOLD+ChatConfig.getCustomName(changer.getUniqueId())+ChatColor.WHITE+" 으로 설정했습니다.");
                            }else{
                                player.sendMessage(Subject+"설정하던 도중 오류가 발생했습니다. 관리자에게 문의해주세요");
                            }
                            return true;
                        }else {
                            helpMessage(player,0);
                            return true;
                        }

                    }else if ("목록".equals(args[0]) || "list".equals(args[0])) {
                        Operator.getInstance().showOperators(true, player);
                        return true;
                    }else if ("도움말".equals(args[0])||"help".equals(args[0])){
                        if (args.length<2||"1".equals(args[1])) {
                            helpMessage(player,1);
                            return true;
                        }
                        if ("2".equals(args[1])){
                            helpMessage(player,2);
                            return true;
                        }else if("3".equals(args[1])) {
                            helpMessage(player,3);
                        }else{
                            player.sendMessage(Subject + "/관리자 도움말 [PAGE]" + ChatColor.GRAY + ":: 관리자 명령어에 대한 설명을 봅니다.");
                        }
                        return true;
                    }else {
                        helpMessage(player,0);
                        return true;
                    }
                }
                return true;
            case "oc":
                if (player!=null) {
                    if (!Operator.getInstance().isOperator(player)) {player.sendMessage("Unknown command. Type \"/help\" for help.");return true;}
                    if (Operator.getInstance().isOperator(player)){
                        ChatConfig.changeChatMode(player,player.getUniqueId());
                        return true;
                    }
                }
                return true;
            case "ov":
                if (player!=null) {
                    if (!Operator.getInstance().isOperator(player)) {player.sendMessage("Unknown command. Type \"/help\" for help.");return true;}
                    if (Operator.getInstance().isOperator(player)) {
                        Invisible.changeInvisible(player);
                    }
                }
                return true;
        }
        return true;

    }
    public void helpMessage(int nowPage){
        switch (nowPage){
            case 0:
                log("/관리자 도움말 [PAGE]\033[0;33m :: 관리자 명령어에 대한 설명을 봅니다.\033[0m",Level.INFO);
                break;
            case 1:
                log("/관리자 도움말 [PAGE]\033[0;33m :: 관리자 명령어에 대한 설명을 봅니다.\033[0m",Level.INFO);
                log("/관리자 추가 [플레이어]\033[0;33m :: [플레이어] 를 관리자 목록에 추가 시킵니다.\033[0m",Level.INFO);
                log("/관리자 제거 [플레이어]\033[0;33m :: [플레이어] 를 관리자 목록에서 제거 시킵니다.\033[0m",Level.INFO);
                log("/관리자 초기화\033[0;33m :: 관리자 목록을 초기화 시킵니다.\033[0m",Level.INFO);
                log("\033[0;33m[\033[1;35m1\033[1;31m/2\033[0;33m]\033[0m",Level.INFO);
                break;
            case 2:
                log("/관리자 목록\033[0;33m :: 관리자 목록을 확인 합니다.\033[0m",Level.INFO);
                log("/관리자 닉네임\033[0;33m :: 본인의 OPERATOR CHAT 닉네임을 기존 닉네임(ex.Geullo) 변경 시킵니다.\033[0m",Level.INFO);
                log("/관리자 닉네임 [변경할 닉네임]\033[0;33m :: 본인의 OPERATOR CHAT 닉네임을 [변경할 닉네임]으로 변경 시킵니다.\033[0m",Level.INFO);
                log("/관리자 닉네임 [변경할 닉네임] [변경할 대상]\033[0;33m :: [변경할 대상]의 닉네임을 [변경할 닉네임]으로 변경 시킵니다.\033[0m",Level.INFO);
                log("\033[0;33m[\033[1;35m2\033[1;31m/2\033[0;33m]\033[0m",Level.INFO);
                break;
        }
    }
    public void helpMessage(Player player,int nowPage){
        switch (nowPage){
            case 0:
                player.sendMessage(Subject + "/관리자 도움말 [PAGE]" + ChatColor.GRAY + " :: 관리자 명령어에 대한 설명을 봅니다.");
                break;
            case 1:
                player.sendMessage(Subject + "/관리자 도움말 [PAGE]" + ChatColor.GRAY + " :: 관리자 명령어에 대한 설명을 봅니다.");
                player.sendMessage(Subject + "/관리자 추가 [플레이어] " + ChatColor.GRAY + " :: [플레이어] 를 관리자 목록에 추가 시킵니다.");
                player.sendMessage(Subject + "/관리자 제거 [플레이어]" + ChatColor.GRAY + " :: [플레이어] 를 관리자 목록에서 제거 시킵니다.");
                player.sendMessage(Subject + "/관리자 초기화" + ChatColor.GRAY + " :: 관리자 목록을 초기화 시킵니다.");
                player.sendMessage(Subject + ChatColor.DARK_GRAY + "["+ChatColor.RED+"1"+ChatColor.DARK_RED+"/3"+ ChatColor.DARK_GRAY +"]");
                break;
            case 2:
                player.sendMessage(Subject+"/관리자 목록"+ChatColor.GRAY+" :: 관리자 목록을 확인 합니다.");
                player.sendMessage(Subject+"/관리자 닉네임"+ChatColor.GRAY+" :: 본인의 OPERATOR CHAT 닉네임을 기존 닉네임(ex.Geullo) 변경 시킵니다.");
                player.sendMessage(Subject+"/관리자 닉네임 [변경할 닉네임]"+ChatColor.GRAY+" :: 본인의 OPERATOR CHAT 닉네임을 [변경할 닉네임]으로 변경 시킵니다.");
                player.sendMessage(Subject+"/관리자 닉네임 [변경할 닉네임] [변경할 대상]"+ChatColor.GRAY+" :: [변경할 대상]의 닉네임을 [변경할 닉네임]으로 변경 시킵니다.");
                player.sendMessage(Subject + ChatColor.DARK_GRAY + "["+ChatColor.RED+"2"+ChatColor.DARK_RED+"/3"+ ChatColor.DARK_GRAY +"]");
                break;
            case 3:
                player.sendMessage(Subject + "/oc" + ChatColor.GRAY + " :: 관리자 채팅과 일반채팅을 전환 합니다.");
                player.sendMessage(Subject + "/ov" + ChatColor.GRAY + " :: 투명 여부를 설정 합니다.");
                player.sendMessage(Subject + ChatColor.DARK_GRAY + "["+ChatColor.DARK_RED+"3"+ChatColor.DARK_RED+"/3"+ ChatColor.DARK_GRAY +"]");
                break;
        }
    }

}
