package com.brewery.management.brewery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.brewery.management.exceptions.BeerAlreadyExistException;
import com.brewery.management.exceptions.BeerNotFoundException;
import com.brewery.management.exceptions.BreweryNotFoundException;
import com.brewery.management.exceptions.CommonServiceException;
import com.brewery.management.models.AddBeer;
import com.brewery.management.models.Beers;
import com.brewery.management.models.Brewery;
import com.brewery.management.models.ClientRequest;
import com.brewery.management.repositories.BeersRepository;
import com.brewery.management.services.BeerServices;
import com.brewery.management.services.BreweryServices;

@ExtendWith(MockitoExtension.class)
class BeerServicesTest {

	@Mock
	private BreweryServices breweryServices;

	@Mock
	BeersRepository beerRepository;

	@InjectMocks
	private BeerServices beerServices;

	@Test
	void testFindByBeerName() {

		String beerName = "test beer";

		Beers beer = new Beers();
		beer.setBeerName("Test Brewery");
		beer.setBreweryId(1);
		beer.setAlcoholContent(5);
		beer.setPrice(2.2);

		lenient().when(beerServices.findByBeerName(beerName)).thenReturn(beer);

		Beers result = beerRepository.findByBeerName(beerName);

		assertEquals(beer, result);

	}

	@Test
	void testAddBeer() throws CommonServiceException, BeerAlreadyExistException, BreweryNotFoundException {

		AddBeer request = new AddBeer();
		request.setBeerName("Test Brewery");
		request.setBreweryId(1);
		request.setAlcoholContent(5);
		request.setPrice(2.2);

		Beers beer = new Beers();
		beer.setBeerName(request.getBeerName());
		beer.setBreweryId(request.getBreweryId());
		beer.setAlcoholContent(request.getAlcoholContent());
		beer.setPrice(request.getPrice());

		Brewery brewery = new Brewery();
		brewery.setBreweryName("Test Brewery");
		brewery.setBreweryId(1);

		lenient().when(breweryServices.findByBreweryId(request.getBreweryId())).thenReturn(brewery);

		lenient().when(beerServices.findByBeerName(request.getBeerName())).thenReturn(null);

		AddBeer result = beerServices.addBeer(request);

		assertEquals(request, result);

	}

	@Test
	void testFindBeersByBrewery() throws CommonServiceException, BeerAlreadyExistException, BreweryNotFoundException {

		long breweryId = 1;

		Beers beer1 = new Beers();
		beer1.setBeerName("beer 1");
		beer1.setBreweryId(1);
		beer1.setAlcoholContent(5);
		beer1.setPrice(2.2);

		Beers beer2 = new Beers();
		beer2.setBeerName("beer 2");
		beer2.setBreweryId(1);
		beer2.setAlcoholContent(6);
		beer2.setPrice(2.1);

		Brewery brewery = new Brewery();
		brewery.setBreweryName("Test Brewery");
		brewery.setBreweryId(1);

		ArrayList<Beers> allBeers = new ArrayList<>();

		allBeers.add(beer1);
		allBeers.add(beer2);

		lenient().when(beerServices.findBeersByBrewery(breweryId)).thenReturn(allBeers);

		List<Beers> result = beerRepository.findByBreweryId(breweryId);

		assertEquals(allBeers, result);

	}

	@Test
	void testDeleteBeer() throws BeerNotFoundException  {

		String beerName = "test beer";

		Beers beer = new Beers();
		beer.setBeerName("Test beer");
		beer.setBreweryId(1);
		beer.setAlcoholContent(5);
		beer.setPrice(2.2);

		lenient().when(beerServices.findByBeerName(beerName)).thenReturn(beer);


		boolean deleteBeer = beerServices.deleteBeer(beerName);

		assertEquals(deleteBeer, true);
	}
	
	@Test
	void testAddBeerWhenBreweryNotFound() {

		AddBeer request = new AddBeer();
		request.setBeerName("Test Brewery");
		request.setBreweryId(1);
		request.setAlcoholContent(5);
		request.setPrice(6.5);
		
		Brewery brewery = new Brewery();
		brewery.setBreweryName("Test Brewery");
		brewery.setBreweryId(1);
		
//		Beers beer = new Beers();
//		beer.setBeerName("beer");
//		beer.setBreweryId(request.getBreweryId());
//		beer.setAlcoholContent(request.getAlcoholContent());
//		beer.setPrice(request.getPrice());

		lenient().when(breweryServices.findByBreweryId(request.getBreweryId())).thenReturn(null);

		Assertions.assertThrows(BreweryNotFoundException.class, () -> {
			beerServices.addBeer(request);
		});

	}
	
	@Test
	void testAddBeerWhenBeerAlreadyExist() {

		AddBeer request = new AddBeer();
		request.setBeerName("beer");
		request.setBreweryId(1);
		request.setAlcoholContent(5);
		request.setPrice(6.5);
		
		Beers beer = new Beers();
		beer.setBeerName("beer");
		beer.setBreweryId(request.getBreweryId());
		beer.setAlcoholContent(request.getAlcoholContent());
		beer.setPrice(request.getPrice());

		Brewery brewery = new Brewery();
		brewery.setBreweryName("Test Brewery");
		brewery.setBreweryId(1);
		
		lenient().when(breweryServices.findByBreweryId(request.getBreweryId())).thenReturn(brewery);
		lenient().when(beerServices.findByBeerName(request.getBeerName())).thenReturn(beer);

		Assertions.assertThrows(BeerAlreadyExistException.class, () -> {
			beerServices.addBeer(request);
		});

	}

	@Test
	public void testValidateRequestAddBeer() {
		AddBeer requestBeerName = new AddBeer();
		requestBeerName.setBeerName("");
		requestBeerName.setAlcoholContent(0);
		requestBeerName.setBreweryId(0);
		requestBeerName.setPrice(0);
		try {
			AddBeer result = beerServices.validateAddBeerRequest(requestBeerName);
			assertEquals(requestBeerName, result);
		} catch (CommonServiceException e) {
		}
	}
	
	@Test
	void testDeleteBeerWhenBeerNotFound() throws BeerNotFoundException  {

		String beerName = "test beer";

		Beers beer = new Beers();
		beer.setBeerName("Test beer");
		beer.setBreweryId(1);
		beer.setAlcoholContent(5);
		beer.setPrice(2.2);

		lenient().when(beerServices.findByBeerName(beerName)).thenReturn(null);

		Assertions.assertThrows(BeerNotFoundException.class, () -> {
			beerServices.deleteBeer(beerName);
		});
	}
}
