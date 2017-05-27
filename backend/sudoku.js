var EXPRESS = require('./modules/http.js');
var IO = require('./modules/socket-io.js');

EXPRESS.start((server)=>{
  IO.start(server, ()=>{

  })
})
