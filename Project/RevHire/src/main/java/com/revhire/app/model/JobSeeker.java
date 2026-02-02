package main.java.com.revhire.app.model;

public class JobSeeker {

    private int id;
    private String name;
    private String email;
    private String password;

    public JobSeeker(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
