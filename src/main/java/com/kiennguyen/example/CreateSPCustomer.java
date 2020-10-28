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
public class CreateSPCustomer extends ServicePlusAPI {
    public CreateSPCustomer(String frameworkId) {
        super(frameworkId);
    }

    public static void main(String[] args) throws Exception {
        new CreateSPCustomer("").process();
    }

    @Override
    public void process() throws Exception {
        create500KUser();
    }

    private void create500KUser() {
        int i = 0;

        for (i = 0; i < 500000; i++) {
            System.out.print(i);
            String email = "test_large_user" + i + "@gmail.com";
            User u = new User();
            u.email = email;
            u.firstName = email;
            u.lastName = email;
            u.brandId = "1kqPBEex4hbFkpbzmmX4qA";
            u.password = "123456";
            User user = User.create(u, "4Pb9vw7iPByQACgJniJgZz");
            if (user.hasErrors()) {
                System.out.println("Has error " + user.getErrorMessage());
                return;
            }
            System.out.println("Create user " + user.email);
        }
    }

    private void createFromFile() throws IOException {
        CSVReader reader = new CSVReader(new FileReader("/home/kiennguyen/emails.csv"));
//        String accessToken = "3u50cNX84VdFhCQHv5cqce";
        String accessToken = "293kB6QnrRnJe8yoxLVMlP";
        List<String[]> lines = reader.readAll();
        List<String> emails = new ArrayList<String>();
        for (String[] strings : lines) {
            String email = strings[0];
            emails.add(email);
        }

        for (String email : emails) {
            System.out.println("================");
            System.out.println(email);
            String query = "email_sci:" + email;
            SearchParameters sp = SearchParameters.getBuilder().query(query).build();
            SearchResult<User> user = User.find(sp, accessToken);
            String[] groups = email.split("@");
            String firstName = groups[0];
            String lastName = "Test";
            if (!user.hasErrors()) {
                if (user.getItems().size() == 0) {
                    User u = new User();
                    u.email = email;
                    u.firstName = firstName;
                    u.lastName = lastName;
                    u.password = "123456";
                    u.brandId = "0uAK7wv28CxMk2lSBitm9Y";

                    User result = User.create(u, accessToken);
                    if (result.hasErrors()) {
                        System.out.println(result.getErrorMessage());
                    } else {
                        System.out.println("Created = " + result.email);
                    }
                } else {
                    System.out.println(email + " has been created");
                }
            } else {
                System.out.println("An error " + user.getErrorMessage());
            }
        }
    }
}
