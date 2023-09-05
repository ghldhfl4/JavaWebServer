package org.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {
    public HashMap<String, String> loopConvert(Properties prop) {
        HashMap<String, String> retMap = new HashMap<>();
        for (Map.Entry<Object, Object> entry : prop.entrySet()) {
            retMap.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return retMap;
    }

}
