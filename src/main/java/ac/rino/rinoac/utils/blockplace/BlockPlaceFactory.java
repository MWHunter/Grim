package ac.rino.rinoac.utils.blockplace;

import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.anticheat.update.BlockPlace;

public interface BlockPlaceFactory {
    void applyBlockPlaceToWorld(RinoPlayer player, BlockPlace place);
}
