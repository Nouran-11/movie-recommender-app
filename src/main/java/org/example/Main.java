package org.example;

import org.example.io.FileHandler;
import org.example.model.Movie;
import org.example.model.User;
import org.example.service.Recommendation;

import java.util.List;

public class Main {

    // ================ Load and Validate ================== //
    public static void load_and_validate(String movieFile, String userFile){
        String outputFile = "recommendations.txt";

        try {
            List<Movie> movies = FileHandler.readMovies(movieFile);
            List<User> users = FileHandler.readUsers(userFile, movies);

            Recommendation engine = new Recommendation();
            engine.generateRecommendations(users, movies, outputFile);

            System.out.println("Success! Recommendations generated.");

        } catch (Exception e) {


            String errorMsg = e.getMessage();

            if (errorMsg != null) {
                FileHandler.writeError(outputFile, errorMsg);
                System.err.println("Validation Failed. detailed error written to " + outputFile);
            } else {
                e.printStackTrace();
            }
        }
    }

    // =========== Main where user inputs the files =============== //
    public static void main(String[] args) {

        load_and_validate("movies.txt", "users.txt");
    }
    }

