package ac.grim.grimac.manager.init.load;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.manager.init.Initable;
import ac.grim.grimac.shaded.com.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.packetevents.protocol.chat.ChatTypes;
import ac.grim.grimac.shaded.com.packetevents.protocol.entity.data.EntityDataTypes;
import ac.grim.grimac.shaded.com.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.packetevents.protocol.particle.type.ParticleTypes;
import ac.grim.grimac.shaded.com.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.io.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import ac.grim.grimac.utils.anticheat.LogUtil;

import java.util.concurrent.Executors;

public class PacketEventsInit implements Initable {
    @Override
    public void start() {
        LogUtil.info("Loading PacketEvents...");
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(GrimAPI.INSTANCE.getPlugin()));
        PacketEvents.getAPI().getSettings()
                .fullStackTrace(true)
                .kickOnPacketException(true)
                .checkForUpdates(false)
                .reEncodeByDefault(false)
                .debug(false);
        PacketEvents.getAPI().load();
        // This may seem useless, but it causes java to start loading stuff async before we need it
        Executors.defaultThreadFactory().newThread(() -> {
            StateTypes.AIR.getName();
            ItemTypes.AIR.getName();
            EntityTypes.PLAYER.getParent();
            EntityDataTypes.BOOLEAN.getName();
            ChatTypes.CHAT.getName();
            EnchantmentTypes.ALL_DAMAGE_PROTECTION.getName();
            ParticleTypes.DUST.getName();
        }).start();
    }
}
