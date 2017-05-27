import React from 'react';
import ReactDom from 'react-dom';
import _ from 'lodash';

var io = require('socket.io-client')();

class App extends React.Component{
  constructor(props){
    super(props)

    this.state = {
      puzzle :  [ '', '', '', '', '', '', '', '', '',
                  '', '', '', '', '', '', '', '', '',
                  '', '', '', '', '', '', '', '', '',
                  '', '', '', '', '', '', '', '', '',
                  '', '', '', '', '', '', '', '', '',
                  '', '', '', '', '', '', '', '', '',
                  '', '', '', '', '', '', '', '', '',
                  '', '', '', '', '', '', '', '', '',
                  '', '', '', '', '', '', '', '', ''],
      solved: 0,
    }
  }

  componentDidMount(){
    io.on('done', (solution)=>{
      this.setState({puzzle:solution, solved:1})
    })
    io.on('failed', ()=>{
      this.setState({solved:-1})
    })
  }

  updatePuzzle(e){
    var test = this.validate(e.target.value)
    if(test){
      var puzzle = this.state.puzzle;
      puzzle[e.target.id] = e.target.value;
      this.setState({puzzle})
    }
  }

  validate(value) {
    if(isNaN(value)){
      return false;
    }
    else{
      return true;
    }
  }

  checkSolution(){
    if(this.state.solved === 0){
      return null;
    }
    else if(this.state.solved === 2){
      return <h3 style={{color:'#ff1900', textAlign:'center'}}>Too Many Empty Spaces!</h3>
    }
    else if(this.state.solved === 1){
      return <h3 style={{color:'#00f925', textAlign:'center'}}>Solution Found!</h3>
    }
    else if(this.state.solved === -1){
      return <h3 style={{color:'#ff1900', textAlign:'center'}}>No Solution Found!</h3>
    }
  }

  sendPuzzle(){
    var empty = 0
    for(var key in this.state.puzzle){
      if(this.state.puzzle[key] === '' || this.state.puzzle[key] === '0'){
        empty++
      }
    }
    if(empty >= (81-17)){
      this.setState({solved: 2})
    }
    else{io.emit('solve', {puzzle: this.state.puzzle})}
  }

  render(){

    var BOARD = this.state.puzzle.map((data,key)=>{
      return(
        <td key={key}><input style={{textAlign:'center'}} id={key} key={key} type="text" size="2" value={this.state.puzzle[key]} onChange={(e)=>this.updatePuzzle(e)} maxLength="1" /></td>
      )
    })
    var SOLVED = this.checkSolution();

    return(
              <div className='container'>
                <h1 style={{textAlign:"center", color:"orange"}}>Sudoku Solver</h1>
                {SOLVED}
                <table style={{margin:"0px auto"}}>
                  <colgroup><col/><col/><col/></colgroup>
                  <colgroup><col/><col/><col/></colgroup>
                  <colgroup><col/><col/><col/></colgroup>
                  <tbody>
                    <tr>{BOARD.slice(0,9)}</tr>
                    <tr>{BOARD.slice(9,18)}</tr>
                    <tr>{BOARD.slice(18,27)}</tr>
                  </tbody>
                  <tbody>
                    <tr>{BOARD.slice(27,36)}</tr>
                    <tr>{BOARD.slice(36,45)}</tr>
                    <tr>{BOARD.slice(45,54)}</tr>
                  </tbody>
                  <tbody>
                    <tr>{BOARD.slice(54,63)}</tr>
                    <tr>{BOARD.slice(63,72)}</tr>
                    <tr>{BOARD.slice(72,81)}</tr>
                  </tbody>
                </table>
                <br />
                <div className="row">
                  <div className="col-md-4 col-md-offset-4">
                    <button className="btn btn-success form-control" onClick={()=>this.sendPuzzle()}>Solve Puzzle</button>
                  </div>
                </div>
              </div>

    );
  }
}

ReactDom.render(<App />, document.getElementById('app'))
