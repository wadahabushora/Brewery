package com.brewery.management.services;

import java.util.List;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brewery.management.constant.Constant;
import com.brewery.management.exceptions.BeerAlreadyExistException;
import com.brewery.management.exceptions.BeerNotFoundException;
import com.brewery.management.exceptions.BreweryNotFoundException;
import com.brewery.management.exceptions.CommonServiceException;
import com.brewery.management.models.AddBeer;
import com.brewery.management.models.Beers;
import com.brewery.management.models.Brewery;
import com.brewery.management.repositories.BeersRepository;
import com.brewery.management.repositories.BreweryRepository;
import com.brewery.management.repositories.WholeSalerRepository;


@Service
public class BeerServices {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	BeersRepository beerRepository;

	@Autowired
	BreweryRepository breweryRepository;

	@Autowired
	WholeSalerRepository wholeSalerRepository;

	@Autowired
	BreweryServices breweryServices;
	
	

	public Beers findByBeerName(String beerName) {
		return beerRepository.findByBeerName(beerName);

	}

	public AddBeer addBeer(AddBeer request)
			throws CommonServiceException, BeerAlreadyExistException, BreweryNotFoundException {

		final String thisMethod = "addBeer: ";
		if (logger.isDebugEnabled()) {
		logger.debug(String.format(Constant.STRING_FORMAT,thisMethod + Constant.ENTERING + request));
		}
		validateAddBeerRequest(request);

		Brewery findBrewery = breweryServices.findByBreweryId(request.getBreweryId());

		if (findBrewery == null) {
			throw new BreweryNotFoundException();
		}

		Beers findBeer = findByBeerName(request.getBeerName());
		if (findBeer != null) {
			throw new BeerAlreadyExistException();
		}

		Beers beer = new Beers();

		beer.setBeerName(request.getBeerName());
		beer.setAlcoholContent(request.getAlcoholContent());
		beer.setPrice(request.getPrice());
		beer.setBreweryId(request.getBreweryId());
		 beerRepository.save(beer);

		return request;

	}

	public AddBeer validateAddBeerRequest(AddBeer request) throws CommonServiceException {
		if (request.getBeerName() == null || request.getBeerName().equalsIgnoreCase("")) {
			throw new CommonServiceException();
		}

		if (request.getAlcoholContent() == 0) {
			throw new CommonServiceException();
		}

		if (request.getPrice() == 0) {
			throw new CommonServiceException();
		}
		return request;
	}

	public List<Beers> findBeersByBrewery(long breweryId) {
		return beerRepository.findByBreweryId(breweryId);
	}

	

	@Transactional
	public boolean deleteBeer(String beerName) throws BeerNotFoundException {

		Beers beer = findByBeerName(beerName);

		if (beer == null) {
			throw new BeerNotFoundException();
		}

		beerRepository.deleteById(beer.getSystemId());

		return true;

	}

	
	

}
