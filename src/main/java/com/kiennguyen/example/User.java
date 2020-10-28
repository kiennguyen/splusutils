package com.kiennguyen.example;

/**
 * @author kien.nguyen
 */
public class User {
    public String email;
    public String subscriberId;
    public String firstName;
    public String lastName;
    public String paper;
    public String product;

    public User(String subscriberId, String email, String firstName, String lastName, String paper, String product) {
        this.subscriberId = subscriberId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.paper = paper;
        this.product = product;
    }

    public User(String email) {
        this.email = email;
    }
}
