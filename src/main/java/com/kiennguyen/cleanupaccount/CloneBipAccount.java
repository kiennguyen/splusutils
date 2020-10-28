package com.kiennguyen.cleanupaccount;

import au.com.bytecode.opencsv.CSVReader;
import com.kiennguyen.commons.ServicePlusAPI;
import se.bonnier.api.model.entitlementservice.Entitlement;
import se.bonnier.api.model.orderservice.Order;
import se.bonnier.api.model.userservice.BipAccount;
import se.bonnier.api.model.userservice.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="kien.nguyen@niteco.se">KIEN NGUYEN</a>
 */
public class CloneBipAccount extends ServicePlusAPI {
    public CloneBipAccount(String frameworkId) {
        super(frameworkId);
    }

    public static void main(String[] args) throws Exception {
        new CloneBipAccount("prod").process();
    }
    @Override
    public void process() throws Exception {

        List<User> users = getUsers("/home/kiennguyen/Desktop/data/20190523/ano_bip_20190529.csv");
        String accessToken = "0zSAsQGQiJzSrvCY3a3Fgr";
        int i = 0;
        for (User user : users) {
            System.out.println(i ++);
            clone(user.email, user.brandId, accessToken);
        }
    }

    private void clone(String email, String brandId, String accessToken) {
//        Account order = Account.clone(email, brandId, accessToken);
//        if (order.hasErrors()) {
//            System.out.println("CANNOT clone Bip account " + order.getApiErrorCode() + " email: " + email);
//        }
    }

    private static List<User> getUsers(String fileName) throws IOException {
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        List<String[]> accounts = reader.readAll();
        List<User> accountList = new ArrayList<User>();
        for (String[] strings : accounts) {
            User account = new User();
            account.email = strings[0];
            account.brandId = strings[1];
            accountList.add(account);
        }
        return accountList;
    }
}
