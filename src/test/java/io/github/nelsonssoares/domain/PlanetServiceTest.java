package io.github.nelsonssoares.domain;

import static io.github.nelsonssoares.commons.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.nelsonssoares.domain.model.Planet;
import io.github.nelsonssoares.service.PlanetService;

//@SpringBootTest(classes = PlanetService.class)
@ExtendWith(MockitoExtension.class) // mais indicado para teste unitario comparado com @springboottest
public class PlanetServiceTest {
	
	//@Autowired
	@InjectMocks //Instancia a classe a ser testada
	private PlanetService planetService;
	
	//Anotacao para se usar quando tem o @springBootTest(classes ="")
	//Usuado para mocar depédencia da classe que esta em teste
	//@MockBean usa-se quando esta com @springboottest
	@Mock
	private PlanetRepository planetRepository;
	
	
	@Test
	public void createPlanet_WithValueData_ReturnsPlanet() {
		/*
		 * AAA 
		 * ARRANGE/ ACT / ASSERT
		 */
		
		
		//emula o comportamento da dependencia dentro da classe testada
		when(planetRepository.save(PLANET)).thenReturn(PLANET);
		//System under test
		Planet sut =  planetService.create(PLANET);
		
		assertThat(sut).isEqualTo(PLANET);
	}
}
