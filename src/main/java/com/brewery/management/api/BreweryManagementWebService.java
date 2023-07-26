package com.brewery.management.api;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



@RequestMapping(value = "/brewery")
@RestController
public class BreweryManagementWebService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BreweryServices breweryServices;
	
	@Autowired
	private BeerServices beerServices;
	
	@Autowired
	private WholeSalerServices wholeSalerServices;

	@PostMapping(value = "/add-beer")
	public ResponseEntity<AddBeer> addBeer(@RequestBody AddBeer request) {

		final String thisMethod = "addBeer: ";
		if (logger.isDebugEnabled()) {
		logger.debug(String.format( Constant.STRING_FORMAT,thisMethod + Constant.ENTERING  + request));
		}
		
		AddBeer response = new AddBeer();

		try {
			response = beerServices.addBeer(request);

		} catch (CommonServiceException e) {
			logger.debug(e.getMessage());
			response.setResponseMassege(Constant.DATA_REQUIRED);
			return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);

		} catch (BeerAlreadyExistException e) {
			response.setResponseMassege("beer already exsist");
			return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
		} catch (BreweryNotFoundException e) {
			response.setResponseMassege("brewery not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.setResponseMassege(Constant.SUCCESSFUL);
		if (logger.isDebugEnabled()) {
		logger.debug(String.format(Constant.STRING_FORMAT, thisMethod + Constant.END + response));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/list-brewery-beer/{breweryId}")
	public ResponseEntity<AllBreweryBeers> getBreweryBeers(@PathVariable("breweryId") long breweryId) throws BreweryNotFoundException {

		final String thisMethod = "getBreweryBeers: ";
		if (logger.isDebugEnabled()) {
		logger.debug(String.format(Constant.STRING_FORMAT, thisMethod + Constant.ENTERING + breweryId));
		}
		AllBreweryBeers response = new AllBreweryBeers();

		Brewery findBrewery = breweryServices.findByBreweryId(breweryId);

		if (findBrewery == null) {
			response.setResponseMassage("brewery not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		List<Beers> allBeers = beerServices.findBeersByBrewery(breweryId);

		response.setBreweryId(breweryId);
		response.setBreweryName(findBrewery.getBreweryName());
		response.setBeers(allBeers);

		response.setResponseMassage(Constant.SUCCESSFUL);
		if (logger.isDebugEnabled()) {
		logger.debug(String.format(Constant.STRING_FORMAT,thisMethod + Constant.END+ response));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping(value = "/delete-beer/{beerName}")
	public ResponseEntity<AddBeer> deleteBeneficiare(@PathVariable("beerName") String beerName)
			throws BeerNotFoundException{

		final String thisMethod = "getBreweryBeers: ";
		if (logger.isDebugEnabled()) {
		logger.debug(String.format( Constant.STRING_FORMAT,thisMethod + Constant.ENTERING+ beerName));
		}
		AddBeer response = new AddBeer();

		try {

			beerServices.deleteBeer(beerName);

		} catch (BeerNotFoundException e) {
			response.setResponseMassege(Constant.BEER_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.setResponseMassege("beer deleted successfully");
		if (logger.isDebugEnabled()) {
		logger.debug(String.format(Constant.STRING_FORMAT,thisMethod + Constant.END+ response));
		}
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);

	}

	@PostMapping(value = "/add-sale")
	public ResponseEntity<AddSale> addSale(@RequestBody AddSale request) {

		final String thisMethod = "addSale: ";
		if (logger.isDebugEnabled()) {
		logger.debug(String.format (Constant.STRING_FORMAT,thisMethod + Constant.ENTERING + request));
		}
		AddSale response = new AddSale();

		try {
			response = wholeSalerServices.addSale(request);

		} catch (BeerNotFoundException e) {
			response.setResponseMassage(Constant.BEER_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (CommonServiceException e) {
			logger.debug(e.getMessage());
			response.setResponseMassage(Constant.DATA_REQUIRED);
			return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
		} catch (WholeSalerNotFoundException e) {
			response.setResponseMassage("Whole-Saler not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.setResponseMassage(Constant.SUCCESSFUL);
		if (logger.isDebugEnabled()) {
		logger.debug(String.format(Constant.STRING_FORMAT,thisMethod + Constant.END+ response));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/request-quote")
	public ResponseEntity<ClientRequest> requestQuote(@RequestBody ClientRequest request) {

		final String thisMethod = "requestQuote: ";
		if (logger.isDebugEnabled()) {
		logger.debug(String.format(Constant.STRING_FORMAT,thisMethod + Constant.ENTERING + request));
		}
		ClientRequest response = new ClientRequest();

		try {
			response = wholeSalerServices.requestQuote(request);
		} catch (CommonServiceException e) {
			response.setResponseMassage(Constant.DATA_REQUIRED);
			return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
			
		} catch (WholeSalerNotFoundException e) {
			response.setResponseMassage("Whole-Saler not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			
		} catch (OrderGreaterThanStockException e) {
			response.setResponseMassage("Order Greater Than Stock");
			return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
		} catch (BeerNotFoundException e) {
			response.setResponseMassage(Constant.BEER_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.setResponseMassage(Constant.SUCCESSFUL);
		if (logger.isDebugEnabled()) {
		logger.debug(String.format(Constant.STRING_FORMAT,thisMethod + Constant.END + response));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
