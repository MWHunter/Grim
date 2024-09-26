package ac.rino.rinoac.manager.init.start;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.manager.init.Initable;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ExemptOnlinePlayers implements Initable {
    @Override
    public void start() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
            RinoAPI.INSTANCE.getPlayerDataManager().exemptUsers.add(user);
        }
    }
}
