package com.intech.webfluxtest.beans;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Бин сообщения чат-а
 * 
 * @author legioner
 *
 */
public class ChatMessage {

	private static AtomicInteger ID_GENERATOR = new AtomicInteger(0);


	private MessageType type;

	private final int id;

	private Payload payload;

	private final long timestamp;

	 @JsonCreator
	public ChatMessage(@JsonProperty("type") MessageType type, @JsonProperty("payload") Payload payload) {
		this.type = type;
		this.payload = payload;
		this.id = ID_GENERATOR.addAndGet(1);
		this.timestamp = System.currentTimeMillis();
	}


	public MessageType getMessageType() {
		return type;
	}

	public Payload getPayload() {
		return payload;
	}

	public User getUser(){
		return payload.getUser();
	}

	public int getId() {
		return id;
	}


	public long getTimestamp() {
		return timestamp;
	}
	
	public static ChatMessage user(User user, MessageType messageType) {
		Payload payload = new Payload(user, new HashMap<>());
		return new ChatMessage(messageType, payload);
	}

}
