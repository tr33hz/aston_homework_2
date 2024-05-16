package mapper;

import entity.Actor;
import entity.Film;
import entity.dto.ActorDto;
import entity.dto.FilmDto;

import java.util.List;
import java.util.stream.Collectors;

public class ActorMapper {
    public static ActorDto toDto(Actor actor) {
        List<Film> films = actor.getFilms();

        List<FilmDto> filmDtos = null;
        if (films != null) {
            filmDtos = films.stream()
                    .map(FilmMapper::toDto)
                    .collect(Collectors.toList());
        }

        return ActorDto.builder()
                .actorId(actor.getActorId())
                .name(actor.getName())
                .films(filmDtos)
                .build();
    }

    public static Actor toEntity(ActorDto actorDto) {
        List<FilmDto> filmDtos = actorDto.getFilms();

        List<Film> films = null;
        if (filmDtos != null) {
            films = filmDtos.stream()
                    .map(FilmMapper::toFilm)
                    .collect(Collectors.toList());
        }

        return Actor.builder()
                .actorId(actorDto.getActorId())
                .name(actorDto.getName())
                .films(films)
                .build();
    }
}