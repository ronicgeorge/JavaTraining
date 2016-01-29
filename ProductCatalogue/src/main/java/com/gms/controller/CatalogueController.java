package com.gms.controller;

import java.util.concurrent.CountDownLatch;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.view.AsyncViewResult;
import com.gms.config.GMDatabase;
import com.gms.service.GMCouchbaseService;
import com.gms.service.GMCouchbaseServiceImpl;

import rx.Observable;

@Path("/gms")
@Produces(MediaType.APPLICATION_JSON)
public class CatalogueController {
	
	GMCouchbaseService gmCouchbaseService;
	
	public CatalogueController(GMDatabase c) {
		gmCouchbaseService=new GMCouchbaseServiceImpl(c);
	}
	
	/**
	 * 
	 * @param productData
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProduct(String productData) {
		JsonObject productObject=JsonObject.fromJson(productData);
		String id="gm-"+productObject.get("sku");
		return Response.ok(gmCouchbaseService.addProduct(id,productObject).content().toString()).build();
	}
	
	/**
	 * 
	 * @param productData
	 * @return
	 */
	@POST
	@Path("/assync")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProductAssync(String productData) {
		JsonObject productObject=JsonObject.fromJson(productData);
		String id="gm-"+productObject.get("sku");
		Observable<JsonDocument> returnObservable = gmCouchbaseService.addProductAssync(id,productObject);
//Below code will be used to make the async call to blocking sync
//		 returnObservable.toBlocking().single();
     return Response.created(null).build();
	}
	
	@GET
	@Path("/productbatch")
	public Response getProductBatch(@QueryParam("offset") int offset, @QueryParam("limit") int limit) {
		JsonArray array = JsonArray.create();
		final CountDownLatch latch = new CountDownLatch(limit);
		Observable<AsyncViewResult> viewResult = gmCouchbaseService.getProductsBatch(offset, limit);
		viewResult.toBlocking().subscribe(onNext -> {
			onNext.rows().forEach(row -> {
				row.document().subscribe(jsonDocument -> {
					System.out.println("json "+jsonDocument.content());
					array.add(jsonDocument.content());
					latch.countDown();
				});
			});
			;
		});
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).build();
		}
		return Response.ok(array.toString()).build();
	}
	
	@GET
	@Path("/products/")
	public Response getProducts(@QueryParam("category") String category) {
        return Response.ok(gmCouchbaseService.getProducts(category).toString()).status(HttpStatus.OK_200).build();
	}
	
	@GET
	@Path("/{sku}")
	public Response getProduct(@PathParam("sku") String sku) {
		JsonDocument doc =gmCouchbaseService.getProduct(sku);
        if (doc != null) {
            return Response.ok(gmCouchbaseService.getProduct(sku).content().toString()).status(HttpStatus.OK_200).build();
        } else {
            return Response.status(HttpStatus.NOT_FOUND_404).build();
        }
	}
	
	@DELETE
	@Path("/{sku}")
	public Response deleteProduct(@PathParam("sku") String sku) {
		JsonDocument doc = gmCouchbaseService.deleteProduct(sku);
		if(doc != null){
			return Response.status(HttpStatus.NO_CONTENT_204).build();
		}
		else{
			return Response.status(HttpStatus.NOT_FOUND_404).build();
		}
	}
	
}
