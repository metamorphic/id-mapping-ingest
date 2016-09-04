package cxp.ingest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Created by markmo on 14/04/15.
 */
public class EndJobTasklet implements Tasklet {

    private static final Log log = LogFactory.getLog(EndJobTasklet.class);

    MetadataProvider metadataProvider;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileDataset fileDataset = metadataProvider.getFileDataset();

        metadataProvider.endJob(fileDataset.getId());

        return RepeatStatus.FINISHED;
    }

    public void setMetadataProvider(MetadataProvider metadataProvider) {
        this.metadataProvider = metadataProvider;
    }
}
