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
public class FilmDto {
    Long id;
    String name;
    String description;
    List<GenreDto> genres;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmDto filmDto = (FilmDto) o;
        return Objects.equals(id, filmDto.id) && Objects.equals(name, filmDto.name) && Objects.equals(description, filmDto.description) && Objects.equals(genres, filmDto.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, genres);
    }
}
