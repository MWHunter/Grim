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
    
}
