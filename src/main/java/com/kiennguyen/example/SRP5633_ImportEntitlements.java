package com.kiennguyen.example;

import au.com.bytecode.opencsv.CSVReader;
import com.kiennguyen.commons.ServicePlusAPI;
import se.bonnier.api.model.SearchParameters;
import se.bonnier.api.model.entitlementservice.Entitlement;
import se.bonnier.api.model.userservice.User;
import se.bonnier.api.response.SearchResult;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */

// Use splunk for tracking
public class SRP5633_ImportEntitlements extends ServicePlusAPI {
    private String accessToken = "1FDeQEOCKRE9VQ2Qcy6gE4";
    public static void main(String[] args) throws Exception {
        SRP5633_ImportEntitlements app = new SRP5633_ImportEntitlements("prod");
//        app.process();

        // Verify after process
         app.verify();

    }

    public void verify() throws IOException {
        List<CustomUser> users = readCSV();
        System.out.println("Total = " + users.size());
        for (CustomUser customUser : users) {
            verify(customUser);
        }
    }
    public SRP5633_ImportEntitlements(String frameworkId) {
        super(frameworkId);
    }

    public void verify(CustomUser u) throws IOException {
        SearchParameters sp = SearchParameters.getBuilder().query("userId_s:" + u.userId).sortField("updated").sortOrder("desc").build();
        SearchResult<Entitlement> e = Entitlement.find(sp, accessToken);
        if (!e.hasErrors()) {
            Entitlement entitlement = e.getItems().get(0);
            if (entitlement.externalSubscriptionId.equalsIgnoreCase(u.subscriptionId)) {
                System.out.println(entitlement.productTags);
            } else {
                System.out.println("ERROR " + u.userId);
            }
        }
    }

    @Override
    public void process() throws Exception {
        List<CustomUser> users = readCSV();
        System.out.println("Total = " + users.size());
        for (CustomUser customUser : users) {
            User u = User.findByEmail(customUser.email, "5DuzcZz0j8u0zArSNzZgHO");
            System.out.print(u.email + " " + customUser.productId);
            if (!u.hasErrors()) {
                if (!u.email.equals(customUser.email)) {
                    System.out.println("Wrong " + u.email);
                } else {
                    customUser.userId = u.id;
                    importEntitlement(customUser);
                }
            } else {
                System.out.println("Cannot get user " + u.email);
            }
        }


    }

    private List<CustomUser> readCSV() throws IOException {
        CSVReader reader = new CSVReader(new FileReader("/home/kiennguyen/working/niteco/data/imported_di_users_20140611.csv"));
        String[] line;
        List<CustomUser> users = new ArrayList<CustomUser>();
        while ((line = reader.readNext()) != null) {
            CustomUser u = new CustomUser();
            u.userId = line[0];

            u.email = line[1];
            u.subscriberId = line[2];
            u.productId = line[3];
            u.subscriptionId = line[4];
            users.add(u);
        }
        reader.close();
        return users;
    }

    private void importEntitlement(CustomUser u) {
        String productId = "";
        if (u.productId.equalsIgnoreCase("01")) {
            productId = "11G8C6AAMCr9jAU9j7oKbm";
        } else if (u.productId.equalsIgnoreCase("05")) {
            productId = "1edG3ChVjlxScoKsI8KzuA";
        }
        if (productId.length() > 0) {

            Entitlement en = Entitlement.importEntitlements(u.userId, productId, u.subscriberId, null, false, accessToken);
            if (en.hasErrors()) {
                System.out.println("ERROR with id " + u.userId + "  " + en.getErrorMessage());
            } else {
                System.out.println("SUCCESSFULLY IMPORT ENTITLEMENT FOR PROD " + productId);
            }
        } else {
            System.out.println("DONT HAVE PRODUCT");
        }
    }
}

class CustomUser {
    String email;
    String userId;
    String subscriberId;
    String subscriptionId;
    String productId;
}
