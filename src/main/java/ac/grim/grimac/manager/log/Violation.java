package ac.grim.grimac.manager.log;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class Violation {

    private final UUID playerUUID;
    private final String checkName;
    private final String verbose;
    private final String vl;
    private final Date createdAt;

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