package com.intech.webfluxtest;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Опять же - посмотрел в документации, что инициализатор нужен. В примерах его нет
 * Что будет - если удалить - не пробовал
 * 
 * @author legioner
 *
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(WebFluxTest2Application.class);
	}

}

