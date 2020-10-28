package com.kiennguyen.example;

import com.kiennguyen.commons.ServicePlusAPI;
import se.bonnier.api.model.userservice.User;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public class MigrateAPIUser extends ServicePlusAPI {
    public MigrateAPIUser(String frameworkId) {
        super(frameworkId);
        this.accessToken = "2NYhrkohF0rSFleukN0uvx";
    }

    public static void main(String[] args) throws Exception {
        MigrateAPIUser app = new MigrateAPIUser("");
        app.process();
    }

    @Override
    public void process() throws Exception {

        User u = User.findById("6ABA2F2ac8ZN7dCOxxJASt");
        System.out.println(u.type);
    }
}
