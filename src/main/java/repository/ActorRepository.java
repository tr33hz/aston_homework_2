package repository;

import entity.Actor;
import entity.Film;
import entity.Genre;
import exception.NotFoundException;

import java.sql.*;
import java.util.Optional;

import static Database.DBInitializer.*;

public class ActorRepository {

    public Actor postActor(Actor actor) {
        boolean presentFilms = actor.getFilms() != null;

        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            String sql = "INSERT INTO actor (name) VALUES(?);";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, actor.getName());

                preparedStatement.executeUpdate();

                ResultSet keys = preparedStatement.getGeneratedKeys();
                if (keys.next()) {
                    actor.setActorId(keys.getLong("actor_id"));
                }
            }

            if (presentFilms) saveFilmToActor(actor, connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return actor;
    }

    private void saveFilmToActor(Actor actor, Connection connection) throws SQLException {
        String sql = "INSERT INTO films_actors (film_id, actor_id) VALUES(?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (Film film : actor.getFilms()) {
                preparedStatement.setLong(1, film.getId());
                preparedStatement.setLong(2, actor.getActorId());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        }
    }

    public Actor patchActor(Actor actor) {
        boolean presentFilms = actor.getFilms() != null;
        Optional<Actor> oldActor = getActorById(actor.getActorId());

        if (oldActor.isPresent()) {
            try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
                String sql = "UPDATE actor SET name = ? WHERE actor_id = ?;";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, actor.getName());
                    preparedStatement.setLong(2, actor.getActorId());

                    preparedStatement.executeUpdate();
                }

                removeFilmFromActor(oldActor.get(), connection);
                if (presentFilms) saveFilmToActor(actor, connection);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return actor;
        } else {
            throw new NotFoundException("Actor not found");
        }
    }

    private void removeFilmFromActor(Actor actor, Connection connection) throws SQLException {
        String sql = "DELETE FROM films_actors WHERE actor_id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, actor.getActorId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Actor> getActorById(long actorId) {
        Actor actor = null;

        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            String sql = "SELECT * FROM actor WHERE actor_id = ?;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, actorId);

                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    actor = new Actor();
                    actor.setActorId(rs.getLong("actor_id"));
                    actor.setName(rs.getString("name"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(actor);
    }

    public void removeActor(long actorId) {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            String sql = "DELETE FROM actor WHERE actor_id = ?;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, actorId);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
