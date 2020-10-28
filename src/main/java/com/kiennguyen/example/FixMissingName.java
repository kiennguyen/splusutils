package com.kiennguyen.example;

import au.com.bytecode.opencsv.CSVReader;
import com.kiennguyen.commons.ServicePlusAPI;
import se.bonnier.api.model.userservice.User;

import java.io.FileReader;
import java.util.*;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public class FixMissingName extends ServicePlusAPI {
    public FixMissingName(String frameworkId) {
        super(frameworkId);
    }

    public static void main(String[] args) throws Exception {
        FixMissingName app = new FixMissingName("prod");
        app.process();
    }
    @Override
    public void process() throws Exception {
        CSVReader reader = new CSVReader(new FileReader("/home/kiennguyen/fixname.csv"));
        String line[];
        List<Map<String, String>> listUser = new ArrayList<Map<String, String>>();
        while ((line = reader.readNext()) != null) {
            Map<String, String> info = new HashMap<String, String>();
            info.put("email", line[0]);
            info.put("id", line[1]);
            info.put("first_name", line[2]);
            info.put("last_name", line[3]);
            listUser.add(info);
        }

        Iterator<Map<String, String>> ite = listUser.iterator();
        while (ite.hasNext()) {
            Map<String, String> obj = ite.next();
            String id = obj.get("id");
            System.out.println(id);
            User u = User.findById(id);
            if (u != null && u.email.equals(obj.get("email"))) {
                u.firstName = obj.get("first_name");
                u.lastName = obj.get("last_name");
                User updatedU = User.update(id, u);
                if (updatedU.email.equals(obj.get("email"))) {
                    System.out.println("DONE FOR USER " + id);
                } else {
                    System.out.println("FAILED " + id + updatedU.getApiErrorCode());
                }
            }
        }
        System.out.println(listUser.size());
    }
}
