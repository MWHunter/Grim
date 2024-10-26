package ac.grim.grimac.checks.impl.breaking;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;
import ac.grim.grimac.utils.math.GrimMath;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

public class NoBreakDelayMitigation extends Check implements BlockBreakCheck {
    public NoBreakDelayMitigation(GrimPlayer player) {
        super(player);
    }

    private int minTicks;
    private int ticks;

    @Override
    public void onBlockBreak(BlockBreak blockBreak) {
        if (blockBreak.action == DiggingAction.START_DIGGING
                && player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8)
                && ticks < minTicks
        ) blockBreak.cancel();

        if (blockBreak.action == DiggingAction.FINISHED_DIGGING) {
            ticks = 0;
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType()) && !player.packetStateData.lastPacketWasTeleport && !player.packetStateData.lastPacketWasOnePointSeventeenDuplicate) {
            ticks++;
        }
    }

    @Override
    public void onReload(ConfigManager config) {
        minTicks = GrimMath.clampInt(config.getIntElse("min-break-delay", 3), 0, 6);
    }
}
