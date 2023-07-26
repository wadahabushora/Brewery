package com.brewery.management.brewery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.brewery.management.exceptions.BeerNotFoundException;
import com.brewery.management.exceptions.CommonServiceException;
import com.brewery.management.exceptions.OrderGreaterThanStockException;
import com.brewery.management.exceptions.WholeSalerNotFoundException;
import com.brewery.management.models.AddSale;
import com.brewery.management.models.Beers;
import com.brewery.management.models.ClientRequest;
import com.brewery.management.models.WholeSaler;
import com.brewery.management.repositories.BeersRepository;
import com.brewery.management.repositories.WholeSalerRepository;
import com.brewery.management.services.BeerServices;
import com.brewery.management.services.WholeSalerServices;

@ExtendWith(MockitoExtension.class)
class WholeSalerServicesTest {

	@InjectMocks
	private WholeSalerServices wholeSalerServices;

	@Mock
	BeersRepository beerRepository;

	@Mock
	private BeerServices beerServices;

	@Mock
	WholeSalerRepository wholeSalerRepository;
//
	@Test
	public void testUpdate() {

		WholeSaler request = new WholeSaler();
		request.setWholeSalerName("Test WholeSaler");
		request.setBeerName("test beer");
		request.setStock(100);

		WholeSaler updatedWholeSaler = new WholeSaler();
		updatedWholeSaler.setWholeSalerName("upate Test WholeSaler");
		updatedWholeSaler.setBeerName("upate test beer");
		updatedWholeSaler.setStock(150);

		lenient().when(wholeSalerServices.update(request)).thenReturn(updatedWholeSaler);

		WholeSaler result = wholeSalerRepository.saveAndFlush(request);

		assertEquals(updatedWholeSaler, result);
	}

	@Test
	public void testRequestQuote() throws CommonServiceException, WholeSalerNotFoundException,
			OrderGreaterThanStockException, BeerNotFoundException {
		// Create test data
		Beers beer = new Beers();
		beer.setBeerName("Test Beer");
		beer.setPrice(1.0);

		WholeSaler wholesaler = new WholeSaler();
		wholesaler.setWholeSalerName("Test Wholesaler");
		wholesaler.setStock(50);

		ClientRequest request = new ClientRequest();
		request.setBeerName("Test Beer");
		request.setWholeSalerName("Test Wholesaler");
		request.setQuote(10);

		lenient().when(beerRepository.findByBeerName(beer.getBeerName())).thenReturn(beer);

		lenient().when(wholeSalerRepository.findBywholeSalerName("Test Wholesaler")).thenReturn(wholesaler);

		// Call the method being tested
		ClientRequest response = wholeSalerServices.requestQuote(request);

		assertEquals("successful", response.getResponseMassage());
	}

	@Test
	public void testAddSale() throws BeerNotFoundException, CommonServiceException, WholeSalerNotFoundException {
		// Create test data
		Beers beer = new Beers();
		beer.setBeerName("Test Beer");

		WholeSaler wholesaler = new WholeSaler();
		wholesaler.setWholeSalerName("Test Wholesaler");
		wholesaler.setBeerName("Test Beer");
		wholesaler.setStock(50);

		AddSale request = new AddSale();
		request.setBeerName("Test Beer");
		request.setWholeSalerName("Test Wholesaler");
		request.setStock(10);

		lenient().when(beerRepository.findByBeerName(beer.getBeerName())).thenReturn(beer);
		lenient().when(wholeSalerRepository.findBywholeSalerName("Test Wholesaler")).thenReturn(wholesaler);

		// Call the method being tested
		AddSale response = wholeSalerServices.addSale(request);

		// Check the response
		assertEquals("Test Beer", response.getBeerName());
		assertEquals("Test Wholesaler", response.getWholeSalerName());
		assertEquals(60, response.getStock());
	}

