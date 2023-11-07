package io.github.nelsonssoares.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.nelsonssoares.domain.model.Planet;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long>{

}
