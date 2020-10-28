package models;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {
    public static void main(String[] args) {
//        String ua = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/80.0.3987.95 Mobile/15E148 Safari/604.1\n";
//        String regex = "\\(iP.+; CPU .*OS (\\d+)_(\\d)*.*\\) AppleWebKit";
//        Pattern p = Pattern.compile(regex);
//        Matcher m = p.matcher(ua);
//        while (m.find()) {
//            System.out.println(m.group(1)); // Prints 12345
//            System.out.println(m.group(2)); // Prints 12345
//            for (int i = 0; i <= m.groupCount(); i++) {
//                System.out.println("GroupIOS " + i + ": " + m.group(i));
//            }
//        }

        String iosUserAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/80.0.3987.95 Mobile/15E148 Safari/604.1";
        //System.out.println(isIosVersion(12, iosUserAgent));

        String safariUserAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1 Mobile/15E148 Safari/604.1";
        //System.out.println(isSafari(safariUserAgent));

        String macEmbeddedUserAgent = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_6; en-en) AppleWebKit/533.19.4 (KHTML, like Gecko) Version/5.0.3 Safari/533.19.4";
        //System.out.println(isMacEmbeddedBrowser(macEmbeddedUserAgent));

        String ucUserAgent = "Mozilla/5.0 (Linux; U; Android 6.0.1; zh-CN; F5121 Build/34.0.A.1.247) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.5.1.944 Mobile Safari/537.36";
        //System.out.println(isUcBrowser(ucUserAgent));
        //System.out.println(isUcBrowserVersionAtLeast(12, 13, 2, ucUserAgent));

        String chromeUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
        //System.out.println(isChromiumBased(chromeUserAgent));
        //System.out.println(isChromiumVersionAtLeast(67, chromeUserAgent));

        String chrome85MacOsX = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36";
        String chromeIOS12 = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/80.0.3987.95 Mobile/15E148 Safari/604.1";
        System.out.println(isSafari(chromeIOS12));
    }

    public static boolean isMacEmbeddedBrowser(String userAgent) {
        String regex = "Mozilla/.* \\(Macintosh;.*Mac OS X .*\\) AppleWebKit/.* \\(KHTML, like Gecko\\).*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(userAgent);
        return m.matches();
    }

    public static boolean isMacosxVersion(int major, int minor, String useragent) {
        String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36\n";
        String regex = "\\(Macintosh;.*Mac OS X (\\d+)_(\\d+)_(\\d)*.*\\) AppleWebKit";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(ua);
        while (m.find()) {
            System.out.println(m.group(1)); // Prints 12345
            System.out.println(m.group(2)); // Prints 12345
            System.out.println(m.group(3)); // Prints 12345
            for (int i = 0; i <= m.groupCount(); i++) {
                System.out.println("GroupMacOsX " + i + ": " + m.group(i));
            }
        }
        return false;
    }

    public static boolean isIosVersion(int major, String userAgent) {
        String regex = "\\(iP.+; CPU .*OS (\\d+)_(\\d)*.*\\) AppleWebKit";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(userAgent);
        return (m.find() && major == Integer.valueOf(m.group(1)));
    }

    public static boolean isSafari(String userAgent) {
        String regex = ".*Version/.* Safari/.*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(userAgent);
        return m.matches();
    }

    public static boolean isChromiumBased(String userAgent) {
        String regex = ".*Chrom(e|ium).*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(userAgent);
        return m.matches();
    }

    private static boolean isUcBrowser(String userAgent) {
        try {
            String regex = ".*UCBrowser.*";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(userAgent);
            return m.matches();
        } catch (Exception e) {
        }
        return false;
    }

    private static boolean isUcBrowserVersionAtLeast(int major, int minor, int build, String userAgent) {
        try {
            String regex = "UCBrowser/(\\d+)\\.(\\d+)\\.(\\d+)[\\.\\d]* ";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(userAgent);
            if (m.find()) {
                int majorVersion = Integer.parseInt(m.group(1));
                int minorVersion = Integer.parseInt(m.group(2));
                int buildVersion = Integer.parseInt(m.group(3));
                if (majorVersion != major) {
                    return majorVersion > major;
                }
                if (minorVersion != minor) {
                    return minorVersion > minor;
                }
                return buildVersion >= build;
            }
        } catch (Exception e) {
        }
        return false;
    }

    private static boolean isChromiumVersionAtLeast(int major, String userAgent) {
        try {
            String regex = "Chrom[^ /]+/(\\d+)[\\.\\d]* ";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(userAgent);
            if (m.find()) {
                return Integer.parseInt(m.group(1)) > major;
            }
        } catch (Exception e) {
        }
        return false;
    }
}


