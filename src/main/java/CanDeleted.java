import au.com.bytecode.opencsv.CSVReader;
import se.bonnier.api.util.ObjectUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public class CanDeleted {
    public static void main(String[] args) throws IOException {
//        CSVReader reader = new CSVReader(new FileReader("/home/kiennguyen/Desktop/ssorder_20140610_20140610_digitalt.20140611113445.csv"), ';');
//
//        List<String[]> lines = reader.readAll();
//        String[] header = lines.get(0);
//        String[] row1 = lines.get(1);
//        String str = "";
//        for (int i = 0; i < row1.length; i++) {
//            String s = row1[i];
//            if (s.trim().length() > 0) {
//                str += " -d" + header[i] + "=\"" + s + "\"";
//
//            }
//
//        }
//        System.out.println(str);
        String redirectUrl = "https://www.di.se/nyheter/analytikern-darfor-fortsatter-borsfesten-20-aktier-med-storst-chans-att-lyfta/?v=2";
        redirectUrl = appendParamToUrlIfNeeded(redirectUrl, "aaa", "1", true);
        System.out.println(redirectUrl);
    }

    private static String appendParamToUrlIfNeeded(String url, String param, String value, boolean strict) {
        if (ObjectUtil.empty(param) || ObjectUtil.empty(url) || ObjectUtil.empty(value)) {
            return url;
        }
        boolean shouldAppend;
        if (strict) {
            shouldAppend = !url.contains(param);
        } else {
            Pattern pattern = Pattern.compile("([?&]|%26|%3F)" + param);
            shouldAppend = !pattern.matcher(url).find();
        }

        if ("forceScript".equals(param)) {
            try {
                if (shouldAppend) {
                    StringBuilder urlBuilder = new StringBuilder();
                    int index = url.indexOf("?");
                    if (index > 0) {
                        urlBuilder.append(url.substring(0, index));
                        urlBuilder.append("?").append(param).append("=").append(value).append("&");
                        urlBuilder.append(url.substring(index + 1));
                    } else {
                        urlBuilder.append(url).append("?").append(param).append("=").append(value);
                    }
                    return urlBuilder.toString();
                } else {
                    return url;
                }
            } catch (Exception e) {
                //logger.error("Error during injecting parameters to url: " + url);
            }

            return url;
        } else {
            return url + (!shouldAppend ? "" : (url.indexOf("?") > 0 ? "&" : "?") + param + "=" + value);
        }
    }
}
