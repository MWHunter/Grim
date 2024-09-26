package ac.rino.rinoac.checks.impl.badpackets;

import ac.rino.rinoac.checks.Check;
import ac.rino.rinoac.checks.CheckData;
import ac.rino.rinoac.checks.type.PacketCheck;
import ac.rino.rinoac.player.RinoPlayer;

@CheckData(name = "BadPacketsN")
public class BadPacketsN extends Check implements PacketCheck {
    public BadPacketsN(final RinoPlayer player) {
        super(player);
    }
}
