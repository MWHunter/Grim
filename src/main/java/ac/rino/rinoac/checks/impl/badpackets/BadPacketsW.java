package ac.rino.rinoac.checks.impl.badpackets;

import ac.rino.rinoac.checks.Check;
import ac.rino.rinoac.checks.CheckData;
import ac.rino.rinoac.checks.type.PacketCheck;
import ac.rino.rinoac.player.RinoPlayer;

@CheckData(name = "BadPacketsW", experimental = true)
public class BadPacketsW extends Check implements PacketCheck {
    public BadPacketsW(RinoPlayer player) {
        super(player);
    }
}
