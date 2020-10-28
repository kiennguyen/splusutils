package com.kiennguyen;

import au.com.bytecode.opencsv.CSVReader;
import com.kiennguyen.model.Account;
import com.kiennguyen.model.ExternalUser;
import com.kiennguyen.model.SplusUser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BipDNMerge {

    public static void main(String[] args) throws IOException {
        mergeEXPBIP();

    }

    public static void mergeEXPBIP() throws IOException {
        List<Account> bipAccounts = loadBipAccount("/home/kiennguyen/Desktop/data/20190502/exp_bip_account_20190506.csv");
        List<SplusUser> dnUsers = loadSplusUser("/home/kiennguyen/Desktop/data/20190502/DNDI_with_exp_bip_20190506.csv");

        FileWriter writer = new FileWriter("/home/kiennguyen/Desktop/data/20190502/exp_bip_without_dndi_20190506.csv");
        writer.append("id, email\n");

        boolean existed;
        int count = 0, countEnt = 0, countUser = 0;

        List<ExternalUser> results = new ArrayList<ExternalUser>();
        for (Account bip : bipAccounts ) {
            if (count % 1000 == 0) {
                System.out.println(count);
            }

            count++;
            boolean hasBrandUser = false;

            for (SplusUser user : dnUsers) {
                if (bip.email.equals(user.email)) {
                    hasBrandUser = true;
                    break;
                }
            }

            if (!hasBrandUser) {
                try {
                    writer.append(bip.id).append(",");
                    writer.append(bip.email);
                    writer.append('\n');
                } catch (IOException e) {
                    System.out.println("IOException during writing CSV line: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Exception during writing CSV line: " + e.getMessage());
                }
            }
        }

        writer.flush();
        writer.close();
    }

    public static void mergeAXDNBIP() throws IOException {
        List<Account> bipAccounts = loadBipAccount("/home/kiennguyen/Desktop/data/20190412/bip.csv");
        List<SplusUser> dnUsers = loadSplusUser("/home/kiennguyen/Desktop/data/20190412/dn_ax.csv");

        FileWriter writer = new FileWriter("/home/kiennguyen/Desktop/data/20190412/dn_ax_no_bip.csv");
        writer.append("id, email\n");

        boolean existed;
        int count = 0, countEnt = 0, countUser = 0;

        List<ExternalUser> results = new ArrayList<ExternalUser>();
        for (SplusUser user : dnUsers) {
            if (count % 1000 == 0) {
                System.out.println(count);
            }

            count++;
            boolean hasBip = false;

            for (Account bip : bipAccounts) {
                if (bip.email.equals(user.email)) {
                    hasBip = true;
                    break;
                }
            }

            if (!hasBip) {
                try {
                    writer.append(user.id).append(",");
                    writer.append(user.email).append(",");
                    writer.append('\n');
                } catch (IOException e) {
                    System.out.println("IOException during writing CSV line: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Exception during writing CSV line: " + e.getMessage());
                }
            }
        }

        writer.flush();
        writer.close();
    }

    public static void mergeAXDN() throws IOException {
        List<SplusUser> dnUsers = loadSplusUser("/home/kiennguyen/Desktop/data/20190412/dn.csv");
        List<ExternalUser> externalUsers = loadExternalUser("/home/kiennguyen/Desktop/data/20190412/ax_customers.csv");

        FileWriter writer = new FileWriter("/home/kiennguyen/Desktop/data/20190412/dn_ax.csv");
        writer.append("id, email\n");

        boolean existed;
        int count = 0, countEnt = 0, countUser = 0;

        List<ExternalUser> results = new ArrayList<ExternalUser>();
        for (ExternalUser extUser : externalUsers) {
            if (count % 1000 == 0) {
                System.out.println(count);
            }

            count++;
            String email = null;
            //String email = extUser.email.toLowerCase();

            for (SplusUser user : dnUsers) {
                if (user.id.equals(extUser.userId)) {
                    email = user.email;
                    break;
                }
            }

            try {
                writer.append(extUser.userId).append(",");
                writer.append(email);
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

    public static List<ExternalUser> loadExternalUser(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file), ',');
        String[] line;
        List<ExternalUser> users = new ArrayList<ExternalUser>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            ExternalUser u = new ExternalUser();
            u.userId = line[0];
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
