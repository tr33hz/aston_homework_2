package Database;

import lombok.Getter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer implements ServletContextListener {
    private final String DROP_TABLES = "DROP TABLE IF EXISTS PUBLIC.FILMS CASCADE; " +
            "DROP TABLE IF EXISTS PUBLIC.GENRE CASCADE; " +
            "DROP TABLE IF EXISTS PUBLIC.films_GENRE CASCADE;" +
            "DROP TABLE IF EXISTS ACTOR CASCADE;" +
            "DROP TABLE IF EXISTS films_actors CASCADE";
    private final String CREATE_TABLE_FILM = "CREATE TABLE IF NOT EXISTS PUBLIC.FILMS (" +
            "FILM_ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
            "NAME varchar(100) NOT NULL, " +
            "DESCRIPTION varchar(500) NOT NULL);";
    private final String CREATE_TABLE_GENRE = "CREATE TABLE IF NOT EXISTS PUBLIC.GENRE (" +
            "GENRE_ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
            "NAME varchar(50) NOT NULL);";
    private final String CREATE_TABLE_FILMS_GENRE = "CREATE TABLE IF NOT EXISTS PUBLIC.films_GENRE (" +
            "FILM_ID BIGINT NOT NULL, " +
            "GENRE_ID BIGINT NOT null, " +
            "CONSTRAINT FRIENDS_LIST_PK PRIMARY KEY (film_ID,GENRE_ID), " +
            "CONSTRAINT films_ID_FK FOREIGN KEY (film_ID) REFERENCES PUBLIC.FILMS(film_ID) ON DELETE CASCADE, " +
            "CONSTRAINT GENRE_ID_FK FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC.GENRE(GENRE_ID) ON DELETE CASCADE);";

    private final String CREATE_TABLE_ACTOR = "CREATE TABLE IF NOT EXISTS ACTOR (" +
            "ACTOR_ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
            "NAME varchar(50) NOT NULL);";

    private final String CREATE_TABLE_FILMS_ACTORS = "CREATE TABLE IF NOT EXISTS films_actors (" +
            "FILM_ID BIGINT NOT NULL, " +
            "ACTOR_ID BIGINT NOT NULL, " +
            "CONSTRAINT FRIENDSHIP_LIST_PK PRIMARY KEY (film_ID,actor_ID), " +
            "CONSTRAINT films_ID_FK FOREIGN KEY (film_ID) REFERENCES PUBLIC.FILMS(film_ID) ON DELETE CASCADE, " +
            "CONSTRAINT actor_ID_FK FOREIGN KEY (actor_ID) REFERENCES ACTOR(ACTOR_ID) ON DELETE CASCADE);";

    @Getter
    private static final String url = "jdbc:postgresql://localhost:5432/aston";
    @Getter
    private static final String username = "trhz";
    @Getter
    private static final String password = "658311";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(DROP_TABLES);
            statement.executeUpdate(CREATE_TABLE_FILM);
            statement.executeUpdate(CREATE_TABLE_GENRE);
            statement.executeUpdate(CREATE_TABLE_FILMS_GENRE);
            statement.executeUpdate(CREATE_TABLE_ACTOR);
            statement.executeUpdate(CREATE_TABLE_FILMS_ACTORS);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
