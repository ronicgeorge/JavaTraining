package com.gms.service;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.view.AsyncViewResult;

import rx.Observable;

public interface GMCouchbaseService {
	public JsonArray getProducts(String productCategory);
	public Observable<AsyncViewResult> getProductsBatch(int offset,int limit);
	public JsonDocument getProduct(String sku);
	public JsonDocument addProduct(String productId, JsonObject product);
	public Observable<JsonDocument> addProductAssync(String productId, JsonObject product);
	public JsonDocument deleteProduct(String productId);
	public JsonDocument updateProduct(JsonDocument product);

}
