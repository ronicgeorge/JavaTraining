var http=require('http');
var express = require('express');
var request = require('request');
var async = require('async');
const client=require('./productdetail.js');
var app=express();
app.get('/product',function(request,response){
  client.handler(request,response);
})

app.get('/product/getbatch',function(request,response){
  client.batchHandler(request,response);
})
app.listen(3000,function(){
  console.log('Listening to port 3000');
});
