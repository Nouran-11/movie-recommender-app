package org.example.service;



import org.example.model.Movie;
import org.example.model.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RecommendationWriter {

    public void writeToFile(
            Map<User, List<String>> recommendations,
            String outputPath
    ) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {

                for (Map.Entry<User, List<String>> entry : recommendations.entrySet()) {
                    User user = entry.getKey();
                    List<String> recs = entry.getValue();

                    bw.write(user.getName() + ", " + user.getId());
                    bw.newLine();

                    bw.write(String.join(", ", recs));
                    bw.newLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
