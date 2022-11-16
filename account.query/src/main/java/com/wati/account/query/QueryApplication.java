package com.wati.account.query;


import com.wati.cqrs.core.infrastructure.QueryDispatcher;
import com.wati.account.query.api.queries.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class QueryApplication {

	private final QueryDispatcher queryDispatcher;

	private final QueryHandler queryHandler ;

	@Autowired
	public QueryApplication(QueryDispatcher queryDispatcher, QueryHandler queryHandler) {
		this.queryDispatcher = queryDispatcher;
		this.queryHandler = queryHandler;
	}

	public static void main(String[] args) {
		SpringApplication.run(QueryApplication.class, args);
	}

	@PostConstruct
	public void registerQueryHandler(){
		queryDispatcher.registerHandler(FindAllAccountsQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountsByIdQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountByHolderQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountWithBalanceQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountByAccountNumber.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindTransactionByAccountId.class,queryHandler::handle);

	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/v3/api-docs").allowedOrigins("http://localhost:9093");
			}
		};
	}

}
