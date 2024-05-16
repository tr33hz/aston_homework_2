package entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActorDtoShort {

    Long actorId;
    String name;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorDtoShort that = (ActorDtoShort) o;
        return Objects.equals(actorId, that.actorId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actorId, name);
    }
}
