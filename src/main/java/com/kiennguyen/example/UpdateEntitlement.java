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
 * @author <a href="kien.nguyen@niteco.se">Kien NGUYEN</a>
 */
public class UpdateEntitlement extends ServicePlusAPI {
    public UpdateEntitlement(String frameworkId) {
        super(frameworkId);
    }

    public static void main(String[] args) throws Exception {
        new UpdateEntitlement("prod").process();
    }
    @Override
    public void process() throws Exception {
        processFromFile();
    }

    private void updateEntitlement() {
        String accessToken = "3JRKtMmuRvNQDc40DBWRnY";

        String id = "004dXOVgpI87R3YUgYJEfn";
        Entitlement e = Entitlement.findById(id, accessToken);
        if (!e.hasErrors()) {
            e.productTags = new ArrayList<String>();
            e.productTags.add("DNPREMIUM2");
            e.productTags.add("DNEKP");
            Entitlement u = Entitlement.update(e.id, e, accessToken);
            System.out.println(e.id);
        }
    }
    public void processFromFile() throws IOException {
        String accessToken = "3JRKtMmuRvNQDc40DBWRnY";
        CSVReader reader = new CSVReader(new FileReader("/home/kiennguyen/Desktop/data/20200206/update_entitlements_tags.csv"));
        List<String[]> lines = reader.readAll();
        List<String> emails = new ArrayList<String>();
        for (String[] strings : lines) {
            String id = strings[0];
            Entitlement e = Entitlement.findById(id, accessToken);
            e.productTags = new ArrayList<String>();
            e.productTags.add("DNPREMIUM2");
            e.productTags.add("DNEKP");
            Entitlement updated = Entitlement.update(e.id, e, accessToken);
            System.out.println(updated.id);
        }
    }


}
