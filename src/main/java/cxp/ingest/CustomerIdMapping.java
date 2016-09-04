package cxp.ingest;

import org.joda.time.LocalDateTime;

import java.sql.Timestamp;

/**
 * Created by markmo on 28/04/15.
 */
public class CustomerIdMapping {

    private int customerIdTypeId1;
    private String customerId1;
    private int customerIdTypeId2;
    private String customerId2;
    private Double confidence;
    private Timestamp start;
    private Timestamp end;
    private Long jobId;

    public CustomerIdMapping(int customerIdTypeId1, String customerId1,
                             int customerIdTypeId2, String customerId2,
                             Double confidence,
                             LocalDateTime start, LocalDateTime end,
                             Long jobId) {
        this.customerIdTypeId1 = customerIdTypeId1;
        this.customerId1 = customerId1;
        this.customerIdTypeId2 = customerIdTypeId2;
        this.customerId2 = customerId2;
        this.confidence = confidence;
        setStart(start);
        setEnd(end);
        this.jobId = jobId;
    }

    public int getCustomerIdTypeId1() {
        return customerIdTypeId1;
    }

    public void setCustomerIdTypeId1(int customerIdTypeId1) {
        this.customerIdTypeId1 = customerIdTypeId1;
    }

    public String getCustomerId1() {
        return customerId1;
    }

    public void setCustomerId1(String customerId1) {
        this.customerId1 = customerId1;
    }

    public int getCustomerIdTypeId2() {
        return customerIdTypeId2;
    }

    public void setCustomerIdTypeId2(int customerIdTypeId2) {
        this.customerIdTypeId2 = customerIdTypeId2;
    }

    public String getCustomerId2() {
        return customerId2;
    }

    public void setCustomerId2(String customerId2) {
        this.customerId2 = customerId2;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        if (start != null) {
            this.start = new Timestamp(start.toDateTime().getMillis());
        }
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        if (end != null) {
            this.end = new Timestamp(end.toDateTime().getMillis());
        }
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
}
