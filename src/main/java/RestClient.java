import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * A simple FishEye REST client in Java. Prints details of recent changesets to stdout.
 * <p/>
 * You'll need the following libraries:
 * <p/>
 * dom4j   http://dom4j.org (tested with dom4j-1.6.1.jar)
 * jaxen   http://jaxen.org (tested with jaxen-1.1-beta-5.jar)
 */
public class RestClient {
    public static void main(String[] args) {

        RestClient fisheye = new RestClient("http://fisheye.devtest.newsplus.se");
        fisheye.printRecentChangesets("", 2);

    }

    private String fisheyeHost;
    private DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
    private DateFormat printFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    public RestClient(String fisheyeHost) {
        this.fisheyeHost = fisheyeHost;
    }

    public void printRecentChangesets(String rep, int days) {
        // 'service' is the last component of the rest path, i.e. api/rest/<service>
        // If you wanted to execute an EyeQL query, you would set service = "query"
        // and supply the parameters "rep" and "query" to callFisheye()
        String service = "changesets";
        Map<String, String> params = new HashMap<String, String>();
        params.put("rep", rep);
        params.put("path", "/");
        params.put("start", iso8601Format.format(new Date(System.currentTimeMillis() - 24L * 60L * 60L * 1000L * days)));

        try {
            Document changes = callFisheye(service, params);

            Node root = changes.getRootElement();
            if (root.getName().equals("error")) {
                System.err.println("*** received error response: " + root.getText());
            } else {

                List<Node> csids = changes.selectNodes("/response/changesets/csids/string");

                System.out.println("Changes in repository \"" + rep + "\" over the last " + days + " day" +
                        (days != 1 ? "s" : ""));
                System.out.println("(" + csids.size() + " changeset" + (csids.size() != 1 ? "s" : "") + ")");

                for (Node s : csids) {
                    String csid = s.getText().trim();
                    printChangeSet(rep, csid);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }




    private Document callFisheye(String cmd, Map<String, String> params) throws IOException, DocumentException {
        URL url = new URL(fisheyeHost + (fisheyeHost.endsWith("/") ? "" : "/") +
                "api/rest/" + cmd);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);

        PrintWriter out = new PrintWriter(connection.getOutputStream());

        StringBuffer paramStr = new StringBuffer();

        String sep = "";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            paramStr.append(sep).append(key).append("=").append(URLEncoder.encode(val, "UTF-8"));
            sep = "&";
        }

        out.print(paramStr.toString());
        out.close();

        SAXReader xmlReader = new SAXReader();
        return xmlReader.read(connection.getInputStream());
    }

    private void printChangeSet(String rep, String csid) throws IOException, DocumentException, ParseException {

        Map<String, String> params;
        params = new HashMap<String, String>();
        params.put("rep", rep);
        params.put("csid", csid);

        Document changeset = callFisheye("changeset", params);

        Node root = changeset.getRootElement();
        if (root.getName().equals("error")) {
            System.err.println("*** received error response: " + root.getText());
        } else {
            Date timestamp = iso8601Format.parse(changeset.valueOf("/response/changeset/@date"));
            String author = changeset.valueOf("/response/changeset/@author");
            String checkinmsg = changeset.valueOf("/response/changeset/log").trim();
            List<Node> revisions = changeset.selectNodes("/response/changeset/revisions/revisionkey");
            int filecount = revisions.size();
            System.out.println("------");
            System.out.println(" ChangeSet: " + csid + " author=" + author +
                    " date=" + printFormat.format(timestamp) + " (" + filecount + " file" +
                    (filecount != 1 ? "s" : "") + ")");
            System.out.println(" Log: " + checkinmsg);
            System.out.println(" Files:");
            for (Node node : revisions) {
                System.out.println("  " + node.valueOf("@path") + " [" + node.valueOf("@rev") + "]");
            }
            System.out.println();
        }
    }
}


