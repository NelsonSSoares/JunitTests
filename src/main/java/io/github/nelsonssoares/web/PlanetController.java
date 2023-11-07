package io.github.nelsonssoares.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.nelsonssoares.domain.model.Planet;
import io.github.nelsonssoares.service.PlanetService;
import jakarta.websocket.server.PathParam;

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
	
	@GetMapping("/{id}")
	public ResponseEntity<Planet> getById(@PathVariable("id") Long id){
		return service.get(id).map(planet -> ResponseEntity.ok(planet))
				.orElseGet(()-> ResponseEntity.notFound().build());
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<Planet> getByName(@PathVariable("name") String name){
		return service.getByName(name).map(planet -> ResponseEntity.ok(planet))
				.orElseGet(()-> ResponseEntity.notFound().build());
	}
	
	//BUSCA COM FILTROS NÃO OBRIGATORIOS!
	
	@GetMapping
	public ResponseEntity<List<Planet>> getList(@RequestParam(required = false)String terrain, @RequestParam(required = false)String climate){
		List<Planet> planets = service.getByFilter(terrain, climate);
		return ResponseEntity.ok(planets);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Planet> remove(@PathVariable("id") Long id){
		service.remove(id);
		return ResponseEntity.noContent().build();
	}
	
	
}
