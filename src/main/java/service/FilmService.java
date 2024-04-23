package service;

import entity.dto.FilmDto;

public interface FilmService {
    FilmDto postFilm(FilmDto filmDto);

    FilmDto getFilmById(long filmId);

    void removeFilm(long filmId);

    FilmDto patchFilm(FilmDto filmDto);
}
