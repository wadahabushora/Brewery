package com.brewery.management.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.brewery.management.models.Beers;

@Repository
public interface BeersRepository extends JpaRepository<Beers, Long>, JpaSpecificationExecutor<Beers>{


	Beers findByBeerName(String beerName);

	List<Beers> findByBreweryId(long breweryId);


	
	
}
