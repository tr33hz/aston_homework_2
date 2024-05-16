package entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActorDto {

    Long actorId;
    String name;
    List<FilmDto> films;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorDto actorDto = (ActorDto) o;
        return Objects.equals(actorId, actorDto.actorId) && Objects.equals(name, actorDto.name) && Objects.equals(films, actorDto.films);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actorId, name, films);
    }
}
