package ac.grim.grimac.checks;

import org.apache.commons.lang.StringUtils;

public enum CheckType {
    MOVEMENT,
    ROTATION,
    COMBAT,
    PACKETS,
    WORLD,
    OTHER;

    public String displayName() {
        return StringUtils.capitalize(this.name().toLowerCase());
    }

}
