package com.brewery.management.beers.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import com.brewery.management.api.BreweryManagementWebService;
import com.brewery.management.constant.Constant;
import com.brewery.management.exceptions.BeerAlreadyExistException;
import com.brewery.management.exceptions.BeerNotFoundException;
import com.brewery.management.exceptions.BreweryNotFoundException;
import com.brewery.management.exceptions.CommonServiceException;
import com.brewery.management.exceptions.OrderGreaterThanStockException;
import com.brewery.management.exceptions.WholeSalerNotFoundException;
import com.brewery.management.models.AddBeer;
import com.brewery.management.models.AddSale;
import com.brewery.management.models.AllBreweryBeers;
import com.brewery.management.models.Beers;
import com.brewery.management.models.Brewery;
import com.brewery.management.models.ClientRequest;
import com.brewery.management.services.BeerServices;
import com.brewery.management.services.BreweryServices;
import com.brewery.management.services.WholeSalerServices;

@ExtendWith(MockitoExtension.class)
class BreweryControllerTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Mock
	private BeerServices beerServices;

	@Mock
	WholeSalerServices wholeSalerServices;

	@Mock
	private BreweryServices breweryServices;

	@InjectMocks
	private BreweryManagementWebService breweryController;

	@Test
	void testAddBeer() throws CommonServiceException, BeerAlreadyExistException, BreweryNotFoundException {

		// Prepare test data
		AddBeer request = new AddBeer();
		request.setBeerName("Test Beer");
		request.setAlcoholContent(10);
		request.setBreweryId(1);
		request.setPrice(2.2);

		AddBeer expectedResponse = new AddBeer();
		expectedResponse.setBreweryId(request.getBreweryId());
		expectedResponse.setBeerName(request.getBeerName());
		expectedResponse.setAlcoholContent(request.getAlcoholContent());
		expectedResponse.setPrice(request.getPrice());

		// when(logger.isDebugEnabled()).thenReturn(true);

		when(beerServices.addBeer(request)).thenReturn(expectedResponse);

		ResponseEntity<AddBeer> response = breweryController.addBeer(request);

		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertEquals("successful", response.getBody().getResponseMassege());
		assertEquals(expectedResponse, response.getBody());

	}

	@Test
	void testWhenBreweryNotFound() throws BreweryNotFoundException, CommonServiceException, BeerAlreadyExistException {

		final String thisMethod = "addBeer: ";
		if (logger.isDebugEnabled()) {
			logger.debug(String.format(Constant.STRING_FORMAT, thisMethod + Constant.ENTERING));
		}

		AddBeer request = new AddBeer();
		request.setBreweryId(1);

		when(beerServices.addBeer(request)).thenThrow(new BreweryNotFoundException());

		ResponseEntity<AddBeer> response = breweryController.addBeer(request);

		assertThat(response.getStatusCodeValue()).isEqualTo(404);
		assertEquals("brewery not found", response.getBody().getResponseMassege());
		if (logger.isDebugEnabled()) {
			logger.debug(String.format(Constant.STRING_FORMAT, thisMethod + Constant.END + response));
		}
	}

	@Test
	void testWhenBreweryAlreadyExist()
			throws BreweryNotFoundException, CommonServiceException, BeerAlreadyExistException {

		final String thisMethod = "addBeer: ";
		if (logger.isDebugEnabled()) {
			logger.debug(String.format(Constant.STRING_FORMAT, thisMethod + Constant.ENTERING));
		}

		AddBeer request = new AddBeer();
		request.setBeerName("Test Beer");
		request.setAlcoholContent(10);
		request.setBreweryId(1);
		request.setPrice(2.2);

		when(beerServices.addBeer(request)).thenThrow(new BeerAlreadyExistException());

		ResponseEntity<AddBeer> response = breweryController.addBeer(request);

		assertThat(response.getStatusCodeValue()).isEqualTo(406);
		assertEquals("beer already exsist", response.getBody().getResponseMassege());
		if (logger.isDebugEnabled()) {
			logger.debug(String.format(Constant.STRING_FORMAT, thisMethod + Constant.END + response));
		}
	}

	@Test
	void testWhenRequiredData() throws BreweryNotFoundException, CommonServiceException, BeerAlreadyExistException {

		final String thisMethod = "addBeer: ";
		if (logger.isDebugEnabled()) {
			logger.debug(String.format(Constant.STRING_FORMAT, thisMethod + Constant.ENTERING));
		}

		AddBeer request = new AddBeer();
		request.setBeerName("Test Beer");
		request.setAlcoholContent(10);
		request.setBreweryId(1);
		request.setPrice(2.2);

		when(beerServices.addBeer(request)).thenThrow(new CommonServiceException());

		ResponseEntity<AddBeer> response = breweryController.addBeer(request);

		assertThat(response.getStatusCodeValue()).isEqualTo(406);
		assertEquals("data required", response.getBody().getResponseMassege());
		if (logger.isDebugEnabled()) {
			logger.debug(String.format(Constant.STRING_FORMAT, thisMethod + Constant.END + response));
		}
	}

	@Test
	void testGetBreweryBeers() throws BreweryNotFoundException {

		long breweryId = 1L;

		AllBreweryBeers expectedResponse = new AllBreweryBeers();
		expectedResponse.setBreweryId(breweryId);
		expectedResponse.setBreweryName("Test Brewery");
		expectedResponse.setResponseMassage("successful");

		Beers beer1 = new Beers();
		beer1.setBeerName("Beer1");
		beer1.setAlcoholContent(10);
		beer1.setBreweryId(1);
		beer1.setPrice(2.2);

		Beers beer2 = new Beers();
		beer2.setBeerName("Test Beer2");
		beer2.setAlcoholContent(6);
		beer2.setBreweryId(1);
		beer2.setPrice(2);

		ArrayList<Beers> allBeers = new ArrayList<>();

		allBeers.add(beer1);
		allBeers.add(beer2);
		expectedResponse.setBeers(allBeers);

		Brewery brewery = new Brewery();
		brewery.setBreweryId(breweryId);
		brewery.setBreweryName("Test Brewery");

		when(breweryServices.findByBreweryId(breweryId)).thenReturn(brewery);
		when(beerServices.findBeersByBrewery(breweryId)).thenReturn(allBeers);

		ResponseEntity<AllBreweryBeers> response = breweryController.getBreweryBeers(breweryId);

		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertEquals(expectedResponse, response.getBody());
	}

	@Test
	void testGetBreweryBeersWhenBreweryNotFound() throws BreweryNotFoundException {

		long breweryId = 1L;

		AllBreweryBeers expectedResponse = new AllBreweryBeers();
		expectedResponse.setBreweryId(breweryId);
		expectedResponse.setBreweryName("Test Brewery");
		expectedResponse.setResponseMassage("successful");

		Beers beer1 = new Beers();
		beer1.setBeerName("Beer1");
		beer1.setAlcoholContent(10);
		beer1.setBreweryId(1);
		beer1.setPrice(2.2);

		Beers beer2 = new Beers();
		beer2.setBeerName("Test Beer2");
		beer2.setAlcoholContent(6);
		beer2.setBreweryId(1);
		beer2.setPrice(2);

		ArrayList<Beers> allBeers = new ArrayList<>();

		allBeers.add(beer1);
		allBeers.add(beer2);
		expectedResponse.setBeers(allBeers);

		Brewery brewery = new Brewery();
		brewery.setBreweryId(breweryId);
		brewery.setBreweryName("Test Brewery");

		when(breweryServices.findByBreweryId(breweryId)).thenReturn(null);

		ResponseEntity<AllBreweryBeers> response = breweryController.getBreweryBeers(breweryId);

		assertThat(response.getStatusCodeValue()).isEqualTo(404);
		assertEquals("brewery not found", response.getBody().getResponseMassage());

	}

	@Test
	void testDeleteBeer() throws BeerNotFoundException {

		AddBeer request = new AddBeer();
		request.setBeerName("test beer");

		Beers beer = new Beers();
		beer.setBeerName("Beer1");
		beer.setAlcoholContent(10);
		beer.setBreweryId(1);
		beer.setPrice(2.2);

		when(beerServices.deleteBeer(request.getBeerName())).thenReturn(true);

		ResponseEntity<AddBeer> responseEntity = breweryController.deleteBeneficiare(request.getBeerName());

		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(204);
		assertEquals("beer deleted successfully", responseEntity.getBody().getResponseMassege());
	}

	@Test
	void testDeleteBeerNotFound() throws BeerNotFoundException {

		AddBeer request = new AddBeer();
		request.setBeerName("test beer");

		Beers beer = new Beers();
		beer.setBeerName("Beer1");
		beer.setAlcoholContent(10);
		beer.setBreweryId(1);
		beer.setPrice(2.2);

		when(beerServices.deleteBeer(request.getBeerName())).thenThrow(new BeerNotFoundException());

		ResponseEntity<AddBeer> responseEntity = breweryController.deleteBeneficiare(request.getBeerName());

		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
		assertEquals("beer not found", responseEntity.getBody().getResponseMassege());
	}

	@Test
	void testAddSale() throws BeerNotFoundException, CommonServiceException, WholeSalerNotFoundException {

		AddSale request = new AddSale();
		request.setBeerName("test beer");
		request.setStock(55);
		request.setWholeSalerName("test whole saler");

		AddSale expectedResponse = new AddSale();
		expectedResponse.setBeerName(request.getBeerName());
		expectedResponse.setStock(request.getStock());
		expectedResponse.setWholeSalerName(request.getWholeSalerName());

		when(wholeSalerServices.addSale(request)).thenReturn(expectedResponse);

		ResponseEntity<AddSale> responseEntity = breweryController.addSale(request);

		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
		assertEquals("successful", responseEntity.getBody().getResponseMassage());
	}

	@Test
	void testAddSaleWhenBeerNotFound()
			throws BeerNotFoundException, CommonServiceException, WholeSalerNotFoundException {

		AddSale request = new AddSale();
		request.setBeerName("test beer");
		request.setStock(55);
		request.setWholeSalerName("test whole saler");

		when(wholeSalerServices.addSale(request)).thenThrow(new BeerNotFoundException());

		ResponseEntity<AddSale> responseEntity = breweryController.addSale(request);

		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
		assertEquals("beer not found", responseEntity.getBody().getResponseMassage());
	}

	@Test
	void testAddSaleWhenDataRequired()
			throws BeerNotFoundException, CommonServiceException, WholeSalerNotFoundException {

		AddSale request = new AddSale();
		request.setBeerName("");
		request.setStock(0);
		request.setWholeSalerName("");

		when(wholeSalerServices.addSale(request)).thenThrow(new CommonServiceException());

		ResponseEntity<AddSale> responseEntity = breweryController.addSale(request);

		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(406);
		assertEquals("data required", responseEntity.getBody().getResponseMassage());
	}

	@Test
	void testAddSaleWhenWholeSalerNotFound()
			throws BeerNotFoundException, CommonServiceException, WholeSalerNotFoundException {

		AddSale request = new AddSale();
		request.setWholeSalerName("test whole saler");

		when(wholeSalerServices.addSale(request)).thenThrow(new WholeSalerNotFoundException());

		ResponseEntity<AddSale> responseEntity = breweryController.addSale(request);

		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
		assertEquals("Whole-Saler not found", responseEntity.getBody().getResponseMassage());
	}

	@Test
	void testRequestQuote() throws CommonServiceException, WholeSalerNotFoundException, OrderGreaterThanStockException,
			BeerNotFoundException {

		ClientRequest request = new ClientRequest();
		request.setBeerName("test beer");
		request.setQuote(20);
		request.setWholeSalerName("test whole saler");

		ClientRequest expectedResponse = new ClientRequest();
		expectedResponse.setBeerName(request.getBeerName());
		expectedResponse.setQuote(request.getQuote());
		expectedResponse.setWholeSalerName(request.getWholeSalerName());

		when(wholeSalerServices.requestQuote(request)).thenReturn(expectedResponse);

		ResponseEntity<ClientRequest> responseEntity = breweryController.requestQuote(request);

		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
		assertEquals("successful", responseEntity.getBody().getResponseMassage());
	}

	@Test
	void testRequestQuoteWhenDataRequired() throws CommonServiceException, WholeSalerNotFoundException,
			OrderGreaterThanStockException, BeerNotFoundException {

		ClientRequest request = new ClientRequest();
		request.setBeerName("test beer");
		request.setQuote(20);
		request.setWholeSalerName("test whole saler");

		ClientRequest expectedResponse = new ClientRequest();
		expectedResponse.setBeerName(request.getBeerName());
		expectedResponse.setQuote(request.getQuote());
		expectedResponse.setWholeSalerName(request.getWholeSalerName());

		when(wholeSalerServices.requestQuote(request)).thenThrow(new CommonServiceException());

		ResponseEntity<ClientRequest> responseEntity = breweryController.requestQuote(request);

		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(406);
		assertEquals("data required", responseEntity.getBody().getResponseMassage());
	}

	@Test
	void testRequestQuoteWhenWholeSalerNotFound() throws CommonServiceException, WholeSalerNotFoundException,
			OrderGreaterThanStockException, BeerNotFoundException {

		ClientRequest request = new ClientRequest();
		request.setBeerName("test beer");
		request.setQuote(20);
		request.setWholeSalerName("test whole saler");

		ClientRequest expectedResponse = new ClientRequest();
		expectedResponse.setBeerName(request.getBeerName());
		expectedResponse.setQuote(request.getQuote());
		expectedResponse.setWholeSalerName(request.getWholeSalerName());

		when(wholeSalerServices.requestQuote(request)).thenThrow(new WholeSalerNotFoundException());

		ResponseEntity<ClientRequest> responseEntity = breweryController.requestQuote(request);

		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
		assertEquals("Whole-Saler not found", responseEntity.getBody().getResponseMassage());
	}

	@Test
	void testRequestQuoteWhenOrderGreaterThanStock() throws CommonServiceException, WholeSalerNotFoundException,
			OrderGreaterThanStockException, BeerNotFoundException {

		ClientRequest request = new ClientRequest();
		request.setBeerName("test beer");
		request.setQuote(20);
		request.setWholeSalerName("test whole saler");

		ClientRequest expectedResponse = new ClientRequest();
		expectedResponse.setBeerName(request.getBeerName());
		expectedResponse.setQuote(request.getQuote());
		expectedResponse.setWholeSalerName(request.getWholeSalerName());

		when(wholeSalerServices.requestQuote(request)).thenThrow(new OrderGreaterThanStockException());

		ResponseEntity<ClientRequest> responseEntity = breweryController.requestQuote(request);

		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(406);
		assertEquals("Order Greater Than Stock", responseEntity.getBody().getResponseMassage());
	}

	@Test
	void testRequestQuoteWhenBeerNotFound() throws CommonServiceException, WholeSalerNotFoundException,
			OrderGreaterThanStockException, BeerNotFoundException {

		ClientRequest request = new ClientRequest();
		request.setBeerName("test beer");
		request.setQuote(20);
		request.setWholeSalerName("test whole saler");

		ClientRequest expectedResponse = new ClientRequest();
		expectedResponse.setBeerName(request.getBeerName());
		expectedResponse.setQuote(request.getQuote());
		expectedResponse.setWholeSalerName(request.getWholeSalerName());

		when(wholeSalerServices.requestQuote(request)).thenThrow(new BeerNotFoundException());

		ResponseEntity<ClientRequest> responseEntity = breweryController.requestQuote(request);

		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
		assertEquals("beer not found", responseEntity.getBody().getResponseMassage());
	}

}
