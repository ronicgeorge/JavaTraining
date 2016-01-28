package com.gms.service;

import com.couchbase.client.java.document.JsonDocument;

public interface GMCouchbaseService {

    public JsonDocument create(JsonDocument doc);
    
    public JsonDocument read(String id);

    public JsonDocument update(JsonDocument doc);  
    
    public JsonDocument delete(String id);
}
