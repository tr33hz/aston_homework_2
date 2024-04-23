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
            "DROP TABLE IF EXISTS PUBLIC.films_GENRE CASCADE;";
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
    @Getter
    private static final String url = "jdbc:postgresql://localhost:5432/film";
    @Getter
    private static final String username = "postgres";
    @Getter
    private static final String password = "postgres";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(DROP_TABLES);
            statement.executeUpdate(CREATE_TABLE_FILM);
            statement.executeUpdate(CREATE_TABLE_GENRE);
            statement.executeUpdate(CREATE_TABLE_FILMS_GENRE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
