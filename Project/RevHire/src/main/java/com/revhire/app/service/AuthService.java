package main.java.com.revhire.app.service;

import main.java.com.revhire.app.dao.JobSeekerDAO;
import main.java.com.revhire.app.dao.EmployerDAO;
import main.java.com.revhire.app.model.JobSeeker;
import main.java.com.revhire.app.model.Employer;

public class AuthService {

    private JobSeekerDAO jobSeekerDAO = new JobSeekerDAO();
    private EmployerDAO employerDAO = new EmployerDAO();


    public void registerSeeker(String name, String email, String pass) {
        jobSeekerDAO.register(new JobSeeker(name, email, pass));
    }

    public int loginSeeker(String email, String pass) {
        return jobSeekerDAO.login(email, pass);
    }

    public void registerEmployer(String company, String email, String pass) {
        employerDAO.register(new Employer(company, email, pass));
    }

    public int loginEmployer(String email, String pass) {
        return employerDAO.login(email, pass);
    }
}
