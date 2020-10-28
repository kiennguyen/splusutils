package com.kiennguyen;

import au.com.bytecode.opencsv.CSVReader;
import com.kiennguyen.model.ExternalUser;
import com.kiennguyen.model.SplusUser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ComHemMergeBip {

    public static void main(String[] args) throws IOException {
        mergeEXPBIP();

    }

    public static void mergeEXPBIP() throws IOException {
        List<SplusUser> dn = loadSplusUser("/home/kiennguyen/Desktop/data/20201028/dn.csv");
        List<SplusUser> dne = loadSplusEntitlement("/home/kiennguyen/Desktop/data/20201028/dne.csv");

        FileWriter writerDn = new FileWriter("/home/kiennguyen/Desktop/data/20201028/dn_active.csv");

        writerDn.append("email\n");

        int count = 0;

        for (SplusUser et : dne ) {
            if (count % 1000 == 0) {
                System.out.println(count);
            }

            count++;

            for (SplusUser user : dn) {
                if (et.id.equals(user.id)) {
                    try {
                        writerDn.append(user.email);
                        writerDn.append('\n');
                    } catch (IOException e) {
                        System.out.println("IOException during writing CSV line: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Exception during writing CSV line: " + e.getMessage());
                    }

                    break;
                }
            }
        }

        writerDn.flush();
        writerDn.close();
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

    public static List<SplusUser> loadSplusEntitlement(String file) throws IOException {
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
}
