package com.kiennguyen;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author kien.nguyen
 */
public class MainTest {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

//    public static void main(String[] args) throws IOException, SignatureException {
//        String data = "sds.2014";
//        String key = "ccfe52d6a9";
//        String result;
//        try {
//            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
//            Mac mac = Mac.getInstance("HmacSHA256");
//            mac.init(signingKey);
//            byte[] rawHmac = mac.doFinal(data.getBytes());
//            //result = Base64.encodeBase64String(rawHmac);
//            StringBuilder buf = new StringBuilder();
//            for (byte aData : rawHmac) {
//                int halfbyte = (aData >>> 4) & 0x0F;
//                int two_halfs = 0;
//                do {
//                    if ((0 <= halfbyte) && (halfbyte <= 9)) {
//                        buf.append((char) ('0' + halfbyte));
//                    } else {
//                        buf.append((char) ('a' + (halfbyte - 10)));
//                    }
//                    halfbyte = aData & 0x0F;
//                } while (two_halfs++ < 1);
//            }
//            result = buf.toString();
//        } catch (Exception e) {
//            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
//        }
//
//        System.out.println(result);
//    }
    public static void main(String[] args) throws Exception {
        //rsa();
        try {
            System.out.println("Try");
            throw new Exception();
//        } catch (Exception e) {
//            System.out.println("Catch");
//            throw new NullPointerException();
        } finally {
            System.out.println("Finnally");

        }

        //System.out.println("Out of Try/Catch");
    }
}
