// Importing combination 
import React, {Component} from 'react'; 
var WebSocketService = require('./webSocketService');
var Redirect = require('react-router-dom').Redirect;
import { Check, CheckAll, ExclamationCircle } from 'react-bootstrap-icons';

const userNameMap = {
		'test@pub1.org': 'John Doe',
		'test@pub2.org': 'Jane Doe'
}

const typeMap = {
		Inbound: "direct-chat-msg",
	    Outbound: "direct-chat-msg right"
};

const spanMap = {
		Inbound: "direct-chat-sender-left",
		Outbound: "direct-chat-sender-right"
		
};

const imgMap = {
		'test@pub1.org': "https://bootdey.com/img/Content/user_1.jpg",
		'John Doe': "https://bootdey.com/img/Content/user_1.jpg",
		'test@pub2.org': "https://bootdey.com/img/Content/user_2.jpg",
		'Jane Doe': "https://bootdey.com/img/Content/user_2.jpg"
};

const statusIconStyle = {
		marginLeft: "-30px",
		marginTop: "-60px"
		};

class MessageList extends Component {
	constructor(props) {
		super(props);
	}
	
	render() {
		return (
				<div className="box-body">
		        <div className="direct-chat-messages">
		        {this.props.messages.map(message => <Message {...message}/>)}
			    </div>
		      </div>
		);
	};
}

class ChatHeader extends Component {
	constructor(props) {
		super(props);
		this.state = {
				isOnline : 'Offline',
				chatWithUser: this.props.user.username == 'test@pub1.org' ? 'Jane Doe' : 'John Doe'
	        };
	}
	
	componentDidMount() {
    	WebSocketService.register('/topic/login', (resp) => {
    		if(JSON.parse(resp.body).username !=  this.props.user.username) {
        		this.setState({isOnline : 'Online'}); 
    		}
    	});
    	
    	WebSocketService.register('/topic/logout', (resp) => {
    		if(JSON.parse(resp.body).username !=  this.props.user.username) {
        		this.setState({isOnline : 'Offline'});    			
    		}
    	});
    	
    	WebSocketService.register('/topic/typing', (resp) => {
    		
    		if(JSON.parse(resp.body).username !=  this.props.user.username) {
    			this.setState({isOnline : 'Online'});  			
    		}
    	});
	}
	handleLogout = (event) => {
		console.log('Logging off...');
		WebSocketService.send('/logout', JSON.stringify({username: this.props.user.username}));	
	};
	
	render() {
		return (
				<div className="box-header with-border">
					<img className="direct-chat-img" src={imgMap[this.state.chatWithUser]} alt="Message User Image" />
					<i className="fa fa-circle text-success" style = {{ color: this.state.isOnline, marginLeft : -15, marginTop : 30 }} aria-hidden="true"></i>
					<h4 className="box-title">&nbsp;&nbsp;Chat with {this.state.chatWithUser}</h4>
					<button onClick={this.handleLogout} type="button" className="btn btn-danger btn-xs pull-right">Log out</button>
				</div>
		);
	};
}

class ChatFooter extends Component {
	constructor(props) {
		super(props);
		this.state = {
	        	newMessage : '',
	        	typingMsg: ''
	        };
	}
	
	componentDidMount() {
    	WebSocketService.register('/topic/typing', (resp) => {
    		
    		if(JSON.parse(resp.body).username !=  this.props.user.username) {
        		this.setState({typingMsg : userNameMap[JSON.parse(resp.body).username] + ' is typing...'});    			
    		}
    	});
    	
    	WebSocketService.register('/topic/publish', (resp) => {
    		
    		if(JSON.parse(resp.body).username !=  this.props.user.username) {
        		this.setState({typingMsg : ''});    			
    		}
    	});
    	
    	WebSocketService.register('/topic/stoppedtyping', (resp) => {
    		if(JSON.parse(resp.body).username !=  this.props.user.username) {
        		this.setState({typingMsg : ''});    			
    		}
    	});
    	
	};
	
	handleSubmit = (event) => {
	  	event.preventDefault();
	    console.log('Submit the new message ' + this.state.newMessage);
	    WebSocketService.send('/publish', JSON.stringify({username: this.props.user.username, message: this.state.newMessage}), ()=>{     
	    	console.log('Successfully sent message');
	    });
	    
	    this.setState({newMessage : ''});
	  };
	  
	handleOnInputChange = (event) => {
		this.setState({ newMessage: event.target.value });
		
		if(event.target.value.length == 0) {
			WebSocketService.send('/stoppedtyping', JSON.stringify({username: this.props.user.username}));
			
		} else {
			WebSocketService.send('/typing', JSON.stringify({username: this.props.user.username}));			
		}
	
	};
	
	render() {
		return (
				<div className="box-footer">
				<form onSubmit={this.handleSubmit}>
				  <small><i>{this.state.typingMsg}</i></small>
				  <div className="input-group">
				    <input type="text" value={this.state.newMessage} onChange={this.handleOnInputChange} name="message" placeholder="Type Message ..." className="form-control" />
				        <span className="input-group-btn">
				          <button type="submit" className="btn btn-primary btn-flat">Send</button>
				        </span>
				  </div>
				</form>
				</div>
		);
	};
}

class MessageStatus extends Component {
	constructor(props) { 
		super(props);
		this.state = {
			status : this.props.status
		}
	}
	