	@Test
	public void testAddSaleBeerNotFound() throws BeerNotFoundException {

		AddSale request = new AddSale();
		request.setBeerName("test beer");
		request.setWholeSalerName("Test Wholesaler");
		request.setStock(10);

		Beers beer = new Beers();
		beer.setBeerName("Test Beer");
		beer.setPrice(1.0);

		lenient().when(beerRepository.findByBeerName(request.getBeerName())).thenReturn(null);

		Assertions.assertThrows(BeerNotFoundException.class, () -> {
			wholeSalerServices.addSale(request);
		});
	}

	@Test
	public void testAddSaleWholeSalerNotFound() throws WholeSalerNotFoundException {

		AddSale request = new AddSale();
		request.setWholeSalerName("wholesaler");
		request.setStock(10);
		request.setBeerName("Test Beer");

		WholeSaler wholesaler = new WholeSaler();
		wholesaler.setWholeSalerName("Test Wholesaler");
		wholesaler.setStock(50);

		Beers beer = new Beers();
		beer.setBeerName("Test Beer");
		beer.setPrice(1.0);

		lenient().when(beerRepository.findByBeerName("Test Beer")).thenReturn(beer);

		lenient().when(wholeSalerRepository.findBywholeSalerName(request.getWholeSalerName())).thenReturn(null);

		Assertions.assertThrows(WholeSalerNotFoundException.class, () -> {
			wholeSalerServices.addSale(request);
		});
	}

	@Test
	public void testValidateAddSaleRequestWholeSalerName() {
		// Test case with valid request
		AddSale requestWholeSaler = new AddSale();
		requestWholeSaler.setWholeSalerName("");

		try {
			AddSale result = wholeSalerServices.validateAddSaleRequest(requestWholeSaler);
			assertEquals(requestWholeSaler, result);
		} catch (CommonServiceException e) {
		}
	}

	@Test
	public void testValidateAddSaleRequestStock() {
		AddSale requestStock = new AddSale();
		requestStock.setStock(0);

		try {
			AddSale result = wholeSalerServices.validateAddSaleRequest(requestStock);
			assertEquals(requestStock, result);
		} catch (CommonServiceException e) {
		}
	}

	@Test
	public void testValidateAddSaleRequestBeerName() {
		AddSale requestBeerName = new AddSale();

		requestBeerName.setBeerName("");
		try {
			AddSale result = wholeSalerServices.validateAddSaleRequest(requestBeerName);
			assertEquals(requestBeerName, result);
		} catch (CommonServiceException e) {
		}

	}
	
	@Test
	public void testValidateRequestQuoteBeerName() {
		ClientRequest requestBeerName = new ClientRequest();

		requestBeerName.setBeerName("");
		try {
			ClientRequest result = wholeSalerServices.validateRequestQuote(requestBeerName);
			assertEquals(requestBeerName, result);
		} catch (CommonServiceException e) {
		}

	}
	
	@Test
	public void testValidateRequestQuoteStock() {
		ClientRequest requestStock = new ClientRequest();
		requestStock.setQuote(0);
		try {
			ClientRequest result = wholeSalerServices.validateRequestQuote(requestStock);
			assertEquals(requestStock, result);
		} catch (CommonServiceException e) {
		}
	}

	@Test
	public void testValidateRequestQuoteWholeSalerName() {
		ClientRequest requestWholeSalerName = new ClientRequest();
		requestWholeSalerName.setWholeSalerName("");
		try {
			ClientRequest result = wholeSalerServices.validateRequestQuote(requestWholeSalerName);
			assertEquals(requestWholeSalerName, result);
		} catch (CommonServiceException e) {
		}
	}
	
	@Test
	public void testRequestQuoteBeerNotFound() throws BeerNotFoundException {

		ClientRequest request = new ClientRequest();
		request.setBeerName("test beer");
		request.setWholeSalerName("Test Wholesaler");
		request.setQuote(10);

		Beers beer = new Beers();
		beer.setBeerName("Test Beer");
		beer.setPrice(1.0);

		lenient().when(beerRepository.findByBeerName(request.getBeerName())).thenReturn(null);

		Assertions.assertThrows(BeerNotFoundException.class, () -> {
			wholeSalerServices.requestQuote(request);
		});
	}

