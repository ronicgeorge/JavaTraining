ProductPricing(Spring boot service-maven project)
--------------
	1. Import the project as a maven project. Update the project dependencies. 
	2. Sample requests
		/gms/{sku}	---  RequestMethod.GET 		--- List the price and details of requested sku  --- Sample: /gms/gm-100
        /gms 		---	 RequestMethod.POST 	--- Create a price details for a particular sku
        /gms/{sku} 	---  RequestMethod.DELETE 	--- Delete the price document of requested sku	--- Sample: /gms/gm-100
    3. Sample price document:
    	{
		  "sku": "100",//document key will be gm-100
		  "price": 10,
		  "currency": "INR",
		  "updated": "2016-01-01 10:10:10",
		  "description": "Smart watch",
		  "category": "Electronics"
		  "type": "gmprice"
		}
	4. Couchbase details
		Bucket name	is 'price'
		No views used for this bucket


ProductCatalogue(Dropwizard service-gradle project)
----------------
	1.Import the project as a gradle project. Update the project dependencies.
	2.Sample requests
		/gms 		---	 RequestMethod.POST 	--- Create a price details for a particular sku
		/gms/{sku}	---  RequestMethod.GET 		--- List the product details of requested sku --- Sample: /gms/gm-100 
		/gms/{sku} 	---  RequestMethod.DELETE 	--- Delete the price document of requested sku	--- Sample: /gms/gm-100
		/gms/assync ---	 RequestMethod.POST 	--- Create a price details for a particular sku in a assync method(same method has the blocking concept but it is commented)
		/gms/productbatch?offset=2&limit=5 ---- RequestMethod.GET --- List the product details in a batch(this uses a couchbase view)
		/gms/products?category=Electronics ---- RequestMethod.GET --- List the product details with category as Electronics
	3. Sample product document:
		{								//document key will be gm-100
		  "dialshape": "square",
		  "description": "Smart watch",
		  "sku": "100",					
		  "category": "Electronics",
		  "type": "gmproduct",
		  "strapmaterial": "leather",
		  "brand": "Motorola"
		}
	4. Couchbase details
		Bucket name is 'default'
		One view is used with name 'byproductsku' and design name as 'dev_gms'
		View code for 'byproductsku' is
				function (doc, meta) {
				   if(doc.category && doc.type == "gmproduct") {
					 emit(doc.category, doc);
				   }
				}
		
ClientService(nodejs service)
-------------
	1.Run the nodejs project with main file as 'index.js'
	2.Hit the 3000 port
	3.Sample requests
		http://localhost:3000/product/?id=gm-111 --- this will call both the service and combine the product detail and price detail
		http://localhost:3000/product/getbatch --- this will get a batch of products and its price
		
		
    