package main.java.com.revhire.app.dao;

import main.java.com.revhire.app.config.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ApplicationDAO {

    public void applyJob(int jobId, int seekerId) {
        String sql = "INSERT INTO application(job_id, seeker_id, status) VALUES (?, ?, 'APPLIED')";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, seekerId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void viewMyApplications(int seekerId) {
        String sql = """
                SELECT a.id, j.title, j.location, a.status
                FROM application a
                JOIN job j ON a.job_id = j.id
                WHERE a.seeker_id = ?
                """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, seekerId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- My Job Applications ---");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                        "Application ID: " + rs.getInt("id") +
                                " | Job: " + rs.getString("title") +
                                " | Location: " + rs.getString("location") +
                                " | Status: " + rs.getString("status")
                );
            }

            if (!found) {
                System.out.println("No applications found.");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void viewApplicantsByJob(int jobId) {

        String sql = """
        SELECT a.id, js.name, js.email, a.status
        FROM application a
        JOIN job_seeker js ON a.seeker_id = js.id
        WHERE a.job_id = ?
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- Applicants for Job ID: " + jobId + " ---");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                        "Application ID: " + rs.getInt("id") +
                                " | Name: " + rs.getString("name") +
                                " | Email: " + rs.getString("email") +
                                " | Status: " + rs.getString("status")
                );
            }

            if (!found) {
                System.out.println("No applicants found.");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void updateApplicationStatus(int applicationId, String status) {

        String sql = "UPDATE application SET status=? WHERE id=?";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, applicationId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void shortlistOrReject(int applicationId, int seekerId, String status) {

        updateApplicationStatus(applicationId, status);

        String msg = status.equals("SHORTLISTED")
                ? "🎉 You have been shortlisted!"
                : "❌ Your application was rejected.";

        addNotification(seekerId, msg);
    }

    private void addNotification(int seekerId, String msg) {
    }

    public int getSeekerIdByApplication(int applicationId) {

        String sql = "SELECT seeker_id FROM application WHERE id=?";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, applicationId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("seeker_id");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return -1;
    }

}
