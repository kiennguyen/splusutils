package com.kiennguyen.cleanupaccount;

import au.com.bytecode.opencsv.CSVReader;
import com.kiennguyen.cleanupaccount.utils.CleanupUtils;
import org.apache.commons.lang.WordUtils;
import se.bonnier.api.model.userservice.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public class CheckSPMatrixAccount {
    private static String directory = "/home/kiennguyen/Desktop";

    public static void main(String[] args) throws IOException {
        List<User> accounts = getValidAccounts(directory + File.separator + "out_invalid.csv");
        List<User> matrixAccounts = getMatrixAccounts(directory + File.separator + "TBL_PERSON_sd_prod.csv");
        Map<String, User> accountMap = new HashMap<String, User>();
        Map<String, User> matrixAccountMap = new HashMap<String, User>();

        for (User account : accounts) {
            if (accountMap.containsKey(account.email)) {
                System.out.println("Email " + account.email);
            }
            accountMap.put(account.email, account);
        }

        for (User account : matrixAccounts) {
            matrixAccountMap.put(account.email, account);
        }

        System.out.println(accountMap.size());

        List<User> refinedAccounts = new ArrayList<User>();
        List<User> notexistAccounts = new ArrayList<User>();
        for (String email : accountMap.keySet()) {
            if (matrixAccountMap.containsKey(email)) {
                User ac = accountMap.get(email);
                User matrixAc = matrixAccountMap.get(email);
                ac.firstName = matrixAc.firstName;
                ac.lastName = matrixAc.lastName;
                refinedAccounts.add(ac);
            } else {
                User ac1 = accountMap.get(email);
                notexistAccounts.add(ac1);
            }
        }

        CleanupUtils.writeOutput(refinedAccounts, directory + File.separator + "out_refine1.csv");
        CleanupUtils.writeOutput(notexistAccounts, directory + File.separator + "out_notexist1.csv");

    }

    public static List<User> getValidAccounts(String fileName) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(fileName));
        List<String[]> accounts = reader.readAll();
        List<User> accountList = new ArrayList<User>();
        for (String[] strings : accounts) {
            User account = new User();
            account.id = strings[0];
            account.email = strings[1].toLowerCase();
            account.created = Long.parseLong(strings[2]);
            accountList.add(account);
        }
        return accountList;
    }

    public static List<User> getMatrixAccounts(String fileName) throws IOException {
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(fileName), "ISO-8859-1"));
        List<String[]> accounts = reader.readAll();
        String[] line;
        List<User> accountList = new ArrayList<User>();
        for (String[] strings : accounts) {
            User account = new User();
            account.firstName = WordUtils.capitalize(strings[0].toLowerCase());
            account.lastName = WordUtils.capitalize(strings[1].toLowerCase());
            account.email = strings[2].toLowerCase();
            accountList.add(account);
        }
        return accountList;
    }

}
