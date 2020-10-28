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


public class ComHemMerge {

    public static void main(String[] args) throws IOException {
        mergeEXPBIP();

    }

    public static void mergeEXPBIP() throws IOException {
        List<SplusUser> DI = loadSplusUser("/home/kiennguyen/Desktop/data/20201020/comhem_DI.csv");
        List<SplusUser> DN = loadSplusUser("/home/kiennguyen/Desktop/data/20201020/comhem_DN.csv");

        FileWriter writerDn = new FileWriter("/home/kiennguyen/Desktop/data/20201020/dn.csv");
        FileWriter writerDi = new FileWriter("/home/kiennguyen/Desktop/data/20201020/di.csv");
        FileWriter writerDnDi = new FileWriter("/home/kiennguyen/Desktop/data/20201020/dndi.csv");


        writerDn.append("email\n");
        writerDi.append("email\n");
        writerDnDi.append("email\n");

        boolean existed;
        int count = 0, countEnt = 0, countUser = 0;

        List<ExternalUser> results = new ArrayList<ExternalUser>();
        for (SplusUser bip : DI ) {
            if (count % 1000 == 0) {
                System.out.println(count);
            }

            count++;
            boolean hasDN = false;

            for (SplusUser user : DN) {
                if (bip.email.equals(user.email)) {
                    hasDN = true;
                    break;
                }
            }

            if (hasDN) {
                try {
                    writerDnDi.append(bip.email);
                    writerDnDi.append('\n');
                } catch (IOException e) {
                    System.out.println("IOException during writing CSV line: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Exception during writing CSV line: " + e.getMessage());
                }
            } else {
                try {
                    writerDi.append(bip.email);
                    writerDi.append('\n');
                } catch (IOException e) {
                    System.out.println("IOException during writing CSV line: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Exception during writing CSV line: " + e.getMessage());
                }
            }
        }

        for (SplusUser bip : DN ) {
            if (count % 1000 == 0) {
                System.out.println(count);
            }

            count++;
            boolean hasDN = false;

            for (SplusUser user : DI) {
                if (bip.email.equals(user.email)) {
                    hasDN = true;
                    break;
                }
            }

            if (hasDN) {
            } else {
                try {
                    writerDn.append(bip.email);
                    writerDn.append('\n');
                } catch (IOException e) {
                    System.out.println("IOException during writing CSV line: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Exception during writing CSV line: " + e.getMessage());
                }
            }
        }

        writerDn.flush();
        writerDn.close();

        writerDi.flush();
        writerDi.close();

        writerDnDi.flush();
        writerDnDi.close();
    }

    public static List<SplusUser> loadSplusUser(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file), ',');
        String[] line;
        List<SplusUser> users = new ArrayList<SplusUser>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            SplusUser u = new SplusUser();
            u.email = line[0];
            users.add(u);
        }

        return users;
    }
}
