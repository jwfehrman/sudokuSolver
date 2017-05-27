import React from 'react'
import _ from 'lodash'
import Modal from 'react-modal'

const customStyles = {
  content : {
    top                   : '50%',
    left                  : '50%',
    right                 : 'auto',
    bottom                : 'auto',
    marginRight           : '-50%',
    transform             : 'translate(-50%, -50%)',
    zIndex                : '1000'
  }
};

var header = {
  backgroundColor: 'gray',
  borderRadius: '10px',
  paddingTop: '5px',
  paddingBottom: '5px',
  marginTop:'10px',
  marginBottom:'10px',
  textAlign: 'center'
}

class Todos extends React.Component{
  constructor(props){
    super(props)
    this.state = {
      newTodo: '',
      modal:false,
      hover: '',
      editID: '',
      editTask: '',
    }
  }

  send(e){
    if(this.state.newTodo !== ''){
      this.props.API.ACT.todoAdd({OBJECT:{task:this.state.newTodo, isDone:false, PID: this.props.API.PID}, WSID: this.props.API.WSID});
      this.setState({newTodo: ''});
    }
  }

  back(){
    this.props.API.ACT.back();
  }

  render(){
    var TODOS_ACTIVE = _.filter(this.props.API.TODOS, {PID: this.props.API.PID, isDone:false})
    var TODOS_COMPLETE = _.filter(this.props.API.TODOS, {PID: this.props.API.PID, isDone:true})
    var PROJECT = _.find(this.props.API.PROJECTS, {id: this.props.API.PID})
    if(TODOS_ACTIVE !== undefined){
      var TASKS_ACTIVE = TODOS_ACTIVE.map((data, key)=>{
        var style = {}
        if(this.state.hover === data._id){
          style.border = '1px solid black'
          style.boxShadow = '2px 5px 7px black'
          style.zIndex = '100'
        }
        return( <li style={style} className='list-group-item' id={data.id} key={key} onMouseEnter={(e)=>this.setState({hover:e.target.id})} onMouseLeave={(e)=>this.setState({hover:''})} onClick={(e)=>this.props.API.ACT.todoEdit({id:data.id, data:{isDone:true}})} >
                  {data.task}
                  <button style={{marginRight:'1%', marginLeft: '1%', boxShadow:'2px 2px 5px black'}} className='btn btn-danger btn-xs pull-right glyphicon glyphicon-remove' key={'child2' + data.id} onClick={(e)=>{this.setState({modalDelete:true, editID:data.id}); e.stopPropagation()}}></button>
                  <button style={{marginRight:'1%', marginLeft: '1%', boxShadow:'2px 2px 5px black'}} className='btn btn-primary btn-xs pull-right glyphicon glyphicon-pencil' key={data.id} onClick={(e)=>{this.setState({modal:true, editID:data.id}); e.stopPropagation()}}></button>
                </li>
        )
      })
    }
    if(TODOS_COMPLETE !== undefined){
      var TASKS_COMPLETE = TODOS_COMPLETE.map((data, key)=>{
        var style = {}
        if(this.state.hover === data._id){
          style.border = '1px solid black'
          style.boxShadow = '2px 5px 7px black'
          style.zIndex = '100'
        }
        return( <li style={style} className='list-group-item' id={data.id} key={key} onMouseEnter={(e)=>this.setState({hover:e.target.id})} onMouseLeave={(e)=>this.setState({hover:''})} onClick={(e)=>this.props.API.ACT.todoEdit({id:data.id, data:{isDone:false}})} >
                  <strike>{data.task}</strike>
                  <button style={{marginRight:'1%', marginLeft: '1%', boxShadow:'2px 2px 5px black'}} className='btn btn-danger btn-xs pull-right glyphicon glyphicon-remove' key={'child2' + data.id} onClick={(e)=>{this.setState({modalDelete:true, editID:data.id}); e.stopPropagation()}}></button>
                </li>
        )
      })
    }

    return( <div className='container'>
              <div style={header}>
                <h1>{PROJECT.name + ' - Tasks'}</h1>
              </div>

              <div className='btn-group btn-group-justified'>
                <a className='btn btn-warning' onClick={()=>this.back()}>Back</a>
                <a className='btn btn-primary' onClick={()=>this.props.API.ACT.voice((newTodo)=>{this.props.API.ACT.todoAdd({task:newTodo, project: this.props.API.ID})})}><span className='glyphicon glyphicon-record' /> Record New Task</a>
              </div>

              <br/>
              <input className='form-control' type='text' placeholder='Add Task' value={this.state.newTodo} onKeyUp={(e)=>(e.keyCode === 13) ? this.send(e) : null} onChange={(e)=>this.setState({newTodo: e.target.value})} />
              <br/>

              <div className='col-md-6'>
                <div style={header}>
                  <h3>ACTIVE TASKS</h3>
                </div>
                <ul className='list-group'>{TASKS_ACTIVE}</ul>
              </div>
              <div className='col-md-6'>
                <div style={header}>
                  <h3>COMPLETED TASKS</h3>
                </div>
                <ul className='list-group'>{TASKS_COMPLETE}</ul>
              </div>


              <Modal isOpen={this.state.modal} onRequestClose={()=>this.setState({modal:false, editID:'', editTask:''})} style={customStyles} contentLabel='Example'>
                <div>
                  <h3>Edit Task</h3>
                  <input type='text' className='form-control' defaultValue={(_.find(this.props.API.TODOS, {id: this.state.editID})) ? _.find(this.props.API.TODOS, {id: this.state.editID}).task : ''} onChange={(e)=>this.setState({editTask:e.target.value})} />
                  <div className="btn-group" role="group">
                    <button className="btn btn-success" onClick={(e)=>{this.props.API.ACT.todoEdit({ID:this.state.editID, OBJECT:{task:this.state.editTask}}); this.setState({modal:false, editID:'', editTask:''})} }>Save</button>
                    <button className="btn btn-danger" onClick={()=>this.setState({modal:false, editID:'', editTask:''})}>Cancel</button>
                  </div>
                </div>
              </Modal>

              {/* <Modal isOpen={this.state.modalDelete} onRequestClose={()=>this.setState({modalDelete:false, editID:'', editTask:''})} style={customStyles} contentLabel='Example'>
                <div>
                  <h3>Delete Task</h3>
                  <p>Are you sure you want to delete task: "{(_.find(this.props.API.TODOS, {_id: this.state.editID})) ? _.find(this.props.API.TODOS, {_id: this.state.editID}).task : ''}"?</p>
                  <div className="btn-group" role="group">
                    <button className="btn btn-success" onClick={(e)=>{this.props.API.ACT.todoDelete({_id:this.state.editID}); this.setState({modalDelete:false, editID:'', editTask:''})} }>Save</button>
                    <button className="btn btn-danger" onClick={()=>this.setState({modalDelete:false, editID:'', editTask:''})}>Cancel</button>
                  </div>
                </div>
              </Modal> */}

            </div>
    )
  }
}

module.exports = Todos
