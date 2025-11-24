package org.example.service;



import org.example.model.Movie;
import org.example.model.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Recommendation {

    public void generateRecommendations(List<User> users, List<Movie> movies, String outputPath) {
        Map<String, Movie> movieMap = new HashMap<>();
        for (Movie m : movies) {
            movieMap.put(m.getId(), m);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {

            for (User user : users) {
                Set<String> likedGenres = new HashSet<>();
                Set<String> likedMovieIds = new HashSet<>(user.getLikedMovieIds());

                for (String likedId : likedMovieIds) {
                    if (movieMap.containsKey(likedId)) {
                        String[] genres = movieMap.get(likedId).getGenre().split(",");
                        for (String g : genres) {
                            likedGenres.add(g.trim());
                        }
                    }
                }

                Set<String> recommendedTitles = new LinkedHashSet<>();

                for (Movie movie : movies) {
                    if (likedMovieIds.contains(movie.getId())) continue;

                    String[] movieGenres = movie.getGenre().split(",");
                    for (String mg : movieGenres) {
                        if (likedGenres.contains(mg.trim())) {
                            recommendedTitles.add(movie.getTitle());
                            break;
                        }
                    }
                }


                bw.write(user.getName() + ", " + user.getId());
                bw.newLine();

                String recommendations = String.join(", ", recommendedTitles);
                bw.write(recommendations);
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
