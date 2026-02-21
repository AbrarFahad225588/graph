package com.example.graph.auth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileDatabase {

    private static final String FILE_PATH = "users.json";
    private static final Gson gson = new Gson();

    public static List<User> loadUsers() {
        File file = new File(FILE_PATH);

        // 1. If the file doesn't exist or is empty (0 bytes), return a new list
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<User>>(){}.getType();
            List<User> users = gson.fromJson(reader, listType);

            // 2. Extra safety: if the file had text but Gson still returns null
            return users != null ? users : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Could not read database file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveUsers(List<User> users) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
