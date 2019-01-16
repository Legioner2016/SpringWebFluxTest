package com.intech.webfluxtest.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ПОльзователь (часть данных сообщения)
 * 
 * @author legioner
 *
 */
public class User {

	private String alias;

	public static User systemUser(){
		return new User("System");
	}

	@JsonCreator
	public User(@JsonProperty("alias") String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}



}
