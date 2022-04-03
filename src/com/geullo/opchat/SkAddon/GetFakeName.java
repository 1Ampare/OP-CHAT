package com.geullo.opchat.SkAddon;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.geullo.opchat.ChatConfig;
import com.geullo.opchat.Opchat;
import com.geullo.opchat.Operator.Chat;
import com.geullo.opchat.Operator.Operator;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.logging.Level;

@Name("Get Fake Name")
@Description({"Get Fake Name"})
@Examples("fake-name of player")
@Since("2.0")
public class GetFakeName extends SimpleExpression<String> {
    private Expression<Player> player;
    static {
        Skript.registerExpression(GetFakeName.class,String.class, ExpressionType.COMBINED,"[Geullo] [the] f[ake-]name of %player%");
    }
    @Override
    @Nullable
    protected String[] get(Event event) {
        Player of = player.getSingle(event);
        if (of!=null){
            return new String[] {Chat.changeFakeName(of.getName())};
        }
        return null;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return "Example expression with expression player: " + player.toString(event,b);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) expressions[0];
        return true;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        Player p = player.getSingle(e);
        if (p!=null){
            if (mode == Changer.ChangeMode.SET){
                System.out.println(delta.length);
                if (Operator.getInstance().isOperator(p.getUniqueId())) {
                    if (!delta[0].toString().equals("")) {
                        if (!ChatConfig.changeCustomName((String) delta[0], p.getUniqueId())) {
                            Opchat.log("Name Change Error Occurred.", Level.INFO);
                        }
                    }else{
                        if (!ChatConfig.changeCustomName(p.getName(), p.getUniqueId())){
                            Opchat.log("Name Change Error Occurred.", Level.INFO);
                        }
                    }
                }else {
                    Opchat.log("Player Is not Operator", Level.INFO);
                }
            }else if (mode == Changer.ChangeMode.RESET||mode == Changer.ChangeMode.DELETE||mode == Changer.ChangeMode.REMOVE||mode == Changer.ChangeMode.REMOVE_ALL){
                if (Operator.getInstance().isOperator(p.getUniqueId())) {
                    if (!ChatConfig.changeCustomName(p.getName(), p.getUniqueId())){
                        Opchat.log("Name Change Error Occurred.", Level.INFO);
                    }
                }else {
                    Opchat.log("Player Is not Operator", Level.INFO);
                }
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.DELETE||mode == Changer.ChangeMode.RESET||mode == Changer.ChangeMode.REMOVE||mode == Changer.ChangeMode.REMOVE_ALL||mode == Changer.ChangeMode.SET){
            return CollectionUtils.array(String.class);
        }
        return null;
    }
}
