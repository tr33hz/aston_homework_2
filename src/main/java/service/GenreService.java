package service;

import entity.dto.GenreDto;

import java.util.List;

public interface GenreService {
    GenreDto postGenre(GenreDto genreDto);

    GenreDto getGenreById(long genreId);

    void removeGenre(long genreId);

    GenreDto patchGenre(GenreDto genreDto);

    void checkExistGenre(List<GenreDto> genres);
}
