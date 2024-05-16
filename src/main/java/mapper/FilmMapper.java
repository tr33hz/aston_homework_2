package mapper;

import entity.Actor;
import entity.Film;
import entity.Genre;
import entity.dto.ActorDto;
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

        List<ActorDto> actors = null;
        if (film.getActors() != null) {
            actors = film.getActors().stream()
                    .map(ActorMapper::toDto)
                    .collect(Collectors.toList());
        }

        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .genres(genres)
                .actors(actors)
                .build();
    }

    public static Film toFilm(FilmDto filmDto) {
        List<Genre> genres = null;
        if (filmDto.getGenres() != null) {
            genres = filmDto.getGenres().stream()
                    .map(GenreMapper::toEntity)
                    .collect(Collectors.toList());
        }

        List<Actor> actors = null;
        if (filmDto.getActors() != null) {
            actors = filmDto.getActors().stream()
                    .map(ActorMapper::toEntity)
                    .collect(Collectors.toList());
        }

        return Film.builder()
                .id(filmDto.getId())
                .name(filmDto.getName())
                .description(filmDto.getDescription())
                .genres(genres)
                .actors(actors)
                .build();
    }
}