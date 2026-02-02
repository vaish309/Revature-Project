package main.java.com.revhire.app.service;

import main.java.com.revhire.app.dao.JobDAO;
import main.java.com.revhire.app.model.Job;

public class JobService {

    private final JobDAO jobDAO = new JobDAO();

    public void postJob(String title, String location, double salary, int employerId) {
        jobDAO.postJob(new Job(title, location, salary, employerId));
    }

    public void viewMyJobs(int employerId) {
        jobDAO.viewJobsByEmployer(employerId);
    }


    public void viewAllJobs() {
        jobDAO.viewAllJobs();
    }

    public void viewJobsByEmployer(int employerId) {
        jobDAO.getJobsByEmployerId(employerId);
    }
}
