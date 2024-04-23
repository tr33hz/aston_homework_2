package service.impl;

import entity.Genre;
import entity.dto.GenreDto;
import exception.NotFoundException;
import exception.ValidateException;
import mapper.GenreMapper;
import repository.GenreRepository;
import service.GenreService;

import java.util.List;
import java.util.Optional;

public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;


    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public GenreDto postGenre(GenreDto genreDto) {
        checkValid(genreDto);

        Genre requestGenre = GenreMapper.toEntity(genreDto);
        Genre responseGenre = genreRepository.postGenre(requestGenre);

        return GenreMapper.toDto(responseGenre);
    }

    @Override
    public GenreDto getGenreById(long genreId) {
        Optional<Genre> genreOptional = genreRepository.getGenreById(genreId);
        if (genreOptional.isPresent()) {
            return GenreMapper.toDto(genreOptional.get());
        } else {
            throw new NotFoundException("Genre not found");
        }
    }

    @Override
    public void removeGenre(long genreId) {
        genreRepository.removeGenre(genreId);
    }

    @Override
    public GenreDto patchGenre(GenreDto genreDto) {
        checkValid(genreDto);

        Genre requestGenre = GenreMapper.toEntity(genreDto);
        Genre responseGenre = genreRepository.patchGenre(requestGenre);

        return GenreMapper.toDto(responseGenre);
    }

    @Override
    public void checkExistGenre(List<GenreDto> genres) {
        for (GenreDto genre : genres) {
            if (!genreRepository.checkExistGenreById(genre.getId())) {
                throw new ValidateException("genre with this identifier does not exist");
            }
        }
    }

    private void checkValid(GenreDto genreDto) {
        if (genreDto.getName() == null || genreDto.getName().isEmpty()) {
            throw new ValidateException("name cannot be empty");
        }
    }

}
