package ac.rino.rinoac.events.packets;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.player.RinoPlayer;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetCooldown;

public class PacketPlayerCooldown extends PacketListenerAbstract {

    public PacketPlayerCooldown() {
        super(PacketListenerPriority.HIGH);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.SET_COOLDOWN) {
            WrapperPlayServerSetCooldown cooldown = new WrapperPlayServerSetCooldown(event);

            RinoPlayer player = RinoAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player == null) return;

            int lastTransactionSent = player.lastTransactionSent.get();

            if (cooldown.getCooldownTicks() == 0) { // for removing the cooldown
                player.latencyUtils.addRealTimeTask(lastTransactionSent + 1, () -> {
                    player.checkManager.getCompensatedCooldown().removeCooldown(cooldown.getItem());
                });
            } else { // Not for removing the cooldown
                player.latencyUtils.addRealTimeTask(lastTransactionSent, () -> {
                    player.checkManager.getCompensatedCooldown().addCooldown(cooldown.getItem(),
                            cooldown.getCooldownTicks(), lastTransactionSent);
                });
            }
        }
    }
}
