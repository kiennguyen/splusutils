package com.kiennguyen;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.kiennguyen.model.ExternalUser;
import com.kiennguyen.model.SplusOrder;
import com.kiennguyen.model.SplusUser;
import se.bonnier.api.model.entitlementservice.Entitlement;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


public class CSVManager {
//    public static void main(String[] args) throws IOException {
//        List<User> users = loadUser("/home/kiennguyen/working/niteco/data/di_user_20140612.csv");
//        Map<String, Entitlement> entitlements = loadEntitlement("/home/kiennguyen/working/niteco/data/di_paid_entitlement_20140612.csv");
//        List<ReportedUser> results = new ArrayList<ReportedUser>();
//        for (User u : users) {
//            ReportedUser ru = new ReportedUser();
//            ru.userId = u.id;
//            ru.email = u.email;
//            if (entitlements.containsKey(u.id)) {
//                ru.subscriberId = entitlements.get(u.id).externalSubscriberId;
//                ru.subscriptionId = entitlements.get(u.id).externalSubscriptionId;
//            }
//
//            results.add(ru);
//        }
//
//        writeReport("/home/kiennguyen/working/niteco/data/reported/srp_5696.csv", results);
//
//    }

//    public static void main(String[] args) throws IOException {
//        List<HdUser> hdUsers = loadHdUser("/home/kiennguyen/Desktop/hd_data/hd_profiles.csv");
//        List<SplusUser> splusUsers = loadSplusUser("/home/kiennguyen/Desktop/hd_data/sds_splus_20150318.csv");
//
//        int count = 0, conflict = 0;
//        Set<String> emails = new HashSet<String>();
//        List<ReportedUser> results = new ArrayList<ReportedUser>();
//        for (SplusUser splus : splusUsers) {
//            for (HdUser hd : hdUsers) {
//                if (splus.email.equalsIgnoreCase(hd.email)) {
//                    ReportedUser user = new ReportedUser();
//                    user.email = splus.email;
//                    user.firstName = splus.firstName;
//                    user.lastName = splus.lastName;
//                    user.subscriberId = splus.externalSubscriberId;
//                    user.kayakSubscriberId = hd.externalSubscriberId;
//                    if (!emails.contains(user.email)) {
//                        results.add(user);
//                        conflict++;
//                        emails.add(user.email);
//                    } else {
//                        System.out.println("Duplicate collide: " + user.email);
//                    }
//                }
//            }
//            count++;
//            if (count % 1000 == 0) {
//                System.out.println("Processed splus users: " + count);
//            }
//        }
//
//        System.out.println("There are conflicted users: " + conflict);
//
//        writeReport("/home/kiennguyen/Desktop/hd_data/sds_hd_conflict_20150318.csv", results);
//
//    }

