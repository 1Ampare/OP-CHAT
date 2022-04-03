package com.geullo.opchat.SkAddon;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import com.geullo.opchat.Operator.Operator;
import org.bukkit.OfflinePlayer;

@Name("Is Operator")
@Description({"Check Player is Operator"})
@Examples("player is operator")
@Since("2.0")
public class PlayerIsOperatorChecked extends PropertyCondition<OfflinePlayer> {
    static {
        register(PlayerIsOperatorChecked.class,"[Geullo] operator","players");
    }
    public PlayerIsOperatorChecked(){
    }

    public boolean check(OfflinePlayer player) {
        /**
         * 스크립터가 해당 명령을 수행하게 될경우 이안에 있는 문구가 실행됩니다.
         * */
        return Operator.getInstance().isOperator(player.getUniqueId());
    }

    protected String getPropertyName() {
        return "operator";
    }
}
