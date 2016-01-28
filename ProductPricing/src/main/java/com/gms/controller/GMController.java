package com.gms.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.gms.service.GMCouchbaseServiceImpl;


@RestController
@RequestMapping("/gms")
public class GMController {

    private final GMCouchbaseServiceImpl gmCouchbaseService;

    @Autowired
    public GMController(GMCouchbaseServiceImpl gmCouchbaseService) {
        this.gmCouchbaseService = gmCouchbaseService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{sku}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPrice(@PathVariable String sku) {
        JsonDocument doc = gmCouchbaseService.read(sku);
        if (doc != null) {
            return new ResponseEntity<String>(doc.content().toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createPrice(@RequestBody Map<String, Object> priceData) {
        String id = "";
        try {
            JsonObject priceObject = parsePrice(priceData);
            id = "gm-" + priceObject.getString("sku");
            JsonDocument doc = GMCouchbaseServiceImpl.createDocument(id, priceObject);
            gmCouchbaseService.create(doc);
            return new ResponseEntity<String>(id, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        } catch (DocumentAlreadyExistsException e) {
            return new ResponseEntity<String>("Id " + id + " already exist", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{gmsku}")
    public ResponseEntity<String> deletePrice(@PathVariable String gmsku) {
        JsonDocument deleted = gmCouchbaseService.delete(gmsku);
        return new ResponseEntity<String>(""+deleted.cas(), HttpStatus.OK);
    }

    private JsonObject parsePrice(Map<String, Object> priceData) {
        String type = (String) priceData.get("type");
        String name = (String) priceData.get("sku");
        if (type == null || name == null || type.isEmpty() || name.isEmpty()) {
           throw new IllegalArgumentException();
        } else {
            JsonObject price = JsonObject.create();
            for (Map.Entry<String, Object> entry : priceData.entrySet()) {
                price.put(entry.getKey(), entry.getValue());
            }
            return price;
        }
    }

    
}
