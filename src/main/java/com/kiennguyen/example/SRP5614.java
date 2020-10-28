package com.kiennguyen.example;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import se.bonnier.api.model.entitlementservice.Entitlement;
import se.bonnier.api.model.userservice.User;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
/*select e.id, e.user_id, e.product_id, e.external_subscriber_id, e.external_subscription_id from entitlementservice.entitlements e where e.brand_id = '5DuzcZz0j8u0zArSNzZgHO'
        and deleted is null and e.external_subscription_id is NOT null AND e.external_subscriber_id is not null
        AND e.external_subscriber_id REGEXP '^[0-9]+$'
        limit 10000000;*/


//select email, id from userservice.users where brand_id = '5DuzcZz0j8u0zArSNzZgHO' and deleted is null and type = 'CUSTOMER' limit 100000;
public class SRP5614 {
    public static void main(String[] args) throws IOException {
        List<User> users = loadUser("/home/kiennguyen/working/niteco/data/di_user_20140612.csv");
        Map<String, Entitlement> entitlements = loadEntitlement("/home/kiennguyen/working/niteco/data/di_paid_entitlement_20140612.csv");
        List<ReportedUser> results = new ArrayList<ReportedUser>();
        for (User u : users) {
            ReportedUser ru = new ReportedUser();
            ru.userId = u.id;
            ru.email = u.email;
            if (entitlements.containsKey(u.id)) {
                ru.subscriberId = entitlements.get(u.id).externalSubscriberId;
                ru.subscriptionId = entitlements.get(u.id).externalSubscriptionId;
            }

            results.add(ru);
        }

        writeReport("/home/kiennguyen/working/niteco/data/reported/srp_5696.csv", results);

    }

    private static void writeReport(String file, List<ReportedUser> users) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(file));
        for (ReportedUser ru : users) {
            writer.writeNext(new String[] {ru.userId, ru.email, ru.subscriberId, ru.subscriptionId});
        }
        writer.flush();
    }

    public static List<User> loadUser(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] line;
        List<User> users = new ArrayList<User>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            User u = new User();
            u.id = line[1];
            u.email = line[0];
            users.add(u);
        }

        return users;
    }

    public static Map<String, Entitlement> loadEntitlement(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] line;
        Map<String, Entitlement> mapEntitlements = new HashMap<String, Entitlement>();
        while ((line = reader.readNext()) != null) {
            Entitlement e = new Entitlement();
            e.id = line[0];
            e.userId = line[1];
            e.productId = line[2];
            e.externalSubscriberId = line[3];
            e.externalSubscriptionId = line[4];
            if (isPaidSubscription(e.externalSubscriptionId)) {
                mapEntitlements.put(e.userId, e);
            }
        }
        return mapEntitlements;
    }

    private static boolean isPaidSubscription(String externalSubscriptionId) {
        try {
            Integer.parseInt(externalSubscriptionId);
            return true;
        } catch (NumberFormatException nfe) {
//            System.out.println(externalSubscriptionId);
        }
        return false;
    }

    static class ReportedUser {
        String userId;
        String email;
        String subscriptionId;
        String subscriberId;
    }
}
