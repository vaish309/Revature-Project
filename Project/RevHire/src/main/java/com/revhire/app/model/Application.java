package main.java.com.revhire.app.model;

public class Application {

    private int id;
    private int jobId;
    private int seekerId;
    private String status;

    public Application(int jobId, int seekerId, String status) {
        this.jobId = jobId;
        this.seekerId = seekerId;
        this.status = status;
    }

    public int getJobId() {
        return jobId;
    }

    public int getSeekerId() {
        return seekerId;
    }

    public String getStatus() {
        return status;
    }

}
