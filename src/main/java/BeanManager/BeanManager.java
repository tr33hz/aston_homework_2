package BeanManager;

import com.google.gson.Gson;
import lombok.Getter;
import repository.ActorRepository;
import repository.FilmRepository;
import repository.GenreRepository;
import service.ActorService;
import service.FilmService;
import service.GenreService;
import service.impl.ActorServiceImpl;
import service.impl.FilmServiceImpl;
import service.impl.GenreServiceImpl;

@Getter
public class BeanManager {
    private FilmService filmService;
    private GenreService genreService;
    private ActorService actorService;
    private Gson gson;

    public void init() {
        GenreRepository genreRepository = createGenreRepository();
        genreService = createGenreService(genreRepository);

        FilmRepository filmRepository = createFilmRepository();
        filmService = createFilmService(filmRepository, genreService);

        ActorRepository actorRepository = createActorRepository();
        actorService = createActorService(actorRepository);
        gson = new Gson();
    }

    private ActorService createActorService(ActorRepository actorRepository) {
        return new ActorServiceImpl(actorRepository);
    }

    private ActorRepository createActorRepository() {
        return new ActorRepository();
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
