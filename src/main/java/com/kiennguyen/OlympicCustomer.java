package com.kiennguyen;

import au.com.bytecode.opencsv.CSVReader;
import com.kiennguyen.model.Account;
import com.kiennguyen.model.Customer;
import com.kiennguyen.model.SplusUser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class OlympicCustomer {

    public static void main(String[] args) throws IOException {
        mergeLoginTime();
    }

    public static void mergeLoginTime() throws IOException {
        List<Customer> customers = loadCustomer("/home/kiennguyen/Desktop/data/20191122/customer.csv");

        FileWriter writer = new FileWriter("/home/kiennguyen/Desktop/data/20191122/customer1.csv");
        writer.append("CUSTOMER_NAME, ID_NUMBER, BIRTH_DATE, SEX, JOB_DES, PHONE, ADDRESS, EMAIL\n");

        int count = 0;

        for (Customer r : customers) {
            if (count % 100 == 0) {
                System.out.println(count);
            }
            count++;
            try {
                writer.append(r.customerName).append(",");
                writer.append(r.idNumber).append(",");
                writer.append(r.birthDate).append(",");
                writer.append(r.sex).append(",");
                writer.append(r.jobDes).append(",");
                writer.append(r.phone).append(",");
                writer.append(r.address).append(",");
                writer.append(r.email).append(",");
                writer.append('\n');
            } catch (IOException e) {
                System.out.println("IOException during writing CSV line: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Exception during writing CSV line: " + e.getMessage());
            }
        }

        writer.flush();
        writer.close();
    }

    public static List<Customer> loadCustomer(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file), '\t');
        String[] line;
        List<Customer> users = new ArrayList<Customer>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            Customer u = new Customer();
            u.customerName = line[0];
            u.idNumber = line[1];
            u.birthDate = line[2];
            u.sex = line[3];
            u.jobDes = line[4];
            u.phone = line[5];
            u.address = line[6];
            u.email = line[7];

            users.add(u);
        }

        return users;
    }
}
