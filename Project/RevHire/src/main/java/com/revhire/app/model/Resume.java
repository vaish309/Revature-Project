package main.java.com.revhire.app.model;

public class Resume {

    private int seekerId;
    private String objective;
    private String education;
    private String experience;
    private String skills;
    private String projects;

    public Resume(int seekerId, String objective, String education,
                  String experience, String skills, String projects) {
        this.seekerId = seekerId;
        this.objective = objective;
        this.education = education;
        this.experience = experience;
        this.skills = skills;
        this.projects = projects;
    }

    public int getSeekerId() {
        return seekerId;
    }

    public String getObjective() {
        return objective;
    }

    public String getEducation() {
        return education;
    }

    public String getExperience() {
        return experience;
    }

    public String getSkills() {
        return skills;
    }

    public String getProjects() {
        return projects;
    }
}
