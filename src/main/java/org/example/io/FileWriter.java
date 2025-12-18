package org.example.io;

import java.io.BufferedWriter;
import java.io.IOException;

public class FileWriter {

    public static void writeError(String filePath, String errorMessage) {
        try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(filePath))) {
            bw.write(errorMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
