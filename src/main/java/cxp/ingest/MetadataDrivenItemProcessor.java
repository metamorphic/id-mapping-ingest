package cxp.ingest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Created by markmo on 7/04/15.
 */
public class MetadataDrivenItemProcessor implements ItemProcessor<Map<String, Object>, List<CustomerIdMapping>> {

    private static final Log log = LogFactory.getLog(MetadataDrivenItemProcessor.class);

    static final String[] dateFormats = new String[] {
            // pattern                      // example
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",   // 2001-07-04T12:08:56.235-0700
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", // 2001-07-04T12:08:56.235-07:00
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS", // 2001-07-04T12:08:56.235000
            "yyyy-MM-dd HH:mm:ss.SSSZ",     // 2001-07-04 12:08:56.235-0700
            "yyyy-MM-dd HH:mm:ss.SSSXXX",   // 2001-07-04 12:08:56.235-07:00
            "yyyy-MM-dd HH:mm:ss.SSSSSS",   // 2001-07-04 12:08:56.235000
            "EEE, MMM d, ''yy",             // Wed, Jul 4, '01
            "EEE, MMM d, yyyy",             // Wed, Jul 4, 2001
            "yyyy.MM.dd",                   // 2001.07.04
            "yyyy-MM-dd",                   // 2001-07-04
            "yyyy/MM/dd",                   // 2001/07/04
            "dd.MM.yyyy",                   // 04.07.2001
            "dd-MM-yyyy",                   // 04-07-2001
            "dd/MM/yyyy",                   // 04/07/2001
            "MM.dd.yyyy",                   // 07.04.2001
            "MM-dd-yyyy",                   // 07-04-2001
            "MM/dd/yyyy",                   // 07/04/2001
            "dd.MM.yy",                     // 04.07.01
            "dd-MM-yy",                     // 04-07-01
            "dd/MM/yy",                     // 04/07/01
            "MM.dd.yy",                     // 07.04.01
            "MM-dd-yy",                     // 07-04-01
            "MM/dd/yy",                     // 07/04/01
            "dd/MMM/yy",                    // 03/APR/15
            "yyyy-MM-dd",
            "yyyy-MM-dd'T'HH",
            "yyyy-MM-dd HH",
            "yyyy-MM-dd'T'HH:mm",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss Z",
            "yyyy-MM-dd HH:mm:ss Z"
    };

    MetadataProvider metadataProvider;

    ExpressionParser parser;

    private List<String> dfList;

    private FileDataset fileDataset = null;

    private Map<Integer, Expression> filterExpressionMap = null;

    private Map<Integer, Expression> customerId1ExpressionMap = null;

    private Map<Integer, Expression> customerId2ExpressionMap = null;

    private Map<Integer, Expression> startTimeExpressionMap = null;

    private Map<Integer, Expression> endTimeExpressionMap = null;

    private Map<Integer, Boolean> shouldFilterMap;

    private Long jobId;

    public MetadataDrivenItemProcessor() {
        shouldFilterMap = new HashMap<Integer, Boolean>();
    }

    private static <T> List<T> rearrange(List<T> items, T input) {
        int index = items.indexOf(input);
        List<T> copy;
        if (index >= 0) {
            copy = new ArrayList<T>(items.size());
            copy.addAll(items.subList(0, index));
            copy.add(0, items.get(index));
            copy.addAll(items.subList(index + 1, items.size()));
        } else {
            copy = new ArrayList<T>(items);
        }
        return copy;
    }

    @Override
    public List<CustomerIdMapping> process(Map<String, Object> item) throws Exception {
        Assert.notNull(fileDataset);
        Assert.notNull(shouldFilterMap);

        List<CustomerIdMapping> mappings = new ArrayList<CustomerIdMapping>();

        if (fileDataset.getCustomerIdMappingRules() != null) {
            for (CustomerIdMappingRule rule : fileDataset.getCustomerIdMappingRules()) {
                Integer ruleId = rule.getId();
                boolean shouldInclude = true;
                if (shouldFilterMap.get(ruleId)) {
                    shouldInclude = filterExpressionMap.get(ruleId).getValue(item, Boolean.class);
                }
                if (shouldInclude) {
                    Integer customerIdType1Id = rule.getCustomerIdType1Id();
                    String customerId1 = null;
                    if (customerId1ExpressionMap != null && customerId1ExpressionMap.containsKey(ruleId)) {
                        customerId1 = (String)customerId1ExpressionMap.get(ruleId).getValue(item);
                    }
                    Integer customerIdType2Id = rule.getCustomerIdType2Id();
                    String customerId2 = null;
                    if (customerId2ExpressionMap != null && customerId2ExpressionMap.containsKey(ruleId)) {
                        customerId2 = (String)customerId2ExpressionMap.get(ruleId).getValue(item);
                    }
                    LocalDateTime start = null;
                    if (startTimeExpressionMap != null && startTimeExpressionMap.containsKey(ruleId)) {
                        String startTime = (String)startTimeExpressionMap.get(ruleId).getValue(item);
                        start = parseDate(startTime);
                    }
                    LocalDateTime end = null;
                    if (endTimeExpressionMap != null && endTimeExpressionMap.containsKey(ruleId)) {
                        String endTime = (String)endTimeExpressionMap.get(ruleId).getValue(item);
                        end = parseDate(endTime);
                    }
                    CustomerIdMapping mapping = new CustomerIdMapping(customerIdType1Id, customerId1,
                            customerIdType2Id, customerId2, rule.getConfidenceLevel(), start, end, jobId);

                    mappings.add(mapping);
                }
            }
        }
        return mappings;
    }

