package com.kiennguyen.example;

import com.kiennguyen.commons.ServicePlusAPI;
import se.bonnier.api.model.userservice.Application;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public class CloneApplication extends ServicePlusAPI {
    public static void main(String[] args) throws Exception {
        CloneApplication app = new CloneApplication("qa");
        app.process();
    }
    public CloneApplication(String frameworkId) {
        super(frameworkId);
    }

    @Override
    public void process() throws Exception {
        Application application = Application.findByName("dagensnyheter.se");
        if (!application.hasErrors()) {
            application.name = "dagens.dn.se";
            Application newapp = Application.create(application);
            if (newapp.hasErrors()) {
                System.out.println(newapp.getErrorMessage());
            } else {
                System.out.println("OK");
            }
        }
    }

    private void getApplication(String name) {

        Application.findByName(name);
    }
}
