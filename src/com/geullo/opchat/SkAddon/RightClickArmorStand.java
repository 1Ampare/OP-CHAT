package com.geullo.opchat.SkAddon;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class RightClickArmorStand extends SkriptEvent {
    static {
        Skript.registerEvent("RigthClick ArmorStand",RightClickArmorStand.class, PlayerInteractAtEntityEvent.class,"[Geullo] interact[ing] armor( |_)stand").description(new String[]{""}).examples(new String[]{""}).since("1.0");
    }
    public RightClickArmorStand(){
    }
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] literals, int i, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(Event event) {
        EntityType entityType = ((PlayerInteractAtEntityEvent) event).getRightClicked().getType();
        if (entityType.equals(EntityType.ARMOR_STAND))
        {
            return true;
        }
        return false;
    }

    @Override
    public String toString(Event event, boolean b) {
        return "interacting armor stand";
    }
}
