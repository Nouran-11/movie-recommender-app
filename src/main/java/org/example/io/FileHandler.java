package org.example.io;

import org.example.model.Movie;
import org.example.model.User;
import org.example.validator.MovieValidator;
import org.example.validator.UserValidator;

import java.io.*;
import java.util.*;

public class FileHandler {

    public static List<Movie> readMovies(String filePath) throws Exception {
        List<Movie> movies = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("ERROR: Movie Title  is wrong");
        }

        if (file.length() == 0) {
            throw new Exception("ERROR: Movie Title  is wrong");
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

                if (!MovieValidator.isValidTitle(title)) {
                    throw new Exception(
                            "ERROR: Movie Title {movie_title} is wrong".replace("{movie_title}", title));

                }

                String idValidation = MovieValidator.validateId(id, title);
                if (!idValidation.equals("Valid")) {

                    throw new Exception("ERROR: " + idValidation.replace("{movie_id}", id));
                }

                movies.add(new Movie(title, id, genre));
            }
        }
        if (movies.isEmpty()) {
            throw new Exception("ERROR: Movie Title  is wrong");
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

                if (!UserValidator.isValidName(name)) {
                    throw new Exception("ERROR: User Name " + name + " is wrong");
                }

                if (!UserValidator.isValidUserId(id)) {
                    throw new Exception("ERROR: User Id " + id + " is wrong");
                }

                if (existingIds.contains(id)) {

                    throw new Exception("ERROR: User Id " + id + " is wrong");
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
                            throw new Exception("ERROR: Movie Id " + midtrimmed + " does not exist");
                        }

                        // Validate ID using the title of the found movie
                        if (!MovieValidator.isValidMovieId(mid, found.getTitle())) {
                            throw new Exception("ERROR: Movie Id " + midtrimmed + " is wrong");
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
