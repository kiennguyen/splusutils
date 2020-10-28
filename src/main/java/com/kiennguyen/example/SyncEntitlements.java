package com.kiennguyen.example;

import au.com.bytecode.opencsv.CSVReader;
import com.kiennguyen.commons.ServicePlusAPI;
import se.bonnier.api.model.SearchParameters;
import se.bonnier.api.model.entitlementservice.Entitlement;
import se.bonnier.api.model.userservice.User;
import se.bonnier.api.response.ErrorResponse;
import se.bonnier.api.response.SearchResult;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien NGUYEN</a>
 */

// Use splunk for tracking
public class SyncEntitlements extends ServicePlusAPI {
    private String accessToken = "5Wwkjpf361BV3lF0XEztpm";
    public static void main(String[] args) throws Exception {
        SyncEntitlements app = new SyncEntitlements("prod");
        app.process();
    }

    public SyncEntitlements(String frameworkId) {
        super(frameworkId);
    }


    @Override
    public void process() throws Exception {
        List<CustomUser> users = readCSV();
        System.out.println("Total = " + users.size());
        for (CustomUser customUser : users) {
            ErrorResponse er = Entitlement.syncEntitlements(customUser.userId, accessToken);
            if (er.hasErrors()) {
                System.out.println("Error with id " + customUser.userId + "  " + er.getErrorMessage());
            } else {
                System.out.println("SUCCESSFULLY Synced for user " + customUser.userId);
            }
        }


    }

    private List<CustomUser> readCSV() throws IOException {
        CSVReader reader = new CSVReader(new FileReader("/home/kiennguyen/Desktop/data/20191220/dn_ungdom_entitlements_temp.csv"));
        String[] line;
        List<CustomUser> users = new ArrayList<CustomUser>();
        while ((line = reader.readNext()) != null) {
            CustomUser u = new CustomUser();
            u.userId = line[0];
            users.add(u);
        }
        reader.close();
        return users;
    }
}

