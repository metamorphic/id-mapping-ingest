package cxp.ingest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by markmo on 4/04/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileDataset {

    private Long id;
    private String name;
    private FileDataSource fileDataSource;
    private SecurityClassification securityClassification;
    private String namespace;
    private String description;
    private String comments;
    private String architectureDomain;
    private String contactPerson;
    private boolean customerData;
    private boolean financialBankingData;
    private boolean idAndServiceHistory;
    private boolean creditCardData;
    private boolean financialReportingData;
    private boolean privacyData;
    private boolean regulatoryData;
    private boolean nbnConfidential;
    private boolean nbnCompliant;
    private String ssuReady;
    private String ssuRemediationMethod;
    private String availableHistoryUnitOfTime;
    private int availableHistoryUnits;
    private int historyDataSizeGb;
    private int refreshDataSizeGb;
    private boolean batch;
    private String refreshFrequencyUnitOfTime;
    private int refreshFrequencyUnits;
    private String dataLatencyUnitOfTime;
    private int dataLatencyUnits;
    private String columnDelimiter;
    private boolean headerRow;
    private String rowDelimiter;
    private String textQualifier;
    private String compressionType;
    private List<FileColumn> columns;
    private List<CustomerIdMappingRule> customerIdMappingRules;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FileDataSource getFileDataSource() {
        return fileDataSource;
    }

    public SecurityClassification getSecurityClassification() {
        return securityClassification;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getDescription() {
        return description;
    }

    public String getComments() {
        return comments;
    }

    public String getArchitectureDomain() {
        return architectureDomain;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public boolean isCustomerData() {
        return customerData;
    }

    public boolean isFinancialBankingData() {
        return financialBankingData;
    }

    public boolean isIdAndServiceHistory() {
        return idAndServiceHistory;
    }

    public boolean isCreditCardData() {
        return creditCardData;
    }

    public boolean isFinancialReportingData() {
        return financialReportingData;
    }

    public boolean isPrivacyData() {
        return privacyData;
    }

    public boolean isRegulatoryData() {
        return regulatoryData;
    }

    public boolean isNbnConfidential() {
        return nbnConfidential;
    }

    public boolean isNbnCompliant() {
        return nbnCompliant;
    }

    public String getSsuReady() {
        return ssuReady;
    }

    public String getSsuRemediationMethod() {
        return ssuRemediationMethod;
    }

    public String getAvailableHistoryUnitOfTime() {
        return availableHistoryUnitOfTime;
    }

    public int getAvailableHistoryUnits() {
        return availableHistoryUnits;
    }

    public int getHistoryDataSizeGb() {
        return historyDataSizeGb;
    }

    public int getRefreshDataSizeGb() {
        return refreshDataSizeGb;
    }

    public boolean isBatch() {
        return batch;
    }

    public String getRefreshFrequencyUnitOfTime() {
        return refreshFrequencyUnitOfTime;
    }

    public int getRefreshFrequencyUnits() {
        return refreshFrequencyUnits;
    }

    public String getDataLatencyUnitOfTime() {
        return dataLatencyUnitOfTime;
    }

    public int getDataLatencyUnits() {
        return dataLatencyUnits;
    }

    public String getColumnDelimiter() {
        return columnDelimiter;
    }

    public boolean isHeaderRow() {
        return headerRow;
    }

    public String getRowDelimiter() {
        return rowDelimiter;
    }

    public String getTextQualifier() {
        return textQualifier;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public List<FileColumn> getColumns() {
        List<FileColumn> ret = new ArrayList<>(columns);
        Collections.sort(ret, new Comparator<FileColumn>() {
            @Override
            public int compare(FileColumn a, FileColumn b) {
                // ascending order
                return a.getColumnIndex() - b.getColumnIndex();
            }
        });
        return ret;
    }

    public String[] getColumnNames() {
        if (columns == null) {
            return null;
        }
        List<FileColumn> cols = getColumns();
        int n = cols.size();
        String[] columnNames = new String[n];
        for (int i = 0; i < n; i++) {
            columnNames[i] = cols.get(i).getName();
        }
        return columnNames;
    }

    public List<CustomerIdMappingRule> getCustomerIdMappingRules() {
        return customerIdMappingRules;
    }
}
