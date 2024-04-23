package service.impl;

import entity.Film;
import entity.dto.FilmDto;
import exception.NotFoundException;
import exception.ValidateException;
import mapper.FilmMapper;
import repository.FilmRepository;
import service.FilmService;
import service.GenreService;

import java.util.Optional;

public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final GenreService genreService;

    public FilmServiceImpl(FilmRepository filmRepository, GenreService genreService) {
        this.filmRepository = filmRepository;
        this.genreService = genreService;
    }

    @Override
    public FilmDto postFilm(FilmDto filmDto) {
        checkValid(filmDto);

        Film requestFilm = FilmMapper.toFilm(filmDto);
        Film responseFilm = filmRepository.postFilm(requestFilm);

        return FilmMapper.toDto(responseFilm);
    }

    @Override
    public FilmDto patchFilm(FilmDto filmDto) {
        checkValid(filmDto);

        Film requestFilm = FilmMapper.toFilm(filmDto);
        Film responseFilm = filmRepository.patchFilm(requestFilm);

        return FilmMapper.toDto(responseFilm);
    }

    @Override
    public FilmDto getFilmById(long filmId) {
        Optional<Film> filmOptional = filmRepository.getFilmById(filmId);
        if (filmOptional.isPresent()) {
            return FilmMapper.toDto(filmOptional.get());
        } else {
            throw new NotFoundException("Film not found");
        }
    }

    @Override
    public void removeFilm(long filmId) {
        filmRepository.removeFilm(filmId);
    }

    private void checkValid(FilmDto filmDto) {
        if (filmDto.getName() == null || filmDto.getName().isEmpty()) {
            throw new ValidateException("name cannot be empty");
        }
        if (filmDto.getDescription() == null || filmDto.getDescription().isEmpty()) {
            throw new ValidateException("description cannot be empty");
        }
        if (filmDto.getGenres() != null) {
            genreService.checkExistGenre(filmDto.getGenres());
        }
    }
}
