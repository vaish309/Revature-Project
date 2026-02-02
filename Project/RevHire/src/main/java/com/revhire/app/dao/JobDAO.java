package main.java.com.revhire.app.dao;

import main.java.com.revhire.app.config.DBConfig;
import main.java.com.revhire.app.model.Job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JobDAO {

    public void postJob(Job job) {
        String sql = "INSERT INTO job(title, location, salary, employer_id) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, job.getTitle());
            ps.setString(2, job.getLocation());
            ps.setDouble(3, job.getSalary());
            ps.setInt(4, job.getEmployerId());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void viewJobsByEmployer(int employerId) {
        String sql = "SELECT * FROM job WHERE employer_id=?";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employerId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- Your Job Postings ---");
            while (rs.next()) {
                System.out.println(
                        "Job ID: " + rs.getInt("id") +
                                " | Title: " + rs.getString("title") +
                                " | Location: " + rs.getString("location") +
                                " | Salary: " + rs.getDouble("salary")
                );
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void viewAllJobs() {
        String sql = "SELECT * FROM job";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- All Available Jobs ---");
            while (rs.next()) {
                System.out.println(
                        "Job ID: " + rs.getInt("id") +
                                " | Title: " + rs.getString("title") +
                                " | Location: " + rs.getString("location") +
                                " | Salary: " + rs.getDouble("salary")
                );
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void getJobsByEmployerId(int employerId) {
            String sql = "SELECT * FROM job WHERE employer_id=?";

            try (Connection con = DBConfig.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, employerId);
                ResultSet rs = ps.executeQuery();

                System.out.println("\n--- Your Job Postings ---");

                boolean found = false;

                while (rs.next()) {
                    found = true;
                    System.out.println(
                            "Job ID: " + rs.getInt("id") +
                                    " | Title: " + rs.getString("title") +
                                    " | Location: " + rs.getString("location") +
                                    " | Salary: " + rs.getDouble("salary")
                    );
                }

                if (!found) {
                    System.out.println("❌ No jobs posted yet.");
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


    }

