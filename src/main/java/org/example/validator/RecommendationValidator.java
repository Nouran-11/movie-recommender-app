package org.example.validator;

import org.example.model.Movie;
import org.example.model.User;

import java.util.*;

public class RecommendationValidator {
    public Map<User, List<String>> generateRecommendations(
            List<User> users,
            List<Movie> movies
    ) {
        Map<String, Movie> movieMap = new HashMap<>();
        for (Movie m : movies) {
            movieMap.put(m.getId(), m);
        }

        Map<User, List<String>> result = new LinkedHashMap<>();

        for (User user : users) {
            Set<String> likedGenres = new HashSet<>();
            Set<String> likedMovieIds = new HashSet<>(user.getLikedMovieIds());

            for (String likedId : likedMovieIds) {
                Movie movie = movieMap.get(likedId);
                if (movie != null) {
                    for (String g : movie.getGenre().split(",")) {
                        likedGenres.add(g.trim());
                    }
                }
            }

            Set<String> recommendedTitles = new LinkedHashSet<>();

            for (Movie movie : movies) {
                if (likedMovieIds.contains(movie.getId())) continue;

                for (String mg : movie.getGenre().split(",")) {
                    if (likedGenres.contains(mg.trim())) {
                        recommendedTitles.add(movie.getTitle());
                        break;
                    }
                }
            }

            result.put(user, new ArrayList<>(recommendedTitles));
        }

        return result;
    }
}
