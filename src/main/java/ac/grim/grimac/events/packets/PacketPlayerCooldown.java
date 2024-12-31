package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.packetevents.wrapper.play.server.WrapperPlayServerSetCooldown;

public class PacketPlayerCooldown extends PacketListenerAbstract {

    public PacketPlayerCooldown() {
        super(PacketListenerPriority.HIGH);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.SET_COOLDOWN) {
            WrapperPlayServerSetCooldown cooldown = new WrapperPlayServerSetCooldown(event);

            GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
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
