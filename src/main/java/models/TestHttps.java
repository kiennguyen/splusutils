package models;

import java.io.*;
import java.net.*;

public class TestHttps {
    public static void main(String[] args) {
        try {
            URL url = new URL("https://d2c-a1electroluxvnuat.eluxmkt.com/api/v1/track");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("apikey", "e7f6a158-efbb-45c7-884b-495ecb7b1e9f");
            con.setDoOutput(true);
            String jsonInputString = "{\n" +
                    "  \"track_response\": {\n" +
                    "    \"order_number\": \"PO5552\",\n" +
                    "    \"status_code\": \"40\",\n" +
                    "    \"status_description\": \"Delivered\",\n" +
                    "    \"status_message\": \"1st time no show, retrying successfully 2nd time\",\n" +
                    "    \"updated_time_UTC\": \"2020-06-22T09:30:39Z\",\n" +
                    "    \"serial_numbers\": [\n" +
                    "      {\n" +
                    "        \"956 004 010\": \"95136199\"\n" +
                    "      },\n" +
                    "\n" +
                    "      {\n" +
                    "        \"942 001 279\": \"95136288\"\n" +
                    "      }\n" +
                    "\n" +
                    "    ]\n" +
                    "\n" +
                    "  }\n" +
                    "\n" +
                    "}";
            OutputStream os = con.getOutputStream();
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