    public static void main(String[] args) throws IOException {
//        List<String> externalEmails = loadSplusUserId("/home/kiennguyen/Desktop/data/SRP-6464/all_aktiva_digitala_20150622.csv");
//        List<SplusUser> splusUsers = loadSplusOrder("/home/kiennguyen/Desktop/data/SRP-6464/di_users_20150629.csv");
//        List<SplusEntitlement> splusEntitlements = loadSplusOrder("/home/kiennguyen/Desktop/data/SRP-6464/di_active_entitlements_20150629.csv");
//
//        FileWriter writer = new FileWriter("/home/kiennguyen/Desktop/data/SRP-6464/all_aktiva_digitala_20150622_marked.csv");
//        writer.append("TP_ID, emailaddress, state, user Id\n");
//        boolean existed;
//        SplusOrder order = null;
//        int count = 1;
//        List<SplusUser> results = new ArrayList<SplusUser>();
//        for (String splus : userIds) {
//            existed = false;
//            order = null;
//            for (SplusOrder o : splusUsers) {
//                if (splus.equals(o.transactionReference)) {
//                    existed = true;
//                    order = o;
//                    break;
//                }
//            }
//
//            if (order != null) {
//                try {
//                    writer.append(order.transactionReference).append(",");
//                    writer.append(order.id).append(",");
//                    writer.append(order.userId).append(",");
//                    writer.append(order.paymentProviderStatus).append(",");
//                    writer.append(order.state).append(",");
//                    writer.append(order.userEmail).append(",");
//                    writer.append(order.recurringReference).append(",");
//                    writer.append(order.recurringCode);
//                    writer.append('\n');
//                } catch (IOException e) {
//                    System.out.println("IOException during writing CSV line: " + e.getMessage());
//                } catch (Exception e) {
//                    System.out.println("Exception during writing CSV line: " + e.getMessage());
//                }
//            } else {
//                try {
//                    writer.append(splus).append(",");
//                    writer.append(",,,,,,");
//                    writer.append('\n');
//                } catch (IOException e) {
//                    System.out.println("IOException during writing CSV line: " + e.getMessage());
//                } catch (Exception e) {
//                    System.out.println("Exception during writing CSV line: " + e.getMessage());
//                }
//            }
//
//        }
//
//        writer.flush();
//        writer.close();

    }

//    public static void main(String[] args) throws IOException {
//        List<SplusUser> users = loadSplusUserId("/home/kiennguyen/Desktop/export/users.csv");
//        Map<String, Entitlement> entitlements = loadEntitlement("/home/kiennguyen/Desktop/export/entitlements.csv");
//
//        int count = 1;
//        List<SplusUser> results = new ArrayList<SplusUser>();
//        for (SplusUser splus : users) {
//            Entitlement e = entitlements.get(splus.id);
//
//            if (e != null) {
//                splus.externalSubscriberId = e.externalSubscriberId;
//            }
//
//            results.add(splus);
//
//            count++;
//            if (count % 1000 == 0) {
//                System.out.println("Processed splus users: " + count);
//            }
//        }
//
//        writeSplusReport1("/home/kiennguyen/Desktop/export/di_users_with_subscriber.csv", results);
//
//    }

    private static void writeReport(String file, List<ReportedUser> users) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(file));
        for (ReportedUser ru : users) {
            writer.writeNext(new String[] {ru.firstName, ru.lastName, ru.email, ru.kayakSubscriberId, ru.subscriberId});
        }
        writer.flush();
    }

    private static void writeSplusReport1(String file, List<SplusUser> users) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(file));
        for (SplusUser ru : users) {
            writer.writeNext(new String[] {ru.id, ru.externalSubscriberId, ru.email});
        }
        writer.flush();
    }

    private static void writeSplusReport(String file, List<SplusUser> users) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(file));
        for (SplusUser ru : users) {
            writer.writeNext(new String[] {ru.id, ru.email, ru.firstName, ru.lastName, ru.phoneNumber, ru.brandId, formatDateTime(ru.created), formatDateTime(ru.updated)});
        }
        writer.flush();
    }

    private static void writeToCSVFile(List<SplusUser> users, String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);

        writer.append("User Id, Email, First Name, Last Name, Phone Number, Brand Id, Created, Updated\n");
        for (SplusUser user : users) {
            writeCSVLine(writer, user);
        }

        writer.flush();
        writer.close();
    }

    private static void writeCSVLine(FileWriter writer,
                              SplusUser user) {
        try {
            writer.append(user.id).append(",");
            writer.append(user.email).append(",");
            writer.append(user.firstName).append(",");
            writer.append(user.lastName).append(",");
            writer.append(user.phoneNumber).append(",");
            writer.append(user.brandId).append(",");
            writer.append(formatDateTime(user.created)).append(",");
            writer.append(formatDateTime(user.updated));
            writer.append('\n');
        } catch (IOException e) {
            System.out.println("IOException during writing CSV line: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception during writing CSV line: " + e.getMessage());
        }

    }

    private static String formatDateTime(Long milliseconds) {
        if (milliseconds == null) {
            return "null";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
        Date date = new Date();
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        return sdf.format(milliseconds);
    }

    public static List<SplusUser> loadSplusUser(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] line;
        List<SplusUser> users = new ArrayList<SplusUser>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            SplusUser u = new SplusUser();
            u.id = line[0];
            u.email = line[1];
            u.firstName = line[2];
            u.lastName = line[3];
            u.phoneNumber = line[4];
            u.brandId = line[5];
            u.created = Long.parseLong(line[6]);
            u.updated = Long.parseLong(line[7]);

            users.add(u);
        }

        return users;
    }

    public static List<SplusOrder> loadSplusOrder(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] line;
        List<SplusOrder> users = new ArrayList<SplusOrder>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            SplusOrder u = new SplusOrder();
            u.id = line[0];
            u.userId = line[1];
            u.paymentProviderStatus = line[2];
            u.transactionReference = line[3];
            u.state = line[4];
            u.userEmail = line[5];
            u.recurringReference = line[6];
            u.recurringCode = line[7];

            users.add(u);
        }

        return users;
    }

    public static List<String> loadSplusUserId(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] line;
        List<String> users = new ArrayList<String>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            users.add(line[0]);
        }

        return users;
    }

