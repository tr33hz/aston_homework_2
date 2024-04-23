package servlets;

import BeanManager.BeanManager;
import com.google.gson.Gson;
import entity.dto.FilmDto;
import exception.ValidateException;
import service.FilmService;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class FilmServlet extends HttpServlet {
    private BeanManager beanManager;
    private FilmService filmService;
    private Gson gson;

    @Override
    public void init(ServletConfig config) {
        beanManager = new BeanManager();
        beanManager.init();
        filmService = beanManager.getFilmService();
        gson = beanManager.getGson();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        FilmDto filmDto = readBodyToFilmDto(req);

        FilmDto responseFilm = filmService.patchFilm(filmDto);

        writeBody(resp, responseFilm);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        long id = parseIdFromPath(req);
        filmService.removeFilm(id);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        FilmDto filmDto = readBodyToFilmDto(req);

        FilmDto responseFilm = filmService.postFilm(filmDto);

        writeBody(resp, responseFilm);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        long id = parseIdFromPath(req);

        FilmDto filmResponse = filmService.getFilmById(id);

        writeBody(resp, filmResponse);
    }

    private long parseIdFromPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.length() > 1) {
            String[] pathParts = pathInfo.split("/");
            String lastPart = pathParts[pathParts.length - 1];

            try {
                return Long.parseLong(lastPart);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Cannot deduce digit from path");
            }

        } else {
            throw new ValidateException("Path does not contain numbers");
        }
    }

    private void writeBody(HttpServletResponse resp, FilmDto responseFilm) {
        try (OutputStream outputStream = resp.getOutputStream()) {
            resp.setContentType("application/json");
            String responseJson = gson.toJson(responseFilm);
            outputStream.write(responseJson.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private FilmDto readBodyToFilmDto(HttpServletRequest req) throws IOException {
        try (InputStream inputStream = req.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonBody = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }
            return gson.fromJson(jsonBody.toString(), FilmDto.class);
        }
    }
}