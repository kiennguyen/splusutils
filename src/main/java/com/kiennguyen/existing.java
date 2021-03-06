package com.kiennguyen;

import au.com.bytecode.opencsv.CSVReader;
import com.kiennguyen.model.Account;
import com.kiennguyen.model.SplusUser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class existing {

    public static void main(String[] args) throws IOException {
        mergeNoBIP();

    }

    public static void mergeNoBIP() throws IOException {
        List<Account> bipAccounts = loadBipAccount("/home/kiennguyen/Desktop/data/20200423/out.csv");
        List<SplusUser> dnUsers = loadSplusUser("/home/kiennguyen/Desktop/data/20200423/splus_ids_2");

        FileWriter writer = new FileWriter("/home/kiennguyen/Desktop/data/20200423/dn_splus_check.csv");
        writer.append("id\n");

        boolean existed;
        int count = 0, countEnt = 0, countUser = 0;

        List<SplusUser> results = new ArrayList<SplusUser>();
        for (SplusUser user : dnUsers) {
            if (count % 1000 == 0) {
                System.out.println(count);
            }

            count++;
            boolean hasBip = false;

            for (Account bip : bipAccounts) {
                if (bip.id.equals(user.id)) {
                    hasBip = true;
                    break;
                }
            }

                try {
                    writer.append(user.id).append(",");
                    if (hasBip) {
                        writer.append("E");
                    } else {
                        writer.append("D");
                    }
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

    public static List<SplusUser> loadSplusUser(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file), ',');
        String[] line;
        List<SplusUser> users = new ArrayList<SplusUser>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            SplusUser u = new SplusUser();
            u.id = line[0];
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
            users.add(u);
        }

        return users;
    }
}
