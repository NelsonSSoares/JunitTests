package io.github.nelsonssoares.service;

import org.springframework.stereotype.Service;

import io.github.nelsonssoares.domain.PlanetRepository;
import io.github.nelsonssoares.domain.model.Planet;

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
}
