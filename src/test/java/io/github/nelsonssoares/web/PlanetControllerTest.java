package io.github.nelsonssoares.web;

import static io.github.nelsonssoares.commons.PlanetConstants.PLANET;
import static io.github.nelsonssoares.commons.PlanetConstants.TATOOINE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.nelsonssoares.domain.model.Planet;
import io.github.nelsonssoares.service.PlanetService;
//Indica qual classe deve ser testa, por padrão instancia todos os controller, caindo asim o desempenho
@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {
	
	//Constroi a requisição
	@Autowired
	private MockMvc mockMvc;
	
	//Mapeador de entidade e serializador e deserializador
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private PlanetService planetService;
	
	@Test
	public void createPlanet_withValidData_ReturnsCreated() throws JsonProcessingException, Exception {
		when(planetService.create(PLANET)).thenReturn(PLANET);
		
		mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(PLANET))
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$").value(PLANET));
		
	}
	
	@Test
	public void createPlanet_WithInvalidData_ReturnsBadRequest() throws JsonProcessingException, Exception {
		Planet emptyPlanet = new Planet();
		Planet invalid_Planet = new Planet("","","");
		
		mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(emptyPlanet))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity());
				
		mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(invalid_Planet))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void createPlanet_WithExistingName_ReturnsConflict() throws JsonProcessingException, Exception {
		when(planetService.create(any())).thenThrow(DataIntegrityViolationException.class);
		
		mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(PLANET))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict());
	}
	
	@Test
	public void getPlanet_ByExistingId_ReturnsPlanet() throws Exception {
		when(planetService.get(1L)).thenReturn(Optional.of(PLANET));
		

		mockMvc
		.perform(get("/planets/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").value(PLANET));
	}
	
	@Test
	public void getPlanet_ByUnexistingId_ReturnsNotFound() throws Exception { 
		
		mockMvc.perform(get("/planets/1"))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void getPlanet_ByExistingName_ReturnsPlanet() throws Exception {
		when(planetService.getByName(PLANET.getName())).thenReturn(Optional.of(PLANET));
		
		mockMvc
		.perform(get("/planets/name/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").value(PLANET));
	}
	
	@Test
	public void getPlanet_ByUnexistingName_ReturnsEmpty() throws Exception {
		mockMvc.perform(get("/planets/name/1"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void listPlanets_ReturnsFilteredPlanets() throws Exception{
		when(planetService.getByFilter(null, null)).thenReturn((List<Planet>) PLANET);
		when(planetService.getByFilter(TATOOINE.getTerrain(), TATOOINE.getClimate())).thenReturn(List.of(TATOOINE));
		
		mockMvc
		.perform(get("/planets"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)));
		
		mockMvc
		.perform(get("/planets?="+String.format("terrain=%s&climate=%s", TATOOINE.getTerrain(), TATOOINE.getClimate())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0]").value(TATOOINE));
		
	}
	
	@Test
	public void listPlanets_ReturnsNoPlanets() throws Exception{
		when(planetService.getByFilter(null, null)).thenReturn(Collections.emptyList());
		
	}
	
	
}
