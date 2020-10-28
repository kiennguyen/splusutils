package com.kiennguyen.example;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public class CheckDiIpadApp {
    public static void main(String[] args) throws IOException {
        Map<String, String> users = readUsersFromDB("/home/kiennguyen/working/niteco/data/di_user_20140513.csv");
        Set<String> searchedUsers = readSplunkSearch("/home/kiennguyen/Downloads/di_access.csv");
        Set<String> result = new HashSet<String>();
        Set<String> notFound = new HashSet<String>();
        for (String id : searchedUsers) {
            if (users.containsKey(id)) {
                result.add(users.get(id));
            } else {
                notFound.add(users.get(id));
            }
        }
        writeFile(result, "/home/kiennguyen/Desktop/result.csv");
        writeFile(notFound, "/home/kiennguyen/Desktop/notfound.csv");
    }

    public static void writeFile(Set<String> result, String path) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(path));
        for (String email : result) {
            writer.writeNext(new String[] {email});
        }
        writer.flush();
    }

    public static Map<String, String> readUsersFromDB(String path) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(path));
        Map<String, String> users = new HashMap<String, String>();
        String[] line;
        while ((line = reader.readNext()) != null) {
            users.put(line[0], line[1]);
        }

        return users;
    }

    public static Set<String> readSplunkSearch(String path) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(path));
        Set<String> users = new HashSet<String>();
        String[] line;
        while ((line = reader.readNext()) != null) {
            users.add(line[0]);
        }

        return users;
    }
}
