package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.com.packetevents.wrapper.PacketWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlockPlaceSnapshot {
    PacketWrapper<?> wrapper;
    boolean sneaking;
}
