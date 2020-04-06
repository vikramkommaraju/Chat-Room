package com.salesforce.chat.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.LoginResult;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.BookA1__e;
import com.sforce.soap.enterprise.sobject.PE2_HV__e;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

@Service
public class EnterpriseService {
	
	private Map<String, LoginResult> activeUsers = new HashMap<>();
	private Map<String, EnterpriseConnection> userNameToConnectionMap = new HashMap<>();

	public LoginResult login(String username, String password, String endpoint) throws ConnectionException {
		if(isActive(username)) {
			return activeUsers.get(username);
		}
		EnterpriseConnection conn = null;
		if(userNameToConnectionMap.containsKey(username)) {
			conn = userNameToConnectionMap.get(username);
		} else {
			conn = new EnterpriseConnection(getConnector(username, password, endpoint));			
		}
		LoginResult loginResult = conn.login(username, password);
		activeUsers.put(username, loginResult);
		userNameToConnectionMap.put(username, conn);
		System.out.println("Login complete: activeUsers=" + activeUsers);
		System.out.println("Login complete: userNameToConnectionMap=" + userNameToConnectionMap);
		
		return loginResult;
	}
	
	private boolean isActive(String username) {
		return activeUsers.containsKey(username);
	}

	public SaveResult[] publish(String username, String message) throws Exception {
		System.out.println("activeUsers: " + activeUsers);
		System.out.println("userNameToConnectionMap: " + userNameToConnectionMap);
		
		EnterpriseConnection conn = userNameToConnectionMap.get(username);
		return conn.create(createPublishEvent(message));
	}
	
	private SObject[] createPublishEvent(String message) {
		SObject[] records = new SObject[1];
		PE2_HV__e event = new PE2_HV__e();
		event.setPE_TEXT2__c(message);
		records[0] = event;
		return records;
	}

	private static ConnectorConfig getConnector(String username, String password, String endpoint) {
		ConnectorConfig wscConfig = new ConnectorConfig();
		wscConfig.setAuthEndpoint(endpoint+"services/Soap/c/46.0");
		wscConfig.setServiceEndpoint(endpoint+"services/Soap/c/46.0");
		wscConfig.setUsername(username);
		wscConfig.setPassword(password);
		return wscConfig;
	}
}
