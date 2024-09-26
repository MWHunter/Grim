package ac.rino.rinoac.checks.impl.misc;

import ac.rino.rinoac.checks.Check;
import ac.rino.rinoac.checks.CheckData;
import ac.rino.rinoac.checks.type.PacketCheck;
import ac.rino.rinoac.player.RinoPlayer;

@CheckData(name = "TransactionOrder", experimental = false)
public class TransactionOrder extends Check implements PacketCheck {

    public TransactionOrder(RinoPlayer player) {
        super(player);
    }

}