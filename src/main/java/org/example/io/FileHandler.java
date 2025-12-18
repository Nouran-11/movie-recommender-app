package org.example.io;

import org.example.constant.ValidationMessages;
import org.example.model.Movie;
import org.example.model.User;
import org.example.validator.MovieValidator;
import org.example.validator.UserValidator;

import java.io.*;
import java.util.*;

public class FileHandler {

    public static List<Movie> readMovies(String filePath) throws Exception {
        List<Movie> movies = new ArrayList<>();
        Set<String> usedMovieNumbers = new HashSet<>();

        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception(String.format(ValidationMessages.ERROR_MOVIE_TITLE, ""));
        }

        if (file.length() == 0) {
            throw new Exception(String.format(ValidationMessages.ERROR_MOVIE_TITLE, ""));
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line1;
            while ((line1 = br.readLine()) != null) {
                String line2 = br.readLine();

                if (line2 == null)
                    break;

                String[] parts = line1.split(",");
                if (parts.length < 2)
                    continue;

                String title = parts[0].trim();
                String id = parts[1].trim();
                String genre = line2.trim();

                String titleValidation = MovieValidator.validateTitle(title);
                if (!titleValidation.equals(ValidationMessages.VALID)) {
                    throw new Exception(titleValidation);
                }

                String idValidation = MovieValidator.validateId(id, title);
                if (!idValidation.equals(ValidationMessages.VALID)) {
                    throw new Exception(idValidation);
                }


                String numericPart = id.substring(id.length() - 3);
                if (usedMovieNumbers.contains(numericPart)) {
                    throw new Exception(String.format(ValidationMessages.ERROR_MOVIE_ID_UNIQUE, id));
                }
                usedMovieNumbers.add(numericPart);

                movies.add(new Movie(title, id, genre));
            }
        }
        if (movies.isEmpty()) {
            throw new Exception(String.format(ValidationMessages.ERROR_MOVIE_TITLE, ""));
        }
        return movies;
    }

    public static List<User> readUsers(String filePath, List<Movie> movies) throws Exception {
        List<User> users = new ArrayList<>();
        Set<String> existingIds = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line1;
            while ((line1 = br.readLine()) != null) {
                String line2 = br.readLine();
                if (line2 == null)
                    break;

                String[] parts = line1.split(",");
                String name = parts[0];
                String nametrimmed = name.trim();
                String id = parts[1].trim();

                String nameValidation = UserValidator.validateName(name);
                if (!nameValidation.equals(ValidationMessages.VALID)) {
                    throw new Exception(nameValidation);
                }

                String idValidation = UserValidator.validateUserId(id);
                if (!idValidation.equals(ValidationMessages.VALID)) {
                    throw new Exception(idValidation);
                }

                if (existingIds.contains(id)) {
                    throw new Exception(String.format(ValidationMessages.ERROR_USER_ID, id));
                }
                existingIds.add(id);

                User user = new User(nametrimmed, id);

                if (!line2.isEmpty()) {
                    String[] likedIds = line2.split(",");
                    for (String mid : likedIds) {
                        String midtrimmed = mid.trim();


                        // Check if this movie exists
                        Movie found = movies.stream()
                                .filter(m -> m.getId().equals(midtrimmed))
                                .findFirst()
                                .orElse(null);

                        if (found == null) {
                            throw new Exception(String.format(ValidationMessages.ERROR_MOVIE_NOT_EXIST, midtrimmed));
                        }

                        // Validate ID using the title of the found movie
                        String midValidation = MovieValidator.validateMovieId(mid, found.getTitle());
                        if (!midValidation.equals(ValidationMessages.VALID)) {
                            throw new Exception(midValidation);
                        }

                        user.addLikedMovie(midtrimmed);

                    }
                }
                users.add(user);
            }
        }
        return users;
    }

    public static void writeError(String filePath, String errorMessage) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(errorMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
