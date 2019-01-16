package com.intech.webfluxtest.beans;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;
import static java.util.Arrays.asList;

/**
 * Статистика пользователей (по сути - просто список пользователей в чате, с событиями типа подключение/отключение)
 * 
 * @author legioner
 *
 */
public class UserStats {

	 UnicastProcessor<ChatMessage> eventPublisher;
	    Map<String, Stats> userStatsMap = new ConcurrentHashMap<>();

	    public UserStats(Flux<ChatMessage> events, UnicastProcessor<ChatMessage> eventPublisher) {
	        this.eventPublisher = eventPublisher;
	        events
	                .filter(type(MessageType.CHAT_MESSAGE, MessageType.USER_JOINED))
	                .subscribe(this::onChatMessage);
	        events
	                .filter(type(MessageType.USER_LEFT))
	                .map(ChatMessage::getUser)
	                .map(User::getAlias)
	                .subscribe(userStatsMap::remove);

//	        events
//	                .filter(type(MessageType.USER_JOINED))
//	                .map(event -> Event.type(USER_STATS)
//	                        .withPayload()
//	                        .systemUser()
//	                        .property("stats", new HashMap<>(userStatsMap))
//	                        .build()
//	                )
//	                .subscribe(eventPublisher::onNext);
	    }

	    private static Predicate<ChatMessage> type(MessageType... types){
	        return event ->  asList(types).contains(event.getMessageType());
	    }

	    private void onChatMessage(ChatMessage event) {
	        String alias = event.getUser().getAlias();
	        Stats stats = userStatsMap.computeIfAbsent(alias, s -> new Stats(event.getUser()));
	        stats.onChatMessage(event);
	    }

	    private static class Stats {
	        private User user;
	        private long lastMessage;
	        private AtomicInteger messageCount = new AtomicInteger();

	        public Stats(User user) {
	            this.user = user;
	        }

	        public void onChatMessage(ChatMessage event) {
	            lastMessage = event.getTimestamp();
	            if(MessageType.CHAT_MESSAGE == event.getMessageType()) messageCount.incrementAndGet();
	        }

	        public User getUser() {
	            return user;
	        }

	        public long getLastMessage() {
	            return lastMessage;
	        }

	        public int getMessageCount() {
	            return messageCount.get();
	        }
	    }
	
}
