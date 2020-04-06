'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
var WebSocketService = require('./webSocketService');
import LoginForm from './login-form';

const MessageList = (props) => (
		<div>
	  	{props.messages.map(message => <MessageRow {...message}/>)}
		</div>
);
	  	
class MessageRow extends React.Component {
	render() {
  	const message = this.props;
  	return (
  			
  			<div className="container">
	  		  <div className="row">{message.from}</div>
	  		  <div className="row">
	  		  	<div className="col-sm">
	  		  		<img src={message.avatar_url} style={{width: "50px", height: "50px"}} />
	  		  			&nbsp;&nbsp;{message.text}
	  		  		</div>
	  		  </div>
	  		  <div className="row"><small>{message.timestamp}</small></div>
	  		  <br />
	  		  <br />
	  		</div>
    );
  }
}
	  	
class Form extends React.Component {
	state = { newMessage: '' };
	handleSubmit = (event) => {
  	event.preventDefault();
  	WebSocketService.send('/publish', JSON.stringify({username: 'test@pub1.org', 'message': this.state.newMessage}));
    console.log(this.state.newMessage);
  };
	render() {
  	return (
    	<form onSubmit={this.handleSubmit}>
    	  <input 
          type="text" 
          value={this.state.newMessage}
          onChange={event => this.setState({ newMessage: event.target.value })}
          placeholder="Type your message " 
          required 
        />
        <button>Send Message</button>
    	</form>
    );
  }
}

class App extends React.Component {
	constructor(props) {
		super(props);
		this.state = {messages: []};
	}

	componentDidMount() {
		WebSocketService.connect( () => {
			console.log('Connected');
			
			WebSocketService.register('/topic/login', (resp) => {
				console.log('Logged in...');
				

				WebSocketService.register('/topic/publish', (resp) => {
					console.log('Published message...');
					this.setState({
						messages: [...this.state.messages, {from: JSON.parse(resp.body).eventId, text: JSON.parse(resp.body).message, avatar_url: "https://avatars0.githubusercontent.com/u/810438?v=4", timestamp: "08:54 PM"}]
						});
				});
			});
			
			WebSocketService.send('/login', JSON.stringify({userName: 'test@pub1.org', password: 'test1234', endpoint: 'https://na44.stmfa.stm.salesforce.com/'}));
			
			
			});
	}	  
	  
    render() {
  	return (
    	<div>
    	  <div className="header">{this.props.title}</div>
        <MessageList messages={this.state.messages} />
        <Form />
        </div>
    );
  }	
}

ReactDOM.render(
	<LoginForm />,	
	document.getElementById('react')
);