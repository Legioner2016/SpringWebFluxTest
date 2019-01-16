package com.intech.webfluxtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import java.util.HashMap;
import java.util.Map;

import com.intech.webfluxtest.beans.ChatMessage;
import com.intech.webfluxtest.beans.UserStats;

import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

/**
 * Изначально проект взят отсюда: https://github.com/monkey-codes/java-reactive-chat
 * Но это не чистый clone (скорре - грязный - большая часть скопирована руками - так часто проще при изучении) 
 * 
 * @author legioner
 *
 */
@SpringBootApplication
@EnableWebFlux
//Если я делаю так - public class WebFluxTest2Application implements WebFluxConfigurer, ApplicationContextAware {
//то view resolver подключается, но работает с ошибкой
public class WebFluxTest2Application { //implements WebFluxConfigurer, ApplicationContextAware {

	@Autowired
	private GreetingHandler handler;
	
	public static void main(String[] args) {
		SpringApplication.run(WebFluxTest2Application.class, args);
	}

	//Если подключаю view resolver - то ему нужен applicationContext
//		private ApplicationContext applicationContext;
//	
//	    @Override
//	    public void setApplicationContext(ApplicationContext applicationContext) {
//	        this.applicationContext = applicationContext;
//	    }

	@Bean
	public UnicastProcessor<ChatMessage> eventPublisher(){
		return UnicastProcessor.create();
	}

	@Bean
	public Flux<ChatMessage> events(UnicastProcessor<ChatMessage> eventPublisher) {
		return eventPublisher
				.replay(25)
				.autoConnect();
	}


	@Bean
	public RouterFunction<ServerResponse> routes(){
		return RouterFunctions.route(
				GET("/"),
				request -> ServerResponse.ok().body(BodyInserters.fromResource(new ClassPathResource("templates/greeting.html")))
				).and(
						RouterFunctions.route(
								GET("/bundle.js"),
								request -> ServerResponse.ok().body(BodyInserters.fromResource(new ClassPathResource("templates/bundle.js")))
								)
						);
		//Вот это я использовал, когда подклбчал viewResolver
		//			return RouterFunctions.route(
		//					GET("/"),
		//					request -> ServerResponse.ok().contentType(MediaType.TEXT_HTML).render("greeting", new HashMap<>())
		//					);
		//Не работает, причем весьма неоднозначно ругается (на маппинг routerfunction)
	}

	@Bean
	public HandlerMapping webSocketMapping(UnicastProcessor<ChatMessage> eventPublisher, Flux<ChatMessage> events) {
		Map<String, Object> map = new HashMap<>();
		map.put("/websocket/chat", new ChatSocketHandler(eventPublisher, events));
		SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
		simpleUrlHandlerMapping.setUrlMap(map);

		//Without the order things break :-/
		simpleUrlHandlerMapping.setOrder(10);
		return simpleUrlHandlerMapping;
	}

	@Bean
	public WebSocketHandlerAdapter handlerAdapter() {
		return new WebSocketHandlerAdapter();
	}

	@Bean
	public UserStats userStats(Flux<ChatMessage> events, UnicastProcessor eventPublisher){
		return new UserStats(events, eventPublisher);
	}
	
	/**
	 * Ниже закомментировано все, что связано с viewresolver-ом
	 */
	//	@Component
	//	public class CustomWebFilter implements WebFilter {
	//	  @Override
	//	  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
	//	    if (exchange.getRequest().getURI().getPath().equals("/")) {
	//	        return chain.filter(exchange.mutate().request(exchange.getRequest().mutate().path("/greeting.html").build()).build());
	//	    }
	//
	//	    return chain.filter(exchange);
	//	  }
	//	}

//	    @Override
//	    public void configureViewResolvers(ViewResolverRegistry registry) {
//	    	ThymeleafReactiveViewResolver resolver = new ThymeleafReactiveViewResolver();
//			resolver.setTemplateEngine(thymeleafTemplateEngine());
//			registry.viewResolver(resolver); 
//	    }
//	    
//	    @Bean
//	    public SpringResourceTemplateResolver thymeleafTemplateResolver() {
//	        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
//	        resolver.setApplicationContext(this.applicationContext);
//	        resolver.setPrefix("classpath:/templates/");
//	        resolver.setSuffix(".html");
//	        resolver.setTemplateMode(TemplateMode.HTML);
//	        resolver.setCacheable(false);
//	        resolver.setCheckExistence(false);
//	        return resolver;
//	
//	    }
//	
//	    @Bean
//	    public ISpringWebFluxTemplateEngine thymeleafTemplateEngine() {
//	        // We override here the SpringTemplateEngine instance that would otherwise be
//	        // instantiated by
//	        // Spring Boot because we want to apply the SpringWebFlux-specific context
//	        // factory, link builder...
//	        SpringWebFluxTemplateEngine templateEngine = new SpringWebFluxTemplateEngine();
//	        templateEngine.setTemplateResolver(thymeleafTemplateResolver());
//	        return templateEngine;
//	    }

}

