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


/*
select id, email from userservice.users where brand_id = '4NjU0sOz4sufDT4yrzngB6' and deleted is null limit 1000000;

select user_id, external_subscriber_id from entitlementservice.entitlements where brand_id = '4NjU0sOz4sufDT4yrzngB6' and deleted is null and external_subscriber_id is not null group by external_subscriber_id limit 1000000;
 */
public class SDSEmailID {

    public static void main(String[] args) throws IOException {
        List<SplusUser> splusUsers = loadSplusUser("/home/kiennguyen/Desktop/SDS_entitlements/sds_user_20151007.csv");
        List<SplusEntitlement> splusEntitlements = loadSplusEntitlement("/home/kiennguyen/Desktop/SDS_entitlements/sds_entitlement_20151007.csv");

        FileWriter writer = new FileWriter("/home/kiennguyen/Desktop/SDS_entitlements/sds_user_email_subscriber_20151007.csv");
        writer.append("Service+ id, email, external subscriber id\n");

        boolean existed;
        int count = 0, countEnt = 0, countUser = 0;

        List<ExternalUser> results = new ArrayList<ExternalUser>();
        for (SplusUser user : splusUsers) {
            if (count % 1000 == 0) {
                System.out.println(count);
            }

            count++;
            for (SplusEntitlement entitlement : splusEntitlements) {
                if (user.id.equals(entitlement.user_id)) {
                    user.externalSubscriberId = entitlement.externalSubscriberId;
                    break;
                }
            }

            try {
                if (user.externalSubscriberId != null && user.externalSubscriberId != "") {
                    Long.parseLong(user.externalSubscriberId);

                    writer.append(user.id).append(",");
                    writer.append(user.email).append(",");
                    writer.append(user.externalSubscriberId);
                    writer.append('\n');
                }
            } catch (IOException e) {
                System.out.println("IOException during writing CSV line: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Exception during writing CSV line: " + e.getMessage());
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
            e.user_id = line[0];
            e.externalSubscriberId = line[1];
            entitlements.add(e);
        }

        return entitlements;
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
