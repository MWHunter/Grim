package ac.rino.rinoac.checks.impl.badpackets;

import ac.rino.rinoac.checks.Check;
import ac.rino.rinoac.checks.CheckData;
import ac.rino.rinoac.checks.type.PacketCheck;
import ac.rino.rinoac.player.RinoPlayer;

@CheckData(name = "BadPacketsS")
public class BadPacketsS extends Check implements PacketCheck {
    public BadPacketsS(RinoPlayer player) {
        super(player);
    }

}