	componentDidMount() {
    	WebSocketService.register('/topic/acks', this.handleAck);
    	WebSocketService.register('/topic/nacks', this.handleNack);
    	WebSocketService.register('/topic/publishComplete', this.handlePublishComplete);
	};
	
	handleAck = (resp) => {
		if(this.props.eventId === resp.body && this.props.type == "Outbound") {
			this.setState({
				status: 'Delivered'
			});	
		}    	
    };
    
    handleNack = (resp) => {
		
		if(this.props.eventId === resp.body && this.props.type == "Outbound") {
			this.setState({
				status: 'Failed'
			});	
		}    	
    };
    
    handlePublishComplete = (resp) => {
    	if(this.props.eventId == JSON.parse(resp.body).msgId && this.props.type == "Outbound") {
    		this.setState({
				status: 'Sent'
			});
    	}
    };
    
    handleRetry = (event) => {
    	WebSocketService.send('/publish', JSON.stringify({username: this.props.username, message: this.props.text}), ()=>{     
	    	console.log('Successfully sent message');
	    });
    };
    
	render() {
		return (
          <div className={spanMap[this.props.type]}>
          
          	<small className="direct-chat-timestamp">
	          	{ this.props.type == "Outbound" && this.state.status == 'Sent' ? <Check /> : 
	              this.props.type == "Outbound" && this.state.status == 'Delivered' ? <CheckAll /> : 
	              this.props.type == "Outbound" && this.state.status == 'Failed' ? < ExclamationCircle onClick={this.handleRetry} style = {{ color: '#de3023' }} /> : '' }
	          	&nbsp;{this.state.status} &nbsp; 
          	    {this.props.timestamp}
          	</small>
          
          </div>

		);
	}
	
}

class Message extends Component {

	constructor(props) 
    { super(props); 
       this.state = {
			eventId : this.props.eventId
		}
    } 
	
	componentDidMount() {
       WebSocketService.register('/topic/publishComplete', (resp) => {
    	   if(this.props.eventId == JSON.parse(resp.body).msgId && this.props.type == "Outbound") {
       		this.setState({
     		   eventId: JSON.parse(resp.body).eventId
       			});
       		}
       });
       
	};
	
	render() 
    { 
		return (
        		<div className={typeMap[this.props.type]}>
                  <span className={spanMap[this.props.type]}>{userNameMap[this.props.sender]}</span>
                  <br />
                  <img className="direct-chat-img" src={imgMap[this.props.sender]} alt="Message User Image" />
                  <br />
                  <div className="direct-chat-text">
                    {this.props.text}
                  </div>
                  <MessageStatus username={this.props.sender} text={this.props.text} eventId={this.state.eventId} type={this.props.type} status='' timestamp={this.props.timestamp}/>
                </div>		
        ); 
        
    }
}

class ChatWindow extends Component { 
    constructor(props) 
    { 
        super(props); 
        this.state = {
        	messages : [] 
        };
    } 
    
    componentDidMount() {		
		WebSocketService.register('/topic/publish', (resp) => {
			console.log('Published message...');
			if(this.getMsgType(resp) == 'Outbound') {
				this.setState({
					messages: [...this.state.messages, {key: JSON.parse(resp.body).eventId, eventId: JSON.parse(resp.body).eventId, sender: JSON.parse(resp.body).username, text: JSON.parse(resp.body).message, type: this.getMsgType(resp), timestamp: this.formatAMPM(new Date)}]
					});				
			}
		});
		
		WebSocketService.register('/topic/newMessage', (resp) => {
			if(this.getMsgType(resp) == 'Inbound') {
				console.log('Received a new message...');
				this.setState({
					messages: [...this.state.messages, {key: JSON.parse(resp.body).eventId, eventId: JSON.parse(resp.body).eventId, sender: JSON.parse(resp.body).username, text: JSON.parse(resp.body).message, type: this.getMsgType(resp), timestamp: this.formatAMPM(new Date)}]
					});				
			}
		});
		
    };
    
    findArrayElementByTitle = (array, key) => {
    	  return array.find((element) => {
    	    return element.key === key;
    	  })
    };
    
    formatAMPM = (date) => {
    	  var hours = date.getHours();
    	  var minutes = date.getMinutes();
    	  var ampm = hours >= 12 ? 'pm' : 'am';
    	  hours = hours % 12;
    	  hours = hours ? hours : 12; // the hour '0' should be '12'
    	  minutes = minutes < 10 ? '0'+minutes : minutes;
    	  var strTime = hours + ':' + minutes + ' ' + ampm;
    	  return strTime;
    };
    
    getMsgType = (resp) => {
    	
    	
    	if(JSON.parse(resp.body).username == this.props.user.username) {
    		console.log('Outbound ');
    	} else {
    		console.log('Inbound');
    	}
    
    	return JSON.parse(resp.body).username == this.props.user.username ? "Outbound" : "Inbound";
    	
    	
    };
    
    postNewMessage = (message) => {
      	console.log('postNewMessage');
      };
    
    render() 
    { 
        return (
        		<div className="row bootstrap snippets w-50">
    			<div className="col-md-3">
    		
    		<div className="box box-primary direct-chat direct-chat-primary">
		      <ChatHeader user={this.props.user}/>
		      <MessageList messages={this.state.messages}/>
		      <ChatFooter user={this.props.user}/>
		    </div>
		    </div>
		    </div>
        		
        		
        ); 
  } 
}   
  
export default ChatWindow; 