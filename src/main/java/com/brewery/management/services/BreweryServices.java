package com.brewery.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.brewery.management.models.Brewery;
import com.brewery.management.repositories.BeersRepository;
import com.brewery.management.repositories.BreweryRepository;
import com.brewery.management.repositories.WholeSalerRepository;


@Service
public class BreweryServices {


	@Autowired
	BeersRepository beerRepository;

	@Autowired
	BreweryRepository breweryRepository;

	@Autowired
	WholeSalerRepository wholeSalerRepository;

	
	
	public Brewery findByBreweryId(long breweryId) {

		return breweryRepository.findByBreweryId(breweryId);
	}

}
