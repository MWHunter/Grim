package ac.rino.rinoac.checks.impl.scaffolding;

import ac.rino.rinoac.checks.CheckData;
import ac.rino.rinoac.checks.type.BlockPlaceCheck;
import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.anticheat.update.BlockPlace;
import ac.rino.rinoac.utils.nmsutil.Materials;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.util.Vector3f;

@CheckData(name = "FabricatedPlace")
public class FabricatedPlace extends BlockPlaceCheck {
    public FabricatedPlace(RinoPlayer player) {
        super(player);
    }

    @Override
    public void onBlockPlace(final BlockPlace place) {
        Vector3f cursor = place.getCursor();
        if (cursor == null) return;

        double allowed = Materials.isShapeExceedsCube(place.getPlacedAgainstMaterial()) || place.getPlacedAgainstMaterial() == StateTypes.LECTERN ? 1.5 : 1;
        double minAllowed = 1 - allowed;

        if (cursor.getX() < minAllowed || cursor.getY() < minAllowed || cursor.getZ() < minAllowed || cursor.getX() > allowed || cursor.getY() > allowed || cursor.getZ() > allowed) {
            if (flagAndAlert() && shouldModifyPackets() && shouldCancel()) {
                place.resync();
            }
        }
    }
}
