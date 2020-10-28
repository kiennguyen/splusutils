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
public class DeleteInvalidAccount extends ServicePlusAPI {
    public DeleteInvalidAccount(String frameworkId) {
        super(frameworkId);
    }

    public static void main(String[] args) throws Exception {
        new DeleteInvalidAccount("prod").process();
    }
    @Override
    public void process() throws Exception {
//        List<Entitlement> entitlements = getEntitlements("/home/kiennguyen/Desktop/data/20190502/exp_entitlement_20190503_1.csv");
//        String accessToken = "3BJUTku6KSWd5wMChRGKDH";
//        int i = 0;
//        for (Entitlement ent : entitlements) {
//            System.out.println(i ++);
//            deleteEntitlement(ent.id, accessToken);
//        }

//        List<User> users = getUsers("/home/kiennguyen/Desktop/data/20190502/exp_bip_without_dndi_20190506.csv");
//        String accessToken = "1Geb1ty65uO7wH3ENMRFOF";
//        int i = 0;
//        for (User user : users) {
//            System.out.println(i ++);
//            deleteBip(user.id, accessToken);
//        }

        List<Order> users = getOrders("/home/kiennguyen/Desktop/data/20190502/exp_order_20190513.csv");
        String accessToken = "7q6DK7fkI9KlQw2oW8owDK";
        int i = 0;
        for (Order user : users) {
            System.out.println(i ++);
            deleteOrder(user.id, accessToken);
        }
    }

    private void deleteOrder(String id, String accessToken) {
        Order order = Order.delete(id, accessToken);
        if (order.hasErrors()) {
            System.out.println("CANNOT delete order " + order.getApiErrorCode() + " id: " + id);
        }
    }


    private void deleteBip(String id, String accessToken) {
        BipAccount user = BipAccount.delete(id, accessToken);
        if (user.hasErrors()) {
            System.out.println("CANNOT delete bip " + user.getApiErrorCode() + " id: " + id);
        }
    }

    private void deleteUser(String id, String accessToken) {
        User user = User.delete(id, accessToken);
        if (user.hasErrors()) {
            System.out.println("CANNOT delete user " + user.getApiErrorCode() + " id: " + id);
        }
    }

    private void deleteEntitlement(String id, String accessToken) {
        Entitlement ent = Entitlement.delete(id, accessToken);
        if (ent.hasErrors()) {
            System.out.println("CANNOT delete entitlement " + ent.getApiErrorCode() + " id: " + id);
        }
    }

    private static List<User> getUsers(String fileName) throws IOException {
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        List<String[]> accounts = reader.readAll();
        List<User> accountList = new ArrayList<User>();
        for (String[] strings : accounts) {
            User account = new User();
            account.id = strings[0];
            //account.userId = strings[1];
            accountList.add(account);
        }
        return accountList;
    }

    private static List<Entitlement> getEntitlements(String fileName) throws IOException {
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        List<String[]> accounts = reader.readAll();
        List<Entitlement> accountList = new ArrayList<Entitlement>();
        for (String[] strings : accounts) {
            Entitlement account = new Entitlement();
            account.id = strings[0];
            //account.userId = strings[1];
            accountList.add(account);
        }
        return accountList;
    }

    private static List<Order> getOrders(String fileName) throws IOException {
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        List<String[]> accounts = reader.readAll();
        List<Order> list = new ArrayList<Order>();
        for (String[] strings : accounts) {
            Order account = new Order();
            account.id = strings[0];
            account.userId = strings[1];
            list.add(account);
        }
        return list;
    }
}
