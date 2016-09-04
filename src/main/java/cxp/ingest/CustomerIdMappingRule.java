package cxp.ingest;

/**
 * Created by markmo on 28/04/15.
 */
public class CustomerIdMappingRule {

    private int id;
    private String name;
    private String filterExpression;
    private Integer customerIdType1Id;
    private String customerIdExpression1;
    private Integer customerIdType2Id;
    private String customerIdExpression2;
    private String startTimeExpression;
    private String endTimeExpression;
    private Double confidenceLevel;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFilterExpression() {
        return filterExpression;
    }

    public Integer getCustomerIdType1Id() {
        return customerIdType1Id;
    }

    public String getCustomerIdExpression1() {
        return customerIdExpression1;
    }

    public Integer getCustomerIdType2Id() {
        return customerIdType2Id;
    }

    public String getCustomerIdExpression2() {
        return customerIdExpression2;
    }

    public String getStartTimeExpression() {
        return startTimeExpression;
    }

    public String getEndTimeExpression() {
        return endTimeExpression;
    }

    public Double getConfidenceLevel() {
        return confidenceLevel;
    }
}
