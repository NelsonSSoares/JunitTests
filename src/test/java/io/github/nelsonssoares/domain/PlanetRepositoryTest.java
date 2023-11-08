package io.github.nelsonssoares.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.nelsonssoares.domain.model.Planet;

import static io.github.nelsonssoares.commons.PlanetConstants.PLANET;


@DataJpaTest // Gera banco em memoria H2, para teste de banco de dados
public class PlanetRepositoryTest {
	
	@Autowired
	private PlanetRepository planetRepository;
	
	//interage com o banco de dados para auxiliar com o teste do repository
	//pois não é recomendado utilizar o proprio metodo repository para testar seus proprios metodos 
	@Autowired
	private TestEntityManager entityManager;
	
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
}
