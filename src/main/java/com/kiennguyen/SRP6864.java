package com.kiennguyen;

import au.com.bytecode.opencsv.CSVReader;
import com.kiennguyen.model.ExternalUser;
import com.kiennguyen.model.SplusEntitlement;
import com.kiennguyen.model.SplusUser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SRP6864 {

    public static void main(String[] args) throws IOException {
        List<ExternalUser> externalUsers = loadExternalUser("/home/kiennguyen/Desktop/dn_data/dn_users_20151012_matched.csv");
        //List<SplusUser> splusUsers = loadSplusUser("/home/kiennguyen/Desktop/dn_data/dn_users_20151012_matched.csv");
        List<SplusEntitlement> splusEntitlements = loadSplusEntitlement("/home/kiennguyen/Desktop/dn_data/dn_entitlements_20151012.csv");

        FileWriter writer = new FileWriter("/home/kiennguyen/Desktop/dn_data/dn_users_20151012_matched1.csv");
        writer.append("Kundnummer, EMAIL, S+ userId, extra Id\n");

        boolean existed;
        int count = 0, countEnt = 0, countUser = 0;

        List<ExternalUser> results = new ArrayList<ExternalUser>();
        for (ExternalUser extUser : externalUsers) {
            if (count % 1000 == 0) {
                System.out.println(count);
            }

            count++;
            String userId = null;
            //String email = extUser.email.toLowerCase();

            for (SplusEntitlement entitlement : splusEntitlements) {
                if (entitlement.externalSubscriberId.equals(extUser.externalSubscriberId)) {
                    userId = entitlement.user_id;

                    if (extUser.userId == null || userId.equals(extUser.userId)) {
                        break;
                    }
                }
            }

            try {
                writer.append(extUser.externalSubscriberId).append(",");
                writer.append(extUser.email).append(",");

                if (userId != null) {
                    writer.append(userId);
                    //writer.append((extUser.userId != null && extUser.userId != "") ? extUser.userId : userId).append(",");
//                    if (!userId.equals(extUser.userId)) {
//                        writer.append(userId);
//                    } else {
//                        writer.append(extUser.userId);
//                    }
                } else {
                    //writer.append("null");
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

    public static List<SplusEntitlement> loadSplusEntitlement(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file), ',');
        String[] line;
        List<SplusEntitlement> entitlements = new ArrayList<SplusEntitlement>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            SplusEntitlement e = new SplusEntitlement();
            e.externalSubscriberId = line[0];
            e.user_id = line[1];
            entitlements.add(e);
        }

        return entitlements;
    }

    public static List<ExternalUser> loadExternalUser(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file), ',');
        String[] line;
        List<ExternalUser> users = new ArrayList<ExternalUser>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            ExternalUser u = new ExternalUser();
            u.externalSubscriberId = line[0];
            u.email = line[1];
            if (line[2] != null && line[2] != "") {
                u.userId = line[2];
            }
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
}
