package ac.grim.grimac.utils.data;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TrackerData {
    // Using wrapper classes for numbers so the compiler shuts up about using annotations on primitives
    @NonNull
    Double x, y, z;
    @NonNull
    Float xRot, yRot;
    @NonNull
    EntityType entityType;
    @NonNull
    Integer lastTransactionHung;
    int legacyPointEightMountedUpon;
}
