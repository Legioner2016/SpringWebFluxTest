package com.intech.webfluxtest.beans;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Сообщение
 * 
 * @author legioner
 *
 */
public class Payload {

	private Map<String, Object> properties = new HashMap<>();
	private User	user;

	public Payload(User user, Map<String, Object> properties){
		this.user = user;
		this.properties = properties;
	}

	@JsonCreator
	private Payload(@JsonProperty("user") User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	@JsonAnySetter
	private void setProperties(String name, Object value){
		properties.put(name, value);
	}

	@JsonAnyGetter
	private Map<String, Object> getProperties(){
		return properties;
	}


}
