import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TestCode {
    public static void main(String[] args) throws IOException {
        //System.out.println("Result:" + solution("+ 3 DUP 5 -"));
        //System.out.println("Result:" + solution(2,2,2,-3));
        //System.out.println("Result:" + solution(-1,3,3,1));
        //System.out.println("Result:" + solution(-2,0,3,0));
//        for (int i = 0; i < 50; i++) {
//            System.out.println("password : " + generateRandomPassword());
//            System.out.println("\n");
//        }

        String text = "http://ripple.bip.local.se/subscribe/webstand?appId=di.se&version=2&token=2aRYWhd7N5s9f4aEgfEJ5i&id=3d9jOge7t2k8yRLVoKgkBs&remembered=true&sl_ver=2-digital&token=&id=&remembered=false&token=1VH2EuHKjifVVc5lZqrZdL&id=0AREANQzPyE1kLx8YQpKrq&remembered=true&sl_ver=2-digital&token=&id=&remembered=false";
        //String text = "https://pren.di.se/pren/digital-paketering/digitalpaper/order/digital?token=&id=&remembered=false&sl_ver=2-digital";
        //String newText = text.replaceAll("\\btoken=.*?(&|$)","$1").replaceAll("\\bid=.*?(&|$)","$1").replaceAll("\\bremembered=.*?(&|$)","$1");
        String newText = text.replaceAll("[&?](token|id|remembered|sl_ver|splus_new_account|responseCode)(=[^&]*)?|^(token|id|remembered|sl_ver|splus_new_account|responseCode)(=[^&]*)?&?","");
        System.out.println(newText);
    }

    private static String generateRandomPassword() {
        final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        final String NUMBER = "0123456789";
        final String OTHER_CHAR = "!@#$%&*()_+-=[]?";

        final Random random = new Random();
        final String CHAR_ALLOW = shuffleString(CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR);

        final int newPasswordLength = 8;
        StringBuilder password = new StringBuilder(newPasswordLength);
        int rndCharAt = random.nextInt(CHAR_LOWER.length());
        password.append(CHAR_LOWER.charAt(rndCharAt));

        rndCharAt = random.nextInt(CHAR_UPPER.length());
        password.append(CHAR_UPPER.charAt(rndCharAt));

        rndCharAt = random.nextInt(NUMBER.length());
        password.append(NUMBER.charAt(rndCharAt));

        rndCharAt = random.nextInt(OTHER_CHAR.length());
        password.append(OTHER_CHAR.charAt(rndCharAt));

        for (int i = 0; i < newPasswordLength - 4; i++) {
            rndCharAt = random.nextInt(CHAR_ALLOW.length());
            char rndChar = CHAR_ALLOW.charAt(rndCharAt);
            password.append(rndChar);
        }

        return shuffleString(password.toString());
    }

    private static String shuffleString(String string) {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        return letters.stream().collect(Collectors.joining());
    }

    public static String solution1(int AX, int AY, int BX, int BY) {
        String result = "";
        int min = 99999;
        for (int i = -50; i < 50; i++) {
            for (int j = -50; j < 50; j++) {
            }
        }
        return result;
    }

    public static String solution(int AX, int AY, int BX, int BY) {
        String point = "";
        int min = 99999;
        for (int x = -50; x < 50; x++) {
            for (int y = -50; y < 50; y++) {
                if ((BY - AY) * (y - BY) + (BX - AX) * (x - BX) == 0) {
                    if ((BX - AX) * (y - AY) - (BY - AY) * (x - AX) > 0) continue;
                    int d =  (y - BY) * (y - BY) + (x - BX) * (x - BX);
                    if ((d > 0) && (d < min)) {
                        min = d;
                        point = x + "," + y;
                    }
                }
            }
        }
        return point;
    }

    public static int solution(String S) {
        int[] stack = new int[2001];
        int position = -1;
        for (String s : S.split(" ")) {
            if (s.equals("POP")) {
                position--;
                if (position < 0) return -1;
            } else if (s.equals("DUP")) {
                stack[position + 1] = stack[position];
                position++;
                if (position > 2000) return -1;
            } else if (s.equals("+")) {
                if (position == 0) return -1;
                int sum = stack[position] + stack[position - 1];
                if (sum > 1024 * 1024 - 1) return -1;
                stack[position-1] = sum;
                position--;
            } else if (s.equals("-")) {
                if (position == 0) return -1;
                int subtract = stack[position] - stack[position-1];
                if (subtract < 0) return -1;
                stack[position-1] = subtract;
                position--;
            } else {
                try {
                    int number = Integer.parseInt(s);
                    if ((number < 0) || (number > 1024 * 1024 - 1)) return -1;
                    position++;
                    stack[position] = number;
                } catch (Exception e) {
                    return -1;
                }
            }
        }
        return stack[position];
    }

    public static int functionTest() {
        int[] input = {2, 1, 3, 5, 4};
        int sum = 0;
        int count = 0;
        Set set = new HashSet();
        for (int i=0; i<input.length; i++) {
            set.add(input[i]);
            if (input[i] > set.size()) {

            }
            sum+=input[i];
            if (sum*2 == (i+1)*(i+2)) {
                System.out.println(i + "th");
                count++;
            }
        }
        return count;
    }
}