    private boolean hasNoValue(String str) {
        if (str == null) return true;
        String trimmed = str.trim();
        return (trimmed.isEmpty() || trimmed.matches("0+"));
    }

    private boolean hasNoValue(Integer i) {
        return (i == null || i == 0);
    }

    // TODO
    // find a faster way
    public LocalDateTime parseDate(String value) {
        if (value == null) return null;
        String v = value.trim();
        if (v.isEmpty()) return null;
        int i = 0;
        for (String format : dfList) {
            try {
                DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
                LocalDateTime dt = LocalDateTime.parse(value, formatter);
                // Move valid format to top of list
                if (i > 0) {
                    dfList = rearrange(dfList, format);
                }

                return dt;

            } catch (IllegalArgumentException e) {
                // ignore
            }
            i += 1;
        }
        return null;
    }

    public void setMetadataProvider(MetadataProvider metadataProvider) {
        this.metadataProvider = metadataProvider;
        if (fileDataset == null && parser != null) setExpressions();
    }

    public void setParser(ExpressionParser parser) {
        this.parser = parser;
        if (fileDataset == null && metadataProvider != null) setExpressions();
    }

    private void setExpressions() {
        if (metadataProvider.getFileDataset() != null) {
            fileDataset = metadataProvider.getFileDataset();

            if (fileDataset.getCustomerIdMappingRules() != null) {
                for (CustomerIdMappingRule rule : fileDataset.getCustomerIdMappingRules()) {
                    Integer ruleId = rule.getId();
                    Integer customerIdType1Id = rule.getCustomerIdType1Id();
                    if (hasNoValue(customerIdType1Id)) {
                        throw new RuntimeException("No source Customer ID Type");
                    }
                    Integer customerIdTypeId2 = rule.getCustomerIdType2Id();
                    if (hasNoValue(customerIdTypeId2)) {
                        throw new RuntimeException("No target Customer ID Type");
                    }
                    String filterExpr = rule.getFilterExpression();
                    if (hasNoValue(filterExpr)) {
                        shouldFilterMap.put(ruleId, false);
                    } else {
                        if (filterExpressionMap == null) {
                            filterExpressionMap = new HashMap<>();
                        }
                        filterExpressionMap.put(ruleId, parser.parseExpression(filterExpr));
                        shouldFilterMap.put(ruleId, true);
                    }

                    if (!hasNoValue(rule.getCustomerIdType1Id()) && !hasNoValue(rule.getCustomerIdExpression1())) {
                        if (customerId1ExpressionMap == null) {
                            customerId1ExpressionMap = new HashMap<>();
                        }
                        customerId1ExpressionMap.put(ruleId, parser.parseExpression(rule.getCustomerIdExpression1()));
                    }

                    if (!hasNoValue(rule.getCustomerIdType2Id()) && !hasNoValue(rule.getCustomerIdExpression2())) {
                        if (customerId2ExpressionMap == null) {
                            customerId2ExpressionMap = new HashMap<>();
                        }
                        customerId2ExpressionMap.put(ruleId, parser.parseExpression(rule.getCustomerIdExpression2()));
                    }

                    if (!hasNoValue(rule.getStartTimeExpression())) {
                        if (startTimeExpressionMap == null) {
                            startTimeExpressionMap = new HashMap<>();
                        }
                        startTimeExpressionMap.put(ruleId, parser.parseExpression(rule.getStartTimeExpression()));
                    }

                    if (!hasNoValue(rule.getEndTimeExpression())) {
                        if (endTimeExpressionMap == null) {
                            endTimeExpressionMap = new HashMap<>();
                        }
                        endTimeExpressionMap.put(ruleId, parser.parseExpression(rule.getEndTimeExpression()));
                    }
                }
            } else {
                // TODO
                log.warn("No customer ID mapping rules defined for dataset " + fileDataset.getName());
            }

            jobId = metadataProvider.getJobId();

        } else {
            // TODO
            log.warn("File dataset not found");
        }
    }
}
