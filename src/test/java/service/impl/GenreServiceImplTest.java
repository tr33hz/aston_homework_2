package service.impl;

import entity.Genre;
import entity.dto.GenreDto;
import exception.NotFoundException;
import exception.ValidateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.GenreRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {
    @Mock
    private GenreRepository genreRepository;
    @InjectMocks
    private GenreServiceImpl genreService;
    private final Genre genre = Genre.builder()
            .id(1L)
            .name("genre")
            .build();
    private final GenreDto genreDto = GenreDto.builder()
            .id(1L)
            .name("genre")
            .build();

    @Test
    void postGenre_withoutName_trowsValidateException() {
        GenreDto genreDtoWithoutName = GenreDto.builder()
                .id(1L)
                .build();

        assertThrows(ValidateException.class, () -> genreService.patchGenre(genreDtoWithoutName));
    }

    @Test
    void postGenre_withName_returnGenre() {
        when(genreRepository.postGenre(genre)).thenReturn(genre);

        GenreDto responseGenreDto = genreService.postGenre(genreDto);
        assertEquals(genreDto, responseGenreDto);
    }

    @Test
    void getGenreById_whenGenreAbsent_throwsNotFoundException() {
        when(genreRepository.getGenreById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> genreService.getGenreById(1L));
    }

    @Test
    void getGenreById_whenGenreExist_returnGenre() {
        when(genreRepository.getGenreById(Mockito.anyLong())).thenReturn(Optional.of(genre));

        GenreDto responseGenreDro = genreService.getGenreById(1L);
        assertEquals(genreDto, responseGenreDro);
    }

    @Test
    void removeGenre() {
        genreService.removeGenre(1L);
        verify(genreRepository).removeGenre(1L);
    }

    @Test
    void patchGenre_returnGenre() {
        when(genreRepository.patchGenre(genre)).thenReturn(genre);

        GenreDto responseGenreDto = genreService.patchGenre(genreDto);
        assertEquals(genreDto, responseGenreDto);
    }

    @Test
    void checkExistGenre_whenGenreExist_ok() {
        when(genreRepository.checkExistGenreById(Mockito.anyLong())).thenReturn(true);

        genreService.checkExistGenre(new ArrayList<>(Arrays.asList(genreDto)));
        verify(genreRepository).checkExistGenreById(1L);
    }
}