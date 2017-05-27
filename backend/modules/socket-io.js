var io = require('socket.io')
var child = require('child_process');
var IO;

module.exports = {
  'start' : (server, callback)=>{
    IO = io(server);

    IO.on('connection', (socket)=>{
      console.log('Client ' + socket.id + ' Connected');

      socket.on('solve', (props)=>{
        var {puzzle} = props;

        var solve = child.fork('solve.js');
        solve.on('message', (props)=>{
          solve.kill()
          if(props !== null){
            var solution = props
            socket.emit('done', solution)
          }
          else{
            socket.emit('failed')
          }
        })
        solve.send({ puzzle: puzzle });
      })
    })

    callback();
  }
}
