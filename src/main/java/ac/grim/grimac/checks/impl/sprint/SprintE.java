package ac.grim.grimac.checks.impl.sprint;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;

@CheckData(name = "SprintE", description = "Sprinting while colliding with a wall", setback = 5, experimental = true)
public class SprintE extends Check implements PostPredictionCheck {
    public SprintE(GrimPlayer player) {
        super(player);
    }

    private boolean startedSprintingThisTick, wasHorizontalCollision;

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
            if (new WrapperPlayClientEntityAction(event).getAction() == WrapperPlayClientEntityAction.Action.START_SPRINTING) {
                startedSprintingThisTick = true;
            }
        }
    }

    @Override
    public void onPredictionComplete(final PredictionComplete predictionComplete) {
        if (!predictionComplete.isChecked()) return;

        // there's a mechanic in 1.18+ that allows this if you are looking far enough away from the wall
        // I'll probably check 1.18+ later
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_18)) return;

        if (wasHorizontalCollision && !startedSprintingThisTick) {
            if (player.isSprinting) {
                if (flagAndAlert()) setbackIfAboveSetbackVL();
            } else reward();
        }

        wasHorizontalCollision = player.horizontalCollision;
        startedSprintingThisTick = false;
    }
}
