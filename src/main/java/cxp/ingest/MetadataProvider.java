package cxp.ingest;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Date;

/**
 * Created by markmo on 11/04/15.
 */
@Component
public class MetadataProvider {

    String datasetUrl;
    String jobUrl;

    FileDataset fileDataset;

    Job job;
    URI jobEntity;
    Long jobId;

    public void setFilename(String filename) {
        RestTemplate restTemplate = new RestTemplate();
        fileDataset = restTemplate.getForObject(datasetUrl + filename, FileDataset.class);
    }

    public void startJob() {
        RestTemplate restTemplate = new RestTemplate();
        job = new Job();
        jobEntity = restTemplate.postForLocation(jobUrl, job);
        String jobStr = jobEntity.toString();
        jobId = Long.parseLong(jobStr.substring(jobStr.lastIndexOf('/') + 1));
    }

    public void endJob(Long datasetId) {
        RestTemplate restTemplate = new RestTemplate();

        // TODO
        // should be done at start of job
        job.setDatasetId(datasetId);

        job.setEnd(new Date());
        job.setStatus(Job.Status.FINISHED);
        restTemplate.put(jobEntity, job);
    }

    public Long getJobId() {
        return jobId;
    }

    public void setDatasetUrl(String datasetUrl) {
        this.datasetUrl = datasetUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    public FileDataset getFileDataset() {
        return fileDataset;
    }
}
