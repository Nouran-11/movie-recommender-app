package org.example.io;



import org.example.model.Movie;
import org.example.model.User;

import java.io.*;
import java.util.*;

public class FileHandler {

    public static List<Movie> readMovies(String filePath) throws Exception {
        List<Movie> movies = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line1;
            while ((line1 = br.readLine()) != null) {
                String line2 = br.readLine();

                if (line2 == null) break;

                String[] parts = line1.split(",");
                if (parts.length < 2) continue;

                String title = parts[0].trim();
                String id = parts[1].trim();
                String genre = line2.trim();



                if (!Movie.isValidTitle(title)) {
                    throw new Exception("ERROR: Movie Title " + title + " is wrong");
                }

                String idValidation = Movie.validateId(id, title);
                if (!idValidation.equals("Valid")) {

                    throw new Exception("ERROR: " + idValidation.replace("{movie_id}", id));
                }

                movies.add(new Movie(title, id, genre));
            }
        }
        return movies;
    }

    public static List<User> readUsers(String filePath) throws Exception {
        List<User> users = new ArrayList<>();
        Set<String> existingIds = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line1;
            while ((line1 = br.readLine()) != null) {
                String line2 = br.readLine();
                if (line2 == null) break;

                String[] parts = line1.split(",");
                String name = parts[0];
                String id = parts[1].trim();

                if (!User.isValidName(name)) {
                    throw new Exception("ERROR: User Name " + name + " is wrong");
                }

                if (!User.isValidUserId(id)) {
                    throw new Exception("ERROR: User Id " + id + " is wrong");
                }

                if (existingIds.contains(id)) {

                    throw new Exception("ERROR: User Id " + id + " is wrong");
                }
                existingIds.add(id);

                User user = new User(name, id);

                if (!line2.isEmpty()) {
                    String[] likedIds = line2.split(",");
                    for (String mid : likedIds) {
                        user.addLikedMovie(mid.trim());
                    }
                }
                users.add(user);
            }
        }
        return users;
    }

    public static void writeError(String filePath, String errorMessage) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("Error\n");
            bw.write(errorMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeRecommendations(
            List<User> users,
            List<Movie> movies,
            List<String> errors,
            String outputPath ) {

        outputPath = "recommendations.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {

            if (!errors.isEmpty()) {
                writer.write(errors.get(0));  // ONLY first error
                return;                      // STOP processing
            }

            Map<String, Movie> movieMap = new HashMap<>();
            for (Movie m : movies) {
                movieMap.put(m.getId(), m);
            }

            for (User user : users) {

                // First line: user's name and id
                writer.write(user.getName() + "," + user.getId());
                writer.newLine();

                List<String> likedIds = user.getLikedMovieIds();

                // Collect genres the user likes
                Set<String> likedGenres = new HashSet<>();
                for (String movieId : likedIds) {
                    Movie movie = movieMap.get(movieId);
                    if (movie != null) {
                        likedGenres.add(movie.getGenre());
                    }
                }

                // Recommend movies from same genres
                Set<String> recommendedTitles = new LinkedHashSet<>();
                for (Movie movie : movies) {
                    if (!likedIds.contains(movie.getId()) &&
                            likedGenres.contains(movie.getGenre())) {

                        recommendedTitles.add(movie.getTitle());
                    }
                }

                // Write recommendations line
                if (recommendedTitles.isEmpty()) {
                    writer.write(""); // empty line, no recommendations
                } else {
                    writer.write(String.join(",", recommendedTitles));
                }

                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
