import React from 'react'
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

class Projects extends React.Component{
  constructor(props){
    super(props)
    this.state = {
      newProject: '',
      modal: false,
      hover:'',
      editID:'',
      editTask: ''
    }
  }

  send(e){
    if(this.state.newProject !== ''){
      this.props.API.ACT.projectAdd({OBJECT: {name: this.state.newProject, isDone:false}, WSID: this.props.API.WSID});
      this.setState({newProject: ''});
    }
  }

  view(e){
    this.props.API.ACT.setState('PID', e.target.id, ()=>{
      this.props.API.ACT.changeView('todos');
    })
  }

  back(){
    this.props.API.ACT.back();
  }

  render(){
    var PROJECTS = this.props.API.PROJECTS.map((data, key)=>{
      var style = {}
      if(this.state.hover === data.id){
        style.border = '1px solid black'
        style.boxShadow = '2px 5px 7px black'
        style.zIndex = '100'
      }
      return( <li style={style} className='list-group-item' id={data.id} key={key} onMouseEnter={(e)=>this.setState({hover:e.target.id})} onMouseLeave={(e)=>this.setState({hover:''})} onClick={(e)=>this.view(e)}>
                {data.name}
                <button style={{marginRight:'1%', marginLeft: '1%', boxShadow:'2px 2px 5px black'}} className='btn btn-danger btn-xs pull-right glyphicon glyphicon-remove' key={'child2' + data.id} onClick={(e)=>{this.setState({modalDelete:true, editID:data.id}); e.stopPropagation()}}></button>
                <button style={{marginRight:'1%', marginLeft: '1%', boxShadow:'2px 2px 5px black'}} className='btn btn-success btn-xs pull-right glyphicon glyphicon-pencil' key={'child1' + data.id} onClick={(e)=>{this.setState({modal:true, editID:data.id}); e.stopPropagation()}}></button>
              </li>
      )
    })

    return( <div>
              <div style={header}>
                <h1>Projects</h1>
              </div>
              <a className='btn btn-warning' onClick={()=>this.back()}>Back</a>
              <div className='input-group'>
                <input className='form-control' type='text' placeholder='Add New Project' value={this.state.newProject} onKeyUp={(e)=>(e.keyCode === 13) ? this.send(e) : null} onChange={(e)=>this.setState({newProject: e.target.value})}/>
                <span className='input-group-btn' ><button className='btn btn-primary' onClick={(e)=>this.send(e)}>Create Project</button></span>
              </div>

              <br/>

              <ul className='list-group'>{PROJECTS}</ul>

              <Modal isOpen={this.state.modal} onRequestClose={()=>this.setState({modal:false, editID:'', editTask:''})} style={customStyles} contentLabel='Example'>
                <div>
                  <h3>Edit Project Name</h3>
                  <input type='text' className='form-control' defaultValue={(_.find(this.props.API.PROJECTS, {id: this.state.editID})) ? _.find(this.props.API.PROJECTS, {id: this.state.editID}).name : ''} onChange={(e)=>this.setState({editTask:e.target.value})} />
                  <div className="btn-group" role="group">
                    <button className="btn btn-success" onClick={(e)=>{this.props.API.ACT.projectEdit({ID:this.state.editID, OBJECT:{name:this.state.editTask}}); this.setState({modal:false, editID:'', editTask:''})} }>Save</button>
                    <button className="btn btn-danger" onClick={()=>this.setState({modal:false, editID:'', editTask:''})}>Cancel</button>
                  </div>
                </div>
              </Modal>

              {/* <Modal isOpen={this.state.modalDelete} onRequestClose={()=>this.setState({modalDelete:false, editID:'', editTask:''})} style={customStyles} contentLabel='Example'>
                <div>
                  <h3>Delete Project</h3>
                  <p>Are you sure you want to delete Project: "{(_.find(this.props.API.PROJECTS, {_id: this.state.editID})) ? _.find(this.props.API.PROJECTS, {_id: this.state.editID}).name : ''}" and all of its tasks?</p>
                  <div className="btn-group" role="group">
                    <button className="btn btn-success" onClick={(e)=>{this.props.API.ACT.projectDelete({_id:this.state.editID}); this.setState({modalDelete:false, editID:'', editTask:''})} }>Delete Project</button>
                    <button className="btn btn-danger" onClick={()=>this.setState({modalDelete:false, editID:'', editTask:''})}>Cancel</button>
                  </div>
                </div>
              </Modal> */}

            </div>
    )
  }
}

module.exports = Projects
