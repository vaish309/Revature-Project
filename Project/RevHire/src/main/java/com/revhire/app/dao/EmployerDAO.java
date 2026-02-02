package main.java.com.revhire.app.dao;

import main.java.com.revhire.app.config.DBConfig;
import main.java.com.revhire.app.model.Employer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmployerDAO {

    public void register(Employer employer) {
        String sql = "INSERT INTO employer(company_name, email, password) VALUES (?, ?, ?)";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, employer.getCompanyName());
            ps.setString(2, employer.getEmail());
            ps.setString(3, employer.getPassword());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int login(String email, String password) {
        String sql = "SELECT id FROM employer WHERE email=? AND password=?";

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
