"use strict"
var _ = require('lodash')

process.on('message', (props)=>{
  //Helper Globals
  var {puzzle} = props;
  // puzzle = ['0','0','4','6','0','0','0','2','8',
  //           '0','8','3','0','2','0','7','0','0',
  //           '9','0','7','0','0','0','0','6','3',
  //           '7','3','0','0','8','0','5','0','1',
  //           '0','0','0','3','0','5','0','0','0',
  //           '5','0','6','0','4','0','0','8','9',
  //           '4','7','0','0','0','0','8','0','2',
  //           '0','0','5','0','9','0','4','3','0',
  //           '3','9','0','0','0','4','6','0','0']
  var n = 9;
  var size = n*n;
  var actions = ['1', '2', '3', '4', '5', '6', '7', '8', '9'];

  for(var key in puzzle){
    if(puzzle[key] === '')
      puzzle[key] = puzzle[key].replace('', '0');
  }

  function pruneColumn(state, depth, action){
    var i = depth % n;
    for(i; i < state.length; i+=9){
      if(action.includes(state[i])){
        action = _.without(action, state[i]);
      }
    }
    return action;
  }

  function pruneRow(state, depth, action){
    var row = Math.floor(depth / n);
    var start = n * row
    for(var i=start; i < (start + n); i++){
      if(action.includes(state[i])){
        action = _.without(action, state[i]);
      }
    }
    return action;
  }

  function pruneSquare(state, depth, action){
    var numRows = 3;
    var numCols = 3;
    var row = Math.floor(Math.floor(depth / n) / numRows) * (numRows * n)
    var col = Math.floor((depth % n) / numCols) * numCols
    var start = row + col

    for(var i=start; i < (start + (numRows * n)); i+=n){
      for(var j=i; j < i + numCols; j++){
        if(action.includes(state[j])){
          action = _.without(action, state[j]);
        }
      }
    }
    return action;
  }



  function getActions(state, depth){
    if(state[depth] === '0'){
      var action = _.cloneDeep(actions);
      action = pruneColumn(state, depth, action)
      action = pruneRow(state, depth, action)
      action = pruneSquare(state, depth, action)
      return action;
    }
    else{
      return undefined;
    }
  }

  function result(state, depth, action){
    if(state[depth] == '0'){
      state[depth] = _.cloneDeep(action);
      return _.cloneDeep(state);
    }
    else{
      return _.cloneDeep(state);
    }
  }

  function goalTest(state){
    if(state.indexOf('0') > -1){
      return false;
    }
    else{
      return true;
    }
  }

  function expand(node){
    var acts = getActions(node.state, node.depth);
    var children = []
    if(acts === undefined){
      children.push({state:_.cloneDeep(node.state), depth: _.cloneDeep(node.depth) + 1})
    }
    else if(acts.length !== 0){
      for(var key in acts){
        var results = result(_.cloneDeep(node.state), _.cloneDeep(node.depth), acts[key])
        children.push({state: _.cloneDeep(results), depth: _.cloneDeep(node.depth) + 1})
      }
    }
    else if(acts.length === 0){
      return []
    }

    return children;
  }

  function DFS(){
    var node = {state: puzzle, depth: 0};
    if(goalTest(node.state)){
      return node;
    }
    var frontier = [];
    frontier.push(_.cloneDeep(node));
    while(frontier.length > 0){
      node = frontier.pop();

      var nodes = expand(node);
      if(nodes.length === 0){
        continue
      }
      for(var child in nodes){
        if(goalTest(nodes[child].state)){
          process.send(nodes[child].state)
          return
        }

        frontier.push(_.cloneDeep(nodes[child]));
      }
    }
    process.send(null)
  }


  DFS();

})
