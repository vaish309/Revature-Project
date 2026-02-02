package main.java.com.revhire.app.service;

import main.java.com.revhire.app.dao.ApplicationDAO;

public class ApplicationService {

    private ApplicationDAO applicationDAO = new ApplicationDAO();

    public void applyJob(int jobId, int seekerId) {
        applicationDAO.applyJob(jobId, seekerId);
        System.out.println("✅ Job applied successfully!");
    }

    public void viewMyApplications(int seekerId) {
        applicationDAO.viewMyApplications(seekerId);
    }
    public void viewApplicantsByJob(int jobId) {
        applicationDAO.viewApplicantsByJob(jobId);
    }
    public void shortlistOrReject(int applicationId, int seekerId, String status) {
        applicationDAO.shortlistOrReject(applicationId, seekerId, status);
        System.out.println("✅ Application status updated to " + status);
    }

}

