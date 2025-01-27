package ac.grim.grimac.manager;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.alerts.AlertManager;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Supplier;

@Getter
public class AlertManagerImpl implements AlertManager {

    private final AlertToggle alerts = new AlertToggle(
            () -> GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("alerts-enabled", "%prefix% &fAlerts enabled"),
            () -> GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("alerts-disabled", "%prefix% &fAlerts disabled")
    );
    private final AlertToggle verbose = new AlertToggle(
            () -> GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("verbose-enabled", "%prefix% &fVerbose enabled"),
            () -> GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("verbose-disabled", "%prefix% &fVerbose disabled")
    );
    private final AlertToggle brands = new AlertToggle(
            () -> GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("brands-enabled", "%prefix% &fBrands enabled"),
            () -> GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("brands-disabled", "%prefix% &fBrands disabled")
    );

    @Override
    public boolean hasAlertsEnabled(Player player) {
        return alerts.isEnabled(player);
    }

    @Override
    public void toggleAlerts(Player player, boolean silent) {
        alerts.toggle(player, silent);
    }

    @Override
    public boolean hasVerboseEnabled(Player player) {
        return verbose.isEnabled(player);
    }

    @Override
    public void toggleVerbose(Player player, boolean silent) {
        verbose.toggle(player, silent);
    }

    @Override
    public boolean hasBrandsEnabled(Player player) {
        return brands.isEnabled(player) && player.hasPermission("grim.brand");
    }

    @Override
    public void toggleBrands(Player player, boolean silent) {
        brands.toggle(player, silent);
    }

    public void handlePlayerQuit(Player player) {
        alerts.remove(player);
        verbose.remove(player);
        brands.remove(player);
    }

    @RequiredArgsConstructor
    public static class AlertToggle {
        @Getter
        private final Set<Player> enabled = new CopyOnWriteArraySet<>(new HashSet<>());
        private final Supplier<String> enableAlertStringSupplier;
        private final Supplier<String> disableAlertStringSupplier;

        public void toggle(Player player, boolean silent) {
            boolean newState = enabled.remove(player);
            if (!newState) {
                enabled.add(player);
            }

            if (!silent) {
                String alertString = newState ? enableAlertStringSupplier.get() : disableAlertStringSupplier.get();
                MessageUtil.sendMessage(player, MessageUtil.miniMessage(alertString));
            }
        }

        public boolean isEnabled(Player player) {
            return enabled.contains(player);
        }

        public void remove(Player player) {
            this.enabled.remove(player);
        }
    }
}
