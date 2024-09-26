package ac.rino.rinoac.checks.impl.scaffolding;

import ac.rino.rinoac.checks.CheckData;
import ac.rino.rinoac.checks.type.BlockPlaceCheck;
import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.anticheat.update.BlockPlace;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;

@CheckData(name = "InvalidPlaceB")
public class InvalidPlaceB extends BlockPlaceCheck {
    public InvalidPlaceB(RinoPlayer player) {
        super(player);
    }

    @Override
    public void onBlockPlace(final BlockPlace place) {
        if (place.getFaceId() == 255 && PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_8)) {
            return;
        }

        if (place.getFaceId() < 0 || place.getFaceId() > 5) {
            // ban
            if (flagAndAlert("direction=" + place.getFaceId()) && shouldModifyPackets() && shouldCancel()) {
                place.resync();
            }
        }
    }
}
