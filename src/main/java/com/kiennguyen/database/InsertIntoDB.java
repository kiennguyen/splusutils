package com.kiennguyen.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public class InsertIntoDB {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection con = null;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost/";
        String db = "serviceplus";
        String dbUser = "root";
        String dbPasswd = "";
        Class.forName(driver);
        con = DriverManager.getConnection(url + db, dbUser, dbPasswd);
        con.setAutoCommit(false);
        Statement st = con.createStatement();
        for (int i = 0; i < 500000; i++) {
            System.out.println(i);
            Random rand = new Random();
            int r = rand.nextInt();
            String id  = String.valueOf(System.currentTimeMillis()) + String.valueOf(r);
            String query = "insert into users (email, password, created, updated, id, brand_id) values ('test@test.com', '123456', 1395386620861, 1395386620861, '"+id+"', '1kqPBEex4hbFkpbzmmX4qA') ";
            st.execute(query);
        }
        con.commit();
    }
}
