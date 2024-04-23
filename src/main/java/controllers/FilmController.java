package controllers;

import repository.FilmRepository;

public class FilmController {
    final FilmRepository filmRepository;

    public FilmController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }
}
