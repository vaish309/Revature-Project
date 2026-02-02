package main.java.com.revhire.app.dao;

import main.java.com.revhire.app.config.DBConfig;
import main.java.com.revhire.app.model.Resume;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ResumeDAO {

    // CREATE or UPDATE resume
    public void saveOrUpdateResume(Resume resume) {

        String sql = """
            INSERT INTO resume (seeker_id, objective, education, experience, skills, projects)
            VALUES (?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                objective=?,
                education=?,
                experience=?,
                skills=?,
                projects=?
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, resume.getSeekerId());
            ps.setString(2, resume.getObjective());
            ps.setString(3, resume.getEducation());
            ps.setString(4, resume.getExperience());
            ps.setString(5, resume.getSkills());
            ps.setString(6, resume.getProjects());

            ps.setString(7, resume.getObjective());
            ps.setString(8, resume.getEducation());
            ps.setString(9, resume.getExperience());
            ps.setString(10, resume.getSkills());
            ps.setString(11, resume.getProjects());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // VIEW resume
    public void viewResume(int seekerId) {

        String sql = "SELECT * FROM resume WHERE seeker_id=?";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, seekerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\n--- My Resume ---");
                System.out.println("Objective: " + rs.getString("objective"));
                System.out.println("Education: " + rs.getString("education"));
                System.out.println("Experience: " + rs.getString("experience"));
                System.out.println("Skills: " + rs.getString("skills"));
                System.out.println("Projects: " + rs.getString("projects"));
            } else {
                System.out.println("❌ Resume not created yet.");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
