package com.geullo.opchat.SkAddon;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

@SuppressWarnings("deprecation")
public class BukkitEventValue {
    public BukkitEventValue() {}
    static {
        EventValues.registerEventValue(PlayerInteractAtEntityEvent.class, Entity.class, new Getter<Entity, PlayerInteractAtEntityEvent>() {
            @Override
            @Nullable
            public Entity get(PlayerInteractAtEntityEvent e) {
                if (e.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
                    return e.getRightClicked();
                }
                return null;
            }
        },1);
        EventValues.registerEventValue(PlayerInteractAtEntityEvent.class, Player.class, new Getter<Player, PlayerInteractAtEntityEvent>() {
            @Override
            @Nullable
            public Player get(PlayerInteractAtEntityEvent e) {
                return e.getPlayer();
            }
        },1);
    }
}
