package com.kiennguyen;

import au.com.bytecode.opencsv.CSVReader;
import com.kiennguyen.model.Account;
import com.kiennguyen.model.SplusUser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BipActive0 {

    public static void main(String[] args) throws IOException {
        mergeLoginTime();
    }

    public static void mergeLoginTime() throws IOException {
        List<Account> bipAccounts = loadBipAccount("/home/kiennguyen/Desktop/data/20190704/bip_active_0.csv");
        List<SplusUser> dnUsers = loadSplusUser("/home/kiennguyen/Desktop/data/20190704/brand_users_active_0.csv");
        List<SplusUser> loginTime = loadLoginTime("/home/kiennguyen/Desktop/data/20190704/brand_users_login_time.csv");

        FileWriter writer = new FileWriter("/home/kiennguyen/Desktop/data/20190704/bip_active0_login_time.csv");
        writer.append("id, email, loginTime\n");

        int count = 0;

        List<Account> results = new ArrayList<Account>();
        for (Account bip : bipAccounts ) {
            if (count % 100 == 0) {
                System.out.println(count);
            }

            count++;

            for (SplusUser user : dnUsers) {
                if (bip.email.equals(user.email)) {
                    for (SplusUser lt : loginTime) {
                        if (lt.id.equals(user.id)) {
                            Account bipResult = new Account();
                            bipResult.id = bip.id;
                            bipResult.email = bip.email;
                            bipResult.loginTime = lt.loginTime;
                            results.add(bipResult);
                            break;
                        }
                    }
                    break;
                }
            }
        }

        for (Account r : results) {
            try {
                writer.append(r.id).append(",");
                writer.append(r.email).append(",");
                writer.append(r.loginTime.toString());
                writer.append('\n');
            } catch (IOException e) {
                System.out.println("IOException during writing CSV line: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Exception during writing CSV line: " + e.getMessage());
            }
        }

        writer.flush();
        writer.close();
    }

    public static List<SplusUser> loadLoginTime(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file), ',');
        String[] line;
        List<SplusUser> users = new ArrayList<SplusUser>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            SplusUser u = new SplusUser();
            u.id = line[0];
            u.loginTime = Long.parseLong(line[1]);
            users.add(u);
        }

        return users;
    }

    public static List<SplusUser> loadSplusUser(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file), ',');
        String[] line;
        List<SplusUser> users = new ArrayList<SplusUser>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            SplusUser u = new SplusUser();
            u.id = line[0];
            u.email = line[1];
            users.add(u);
        }

        return users;
    }

    public static List<Account> loadBipAccount(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file), ',');
        String[] line;
        List<Account> users = new ArrayList<Account>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            Account u = new Account();
            u.id = line[0];
            u.email = line[1];
            users.add(u);
        }

        return users;
    }
}
