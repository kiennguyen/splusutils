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
select id, email from userservice.users where brand_id = '5DuzcZz0j8u0zArSNzZgHO' and deleted is null limit 10000000;

select external_subscriber_id, user_id from entitlementservice.entitlements where brand_id = '5DuzcZz0j8u0zArSNzZgHO' and deleted is null and external_subscriber_id is not null limit 10000000;
 */
public class SRP6838_6896 {

    public static void main(String[] args) throws IOException {
        List<ExternalUser> externalUsers = loadExternalUser("/home/kiennguyen/Desktop/DI_data/alla_aktiva_kunder_151014-2.csv");
        List<SplusUser> splusUsers = loadSplusUser("/home/kiennguyen/Desktop/DI_data/di_users_20151014.csv");
        List<SplusEntitlement> splusEntitlements = loadSplusEntitlement("/home/kiennguyen/Desktop/DI_data/di_entitlements_20151014.csv");

        FileWriter writer = new FileWriter("/home/kiennguyen/Desktop/DI_data/alla_aktiva_kunder_151014-2_matched.csv");
        writer.append("KundNr, E-POST, S+ email, S+ userId\n");

        boolean existed;
        int count = 0;

        List<ExternalUser> results = new ArrayList<ExternalUser>();
        for (ExternalUser extUser : externalUsers) {
            if (count % 1000 == 0) {
                System.out.println(count);
            }

            count++;
            List<String> userIds = new ArrayList<String>();
            String userId = null;
            String email = null;
            for (SplusEntitlement entitlement : splusEntitlements) {
                if (extUser.externalSubscriberId.equals(entitlement.externalSubscriberId)) {
                    userIds.add(entitlement.user_id);
                }
            }

            if (userIds != null && userIds.size() > 0) {
                for (SplusUser user : splusUsers) {
                    for (String id : userIds) {
                        if (id.equals(user.id)) {
                            email = user.email;
                            userId = id;
                            if (extUser.email == null || extUser.email == "" || user.email.equalsIgnoreCase(extUser.email)) {
                                break;
                            }
                        }
                    }
                }
            }

            try {
                writer.append(extUser.externalSubscriberId).append(",");
                if (extUser.email != null) {
                    writer.append(extUser.email).append(",");
                } else {
                    writer.append(",");
                }

                if (email != null) {
                    writer.append(email).append(",");
                } else {
                    writer.append(",");
                }

                if (userId != null) {
                    writer.append(userId).append(",");
                } else {
                    writer.append(",");
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
            if (line[1] != null && line[1] != "") {
                u.email = line[1];
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
