package cxp.ingest;

import java.util.Date;

/**
 * Created by markmo on 14/04/15.
 */
public class Job {

    enum Status {
        RUNNING,
        FINISHED,
        ERROR
    }

    private Long id;
    private Long datasetId;
    private Date start;
    private Date end;
    private Status status;
    private String exitMessage;

    public Job() {
        this.start = new Date();
        this.status = Status.RUNNING;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(Long datasetId) {
        this.datasetId = datasetId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getExitMessage() {
        return exitMessage;
    }

    public void setExitMessage(String exitMessage) {
        this.exitMessage = exitMessage;
    }
}
