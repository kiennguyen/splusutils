package com.kiennguyen.cleanupaccount;

import au.com.bytecode.opencsv.CSVReader;
import com.kiennguyen.commons.ServicePlusAPI;
import com.kiennguyen.model.Account;
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
public class UpdateValidAccount extends ServicePlusAPI {
    public UpdateValidAccount(String frameworkId) {
        super(frameworkId);
    }

    public static void main(String[] args) throws Exception {
        new UpdateValidAccount("prod").process();
    }
    @Override
    public void process() throws Exception {
        List<Account> users = getAccounts("/home/kiennguyen/Desktop/data/20190704/bip_active0_login_time.csv");

        String accessToken = "2RE2Dpp1pVbDv9AsOKo7xN";
        String id = "";
        int i = 1;
        for (Account u : users) {
            id = u.id;
            BipAccount a = BipAccount.findById(id, accessToken);
            System.out.println("Processing account " + a.email);
            System.out.print(i++ + " : ");
            if (a != null && a.email.trim().equalsIgnoreCase(u.email.trim()))
                if (a.active == null || a.active == 0L) {
                    a.active = u.loginTime;
                    BipAccount updatedBipAccount = BipAccount.update(id, a, accessToken);
                    if (!updatedBipAccount.hasErrors()) {
                        System.out.print("Updated for account " + a.email + "  ");
                    } else {
                        System.out.print("CAN NOT UPDATE " + a.email + "  ");
                    }
                } else {
                    System.out.print("do not update " + a.email + "  ");
                }
            else {
                System.out.println("User is null " + u.email);
            }
        }

    }

    private static List<Account> getAccounts(String fileName) throws IOException {
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        List<String[]> accounts = reader.readAll();
        String[] line;
        List<Account> accountList = new ArrayList<Account>();
        for (String[] strings : accounts) {
            Account account = new Account();
            account.id = strings[0];
            account.email = strings[1].toLowerCase();
            account.loginTime = Long.parseLong(strings[2]);
            accountList.add(account);
        }
        return accountList;
    }
}
