package servlets;

import BeanManager.BeanManager;
import com.google.gson.Gson;
import entity.dto.GenreDto;
import exception.ValidateException;
import service.GenreService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class GenreServlet extends HttpServlet {
    private BeanManager beanManager;
    private GenreService genreService;
    private Gson gson;

    @Override
    public void init(ServletConfig config) {
        beanManager = new BeanManager();
        beanManager.init();
        genreService = beanManager.getGenreService();
        gson = beanManager.getGson();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GenreDto genreDto = readBodyToGenreDto(req);

        GenreDto responseGenre = genreService.postGenre(genreDto);

        writeBody(resp, responseGenre);
    }

    private GenreDto readBodyToGenreDto(HttpServletRequest req) throws IOException {
        try (InputStream inputStream = req.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonBody = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }
            return gson.fromJson(jsonBody.toString(), GenreDto.class);
        }
    }

    private void writeBody(HttpServletResponse resp, GenreDto responseGenre) {
        try (OutputStream outputStream = resp.getOutputStream()) {
            resp.setContentType("application/json");
            String responseJson = gson.toJson(responseGenre);
            outputStream.write(responseJson.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GenreDto genreDto = readBodyToGenreDto(req);

        GenreDto responseGenre = genreService.patchGenre(genreDto);

        writeBody(resp, responseGenre);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        long id = parseIdFromPath(req);
        genreService.removeGenre(id);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        long id = parseIdFromPath(req);

        GenreDto genreResponse = genreService.getGenreById(id);

        writeBody(resp, genreResponse);
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
}
