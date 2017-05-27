"use strict"
var app = require('express')();
//var http = require('http').Server(app);
var path  = require('path')

module.exports = {
  start: (callback)=>{
    app.get('/', (req, res)=>{
      res.sendFile(path.resolve(__dirname, './../../frontend/public/index.html'));
    });

    app.get('/bundle.js', (req, res)=>{
      res.sendFile(path.resolve(__dirname, './../../frontend/public/bundle.js'));
    });

    app.get('/styles.css', (req, res)=>{
      res.sendFile(path.resolve(__dirname, './../../frontend/public/styles.css'));
    });

    app.get('/*', (req, res)=>{
      res.sendFile(path.resolve(__dirname, './../../frontend/public/index.html'));
    });

    var server = app.listen(3000, (err)=>{
      if(err){
        console.log(err)
      }
      console.log('SERVER LISTENING ON LOCALHOST:3000')
      if(callback){
        callback(server)
      }
    })
  }
}