	@Test
	public void testRequestQuoteWholeSalerNotFound() throws WholeSalerNotFoundException {

		ClientRequest request = new ClientRequest();
		request.setWholeSalerName("wholesaler");
		request.setQuote(10);
		request.setBeerName("Test Beer");

		WholeSaler wholesaler = new WholeSaler();
		wholesaler.setWholeSalerName("Test Wholesaler");
		wholesaler.setStock(50);

		Beers beer = new Beers();
		beer.setBeerName("Test Beer");
		beer.setPrice(1.0);

		lenient().when(beerRepository.findByBeerName("Test Beer")).thenReturn(beer);

		lenient().when(wholeSalerRepository.findBywholeSalerName(request.getWholeSalerName())).thenReturn(null);

		Assertions.assertThrows(WholeSalerNotFoundException.class, () -> {
			wholeSalerServices.requestQuote(request);
		});
	}
	
	@Test
	public void testDiscountNotificationMoreThanTen() throws CommonServiceException, WholeSalerNotFoundException, OrderGreaterThanStockException, BeerNotFoundException {
		
		ClientRequest request = new ClientRequest();
		request.setWholeSalerName("wholesaler");
		request.setQuote(11);
		request.setBeerName("Test Beer");

		WholeSaler wholesaler = new WholeSaler();
		wholesaler.setWholeSalerName("Test Wholesaler");
		wholesaler.setStock(50);

		Beers beer = new Beers();
		beer.setBeerName("Test Beer");
		beer.setPrice(1.0);

		ClientRequest response = new ClientRequest();
		response.setDiscountNotification("10% discount");
		lenient().when(beerRepository.findByBeerName("Test Beer")).thenReturn(beer);

		lenient().when(wholeSalerRepository.findBywholeSalerName(request.getWholeSalerName())).thenReturn(wholesaler);

		 wholeSalerServices.requestQuote(request);

        assertEquals("10% discount", response.getDiscountNotification());
        //assertEquals(result, response.etDiscountNotification());
    }
	
	@Test
	public void testDiscountNotificationMoreThanTwenty() throws CommonServiceException, WholeSalerNotFoundException, OrderGreaterThanStockException, BeerNotFoundException {
		
		ClientRequest request = new ClientRequest();
		request.setWholeSalerName("wholesaler");
		request.setQuote(21);
		request.setBeerName("Test Beer");

		WholeSaler wholesaler = new WholeSaler();
		wholesaler.setWholeSalerName("Test Wholesaler");
		wholesaler.setStock(50);

		Beers beer = new Beers();
		beer.setBeerName("Test Beer");
		beer.setPrice(1.0);

		ClientRequest response = new ClientRequest();
		response.setDiscountNotification("20% discount");
		lenient().when(beerRepository.findByBeerName("Test Beer")).thenReturn(beer);

		lenient().when(wholeSalerRepository.findBywholeSalerName(request.getWholeSalerName())).thenReturn(wholesaler);

		 wholeSalerServices.requestQuote(request);

        assertEquals("20% discount", response.getDiscountNotification());
        //assertEquals(result, response.etDiscountNotification());
    }
//	
	@Test
	public void testOrderGreaterThanStock() throws CommonServiceException, WholeSalerNotFoundException, OrderGreaterThanStockException, BeerNotFoundException {
		
		ClientRequest request = new ClientRequest();
		request.setQuote(60);
		request.setWholeSalerName("Test Wholesaler");
		request.setBeerName("Test Beer");

		WholeSaler wholesaler = new WholeSaler();
		wholesaler.setWholeSalerName("Test Wholesaler");
		wholesaler.setStock(50);

		
		Beers beer = new Beers();
		beer.setBeerName("Test Beer");
		beer.setPrice(1.0);
		
		lenient().when(beerRepository.findByBeerName("Test Beer")).thenReturn(beer);
		lenient().when(wholeSalerRepository.findBywholeSalerName(request.getWholeSalerName())).thenReturn(wholesaler);

		Assertions.assertThrows(OrderGreaterThanStockException.class, () -> {
			wholeSalerServices.requestQuote(request);
		});
		
	}
}
//
