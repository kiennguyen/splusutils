package com.kiennguyen;

import au.com.bytecode.opencsv.CSVReader;
import com.kiennguyen.model.ExternalUser;
import com.kiennguyen.model.SplusEntitlement;
import com.kiennguyen.model.SplusUser;
import se.bonnier.api.util.ObjectUtil;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SRP6464 {

    public static void main(String[] args) throws IOException {
        List<ExternalUser> externalUsers = loadExternalUser("/home/kiennguyen/Desktop/data/SRP-6464/alla_aktiva_print_20150622.csv");
        List<SplusUser> splusUsers = loadSplusUser("/home/kiennguyen/Desktop/data/SRP-6464/di_users_20150629.csv");
        List<SplusEntitlement> splusEntitlements = loadSplusEntitlement("/home/kiennguyen/Desktop/data/SRP-6464/di_entitlements_20150629_full.csv");

        FileWriter writer = new FileWriter("/home/kiennguyen/Desktop/data/SRP-6464/alla_aktiva_print_20150622_marked.csv");
        writer.append("TP_ID, emailaddress, state\n");


        boolean existed;
        int count = 0;
        int mail1 = 0, mail2 = 0;

        List<ExternalUser> results = new ArrayList<ExternalUser>();
        for (ExternalUser externalUser : externalUsers) {
            if (count % 1000 == 0) {
                System.out.println(count);
            }

            count++;
            if (!ObjectUtil.empty(externalUser.email)) {
                existed = false;
                for (SplusUser splusUser : splusUsers) {
                    if (splusUser.email.trim().equals(externalUser.email.trim())) {
                        existed = true;
                        break;
                    }
                }

                if (!existed) {
                    externalUser.marked = "Mail #2";
                    mail2++;
                } else {
                    boolean existedEntitlement = false;
                    for (SplusEntitlement splusEntitlement : splusEntitlements) {
                        if (splusEntitlement.externalSubscriberId.equals(externalUser.externalSubscriberId)) {
                            existedEntitlement = true;
                            break;
                        }
                    }

                    if (!existedEntitlement) {
                        externalUser.marked = "Mail #1";
                        mail1++;
                    }
                }
            }

            try {
                writer.append(externalUser.externalSubscriberId).append(";");
                if (externalUser.email != null) {
                    writer.append(externalUser.email).append(";");
                }

                if (externalUser.marked != null) {
                    writer.append(externalUser.marked);
                }
                writer.append('\n');
            } catch (IOException e) {
                System.out.println("IOException during writing CSV line: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Exception during writing CSV line: " + e.getMessage());
            }
        }

        System.out.println("Mail #1: " + mail1);
        System.out.println("Mail #2: " + mail2);

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
            u.email = line[1];
            users.add(u);
        }

        return users;
    }

    public static List<SplusEntitlement> loadSplusEntitlement(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file), ',');
        String[] line;
        List<SplusEntitlement> entitlements = new ArrayList<SplusEntitlement>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            SplusEntitlement e = new SplusEntitlement();
            e.user_id = line[0];
            e.externalSubscriberId = line[1];

            entitlements.add(e);
        }

        return entitlements;
    }

    public static List<ExternalUser> loadExternalUser(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file), ';');
        String[] line;
        List<ExternalUser> users = new ArrayList<ExternalUser>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            ExternalUser u = new ExternalUser();
            u.externalSubscriberId = line[0];
            if (!ObjectUtil.empty(line[1])) {
                u.email = line[1];
            }
            users.add(u);
        }

        return users;
    }
}
