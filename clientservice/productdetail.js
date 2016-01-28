
var request = require('request');
var async = require('async');
exports.handler = function(req,res) {
  async.parallel([
    /* Product call*/
    function(callback) {
      var id = req.param('id');
      var url = "http://localhost:9179/gms/"+id;
      request(url, function(err, response, body) {
        // JSON body
        if(err) { console.log(err); callback(true); return; }
        console.log(body);
        var jStr = body;
        var obj = JSON.parse(jStr);
        callback(false,obj);
      });
    },
    /*Price call*/
    function(callback) {
      var id = req.param('id');
      var url = "http://localhost:8080/gms/"+id;
      request(url, function(err, response, body) {
        // JSON body
        if(err) { console.log(err); callback(true); return; }
        console.log(body);
        var jStr = body;
        var obj = JSON.parse(jStr);
        callback(false,obj);
      });
    },
  ],
  /*Collate results*/
  function(err,results) {
    if(err) { console.log(err); res.send(500,"Server Error"); return; }
      res.send({product:results[0], price:results[1]});
  }
  );
};



exports.batchHandler = function(req, res) {
  var prodcutDetailsArray = {products:[]};

  async.parallel([
    /*Product call*/
    function(callback) {
      var url = "http://localhost:9179/gms/products?category=Electronics";
      request(url, function(err, response, body) {
        // JSON body
        if(err) { console.log(err); callback(true); return; }
        products = JSON.parse(body);
        //console.log(products);
        var requestedCompleted = 0;
        for(var i=0 ;i<products.length;i++){
          async.parallel([
            function(callback) {
              var url = "http://localhost:8080/gms/gm-1111";
              console.log(url);
              request(url, function(err, response, body) {
                // JSON body
                console.log("Price Request Happend");
                if(err) { console.log(err); callback(true); return; }
                obj = JSON.parse(body);
                console.log("-------------------------------------------------------");
                console.log(products[i]['sku']);
                console.log("-------------------------------------------------------");
                prodcutDetailsArray.products.push({"product":products,"price":obj});
                console.log(body);
                requestedCompleted++;
                callback(false, obj);
              });
            },
          ],
          /*Collate results*/
          function(err, results) {
            if(err) { console.log(err); res.send(500,"Server Error"); return; }
            if(requestedCompleted == products.length){
              console.log(prodcutDetailsArray);
              res.send(prodcutDetailsArray);
            }
          }
          );
        }
        callback(false, products);
      });
    },
    /*Price call*/
    function(callback) {
      var id = req.param('id');
      var url = "http://localhost:8080/gms/gm-1111";
      request(url, function(err, response, body) {
        // JSON body
        if(err) { console.log(err); callback(true); return; }
        obj = JSON.parse(body);
        console.log(body);
        callback(false, obj);
      });
    },
  ],
  /*
   * Collate results
   */
  function(err, results) {
    if(err) { console.log(err); res.send(500,"Server Error"); return; }
  }
  );


};
