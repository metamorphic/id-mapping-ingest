package cxp.ingest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by markmo on 4/04/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityClassification {

    private String name;

    public String getName() {
        return name;
    }
}
