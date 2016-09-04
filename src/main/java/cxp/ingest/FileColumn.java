package cxp.ingest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by markmo on 4/04/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileColumn {

    private String name;
    private String dataTypeName;
    private String valueTypeName;
    private int columnIndex;
    private String description;
    private String characterSet;
    private String collation;
    private boolean unique;
    private String nullableType;
    private int length;
    private String defaultValue;
    private boolean autoinc;
    private boolean dimension;
    private int precision;
    private int scale;
    private boolean candidateFeatureParam;
    private boolean ignore;

    public String getName() {
        return name;
    }

    public String getDataTypeName() {
        return dataTypeName;
    }

    public String getValueTypeName() {
        return valueTypeName;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public String getDescription() {
        return description;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public String getCollation() {
        return collation;
    }

    public boolean isUnique() {
        return unique;
    }

    public String getNullableType() {
        return nullableType;
    }

    public int getLength() {
        return length;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isAutoinc() {
        return autoinc;
    }

    public boolean isDimension() {
        return dimension;
    }

    public int getPrecision() {
        return precision;
    }

    public int getScale() {
        return scale;
    }

    public boolean isCandidateFeatureParam() {
        return candidateFeatureParam;
    }

    public boolean isIgnore() {
        return ignore;
    }
}
