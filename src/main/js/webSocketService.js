"use strict";
var React = require('react');
var Stomp = require('stompjs');
var SockJS = require('sockjs-client');
var stompClient = null;
	
const server = "/chat-demo";

var WebSocketService = {

	connect : function(callback) {

		var socket = new SockJS(server);
		stompClient = Stomp.over(socket);
		
	    stompClient.connect({}, callback);
	},

	disconnect : function() {
		if (stompClient !== null) {
	        stompClient.disconnect();
	    }
	},

	send : function(channel, msg, callback) {
		stompClient.send(channel, {}, msg);
	},

	register : function(topic, callback) {
		stompClient.subscribe(topic, callback);
	}



};

module.exports = WebSocketService;