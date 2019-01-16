package com.intech.webfluxtest;

import java.io.IOException;
import java.util.Optional;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intech.webfluxtest.beans.ChatMessage;
import com.intech.webfluxtest.beans.MessageType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

/**
 * Handler функции для маппинга с webservlet handler
 * 
 * 
 * @author legioner
 *
 */
public class ChatSocketHandler implements WebSocketHandler {

    private UnicastProcessor<ChatMessage> eventPublisher;
    private Flux<String> outputChatMessages;
    private ObjectMapper mapper;

    public ChatSocketHandler(UnicastProcessor<ChatMessage> eventPublisher, Flux<ChatMessage> events) {
        this.eventPublisher = eventPublisher;
        this.mapper = new ObjectMapper();
        this.outputChatMessages = Flux.from(events).map(this::toJSON);
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        WebSocketMessageSubscriber subscriber = new WebSocketMessageSubscriber(eventPublisher);
        session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(this::toChatMessage)
                .subscribe(subscriber::onNext, subscriber::onError, subscriber::onComplete);
        return session.send(outputChatMessages.map(session::textMessage));
    }


    private ChatMessage toChatMessage(String json) {
        try {
        	ChatMessage message = mapper.readValue(json, ChatMessage.class);
            //return mapper.readValue(json, ChatMessage.class);
            
            System.out.println("Recieved new message of type: " + message.getMessageType());
            return message;
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON:" + json, e);
        }
    }

    private String toJSON(ChatMessage event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static class WebSocketMessageSubscriber {
        private UnicastProcessor<ChatMessage> eventPublisher;
        private Optional<ChatMessage> lastReceivedChatMessage = Optional.empty();

        public WebSocketMessageSubscriber(UnicastProcessor<ChatMessage> eventPublisher) {
            this.eventPublisher = eventPublisher;
        }

        public void onNext(ChatMessage event) {
            lastReceivedChatMessage = Optional.of(event);
            eventPublisher.onNext(event);
        }

        public void onError(Throwable error) {
            //TODO log error
            error.printStackTrace();
        }

        public void onComplete() {

            lastReceivedChatMessage.ifPresent(event -> eventPublisher.onNext(
                    ChatMessage.user(event.getUser(), MessageType.USER_LEFT)));
        }

    }
}
