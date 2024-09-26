package ac.rino.rinoac.utils.nmsutil;

import ac.rino.rinoac.player.RinoPlayer;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import org.bukkit.util.Vector;

import java.util.OptionalInt;

public class JumpPower {
    public static void jumpFromGround(RinoPlayer player, Vector vector) {
        float jumpPower = getJumpPower(player);

        final OptionalInt jumpBoost = player.compensatedEntities.getPotionLevelForPlayer(PotionTypes.JUMP_BOOST);
        if (jumpBoost.isPresent()) {
            jumpPower += 0.1f * (jumpBoost.getAsInt() + 1);
        }

        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_5) && jumpPower <= 1.0E-5F) return;

        vector.setY(jumpPower);

        if (player.isSprinting) {
            float radRotation = player.xRot * ((float) Math.PI / 180F);
            vector.add(new Vector(-player.trigHandler.sin(radRotation) * 0.2f, 0.0, player.trigHandler.cos(radRotation) * 0.2f));
        }
    }

    public static float getJumpPower(RinoPlayer player) {
        return (float) player.compensatedEntities.getSelf().getAttributeValue(Attributes.GENERIC_JUMP_STRENGTH) * getPlayerJumpFactor(player);
    }

    public static float getPlayerJumpFactor(RinoPlayer player) {
        return BlockProperties.onHoneyBlock(player, player.mainSupportingBlockData, new Vector3d(player.lastX, player.lastY, player.lastZ)) ? 0.5f : 1f;
    }
}
