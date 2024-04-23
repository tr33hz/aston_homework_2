package mapper;

import entity.Film;
import entity.Genre;
import entity.dto.FilmDto;
import entity.dto.GenreDto;

import java.util.List;
import java.util.stream.Collectors;

public class FilmMapper {

    public static FilmDto toDto(Film film) {
        List<GenreDto> genres = null;
        if (film.getGenres() != null) {
            genres = film.getGenres().stream()
                    .map(GenreMapper::toDto)
                    .collect(Collectors.toList());
        }

        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .genres(genres)
                .build();
    }

    public static Film toFilm(FilmDto filmDto) {
        List<Genre> genres = null;
        if (filmDto.getGenres() != null) {
            genres = filmDto.getGenres().stream()
                    .map(GenreMapper::toEntity)
                    .collect(Collectors.toList());
        }

        return Film.builder()
                .id(filmDto.getId())
                .name(filmDto.getName())
                .description(filmDto.getDescription())
                .genres(genres)
                .build();
    }
}
