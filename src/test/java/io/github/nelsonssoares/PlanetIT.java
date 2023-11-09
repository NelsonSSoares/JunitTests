package io.github.nelsonssoares;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import io.github.nelsonssoares.domain.model.Planet;
import static io.github.nelsonssoares.commons.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static io.github.nelsonssoares.commons.PlanetConstants.TATOOINE;

//Sobe Tomcat para test end-to-end
@ActiveProfiles("it")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/remove_planets.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD )
@Sql(scripts = {"/import_planets.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD )
public class PlanetIT {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void createPlanet_ReturnsCreated() {
									//simula requisição para o controler
		ResponseEntity<Planet> sut = restTemplate.postForEntity("/planets", PLANET, Planet.class);
		
		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(sut.getBody().getId()).isNotNull();
		assertThat(sut.getBody().getName()).isEqualTo(PLANET.getName());
		assertThat(sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
		assertThat(sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
	}
	
	@Test
	public void getPlanet_ReturnsPlanet() {
		ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/1", Planet.class);
		
		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(sut.getBody()).isEqualTo(TATOOINE);	
	}
	 @Test
	  public void getPlanetByName_ReturnsPlanet() {
		 ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/name/"+TATOOINE.getName(), Planet.class);
		 
		 assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
		 assertThat(sut.getBody()).isEqualTo(TATOOINE);
	 }

	  @Test
	  public void listPlanets_ReturnsAllPlanets() {
		  ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets", Planet[].class);
		  
		  assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
		  assertThat(sut.getBody()).isEqualTo(TATOOINE);
		  assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
	  }

	  @Test
	  public void listPlanets_ByClimate_ReturnsPlanets() {
		  ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets?climate="+TATOOINE.getClimate(), Planet[].class);
		  
		  assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
		  assertThat(sut.getBody()).isEqualTo(TATOOINE);
		  assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
	  }

	  @Test
	  public void listPlanets_ByTerrain_ReturnsPlanets() {
  ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets?terrain="+TATOOINE.getTerrain(), Planet[].class);
		  
		  assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
		  assertThat(sut.getBody()).isEqualTo(TATOOINE);
		  assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
	  }

	  @Test
	  public void removePlanet_ReturnsNoContent() {
	    ResponseEntity<Void> sut = restTemplate.exchange("/planets/"+TATOOINE.getId(), HttpMethod.DELETE, null, Void.class);
	    
	    assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	  }
	
	
}
