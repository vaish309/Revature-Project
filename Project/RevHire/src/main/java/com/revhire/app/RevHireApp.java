package main.java.com.revhire.app;
import main.java.com.revhire.app.service.*;
import main.java.com.revhire.app.service.ApplicationService;
import main.java.com.revhire.app.config.DBConfig;
import java.util.Scanner;

public class RevHireApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        AuthService auth = new AuthService();
        JobService jobService = new JobService();
        ApplicationService applicationService = new ApplicationService();
        ResumeService resumeService = new ResumeService();

        class TestDB {
            public static void main(String[] args) {
                try {
                    System.out.println(
                            main.java.com.revhire.app.config.DBConfig.getConnection()
                    );
                    System.out.println("✅ DB CONNECTED");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        while (true) {
            System.out.println("\n=== RevHire ===");
            System.out.println("1. Job Seeker Register");
            System.out.println("2. Job Seeker Login");
            System.out.println("3. Employer Register");
            System.out.println("4. Employer Login");
            System.out.println("0. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                // JOB SEEKER REGISTER
                case 1:
                    System.out.print("Name: ");
                    String name = sc.nextLine();
                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    System.out.print("Password: ");
                    String pass = sc.nextLine();
                    auth.registerSeeker(name, email, pass);
                    System.out.println("✅ Job Seeker registered");
                    break;

                //  JOB SEEKER LOGIN
                case 2:
                    System.out.print("Email: ");
                    email = sc.nextLine();
                    System.out.print("Password: ");
                    pass = sc.nextLine();

                    int seekerId = auth.loginSeeker(email, pass);
                    if (seekerId == -1) {
                        System.out.println("❌ Invalid credentials");
                        break;
                    }

                    System.out.println("✅ Job Seeker login success");

                    // JOB SEEKER MENu
                    while (true) {
                        System.out.println("1. View All Jobs");
                        System.out.println("2. Apply Job");
                        System.out.println("3. View My Applications");
                        System.out.println("4. Create / Update Resume");
                        System.out.println("5. View Resume");
                        System.out.println("0. Logout");

                        int ch = sc.nextInt();
                        sc.nextLine();

                        if (ch == 1) {
                            jobService.viewAllJobs();

                        }
                        else if (ch == 2) {
                            System.out.print("Enter Job ID to apply: ");
                            int jobId = sc.nextInt();
                            sc.nextLine();

                            applicationService.applyJob(jobId, seekerId);
                        }
                        else if (ch == 4) {
                            System.out.print("Objective: ");
                            String obj = sc.nextLine();

                            System.out.print("Education: ");
                            String edu = sc.nextLine();

                            System.out.print("Experience: ");
                            String exp = sc.nextLine();

                            System.out.print("Skills: ");
                            String skills = sc.nextLine();

                            System.out.print("Projects: ");
                            String projects = sc.nextLine();

                            resumeService.saveResume(seekerId, obj, edu, exp, skills, projects);

                        } else if (ch == 5) {
                            resumeService.viewResume(seekerId);
                        }
                        else if (ch == 3) {
                            applicationService.viewMyApplications(seekerId);

                        } else {
                            break;
                        }
                    }
                    break;

                // EMPLOYER REGISTER
                case 3:
                    System.out.print("Company Name: ");
                    String company = sc.nextLine();
                    System.out.print("Email: ");
                    email = sc.nextLine();
                    System.out.print("Password: ");
                    pass = sc.nextLine();
                    auth.registerEmployer(company, email, pass);
                    System.out.println("✅ Employer registered");
                    break;

                //  EMPLOYER LOGIN
                case 4:
                    System.out.print("Email: ");
                    email = sc.nextLine();
                    System.out.print("Password: ");
                    pass = sc.nextLine();

                    int employerId = auth.loginEmployer(email, pass);
                    if (employerId == -1) {
                        System.out.println("❌ Invalid credentials");
                        break;
                    }

                    System.out.println("✅ Employer logged in");

                    while (true) {
                        System.out.println("\n--- Employer Menu ---");
                        System.out.println("1. Post Job");
                        System.out.println("2. View My Jobs");
                        System.out.println("3. View Applicants");
                        System.out.println("0. Logout");

                        int ch = sc.nextInt();
                        sc.nextLine();

                        if (ch == 1) {
                            System.out.print("Job Title: ");
                            String title = sc.nextLine();
                            System.out.print("Location: ");
                            String location = sc.nextLine();
                            System.out.print("Salary: ");
                            double salary = sc.nextDouble();
                            sc.nextLine();

                            jobService.postJob(title, location, salary, employerId);
                            System.out.println("✅ Job posted successfully");

                        }
                        else if (ch == 2) {
                            jobService.viewJobsByEmployer(employerId);
                        }
                        else if (ch == 3) {
                            System.out.print("Enter Job ID: ");
                            int jobId = sc.nextInt();
                            sc.nextLine();

                            applicationService.viewApplicantsByJob(jobId);
                        }
                        else {
                            break;
                        }
                    }
                    break;

                case 0:
                    System.exit(0);
            }
        }
    }
}
