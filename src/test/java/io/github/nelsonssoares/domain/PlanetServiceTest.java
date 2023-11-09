package io.github.nelsonssoares.domain;

import static io.github.nelsonssoares.commons.PlanetConstants.INVALID_PLANET;
import static io.github.nelsonssoares.commons.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import io.github.nelsonssoares.domain.model.Planet;
import io.github.nelsonssoares.domain.model.QueryBuilder;
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
	
	@Test
	public void createPlanet_WithInvalidData_ThrowsException() {
		
		/*
		 * TESTA SOMENTE SE LANÇA EXCEPTION, NÃO FAZ VALIDAÇÃO!
		 */
		
		when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);
		assertThatThrownBy(()-> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
	}
	
	@Test
	public void getPlanet_ByExistingId_ReturnsPlanet() {
		when(planetRepository.findById(anyLong())).thenReturn(Optional.of(PLANET));
		Optional<Planet> sut = planetService.get(1L);
		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(PLANET);
	}
	
	@Test
	public void getPlanet_ByUnexistingId_ReturnsEmpty() {
		when(planetRepository.findById(anyLong())).thenReturn(Optional.empty());
		
		Optional<Planet> sut = planetService.get(1L);
		
		assertThat(sut).isEmpty();
		
	}
	
	@Test
	public void getPlanet_ByExistingName_ReturnPlanet() {
		when(planetRepository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));
		
		Optional<Planet> sut = planetService.getByName(PLANET.getName());
		
		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(PLANET);
		
		
	}
	
	@Test
	public void getPlanet_ByUnexistingName_ReturnsEmpty() {
		when(planetRepository.findByName(anyString())).thenReturn(Optional.empty());
		
		Optional<Planet> sut = planetRepository.findByName(anyString());
		
		assertThat(sut).isEmpty();
	}
	
//	@Test 
//	public void listPlanets_ReturnsAllPlanets() {
//		List<Planet> planets = new ArrayList<>() {
//			{
//				add(PLANET);
//			}
//		};
//		Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getClimate(), PLANET.getTerrain(), null));
//		when(planetRepository.getList(query)).thenReturn(planets);
//		
//		List<Planet> sut = planetService.getByFilter(PLANET.getTerrain(), PLANET.getClimate());
//		
//		assertThat(sut).isNotEmpty();
//		assertThat(sut).hasSize(1);
//		assertThat(sut.get(0)).isEqualTo(PLANET);
//		
//	}
//	
//	@Test
//	public void listPlanets_ReturnsNoPlanets() {
//		
//		when(planetRepository.getList(any())).thenReturn(Collections.emptyList());
//		
//		List<Planet> sut = planetService.getByFilter(PLANET.getClimate(), PLANET.getTerrain());
//
//		assertThat(sut).isEmpty();
//	}
	
	@Test
	public void deletePlanet_WithExistingId_doesNotThrowAnyException() {
		assertThatCode(() -> planetService.remove(1L)).doesNotThrowAnyException();
	}
	
	@Test
	public void removePlanet_WithUnexistingId_ThrowsException() {
		doThrow(new RuntimeException()).when(planetRepository).deleteById(99L);
		
		assertThatThrownBy(() -> planetService.remove(99L)).isInstanceOf(RuntimeException.class);
	}
}
