package ac.grim.grimac.checks.impl.sprint;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;

import static ac.grim.grimac.shaded.com.packetevents.protocol.potion.PotionTypes.BLINDNESS;

@CheckData(name = "SprintD", description = "Started sprinting while having blindness", setback = 5, experimental = true)
public class SprintD extends Check implements PostPredictionCheck {
    public SprintD(GrimPlayer player) {
        super(player);
    }

    public boolean startedSprintingBeforeBlind = false;

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
            if (new WrapperPlayClientEntityAction(event).getAction() == WrapperPlayClientEntityAction.Action.START_SPRINTING) {
                startedSprintingBeforeBlind = false;
            }
        }
    }

    @Override
    public void onPredictionComplete(final PredictionComplete predictionComplete) {
        if (!predictionComplete.isChecked()) return;

        if (player.compensatedEntities.getSelf().hasPotionEffect(BLINDNESS)) {
            if (player.isSprinting && !startedSprintingBeforeBlind) {
                if (flagWithSetback()) alert("");
            } else reward();
        }
    }
}
