package com.kiennguyen.example;

import au.com.bytecode.opencsv.CSVReader;
import com.kiennguyen.commons.ServicePlusAPI;
import se.bonnier.api.model.SearchParameters;
import se.bonnier.api.model.userservice.BipAccount;
import se.bonnier.api.model.userservice.User;
import se.bonnier.api.response.SearchResult;
import se.bonnier.api.util.SystemConfiguration;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author <a href="kien.nguyen@niteco.se">KIEN NGUYEN</a>
 */
public class UpdateBipAccount extends ServicePlusAPI {
    public UpdateBipAccount(String frameworkId) {
        super(frameworkId);
    }

    public static void main(String[] args) throws Exception {
        new UpdateBipAccount("prod").process();
    }
    @Override
    public void process() throws Exception {
        processFromFile();
    }

    private void updateCustomerName() {
        String id = "0eQu8P1rvoXj5MAW16mIbn";
        User user = User.findById(id);
        if (!user.hasErrors()) {
            user.firstName = null;
            user.lastName = null;
            User u = User.update(user.id, user);
            System.out.println(u.email);
            System.out.println(u.firstName);
            System.out.println(u.hasErrors());
        }
    }
    public void processFromFile() throws IOException {
        String accessToken = "0ZSc8IkXXSzQNfOmQJpu3Z";
        CSVReader reader = new CSVReader(new FileReader("/home/kiennguyen/Desktop/data/20190306/emails_20190307.csv"));
        List<String[]> lines = reader.readAll();
        Set<String> emails = new HashSet<String>();
        for (String[] strings : lines) {
            String email = strings[0];
            emails.add(email.trim());
        }

        for (String email : emails) {
            String query = "email_sci:" + email;
            SearchParameters sp = SearchParameters.getBuilder().query(query).build();
            SearchResult<BipAccount> result = BipAccount.find(sp, accessToken);
            if (result.getItems().size() > 0) {
                BipAccount account = result.getItems().get(0);
                if (account.active == null) {
                    //user.activationToken = UUID.randomUUID().toString();
                    //user.activationTokenCreated = System.currentTimeMillis();
                    //Account.update(user.id, user, accessToken);
                    SearchResult<User> users = BipAccount.findBipAccountUsers(account.id, accessToken);
                    String brandId = "0uAK7wv28CxMk2lSBitm9Y";
                    if (users.getItems().size() > 0) {
                        Long created = 955131877309199l;
                        for (User u : users.getItems()) {
                            if (u.created < created) {
                                created = u.created;
                                brandId = u.brandId;
                            }
                        }
                    }

                    String appId = "dagensnyheter.se";
                    if (!"0uAK7wv28CxMk2lSBitm9Y".equals(brandId)) {
                        appId = "di.se";
                    }
                    //Account sentAccount = Account.sendActivationEmail(account.id, appId, accessToken);
                    //System.out.println("Updated customer " + account.email + " with activationTokenCreated " + account.activationTokenCreated);
                    System.out.println("curl -i -X POST 'https://api.bonnier.se/v1/bipaccounts/" + account.id + "/send-activation-mail?appId=" + appId + "&access_token=0ZSc8IkXXSzQNfOmQJpu3Z' -H 'application/x-www-form-urlencoded'");
                } else {
                    System.out.println("Not update customer " + account.email);
                }
            }
        }
    }

}
