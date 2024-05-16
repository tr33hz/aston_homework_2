package repository;

import entity.Actor;
import entity.Film;
import entity.Genre;
import exception.NotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static Database.DBInitializer.*;

public class FilmRepository {

    public Film postFilm(Film film) {
        boolean presentGenres = film.getGenres() != null;
        boolean presentActors = film.getActors() != null;

        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            String sql = "INSERT INTO films (name, description) VALUES( ?, ?);";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, film.getName());
                preparedStatement.setString(2, film.getDescription());

                preparedStatement.executeUpdate();

                ResultSet keys = preparedStatement.getGeneratedKeys();
                if (keys.next()) {
                    film.setId(keys.getLong("film_id"));
                }

            }

            if (presentGenres) saveGenreInFilm(film, connection);
            if (presentActors) saveActorInFilm(film, connection);


        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
        return film;
    }

    private void saveActorInFilm(Film film, Connection connection) throws SQLException {
        String sql = "INSERT INTO films_actors (film_id, actor_id) VALUES(?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (Actor actor : film.getActors()) {
                preparedStatement.setLong(1, film.getId());
                preparedStatement.setLong(2, actor.getActorId());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        }
    }

    public Film patchFilm(Film film) {
        boolean presentGenres = film.getGenres() != null;
        boolean presentActors = film.getActors() != null;
        Optional<Film> oldFilm = getFilmById(film.getId());

        if (oldFilm.isPresent()) {
            try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
                String sql = "update films set name = ?, description = ? where film_id = ?;";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, film.getName());
                    preparedStatement.setString(2, film.getDescription());
                    preparedStatement.setLong(3, film.getId());

                    preparedStatement.executeUpdate();
                }

                removeGenreFromFilm(oldFilm.get(), connection);
                removeActorFromFilm(oldFilm.get(), connection);
                if (presentGenres) saveGenreInFilm(film, connection);
                if (presentActors) saveActorInFilm(film, connection);


            } catch (RuntimeException | SQLException e) {
                throw new RuntimeException(e);
            }
            return film;
        } else {
            throw new NotFoundException("Film not found");
        }
    }

    private void removeActorFromFilm(Film oldFilm, Connection connection) throws SQLException {
        String sql = "DELETE FROM films_actors WHERE film_id = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, oldFilm.getId());
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeGenreFromFilm(Film oldFilm, Connection connection) {
        String sql = "DELETE FROM films_genre WHERE film_id = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, oldFilm.getId());
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveGenreInFilm(Film film, Connection connection) throws SQLException {
        String sql = "INSERT INTO films_genre (film_id, genre_id) VALUES(?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (Genre genre : film.getGenres()) {
                preparedStatement.setLong(1, film.getId());
                preparedStatement.setLong(2, genre.getId());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        }
    }


    public Optional<Film> getFilmById(long filmId) {
        Film film;

        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            String sql = "select * from films where film_id = ?;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, filmId);

                ResultSet rs = preparedStatement.executeQuery();
                rs.next();
                film = Film.builder()
                        .id(rs.getLong("film_id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .build();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            addGenres(film, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(film);
    }

    private void addGenres(Film film, Connection connection) {
        String sql = "select * from genre where genre_id in (select genre_id from films_genre where film_id = ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, film.getId());

            ResultSet rs = preparedStatement.executeQuery();
            List<Genre> genres = new ArrayList<>();
            while (rs.next()) {
                genres.add(Genre.builder()
                        .id(rs.getLong("genre_id"))
                        .name(rs.getString("name"))
                        .build());
            }

            film.setGenres(genres);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void removeFilm(long filmId) {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            String sql = "DELETE FROM films WHERE film_id = ?;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, filmId);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

