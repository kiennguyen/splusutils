package com.kiennguyen.cleanupaccount.utils;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import se.bonnier.api.model.userservice.User;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public class CleanupUtils {
    public static List<User> getAccounts(String fileName) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(fileName));
        List<String[]> accounts = reader.readAll();
        List<User> accountList = new ArrayList<User>();
        for (String[] strings : accounts) {
            User account = new User();
            account.id = strings[0];
            account.email = strings[1];
//            account.createdStr = strings[4];
            accountList.add(account);
        }
        return accountList;
    }
    public static void writeOutput(List<User> accounts, String file) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(file));
        List<String[]> lines = new ArrayList<String[]>();
        for (User a : accounts) {
            String[] line = new String[]{a.id, a.email, a.firstName, a.lastName, a.created.toString()};
            lines.add(line);
        }
        writer.writeAll(lines);
        writer.flush();
    }
}
