package com.dataforge.dataforge_api.dto;

import java.util.Map;

public class DataForgeRequest {
    private Map<String, Map<String, String>> schema;
    private int count;

    public Map<String, Map<String, String>> getSchema() {

        return schema;
    }

    public void setSchema(Map<String, Map<String, String>> schema) {
        this.schema = schema;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
