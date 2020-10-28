package com.kiennguyen.example;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public class CheckDiIpadApp1 {
    public static void main(String[] args) throws IOException {
        Set<String> searchedUsers = readSplunkSearch("/home/kiennguyen/Desktop/access_di_ipad_users.csv");
        Set<String> davidUsers = readSplunkSearch("/home/kiennguyen/Desktop/david_users.csv");
        Set<String> result = new HashSet<String>();
        Set<String> notFound = new HashSet<String>();

        for (String email : searchedUsers) {
            if (!davidUsers.contains(email)) {
                result.add(email);
            }
        }

        for (String email : davidUsers) {
            if (!searchedUsers.contains(email)) {
                notFound.add(email);
            }
        }


        writeFile(result, "/home/kiennguyen/Desktop/result1.csv");
        writeFile(notFound, "/home/kiennguyen/Desktop/notfound1.csv");
    }

    public static void writeFile(Set<String> result, String path) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(path));
        for (String email : result) {
            writer.writeNext(new String[] {email});
        }
        writer.flush();
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

    public static Set<String> readDavidSearch(String path) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(path));
        Set<String> users = new HashSet<String>();
        String[] line;
        while ((line = reader.readNext()) != null) {
            users.add(line[0]);
        }

        return users;
    }
}
