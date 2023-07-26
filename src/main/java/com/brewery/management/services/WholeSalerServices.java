package com.brewery.management.services;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brewery.management.constant.Constant;
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


@Service
public class WholeSalerServices {

	
	@Autowired
	BeersRepository beerRepository;
	
	@Autowired
	WholeSalerRepository wholeSalerRepository;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	public WholeSaler update(WholeSaler request) {
		return wholeSalerRepository.saveAndFlush(request);

	}
	
	
	@Transactional
	public ClientRequest requestQuote(ClientRequest request)
			throws CommonServiceException, WholeSalerNotFoundException, OrderGreaterThanStockException, BeerNotFoundException {

		final String thisMethod = "requestQuote: ";
		if (logger.isDebugEnabled()) {
		logger.debug(String.format(Constant.STRING_FORMAT,thisMethod + Constant.ENTERING + request));
		}
		ClientRequest response = new ClientRequest();

		validateRequestQuote(request);

		Beers findBeer = beerRepository.findByBeerName(request.getBeerName());

		if (findBeer == null) {
			throw new BeerNotFoundException();
		}

		WholeSaler findWholeSaler = wholeSalerRepository.findBywholeSalerName(request.getWholeSalerName());

		if (findWholeSaler == null) {

			throw new WholeSalerNotFoundException();
		}

		if (request.getQuote() > findWholeSaler.getStock()) {
			throw new OrderGreaterThanStockException();

		}

		if (request.getQuote() > 10) {
			response.setDiscountNotification("10% discount");
		}

		if (request.getQuote() > 20) {
			response.setDiscountNotification("20% discount");
		}

		findWholeSaler.setStock(findWholeSaler.getStock() - request.getQuote());
		update(findWholeSaler);

		response.setBeerName(request.getBeerName());
		response.setWholeSalerName(request.getWholeSalerName());
		response.setQuote(request.getQuote());
		response.setBeerPrice(findBeer.getPrice());
		response.setTotalAmount(findBeer.getPrice() * request.getQuote());
		response.setResponseMassage(Constant.SUCCESSFUL);
		return response;
	}

	public ClientRequest validateRequestQuote(ClientRequest request) throws CommonServiceException {

		if (request.getBeerName() == null || request.getBeerName().equalsIgnoreCase("")) {
			throw new CommonServiceException();
		}


		if (request.getWholeSalerName() == null || request.getWholeSalerName().equalsIgnoreCase("")) {
			throw new CommonServiceException();
		}

		if (request.getQuote() == 0) {
			throw new CommonServiceException();
		}
		return request;
	}
	
	
	@Transactional
	public AddSale addSale(AddSale request)
			throws BeerNotFoundException, CommonServiceException, WholeSalerNotFoundException {
		final String thisMethod = "addSale: ";
		if (logger.isDebugEnabled()) {
		logger.debug(Constant.STRING_FORMAT,thisMethod + Constant.ENTERING + request);
		}
		AddSale response = new AddSale();

		validateAddSaleRequest(request);

		Beers findBeer = beerRepository.findByBeerName(request.getBeerName());

		if (findBeer == null) {
			throw new BeerNotFoundException();
		}

		WholeSaler findWholeSaler = wholeSalerRepository.findBywholeSalerName(request.getWholeSalerName());

		if (findWholeSaler == null) {
			throw new WholeSalerNotFoundException();
		}

		findWholeSaler.setStock(request.getStock() + findWholeSaler.getStock());

		wholeSalerRepository.save(findWholeSaler);

		response.setBeerName(findWholeSaler.getBeerName());
		response.setWholeSalerName(findWholeSaler.getWholeSalerName());
		response.setStock(findWholeSaler.getStock());

		return response;

	}

	public  AddSale validateAddSaleRequest(AddSale request) throws CommonServiceException {
		if (request.getBeerName() == null || request.getBeerName().equalsIgnoreCase("")) {
			throw new CommonServiceException();
		}

		if (request.getStock() == 0) {
			throw new CommonServiceException();
		}

		if (request.getWholeSalerName() == null || request.getWholeSalerName().equalsIgnoreCase("")) {
			throw new CommonServiceException();
		}
		
		return request;
	}

}
