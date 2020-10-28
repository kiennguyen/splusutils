package com.kiennguyen;

import au.com.bytecode.opencsv.CSVReader;
import com.kiennguyen.model.ExternalUser;
import com.kiennguyen.model.SplusEntitlement;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class sdsmigration {

    public static void main(String[] args) throws IOException {
        List<ExternalUser> externalUsers = loadExternalUser("/home/kiennguyen/Desktop/SDS_entitlements/data/Splus_still_wrong_151007.csv");
        //List<SplusEntitlement> splusEntitlements = loadSplusEntitlement("/home/kiennguyen/Desktop/SDS_entitlements/data/not_found.csv");
        List<SplusEntitlement> splusEntitlements = loadSplusEntitlement("/home/kiennguyen/Desktop/SDS_entitlements/data/sds_still_wrong_20151007.csv");

        FileWriter writer = new FileWriter("/home/kiennguyen/Desktop/SDS_entitlements/report/sds_still_wrong_entitlements_matched_20151007.csv");
        writer.append("id, external_subscriber_id, external_subscription_id, valid_from, valid_to, product_id, found, should_update, new_cusno\n");
        //writer.append("id, external_subscriber_id, external_subscription_id, valid_from, valid_to, product_id, real_kayak_cusno\n");

        boolean existed;
        int count = 0;

        List<ExternalUser> results = new ArrayList<ExternalUser>();
        for (SplusEntitlement entitlement : splusEntitlements) {
            if (count % 1000 == 0) {
                System.out.println(count);
            }

            count++;
            boolean found = false;
            boolean shouldUpdate = false;
            String newCusno = "";
            for (ExternalUser user : externalUsers) {
                if (user.externalSubscriberId.equals(entitlement.externalSubscriberId)) {
                    found = true;

                    if (!user.kayakCusno.equals(entitlement.externalSubscriberId)) {
                        shouldUpdate = true;
                        newCusno = user.kayakCusno;
                        break;
                    }
                }

                if (user.kayakCusno.equals(entitlement.externalSubscriberId)) {
                    found = true;
                    break;
                }
            }

            try {
                if (found) {
                    writer.append(entitlement.id).append(",");
                    writer.append(entitlement.externalSubscriberId).append(",");
                    writer.append(entitlement.externalSubscriptionId).append(",");
                    writer.append(entitlement.validFrom).append(",");
                    writer.append(entitlement.validTo).append(",");
                    writer.append(entitlement.productId).append(",");
                    writer.append(found ? "1" : "0").append(",");
                    writer.append(shouldUpdate ? "1" : "0").append(",");
                    if (newCusno != null) {
                        writer.append(newCusno);
                    }
                    writer.append('\n');
                }
            } catch (IOException e) {
                System.out.println("IOException during writing CSV line: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Exception during writing CSV line: " + e.getMessage());
            }
        }

        writer.flush();
        writer.close();

    }

    public static List<SplusEntitlement> loadSplusEntitlement1(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file), ',');
        String[] line;
        List<SplusEntitlement> entitlements = new ArrayList<SplusEntitlement>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            SplusEntitlement e = new SplusEntitlement();
            e.id = line[0];
            e.externalSubscriberId = line[1];
            e.externalSubscriptionId = line[2];
            e.validFrom = line[3];
            e.validTo = line[4];
            e.productId = line[5];
            e.found = line[6];
            e.shouldUpdate = line[7];
            e.newCusno = line[8];

            entitlements.add(e);
        }

        return entitlements;
    }

    public static List<SplusEntitlement> loadSplusEntitlement(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file), ',');
        String[] line;
        List<SplusEntitlement> entitlements = new ArrayList<SplusEntitlement>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            SplusEntitlement e = new SplusEntitlement();
            e.id = line[0];
            e.externalSubscriberId = line[1];
            e.externalSubscriptionId = line[2];
            e.validFrom = line[3];
            e.validTo = line[4];
            e.productId = line[5];

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
            u.kayakCusno = line[1];
            users.add(u);
        }

        return users;
    }
}
