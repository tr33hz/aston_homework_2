package entity;

import lombok.*;

import java.util.List;
import java.util.Objects;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Actor {

    Long actorId;
    String name;
    List<Film> films;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(actorId, actor.actorId) && Objects.equals(name, actor.name) && Objects.equals(films, actor.films);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actorId, name, films);
    }
}
