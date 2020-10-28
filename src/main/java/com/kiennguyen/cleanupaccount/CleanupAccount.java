package com.kiennguyen.cleanupaccount;

import au.com.bytecode.opencsv.CSVReader;
import com.kiennguyen.cleanupaccount.utils.CleanupUtils;
import se.bonnier.api.model.entitlementservice.Entitlement;
import se.bonnier.api.model.userservice.User;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kiennguyen.cleanupaccount.utils.CleanupUtils.writeOutput;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public class CleanupAccount {
    public static void main(String[] args) throws IOException {

        List<User> accounts = CleanupUtils.getAccounts("/home/kiennguyen/Desktop/invalid_users_20140212.csv");
        List<Entitlement> entitlements = getEntitlements();

        Map<String, User> accountMap = new HashMap<String, User>();
        Map<String, Entitlement> entitlementMap = new HashMap<String, Entitlement>();

        for (User a : accounts) {
            accountMap.put(a.id, a);
        }

        for (Entitlement e : entitlements) {
            entitlementMap.put(e.userId, e);
        }

        List<User> invalidAccount = new ArrayList<User>();
        List<User> validAccount = new ArrayList<User>();

        for (String userId : accountMap.keySet()) {
            if (entitlementMap.containsKey(userId)) {
                validAccount.add(accountMap.get(userId));
            } else {
                invalidAccount.add(accountMap.get(userId));
            }
        }
        writeOutput(validAccount, "/home/kiennguyen/Desktop/out_valid.csv");
        writeOutput(invalidAccount, "/home/kiennguyen/Desktop/out_invalid.csv");
    }

    private static List<Entitlement> getEntitlements() throws IOException {
        CSVReader reader = new CSVReader(new FileReader("/home/kiennguyen/Desktop/active_future_sds_entitlements.csv"));
        List<String[]> ents = reader.readAll();
        List<Entitlement> entitlements = new ArrayList<Entitlement>();
        for (String[] strings : ents) {
            Entitlement e = new Entitlement();
            e.id = strings[0];
            /*e.userId = strings[1];
            e.validFrom = strings[2];
            e.validTo = strings[3];
            e.productId = strings[4];
            e.productTags = strings[5];*/
            entitlements.add(e);
        }

        return entitlements;
    }
}