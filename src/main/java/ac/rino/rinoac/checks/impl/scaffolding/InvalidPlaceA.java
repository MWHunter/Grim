package ac.rino.rinoac.checks.impl.scaffolding;

import ac.rino.rinoac.checks.CheckData;
import ac.rino.rinoac.checks.type.BlockPlaceCheck;
import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.anticheat.update.BlockPlace;
import com.github.retrooper.packetevents.util.Vector3f;

@CheckData(name = "InvalidPlaceA")
public class InvalidPlaceA extends BlockPlaceCheck {
    public InvalidPlaceA(RinoPlayer player) {
        super(player);
    }

    @Override
    public void onBlockPlace(final BlockPlace place) {
        Vector3f cursor = place.getCursor();
        if (cursor == null) return;
        if (!Float.isFinite(cursor.getX()) || !Float.isFinite(cursor.getY()) || !Float.isFinite(cursor.getZ())) {
            if (flagAndAlert() && shouldModifyPackets() && shouldCancel()) {
                place.resync();
            }
        }
    }
}
