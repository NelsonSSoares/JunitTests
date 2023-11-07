package io.github.nelsonssoares.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.nelsonssoares.domain.model.Planet;
import io.github.nelsonssoares.service.PlanetService;

@RestController
@RequestMapping("/planets")
public class PlanetController {

	private final PlanetService service;
	
	

	public PlanetController(PlanetService service) {
		super();
		this.service = service;
	}



	@PostMapping
	public ResponseEntity<Planet> create(@RequestBody Planet planet){
		Planet planetCreated = service.create(planet);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(planetCreated);
				
	}
}
