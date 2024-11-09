package ac.grim.grimac.manager.log;

import java.util.Date;
import java.util.UUID;

public class Violation {

    private final UUID playerUUID;
    private final String checkName;
    private final String verbose;
    private final String vl;
    private final Date createdAt;

    public Violation(UUID playerUUID, String checkName, String verbose, String vl, Date createdAt) {
        this.playerUUID = playerUUID;
        this.checkName = checkName;
        this.verbose = verbose;
        this.vl = vl;
        this.createdAt = createdAt;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getCheckName() {
        return checkName;
    }

    public String getVerbose() {
        return verbose;
    }

    public String getVl() {
        return vl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}