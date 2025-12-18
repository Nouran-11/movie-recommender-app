package org.example;

import org.example.io.FileReader;
import org.example.io.FileWriter;
import org.example.model.Movie;
import org.example.model.User;
import org.example.service.RecommendationWriter;
import org.example.validator.RecommendationValidator;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Movie Recommender System!");

        System.out.print("Please enter the path to the movies file (press Enter to use default 'movies.txt'): ");
        String movieInput = scanner.nextLine().trim();
        String movieFile = movieInput.isEmpty() ? "movies.txt" : movieInput;

        System.out.print("Please enter the path to the users file (press Enter to use default 'users.txt'): ");
        String userInput = scanner.nextLine().trim();
        String userFile = userInput.isEmpty() ? "users.txt" : userInput;

        System.out.println("Using movie file: " + movieFile);
        System.out.println("Using user file: " + userFile);

        String outputFile = "recommendations.txt";

        try {

            List<Movie> movies = FileReader.readMovies(movieFile);
            List<User> users = FileReader.readUsers(userFile, movies);

            // Generate recommendations (logic only)
            RecommendationValidator validator = new RecommendationValidator();
            Map<User, List<String>> recommendations =
                    validator.generateRecommendations(users, movies);

            // Write recommendations to file
            RecommendationWriter writer = new RecommendationWriter();
            writer.writeToFile(recommendations, outputFile);

            System.out.println("Success! Recommendations written to " + outputFile);

        } catch (Exception e) {

            String errorMsg = e.getMessage();

            if (errorMsg != null) {
                FileWriter.writeError(outputFile, errorMsg);
                System.err.println("Validation Failed. detailed error written to " + outputFile);
            } else {
                e.printStackTrace();
            }
        }
    }
}