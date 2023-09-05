package org.header;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class Header {
    private static final Logger logger = Logger.getLogger(Header.class.getCanonicalName());
    private String method;
    private String host;
    private String accessFile;
    private String version; // "HTTP/1.0";

    // HashMap
    public Header (String[] properties) {
        String[] strHdr = properties[0].split("\\s");
        String[] strHost = properties[1].split(": ");
        this.method = strHdr[0];
        this.version = strHdr[2];
        this.accessFile = strHdr[1];
        this.host = strHost[1];
    }


    public String getMethod() {
        return method;
    }

    public String getHost() {
        return host;
    }

    public String getAccessFile() {
        return accessFile;
    }

    public String getVersion() {
        return version;
    }
}
