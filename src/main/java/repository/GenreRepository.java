package repository;

import entity.Genre;
import exception.NotFoundException;

import java.sql.*;
import java.util.Optional;

import static Database.DBInitializer.*;

public class GenreRepository {
    public Genre postGenre(Genre genre) {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            String sql = "INSERT INTO genre (name) VALUES (?);";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, genre.getName());

                preparedStatement.executeUpdate();

                ResultSet keys = preparedStatement.getGeneratedKeys();
                if (keys.next()) {
                    genre.setId(keys.getLong("genre_id"));
                }
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
        return genre;
    }

    public Optional<Genre> getGenreById(long genreId) {
        Genre genre;

        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            String sql = "select * from genre where genre_id = ?;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, genreId);

                ResultSet rs = preparedStatement.executeQuery();
                rs.next();
                genre = Genre.builder()
                        .id(rs.getLong("genre_id"))
                        .name(rs.getString("name"))
                        .build();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(genre);
    }

    public void removeGenre(long genreId) {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            String sql = "DELETE FROM genre WHERE genre_id = ?;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, genreId);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Genre patchGenre(Genre genre) {
        Optional<Genre> oldGenre = getGenreById(genre.getId());

        if (oldGenre.isPresent()) {
            try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
                String sql = "update genre set name = ? where genre_id = ?;";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, genre.getName());
                    preparedStatement.setLong(2, genre.getId());

                    preparedStatement.executeUpdate();
                }

            } catch (RuntimeException | SQLException e) {
                throw new RuntimeException(e);
            }
            return genre;
        } else {
            throw new NotFoundException("Genre not found");
        }
    }

    public boolean checkExistGenreById(long id) {
        return getGenreById(id).isPresent();
    }
}
