package com.gms.service;


import javax.annotation.PreDestroy;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.view.AsyncViewResult;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewResult;
import com.couchbase.client.java.view.ViewRow;
import com.gms.config.GMDatabase;

import rx.Observable;




public class GMCouchbaseServiceImpl implements GMCouchbaseService {
	
	GMDatabase config;
	
	CouchbaseCluster cluster;
	Bucket bucket;
	
	public GMCouchbaseServiceImpl(GMDatabase config) {
		cluster = CouchbaseCluster.create(config.getCouchbaseNodes());
		bucket = cluster.openBucket(config.getCouchbaseBucket());
	}
	
	@PreDestroy
	public void preDestroy() {
		if (this.cluster != null) {
			this.cluster.disconnect();
		}
	}
	
	public JsonArray getProducts(String productCategory){
		JsonArray keys = JsonArray.create();
		ViewResult result = bucket.query(ViewQuery.from("dev_gms", "byproductsku").key(productCategory));
		for (ViewRow row : result) {
		    JsonDocument doc = row.document();

		    keys.add(doc.content());
		}
		System.out.println("list size: "+keys.size());
		return keys;
	}
	
	public Observable<AsyncViewResult> getProductsBatch(int offset,int limit){
		return bucket.async().query(ViewQuery.from("dev_gms", "byproductsku").limit(offset).skip(offset));
	}
	
	public JsonDocument getProduct(String sku){
		JsonDocument skuObject = bucket.get(sku);
		return skuObject;
	}
	
	public JsonDocument addProduct(String productId, JsonObject product){
		return bucket.insert(JsonDocument.create(productId, product));
	}
	
	public Observable<JsonDocument> addProductAssync(String productId, JsonObject product){
		return bucket.async().insert(JsonDocument.create(productId, product));
	}
	
	public JsonDocument deleteProduct(String productId){
		return bucket.remove(productId);
	}
	
	public JsonDocument updateProduct(JsonDocument product){
		return bucket.replace(product);
	}
	
	
	


	/**
     * Prepare a new JsonDocument with some JSON content
     */
    public static JsonDocument createDocument(String id,JsonObject content) {
        return JsonDocument.create(id, content);
    }
}
