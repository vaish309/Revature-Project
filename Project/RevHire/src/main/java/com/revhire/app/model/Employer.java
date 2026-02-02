package main.java.com.revhire.app.model;

public class Employer {

    private int id;
    private String companyName;
    private String email;
    private String password;

    public Employer(String companyName, String email, String password) {
        this.companyName = companyName;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
