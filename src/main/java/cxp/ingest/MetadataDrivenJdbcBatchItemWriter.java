package cxp.ingest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDateTime;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by markmo on 7/04/15.
 */
public class MetadataDrivenJdbcBatchItemWriter implements ItemWriter<List<CustomerIdMapping>> {

    private static final Log log = LogFactory.getLog(MetadataDrivenJdbcBatchItemWriter.class);

    JdbcTemplate jdbcTemplate;

    final String INSERT_MAPPING_SQL = "INSERT INTO cxp.customer_id_mapping (customer_id_type_id_1, customer_id_1, customer_id_type_id_2, customer_id_2, confidence, start_ts, end_ts, job_id, created_ts) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private Timestamp created;

    public MetadataDrivenJdbcBatchItemWriter() {
        this.created = new Timestamp(LocalDateTime.now().toDateTime().getMillis());
    }

    @Override
    public void write(List<? extends List<CustomerIdMapping>> items) throws Exception {
        final List<CustomerIdMapping> mappings = new ArrayList<CustomerIdMapping>();
        for (List<CustomerIdMapping> ms : items) {
            for (CustomerIdMapping m : ms) {
                mappings.add(m);
            }
        }
        int[] updateCounts = jdbcTemplate.batchUpdate(INSERT_MAPPING_SQL, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, mappings.get(i).getCustomerIdTypeId1());
                        ps.setString(2, mappings.get(i).getCustomerId1());
                        ps.setInt(3, mappings.get(i).getCustomerIdTypeId2());
                        ps.setString(4, mappings.get(i).getCustomerId2());
                        ps.setDouble(5, mappings.get(i).getConfidence());
                        ps.setTimestamp(6, mappings.get(i).getStart());
                        ps.setTimestamp(7, mappings.get(i).getEnd());
                        ps.setLong(8, mappings.get(i).getJobId());
                        ps.setTimestamp(9, created);
                    }

                    @Override
                    public int getBatchSize() {
                        return mappings.size();
                    }
                });
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
