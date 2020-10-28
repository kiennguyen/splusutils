package com.kiennguyen.example;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.kiennguyen.commons.ServicePlusAPI;
import se.bonnier.api.model.SearchParameters;
import se.bonnier.api.model.entitlementservice.Entitlement;
import se.bonnier.api.model.entitlementservice.EntitlementState;
import se.bonnier.api.model.entitlementservice.EntitlementType;
import se.bonnier.api.model.userservice.User;
import se.bonnier.api.response.SearchResult;
import se.bonnier.api.util.ObjectUtil;

import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public class CreateEntitlement extends ServicePlusAPI {
    public static void main(String[] args) throws Exception {
        new CreateEntitlement("prod").process();

    }
    public CreateEntitlement(String frameworkId) {
        super(frameworkId);
    }

    @Override
    public void process() throws Exception {
        CSVReader yesterdayReader = new CSVReader(new FileReader("/tmp/input.csv"));
        CSVWriter writer = new CSVWriter(new FileWriter("/tmp/output.csv"));
        try {
            List<String[]> yesterdayLines = yesterdayReader.readAll();
            List<com.kiennguyen.example.User> users = new ArrayList<com.kiennguyen.example.User>();
            Set<String> emails = new HashSet<String>();
            for (String[] strings : yesterdayLines) {
//                String subscriberId = strings[0];
//                String firstName = strings[1];
//                String lastName = strings[2];
//                String email = strings[3];
//                String paper = null;
//                if (strings.length > 4) {
//                    paper = strings[4];
//                }
//                String product = null;
//                if (strings.length > 5) {
//                    product = strings[5];
//                }
//                com.kiennguyen.example.User user = new com.kiennguyen.example.User(subscriberId, email, firstName, lastName, paper, product);
                String email = strings[0];

                com.kiennguyen.example.User user = new com.kiennguyen.example.User(email);
                users.add(user);
            }

            String accessToken = "6U28OyrcTFCzu6BHYnNoaD";
            for (com.kiennguyen.example.User u : users) {
                System.out.println("================");
                if (ObjectUtil.empty(u.email)) {
                    //writer.writeNext(new String[]{u.subscriberId, u.firstName, u.lastName, u.email, u.paper, u.product, "No account"});
                    writer.writeNext(new String[]{u.email, "No account"});
                    continue;
                }

                if (emails.contains(u.email)) {
                    //writer.writeNext(new String[]{u.subscriberId, u.firstName, u.lastName, u.email, u.paper, u.product, "Created entitlement"});
                    writer.writeNext(new String[]{u.email, "Created entitlement"});
                    continue;
                }

                u.email = u.email.trim().toLowerCase();
                String query = "email_sci:" + u.email + " AND brandId_s:0uAK7wv28CxMk2lSBitm9Y";
                SearchParameters sp = SearchParameters.getBuilder().query(query).maxResults(1).build();
                SearchResult<User> user = User.find(sp, accessToken);
                if (user.getItems().size() == 0) {
                    System.out.println("cannot found email " + u.email);
                    //writer.writeNext(new String[]{u.subscriberId, u.firstName, u.lastName, u.email, u.paper, u.product, "No account"});
                    writer.writeNext(new String[]{u.email, "No account"});
                } else {
                    System.out.println(u.email);
                    String userId = user.getItems().get(0).id;
                    String productId = "5kDbiYMAczuauhrOWEmiB7";
                    StringBuilder q = new StringBuilder();
                    q.append("userId_s:").append(userId);
                    q.append(" AND ");
                    q.append("productId_s:").append(productId);
                    SearchParameters entitlementSp = SearchParameters.getBuilder().query(q.toString()).build();
                    SearchResult<Entitlement> searchResult = Entitlement.find(entitlementSp);
                    if (searchResult.getItems().size() > 0) {
                        System.out.println("user: " + userId + " " + searchResult.getItems().get(0).productTags);
                    } else {
                        createEntitlement(userId);
                        emails.add(u.email);
                        //writer.writeNext(new String[]{u.subscriberId, u.firstName, u.lastName, u.email, u.paper, u.product, "Created entitlement"});
                        writer.writeNext(new String[]{u.email, "Created entitlement"});
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
        }

        writer.flush();
    }

    private void createEntitlement(String userId) throws ParseException {
        Entitlement e = new Entitlement();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> tags = new ArrayList<String>();
        tags.add("DNPRIO");
        e.brandId = "0uAK7wv28CxMk2lSBitm9Y";
        e.productId = "5kDbiYMAczuauhrOWEmiB7";
        e.renewable = true;
        e.type = EntitlementType.PREMIUM_SUBSCRIPTION;
        e.userId = userId;
        //e.validFrom = sdf.parse("2014-12-01").getTime();
        e.validFrom = Long.valueOf("1417392000000");
        //e.validTo = sdf.parse("2015-04-01").getTime();
        e.validTo = Long.valueOf("1427846400000");
        e.state = EntitlementState.VALID;
        e.productTags = tags;

        Entitlement created = Entitlement.create(e, "6U28OyrcTFCzu6BHYnNoaD");
        if (created != null && !created.hasErrors()) {
            System.out.println(created.id);
        } else {
            System.out.println(created.getApiErrorCode());
        }
    }
}
