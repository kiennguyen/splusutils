import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public class CheckRemaining {
    public static void main(String[] args) throws IOException {
        List<String> vietList = new ArrayList<String>();
        List<String> khoiList = new ArrayList<String>();
        Map<String, Map<String, String>> khoiMap = new LinkedHashMap<String, Map<String, String>>();// ConcurrentHashMap<String, Map<String, String>>();
        CSVReader reader1 = new CSVReader(new FileReader("/home/kiennguyen/Desktop/viet.csv"));
        List<String[]> list1 = reader1.readAll();

        for (String[] line : list1) {
            vietList.add(line[0]);
        }

        CSVReader reader2 = new CSVReader(new FileReader("/home/kiennguyen/Desktop/khoi.csv"));
//        List<String[]> list2 = reader2.readAll();
        reader2.readNext();
        String [] line;
        while ((line = reader2.readNext()) != null) {
            Map<String, String> info = new HashMap<String, String>();
            info.put("email", line[0]);
            info.put("id", line[1]);
            info.put("first_name", line[2]);
            info.put("last_name", line[3]);
            String createdTime = line[4];
            Date d = new Date(Long.parseLong(createdTime));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            info.put("created", sdf.format(d));
            khoiMap.put(line[0], info);
        }

        System.out.println(vietList.size());
        System.out.println(khoiMap.size());
        Iterator<String> ite = khoiMap.keySet().iterator();
        Iterator<Map.Entry<String, Map<String, String>>> ite1 = khoiMap.entrySet().iterator();
        while (ite.hasNext()) {
            String s = ite.next();
            if (vietList.contains(s)) {
//                khoiMap.remove(s);
                ite.remove();
                System.out.println(s);
            }
        }


        CSVWriter writer = new CSVWriter(new FileWriter("/home/kiennguyen/Desktop/remaining.csv"));
        System.out.println(khoiMap.size());
        ite = khoiMap.keySet().iterator();
        while (ite.hasNext()) {
            Map<String, String> obj = khoiMap.get(ite.next());
            String line1[] = new String[5];
            line1[0] = obj.get("email");
            line1[1] = obj.get("id");
            line1[2] = obj.get("first_name");
            line1[3] = obj.get("last_name");
            line1[4] = obj.get("created");
            writer.writeNext(line1);
        }

        writer.flush();
        writer.close();

    }
}
