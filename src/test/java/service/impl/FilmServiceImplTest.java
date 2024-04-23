package service.impl;


import entity.Film;
import entity.Genre;
import entity.dto.FilmDto;
import entity.dto.GenreDto;
import exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.FilmRepository;
import service.GenreService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilmServiceImplTest {
    @Mock
    private FilmRepository filmRepository;
    @Mock
    private GenreService genreService;
    @InjectMocks
    private FilmServiceImpl filmService;

    private final FilmDto filmDtoWithGenres = FilmDto.builder()
            .id(1L)
            .name("name")
            .description("description")
            .genres(new ArrayList<>((Arrays.asList(new GenreDto(1L, "genre"),
                    new GenreDto(2L, "genre2")))))
            .build();
    private final FilmDto filmDtoWithoutGenres = FilmDto.builder()
            .id(1L)
            .name("name")
            .description("description")
            .build();
    private final Film filmWithGenres = Film.builder()
            .id(1L)
            .name("name")
            .description("description")
            .genres(new ArrayList<>((Arrays.asList(new Genre(1L, "genre"),
                    new Genre(2L, "genre2")))))
            .build();
    private final Film filmWithoutGenres = Film.builder()
            .id(1L)
            .name("name")
            .description("description")
            .build();

    @Test
    void postFilm_withGenre_returnFilm() {
        when(filmRepository.postFilm(filmWithGenres)).thenReturn(filmWithGenres);

        FilmDto responseFilmDto = filmService.postFilm(filmDtoWithGenres);
        assertEquals(filmDtoWithGenres, responseFilmDto);
    }

    @Test
    void postFilm_withoutGenre_returnFilm() {
        when(filmRepository.postFilm(filmWithoutGenres)).thenReturn(filmWithoutGenres);

        FilmDto responseFilmDto = filmService.postFilm(filmDtoWithoutGenres);
        assertEquals(filmDtoWithoutGenres, responseFilmDto);
    }

    @Test
    void patchFilm_withoutGenre_returnFilm() {
        when(filmRepository.patchFilm(filmWithoutGenres)).thenReturn(filmWithoutGenres);

        FilmDto responseFilmDto = filmService.patchFilm(filmDtoWithoutGenres);
        assertEquals(filmDtoWithoutGenres, responseFilmDto);
    }

    @Test
    void patchFilm_withGenre_returnFilm() {
        when(filmRepository.patchFilm(filmWithGenres)).thenReturn(filmWithGenres);

        FilmDto responseFilmDto = filmService.patchFilm(filmDtoWithGenres);
        assertEquals(filmDtoWithGenres, responseFilmDto);
    }

    @Test
    void getFilmById_whenFilmExist_returnFilm() {
        when(filmRepository.getFilmById(Mockito.anyLong())).thenReturn(Optional.of(filmWithGenres));

        FilmDto responseFilmDto = filmService.getFilmById(1L);
        assertEquals(filmDtoWithGenres, responseFilmDto);
    }

    @Test
    void getFilmById_whenFilmAbsent_throwNotFoundException() {
        when(filmRepository.getFilmById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> filmService.getFilmById(1L));
    }

    @Test
    void removeFilm() {
        filmService.removeFilm(1L);
        verify(filmRepository).removeFilm(1L);
    }


}