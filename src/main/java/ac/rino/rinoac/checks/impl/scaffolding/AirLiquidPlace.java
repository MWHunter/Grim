package ac.rino.rinoac.checks.impl.scaffolding;

import ac.rino.rinoac.checks.CheckData;
import ac.rino.rinoac.checks.type.BlockPlaceCheck;
import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.anticheat.update.BlockPlace;
import ac.rino.rinoac.utils.nmsutil.Materials;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.util.Vector3i;

@CheckData(name = "AirLiquidPlace")
public class AirLiquidPlace extends BlockPlaceCheck {
    public AirLiquidPlace(RinoPlayer player) {
        super(player);
    }

    @Override
    public void onBlockPlace(final BlockPlace place) {
        if (player.gamemode == GameMode.CREATIVE) return;
        Vector3i blockPos = place.getPlacedAgainstBlockLocation();
        StateType placeAgainst = player.compensatedWorld.getStateTypeAt(blockPos.getX(), blockPos.getY(), blockPos.getZ());

        if (placeAgainst.isAir() || Materials.isNoPlaceLiquid(placeAgainst)) { // fail
            if (flagAndAlert() && shouldModifyPackets() && shouldCancel()) {
                place.resync();
            }
        }
    }

    @Override
    public void reload() {
        super.reload();
        this.cancelVL = getConfig().getIntElse(getConfigName() + ".cancelVL", 0);
    }
}
