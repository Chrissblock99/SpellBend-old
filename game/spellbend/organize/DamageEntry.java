package game.spellbend.organize;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class DamageEntry {
    private final UUID playerUUID;
    private double damage;
}