//    public static List<SplusUser> loadSplusUserId(String file) throws IOException {
//        CSVReader reader = new CSVReader(new FileReader(file));
//        String[] line;
//        List<SplusUser> users = new ArrayList<SplusUser>();
//        reader.readNext();
//        while ((line = reader.readNext()) != null) {
//            SplusUser user = new SplusUser();
//            user.id = line[0];
//            user.email = line[1];
//            users.add(user);
//        }
//
//        return users;
//    }

//    public static List<String> loadSplusUserId(String file) throws IOException {
//        CSVReader reader = new CSVReader(new FileReader(file));
//        String[] line;
//        List<String> users = new ArrayList<String>();
//        reader.readNext();
//        while ((line = reader.readNext()) != null) {
//            users.add(line[0]);
//        }
//
//        return users;
//    }

    public static List<ExternalUser> loadHdUser(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] line;
        List<ExternalUser> users = new ArrayList<ExternalUser>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            ExternalUser u = new ExternalUser();
            u.email = line[0];
            u.externalSubscriberId = line[1];

            users.add(u);
        }

        return users;
    }

    public static List<IpAddress> loadIpAddress(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] line;
        List<IpAddress> users = new ArrayList<IpAddress>();
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            IpAddress u = new IpAddress();
            u.ip = line[0];
            u.count = Integer.parseInt(line[1]);
            users.add(u);
        }

        return users;
    }

    private static List<IpAddress> mergerSum(List<IpAddress> objects) {
        List<IpAddress> results = new ArrayList<IpAddress>();
        for (IpAddress o : objects) {

        }
        return results;
    }

    private static void writeIpAddressReport(String file, List<IpAddress> users) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(file));
        for (IpAddress ru : users) {
            writer.writeNext(new String[] {ru.ip, ru.count.toString()});
        }
        writer.flush();
    }

    public static Map<String, Entitlement> loadEntitlement(String file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] line;
        Map<String, Entitlement> mapEntitlements = new HashMap<String, Entitlement>();
        while ((line = reader.readNext()) != null) {
            Entitlement e = new Entitlement();
            //e.id = line[0];
            e.userId = line[0];
            e.externalSubscriberId = line[1];
            //e.productId = line[2];
            //e.externalSubscriberId = line[3];
            //e.externalSubscriptionId = line[4];
            //if (isPaidSubscription(e.externalSubscriptionId)) {
                mapEntitlements.put(e.userId, e);
            //}
        }
        return mapEntitlements;
    }

    private static boolean isPaidSubscription(String externalSubscriptionId) {
        try {
            Integer.parseInt(externalSubscriptionId);
            return true;
        } catch (NumberFormatException nfe) {
//            System.out.println(externalSubscriptionId);
        }
        return false;
    }

    static class ReportedUser {
        String userId;
        String email;
        String subscriptionId;
        String subscriberId;
        String firstName;
        String lastName;
        String kayakSubscriberId;
    }

    static class IpAddress {
        String ip;
        Integer count;
    }
}
