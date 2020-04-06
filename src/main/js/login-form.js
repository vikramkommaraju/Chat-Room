"use strict";

import React, {Component} from 'react'; 
var WebSocketService = require('./webSocketService');
import ChatWindow from './chat-window.js';

const UsernameInput = (props) => {
	return (
			<div className="form-group">
	        <label htmlFor="exampleInputEmail1">Salesforce username</label>
	        <input
	          value={props.username}
	          onChange={props.onChange}
	          type="email"
	          className="form-control"
	          id="exampleInputEmail1"
	          placeholder="Enter your email" />		
	        </div>
	);	
}

const PasswordInput = (props) => {
	return (
			<div className="form-group">
	        <label htmlFor="exampleInputPassword1">Password</label>
	        <input
	         value={props.password}
	         onChange={props.onChange}
	          type="password"
	          className="form-control"
	          id="exampleInputPassword1"
	          placeholder="Password"
	        />
	      </div>
	);	
}

const LoginButton = (props) => {
	return (
		<button type="submit" className="btn btn-primary btn-lg">
	        Login
	    </button>
	);
}

class LoginForm extends Component {
	
	 constructor(props) 
	    { 
	        super(props); 
	        this.state = {
	    			username: '',
	    			password: '',
	    			loginState: 'signedout',
	    			//endpoint: 'https://na44.stmfa.stm.salesforce.com/'
	    			endpoint: 'http://vkommaraju-ltm.internal.salesforce.com:6109/'
		    				
	    		};
	    } 
	 
	 componentDidMount() {
	    	WebSocketService.connect(() => {
	    		console.log('Connected to WebSocket server. Logging in...');	    		
	    		WebSocketService.register('/topic/login', (resp) => {
					console.log('Logged in successfully');
					if(this.state.username == JSON.parse(resp.body).username) {
						this.setState({ loginState: 'success' });						
					}
					
				});	
	    		
	    		WebSocketService.register('/topic/logout', (resp) => {
					console.log('Logged out successfully');
					if(this.state.username == JSON.parse(resp.body).username) {
						this.setState({ loginState: 'signedout' });						
					}
					
				});	
	    	});	    	
	    };
	
	handleUserNameChange = (event) => {
		var self = this;
		self.setState({ username: event.target.value })
	};
	
	handlePasswordChange = (event) => {
		var self = this;
		self.setState({ password: event.target.value })
	};
			
	handleOnSubmit = (event) => {
		var self = this;
		event.preventDefault();
		self.setState({ loginState: 'in-progress' });
		console.log('Login in Progress!');
		WebSocketService.send('/login', JSON.stringify(this.state));				
		console.log(this.state);
	};
	
	render () {
		if(this.state.loginState == 'in-progress') {
			console.log('Is in Progress');
			return <div>Loggin in. Please wait...</div>;
		} else if(this.state.loginState == 'success') {
			return <ChatWindow user={{username: this.state.username}}/>
		}
		return (
				<div className="container">
			      <div className="row">
			        <div className="col-lg-offset-2 col-lg-10">
				        <form onSubmit={this.handleOnSubmit}>
					      <UsernameInput username={this.state.username} onChange={this.handleUserNameChange}/>
					      <PasswordInput password={this.state.password} onChange={this.handlePasswordChange}/>
					      <LoginButton />
					    </form>
			        </div>
			      </div>
			    </div>	
		);
	}
}

export default LoginForm; 