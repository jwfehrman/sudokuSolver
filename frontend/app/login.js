import React from 'react'
import ReactDom from 'react-dom'
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

class Login extends React.Component{
  constructor(props){
    super(props)
    this.state = {
      modal:false,
      loginFailed:false,
      registerFailed:false,
    }
  }

  validateForm(e, formName, main){
    var form = document.forms[formName]
    if(formName === 'tryLogin' || form['password'].value === form['passwordConfirm'].value){
      var xhttp = new XMLHttpRequest();
      xhttp.onreadystatechange = function(){
        if (this.readyState == 4 && this.status == 200) {
          if(this.responseText === 'false' && formName === 'tryLogin'){
            console.log('login failed')
            main.setState({loginFailed: true});
          }
          else if(this.responseText === 'false' && formName === 'register'){
            console.log('register failed')
            main.setState({registerFailed:true});
          }
          else{
            // console.log(window.location.hostname + ':3000')
            window.location.href = 'http://' + window.location.hostname + ':3000'
          }
        }
      };

      xhttp.open("POST", "/" + formName + "?username=" + form['username'].value + "&password=" + form['password'].value, true);
      xhttp.send();
    }
    else{
      if(e)
        e.preventDefault();
      return false;
    }
  }

  render(){
    console.log(this.state);
    return(
      <div style={{border: '1px solid lightgray', borderRadius: '5px'}} className="container">
        <br/>
        <button className='btn btn-warning pull-right' onClick={()=>this.setState({modal:true})}>Register</button>
        <h1 style={{color:'#33cc33'}}>Login</h1>
        {(this.state.loginFailed) ? <h3 style={{color: 'red'}}>Login Failed - Wrong Username OR Password</h3> : null}
        <form className="form-group" name='tryLogin' action="/tryLogin" method="post">
          <h4 style={{color:'#33cc33'}}>Username</h4>
          <input className="form-control" id="username" type="text" name="username" />
          <h4 style={{color:'#33cc33'}}>Password</h4>
          <input className="form-control" id="password" type="password" name="password" />
        </form>
        <button className='btn btn-default pull-right' onClick={(e)=>this.validateForm(e, 'tryLogin', this)}>Login</button>

        <Modal isOpen={this.state.modal} onRequestClose={()=>this.setState({modal:false})} style={customStyles} contentLabel='Example'>
          <div>
            <h1>Register Account</h1>
            {(this.state.registerFailed) ? <h3 style={{color: 'red'}}>Registration Failed - Username Already Exists</h3> : null}
            <form className='form-group' name='register'>
              <h4>Username</h4>
              <input className='form-control' type='text' name='username' required />
              <h4>Password</h4>
              <input className='form-control' type='password' name='password' required />
              <h4>Confirm Password</h4>
              <input className='form-control' type='password' name='passwordConfirm' required />
            </form>
            <button className='btn btn-success pull-right' onClick={(e)=>this.validateForm(e, 'register', this)}>Confirm</button>
          </div>
        </Modal>
      </div>
    )
  }
}

ReactDom.render(<Login />, document.getElementById('login'))
