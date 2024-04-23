package BeanManager;

import com.google.gson.Gson;
import lombok.Getter;
import repository.FilmRepository;
import repository.GenreRepository;
import service.FilmService;
import service.GenreService;
import service.impl.FilmServiceImpl;
import service.impl.GenreServiceImpl;

@Getter
public class BeanManager {
    private FilmService filmService;
    private GenreService genreService;
    private Gson gson;

    public void init() {
        GenreRepository genreRepository = createGenreRepository();
        genreService = createGenreService(genreRepository);

        FilmRepository filmRepository = createFilmRepository();
        filmService = createFilmService(filmRepository, genreService);
        gson = new Gson();
    }

    private FilmService createFilmService(FilmRepository filmRepository, GenreService genreService) {
        return new FilmServiceImpl(filmRepository, genreService);
    }

    private FilmRepository createFilmRepository() {
        return new FilmRepository();
    }

    private GenreRepository createGenreRepository() {
        return new GenreRepository();
    }

    private GenreService createGenreService(GenreRepository genreRepository) {
        return new GenreServiceImpl(genreRepository);
    }


}
