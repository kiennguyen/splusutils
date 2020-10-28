package com.kiennguyen.statistic;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.jcraft.jsch.Session;
import com.kiennguyen.utils.ConnectDBViaSSH;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public class GetUserByToken {
    private static Connection conn;

    public static void main(String[] args) throws IOException, SQLException {
        List<String> tokens = getTokens();
        Session session = ConnectDBViaSSH.go();
        conn = ConnectDBViaSSH.connectDB();
        Map<String, BlockedAccount> accounts = getUserEmail(tokens);

        for (String email : accounts.keySet()) {

            System.out.println(accounts.get(email));
        }
        writeCSV(accounts);
        session.disconnect();
    }

    private static void writeCSV(Map<String, BlockedAccount> accounts) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter("/home/kiennguyen/accounts.csv"));
        List<String[]> allLines = new ArrayList<String[]>();
        for (String email : accounts.keySet()) {
            BlockedAccount account = accounts.get(email);
            String line[] = new String[]{account.email, String.valueOf(account.count)};
            allLines.add(line);
        }
        writer.writeAll(allLines);
        writer.flush();
    }

    private static Map<String, BlockedAccount> getUserEmail(List<String> tokens) throws SQLException {
        List<String> emails = new ArrayList<String>();
        Statement statement = conn.createStatement();
        Map<String, BlockedAccount> accounts = new HashMap<String, BlockedAccount>();
        for (String token : tokens) {

            String sql = "SELECT * FROM userservice.o_auth_sessions s, userservice.users u where s.id='" + token + "' AND u.id = s.user_id";
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                String email = rs.getString("email");
                System.out.println(email);
                BlockedAccount account;
                if (accounts.containsKey(email)) {
                    account = accounts.get(email);
                    account.count++;
                } else {
                    account = new BlockedAccount();
                    account.email = email;
                }
                accounts.put(email, account);
            }
        }

        return accounts;
    }

    private static List<String> getTokens() throws IOException {
        List<String> tokens = new ArrayList<String>();
        CSVReader reader = new CSVReader(new FileReader("/home/kiennguyen/Downloads/tokens.csv"));
        List<String[]> lines = reader.readAll();
        for (String[] line : lines) {
            tokens.add(line[0]);
        }

        return tokens;
    }

    static class BlockedAccount {
        String email;
        int count = 1;

        public BlockedAccount() {

        }

        @Override
        public String toString() {
            return "Email : " + email + " count : " + count;
        }
    }
}
