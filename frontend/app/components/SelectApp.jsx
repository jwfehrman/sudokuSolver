import React from 'react';

var center = {
  height: '50%',
  position: 'absolute',
  top:'0',
  bottom: '0',
  left: '0',
  right: '0',
  margin: 'auto',
}

var cols = {
  height: '250px',
  backgroundColor: 'gray',
  borderRadius: '5px',
  marginBottom:'5%'
}

var centerHorizontal = {
  margin: '0 auto'
}

class SelectApp extends React.Component{
  constructor(props){
    super(props);
  }

  INIT(APP){
    this.props.API.ACT.INIT(APP);
  }

  render(){
    return(
      <div style={center} className='container'>

        <div className='row'>
          <div className='col-md-6' onClick={()=>this.INIT('TODO')}>
            <div style={cols} className='col-md-12'>
              <h1 style={{textAlign:'center'}}> TASKS </h1>
              <div style={{fontSize:'10em', textAlign:'center'}}><span className='glyphicon glyphicon-th-list'></span></div>
            </div>
          </div>
          <div className='col-md-6' onClick={()=>this.INIT('CHAT')}>
            <div style={cols} className='col-md-12'>
              <h1 style={{textAlign:'center'}}> MESSENGER </h1>
              <div style={{fontSize:'10em', textAlign:'center'}}><span className='glyphicon glyphicon-comment'></span></div>
            </div>
          </div>
        </div>

        <div className='row'>
          <div className='col-md-6'>
            <div style={cols} className='col-md-12'><h1>In Progress...</h1></div>
          </div>
          <div className='col-md-6'>
            <div style={cols} className='col-md-12'><h1>In Progress...</h1></div>
          </div>
        </div>

      </div>
    )
  }
}

module.exports = SelectApp;
