package cxp.ingest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by markmo on 4/04/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileDataSource {

    private String name;
    private String sourcingMethod;
    private String hostname;
    private String ipaddr;
    private int port;
    private String firewallStatus;
    private String description;
    private String network;
    private String filepath;
    private String filenamePattern;

    public String getName() {
        return name;
    }

    public String getSourcingMethod() {
        return sourcingMethod;
    }

    public String getHostname() {
        return hostname;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public int getPort() {
        return port;
    }

    public String getFirewallStatus() {
        return firewallStatus;
    }

    public String getDescription() {
        return description;
    }

    public String getNetwork() {
        return network;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getFilenamePattern() {
        return filenamePattern;
    }
}
