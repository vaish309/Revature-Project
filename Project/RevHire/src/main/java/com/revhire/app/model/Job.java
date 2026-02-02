package main.java.com.revhire.app.model;

public class Job {

    private int id;
    private String title;
    private String location;
    private double salary;
    private int employerId;

    public Job(String title, String location, double salary, int employerId) {
        this.title = title;
        this.location = location;
        this.salary = salary;
        this.employerId = employerId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public double getSalary() {
        return salary;
    }

    public int getEmployerId() {
        return employerId;
    }
}
