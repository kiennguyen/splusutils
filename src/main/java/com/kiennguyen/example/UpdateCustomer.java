package com.kiennguyen.example;

import au.com.bytecode.opencsv.CSVReader;
import com.kiennguyen.commons.ServicePlusAPI;

import se.bonnier.api.model.SearchParameters;
import se.bonnier.api.model.userservice.User;
import se.bonnier.api.response.SearchResult;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public class UpdateCustomer extends ServicePlusAPI {
    public UpdateCustomer(String frameworkId) {
        super(frameworkId);
    }

    public static void main(String[] args) throws Exception {
        new UpdateCustomer("qa").process();
    }
    @Override
    public void process() throws Exception {
        updateCustomerName();
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
        CSVReader reader = new CSVReader(new FileReader("/home/kiennguyen/emails.csv"));
        List<String[]> lines = reader.readAll();
        List<String> emails = new ArrayList<String>();
        for (String[] strings : lines) {
            String email = strings[0];
            emails.add(email);
        }

        for (String email : emails) {
            String query = "email_sci:" + email;
            SearchParameters sp = SearchParameters.getBuilder().query(query).build();
            SearchResult<User> result = User.find(sp);
            if (result.getItems().size() > 0) {
                User user = result.getItems().get(0);
                user.active = System.currentTimeMillis();
                User.update(user.id, user);
            }
        }
    }


}
