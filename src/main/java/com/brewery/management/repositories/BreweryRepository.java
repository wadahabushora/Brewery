package com.brewery.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.brewery.management.models.Brewery;

@Repository
public interface BreweryRepository extends JpaRepository<Brewery, Long>, JpaSpecificationExecutor<Brewery>{

	Brewery findByBreweryId(long breweryId);



	
	
}
