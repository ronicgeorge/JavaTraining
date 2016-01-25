package com.gms.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {

    @Value("${couchbase.nodes}")
    private List<String> nodes = new ArrayList<String>();

    @Value("${couchbase.bucket}")
    private String bucket;

    @Value("${couchbase.password}")
    private String password;

    public List<String> getNodes() {
        return nodes;
    }

    public String getBucket() {
        return bucket;
    }

    public String getPassword() {
        return password;
    }
}
