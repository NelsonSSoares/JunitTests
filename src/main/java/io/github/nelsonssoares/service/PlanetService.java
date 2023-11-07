package io.github.nelsonssoares.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import io.github.nelsonssoares.domain.PlanetRepository;
import io.github.nelsonssoares.domain.model.Planet;
import io.github.nelsonssoares.domain.model.QueryBuilder;

@Service
public class PlanetService {
	
	
	private final PlanetRepository repository;
	
	
	
	public PlanetService(PlanetRepository repository) {
		super();
		this.repository = repository;
	}



	public Planet create(Planet planet) {
		return repository.save(planet);
	}
	
	public Optional<Planet> get(Long id){
		return repository.findById(id);
	}
	
	public Optional<Planet> getByName(String name){
		return repository.findByName(name);
	}
	
	public List<Planet> getByFilter(String terrain, String climate){
		Example<Planet> query = QueryBuilder.makeQuery(new Planet(terrain,climate, climate));
		return repository.findAll(query);
	}
	
	public void remove(Long id) {
		repository.deleteById(id);
	}
}
