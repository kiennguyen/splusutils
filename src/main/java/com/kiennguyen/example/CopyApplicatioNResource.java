package com.kiennguyen.example;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.io.FileUtils;
import se.bonnier.api.model.ApiClientFactory;
import se.bonnier.api.model.userservice.Application;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public class CopyApplicatioNResource {
    public CopyApplicatioNResource() {

    }

    public static void main(String[] args) throws IOException {
        new CopyApplicatioNResource().process();
    }

    public void process() throws IOException {
        InputStream str = ClassLoader.getSystemResourceAsStream("applications.csv");
        CSVReader reader = new CSVReader(new InputStreamReader(str));

        String[] line;
        List<ApplicationResource> apps = new ArrayList<ApplicationResource>();
        while ((line = reader.readNext()) != null) {
            String name = line[0];
            String id = line[1];
            String css;
            String js;
            String cssFile = null;
            String jsFile = null;
            if (line.length == 3) {
                cssFile = line[2] + "/main.css";
                jsFile = line[2] + "/main.js";
            } else if (line.length == 4) {
                cssFile = line[2];
                jsFile = line[3];
            }

            css = FileUtils.readFileToString(new File(cssFile));
            js = FileUtils.readFileToString(new File(jsFile));

            ApplicationResource app = new ApplicationResource();
            app.appId = id;
            app.appName = name;
            app.css = css;
            app.js = js;

            apps.add(app);
        }
        String target = "qa";
        ApiClientFactory.initialiseClients(target);
        String token = "6VOnnAOaIieJTPpH66cZ71";
        for (ApplicationResource a : apps) {
            Application application = Application.findByName(a.appName);
            if (application.name.equals(a.appName)) {
                application.mainScript = a.js;
                application.style = a.css;
                Application updatedApp = Application.update(application.id, application, token);
                if (!updatedApp.hasErrors()) {
                    System.out.println("UPDATE DONE");
                } else {
                    System.out.println(application.name);
                    System.out.println(updatedApp.getErrorMessage());
                }
            }
        }
    }

    class ApplicationResource {
        String appName;
        String appId;
        String css;
        String js;
        String path;
    }
}
