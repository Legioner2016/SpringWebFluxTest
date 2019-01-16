package com.intech.webfluxtest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

/**
 * Когда пробовал разные варианты route(request -> response) - пробовал и вариант с лямбдой и методом из отдельного бин-а
 * Разницы - никакой - ошибки те же  
 * 
 * @author legioner
 *
 */
@Component
public class GreetingHandler {

    public Mono<ServerResponse> listUsers(ServerRequest request)
    {
        Map<String,Object> data = new HashMap<>();
        return ServerResponse.ok().contentType(MediaType.TEXT_HTML).render("greeting", data);
    }
	
}
