package main.java.com.revhire.app.service;

import main.java.com.revhire.app.dao.ResumeDAO;
import main.java.com.revhire.app.model.Resume;

public class ResumeService {

    private ResumeDAO resumeDAO = new ResumeDAO();

    public void saveResume(int seekerId, String objective, String education,
                           String experience, String skills, String projects) {

        Resume resume = new Resume(
                seekerId, objective, education, experience, skills, projects
        );

        resumeDAO.saveOrUpdateResume(resume);
        System.out.println("✅ Resume saved successfully");
    }

    public void viewResume(int seekerId) {
        resumeDAO.viewResume(seekerId);
    }
}
