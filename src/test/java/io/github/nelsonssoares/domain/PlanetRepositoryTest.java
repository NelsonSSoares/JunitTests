package io.github.nelsonssoares.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import io.github.nelsonssoares.domain.model.Planet;
import io.github.nelsonssoares.domain.model.QueryBuilder;

import static io.github.nelsonssoares.commons.PlanetConstants.PLANET;
import static io.github.nelsonssoares.commons.PlanetConstants.TATOOINE;

@DataJpaTest // Gera banco em memoria H2, para teste de banco de dados
public class PlanetRepositoryTest {
	
	@Autowired
	private PlanetRepository planetRepository;
	
	//interage com o banco de dados para auxiliar com o teste do repository
	//pois não é recomendado utilizar o proprio metodo repository para testar seus proprios metodos 
	@Autowired
	private TestEntityManager entityManager;
	
	//Limpa dados apos a execução de cada teste
	@AfterEach
	public void afterEach() {
		PLANET.setId(null);
	}
	
	
	@Test
	public void createPlanet_WithValidData_ReturnsPlanet() {
		Planet planet = planetRepository.save(PLANET);
		System.out.println(planet);
		Planet sut = entityManager.find(Planet.class, planet.getId());
		
		/*
		 * como planet ainda não tem ID
		 * temos que testar a igualdade dos seus atributos
		 */
		assertThat(sut).isNotNull();
		assertThat(sut.getName()).isEqualTo(PLANET.getName());
		assertThat(sut.getClimate()).isEqualTo(PLANET.getClimate());
		assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());
	
	}
	
	@Test
	public void createPlanet_WithInvalidData_Returns_ThrowsException() {
		Planet emptyPlanet = new Planet();
		Planet invalid_Planet = new Planet("","","");
		
		assertThatThrownBy(()-> planetRepository.save(emptyPlanet)).isInstanceOf(RuntimeException.class);
		assertThatThrownBy(()-> planetRepository.save(invalid_Planet)).isInstanceOf(RuntimeException.class);
	}
	
	@Test
	public void creatPlanet_WithExistingName_ThrowsException() {
		Planet planet = entityManager.persistFlushFind(PLANET);
		
		entityManager.detach(planet); //para de monitorar a entidade criada, para não atualizar e gerar conflito caso exista nome
		planet.setId(null);
		
		assertThatThrownBy(()-> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
	}
	
	@Test
	public void getPlanet_ByExistingId_ReturnsPlanet() {
		Planet planet = entityManager.persistAndFlush(PLANET);
		Optional<Planet> planetOpt = planetRepository.findById(planet.getId());
		
		assertThat(planetOpt).isNotEmpty();
		assertThat(planetOpt.get()).isEqualTo(planet);
	}
	
	@Test
	public void getPlanet_byUnexistingId_ReturnsEmpty() {
		Optional<Planet> planetOpt = planetRepository.findById(1L);
		
		assertThat(planetOpt).isEmpty();
		
	}
	
	@Test
	public void getPlanet_ByExistingName_ReturnsPlanet() {
		Planet planet = entityManager.persistAndFlush(PLANET);
		Optional<Planet> planetOpt = planetRepository.findByName(planet.getName());
		
		assertThat(planetOpt).isNotEmpty();
		assertThat(planetOpt.get()).isEqualTo(planet);
	}
	@Test
	public void getPlanet_ByUnexistingName_ReturnsEmpty() {
		Optional<Planet> planetOpt = planetRepository.findByName("name");
		
		assertThat(planetOpt).isEmpty();
	}
	@Test
	public void listPlanets_ReturnsFilteredPlanets() {
		Example<Planet> queryWithoutFilters = QueryBuilder.makeQuery(new Planet());
		Example<Planet> queryWithFilters = QueryBuilder.makeQuery(new Planet(0, TATOOINE.getClimate(),TATOOINE.getTerrain(), null));
		
		List<Planet>  responseWithoutFilters = planetRepository.getList(queryWithoutFilters);
		List<Planet>  responseWithFilters = planetRepository.getList(queryWithFilters);
	
		assertThat(responseWithoutFilters).isNotEmpty();
		assertThat(responseWithoutFilters).hasSize(3);
		assertThat(responseWithFilters).isNotEmpty();
		assertThat(responseWithFilters).hasSize(1);
		assertThat(responseWithFilters.get(0)).isEqualTo(TATOOINE);
	}
	
	@Test
	public void listPlanets_ReturnsNoPlanets() {
		Example<Planet> query = QueryBuilder.makeQuery(new Planet());
		List<Planet>  response = planetRepository.getList(query);
		
		assertThat(response).isEmpty();
	}
}
