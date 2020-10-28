package com.kiennguyen.utils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.sql.*;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public class ConnectDBViaSSH {
    static int lport;
    static String rhost;
    static int rport;

    public static Session go() {
        String user = "dnguyen";
        String password = "WDaq3KDn";
        String host = "bds-mysql01.sth.basefarm.net";
        int port = 22;
        Session session = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            lport = 4321;
            rhost = "localhost";
            rport = 3306;
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            int assinged_port = session.setPortForwardingL(lport, rhost, rport);
            System.out.println("localhost:" + assinged_port + " -> " + rhost + ":" + rport);
        } catch (Exception e) {
            System.err.print(e);
        }
        return session;
    }

    public static Connection connectDB() {
        Connection con = null;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://" + rhost + ":" + lport + "/";
        String db = "serviceplus";
        String dbUser = "bds-dbro";
        String dbPasswd = "jdha5nIV";
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url + db, dbUser, dbPasswd);
            try {
                Statement st = con.createStatement();
                String sql = "select * from userservice.users limit 10";
                ResultSet result = st.executeQuery(sql);
                while (result.next()) {
//                    System.out.println(result.getString("email"));
                }
            } catch (SQLException s) {
                System.out.println("SQL statement is not executed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }

    public static void main(String[] args) {
        try {
            go();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("An example for updating a Row from Mysql Database!");
        Connection con = null;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://" + rhost + ":" + lport + "/";
        String db = "serviceplus";
        String dbUser = "bds-dbro";
        String dbPasswd = "jdha5nIV";
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url + db, dbUser, dbPasswd);
            try {
                Statement st = con.createStatement();
                String sql = "select * from userservice.users limit 10";
                ResultSet result = st.executeQuery(sql);
                while (result.next()) {
                    System.out.println(result.getString("email"));
                }
            } catch (SQLException s) {
                System.out.println("SQL statement is not executed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
