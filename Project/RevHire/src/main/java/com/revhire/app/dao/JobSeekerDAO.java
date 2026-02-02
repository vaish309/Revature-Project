package main.java.com.revhire.app.dao;

import main.java.com.revhire.app.config.DBConfig;
import main.java.com.revhire.app.model.JobSeeker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JobSeekerDAO {

    public void register(JobSeeker js) {
        String sql = "INSERT INTO job_seeker(name, email, password) VALUES (?, ?, ?)";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, js.getName());
            ps.setString(2, js.getEmail());
            ps.setString(3, js.getPassword());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int login(String email, String password) {
        String sql = "SELECT id FROM job_seeker WHERE email=? AND password=?";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
}
