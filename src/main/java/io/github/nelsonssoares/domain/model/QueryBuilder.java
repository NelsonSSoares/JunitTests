package io.github.nelsonssoares.domain.model;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

/*
 * FILTRO PARA QUERY
 */
public class QueryBuilder {
	public static Example<Planet> makeQuery(Planet planet){
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();
		return Example.of(planet, exampleMatcher);
	}
}
